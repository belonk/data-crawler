package com.belonk.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.belonk.taobao.domain.Category;
import com.belonk.taobao.service.CategoryDynamicService;
import com.belonk.taobao.service.CommodityCategoryCrawler;
import com.belonk.taobao.service.impl.AbstractCommodityCategoryCrawler;
import com.belonk.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 商品类目爬取。
 * <p>
 * Created by sun on 2018/5/19.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class DynamicCommodityCategoryCrawler extends AbstractCommodityCategoryCrawler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(DynamicCommodityCategoryCrawler.class);

    private static int testDeep = 0;

    private static int threadCnt = 0;

    private static ExecutorService executorService = new ThreadPoolExecutor(8, 16,
            60, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1024), r -> {
        Thread thread = new Thread(r);
        thread.setName("crawl-thread-" + thread.getId());
        return thread;
    });

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private CategoryDynamicService categoryDynamicService;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static void main(String[] args) throws Exception {
        CommodityCategoryCrawler crawler = new DynamicCommodityCategoryCrawler();
        crawler.run();
    }

    @Override
    public void run() throws IOException {
        crawl();
    }

    private void crawl() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("path", PATH.ALL.toString().toLowerCase());
        params.put("sid", "");
        params.put("pv", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", COOKIE);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String jsonStr = HttpUtil.me().post(URL, params, headers);
        if (!StringUtils.hasLength(jsonStr)) {
            log.error("Request successfully but respond nothing.");
            System.exit(0);
            return;
        }
        JSONArray jsonArray = JSON.parseArray(jsonStr);
        Category category = null;

        List<RequestStartNode> startNodes = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject jsonObject = (JSONObject) json;
            JSONArray dataArray = (JSONArray) jsonObject.get("data");
            for (Object data : dataArray) {
//                if (++testDeep > 1) {
//                    return;
//                }
                JSONObject dataObj = (JSONObject) data;
                // 保存一级类目
                int deep = 1;
                category = newCategory(dataObj, deep);
                log.info("Saving root category named " + category.getName());
                String tblName = category.getName();
                categoryDynamicService.createTable(tblName);
                categoryDynamicService.add(tblName, category);
                Long pid = category.getId();
                JSONArray subDataArray = (JSONArray) dataObj.get("data");
                for (Object subData : subDataArray) {
                    JSONObject subDataObj = (JSONObject) subData;
                    // 保存二级类目
                    deep = 2;
                    category = newCategory(subDataObj, deep);
                    category.setPid(pid);
                    categoryDynamicService.add(tblName, category);
                    String sid = subDataObj.getString("sid");
                    log.info("Starting new thread to crawl " + category.getName() + "...");
                    deep = 3;
                    startNodes.add(new RequestStartNode(tblName, category.getName(), sid, category.getId(), deep));
                }
            }
        }
        log.info("Top and second category are saved completely, start to query and save all sub categories recusively.");
        log.info("Start node count : " + startNodes.size());
        for (RequestStartNode startNode : startNodes) {
            log.info("Using thread pool to proccess category : " + startNode.getName());
//            executorService.execute(new SubDataCrawler(startNode.getpName(), startNode.getSid(), startNode.getId(), startNode.getDeep()));
        }
        log.info("Thread count: " + threadCnt);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private Category newCategory(JSONObject jsonObject, int deep) {
        String spell = jsonObject.getString("spell");
        String name = jsonObject.getString("name");
        Integer supid = jsonObject.getInteger("spuid");
        Integer leaf = jsonObject.getInteger("leaf");
        String csid = jsonObject.getString("sid");
        Integer status = jsonObject.getInteger("status");
        Category category = new Category();
        category.setSpell(spell);
        category.setName(name);
        category.setStatus(status);
        category.setLeaf(leaf);
        category.setDeep(deep);
        return category;
    }

    private static class RequestStartNode {
        private String pName;
        private String name;
        private String sid;
        private Long id;
        private int deep;

        public RequestStartNode() {
        }

        public RequestStartNode(String pName, String name, String sid, Long id, int deep) {
            this.pName = pName;
            this.name = name;
            this.sid = sid;
            this.id = id;
            this.deep = deep;
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getDeep() {
            return deep;
        }

        public void setDeep(int deep) {
            this.deep = deep;
        }
    }

    class SubDataCrawler implements Runnable {
        private Long pid;
        private int deep = 2;
        private String tblName;
        private String sid;
        private String pv;
        private Long prevPid;

        SubDataCrawler(String tableName, String sid, Long pid, int deep) {
            this.sid = sid;
            this.pid = pid;
            this.deep = deep;
            this.tblName = tableName;
            threadCnt++;
        }

        @Override
        public void run() {
            try {
                crawl(PATH.NEXT, sid, "", pid, null, deep);
            } catch (IOException e) {
                log.error("Exception : ", e);
            }
        }

        private void crawl(PATH path, String sid, String pv, Long pid, Long prevPid, int deep) throws IOException {
            Map<String, Object> params = new HashMap<>();
            params.put("path", path.toString().toLowerCase());
            params.put("sid", sid);
            params.put("pv", pv);

            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", COOKIE);
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String jsonStr = HttpUtil.me().post(URL, params, headers);
            Thread.yield();
            if (!StringUtils.hasLength(jsonStr)) {
                log.warn("Request successfully but respond nothing.");
                return;
            }
            JSONArray jsonArray = JSON.parseArray(jsonStr);
            Category category = null;
            for (Object json : jsonArray) {
                JSONObject jsonObject = (JSONObject) json;
                JSONArray dataArray = (JSONArray) jsonObject.get("data");
                for (Object data : dataArray) {
                    JSONObject dataObj = (JSONObject) data;
                    JSONArray subDataArray = (JSONArray) dataObj.get("data");
                    prevPid = pid;
                    for (Object subData : subDataArray) {
                        JSONObject subDataObj = (JSONObject) subData;

                        // 保存细类
                        category = newCategory(subDataObj, deep++);
                        category.setPid(pid);
//                        log.info("Saving category named " + category.getName());
                        categoryDynamicService.add(tblName, category);
//                    if ("万金币".equals(category.getName())) {
//                        System.out.println("....");
//                    }
                        String csid = subDataObj.getString("sid");
                        if (category.getLeaf() != 1) {
                            pid = category.getId();
                            if (csid.indexOf(":") > 0) {
                                if (StringUtils.hasLength(pv)) {
                                    csid = pv + ";" + csid;
                                }
                                crawl(PATH.NEXT, sid, csid, pid, prevPid, deep);
                            } else {
                                crawl(PATH.NEXT, csid, "", pid, prevPid, deep);
                            }
                        }
                        deep--;
                        pid = prevPid;
                    }
                }
                deep--;
                pid = prevPid;
            }
        }
    }
}