package com.belonk.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.belonk.taobao.domain.Category;
import com.belonk.taobao.service.CategoryService;
import com.belonk.taobao.service.CommodityCategoryCrawler;
import com.belonk.taobao.service.impl.AbstractCommodityCategoryCrawler;
import com.belonk.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
public class DefaultCommodityCategoryCrawler extends AbstractCommodityCategoryCrawler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(DefaultCommodityCategoryCrawler.class);
    private static int testDeep = 0;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private CategoryService categoryService;

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
        CommodityCategoryCrawler crawler = new DefaultCommodityCategoryCrawler();
        crawler.run();
    }

    @Override
    public void run() throws IOException {
        log.info("Starting to crawl category data ...");
        long start = System.currentTimeMillis();
        crawl(PATH.ALL, null, null, null, null, 0);
        long end = System.currentTimeMillis();
        log.info("Crawl category data finished, spend " + (end - start) / 1000 + " min");
    }

    private void crawl(PATH path, String sid, String pv, Long pid, Long prevPid, int deep) throws IOException {
        boolean root = path == PATH.ALL;
        if (root) {
            sid = "";
            pv = "";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("path", path.toString().toLowerCase());
        params.put("sid", sid);
        params.put("pv", pv);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", COOKIE);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String jsonStr = HttpUtil.me().post(URL, params, headers);
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
                if (root) {
                    // 保存大类
                    deep = 1;
                    category = newCategory(dataObj, deep++);
                    categoryService.add(category);
                    pid = category.getId();
                    prevPid = null;
//                    if (++testDeep > 1) {
//                        return;
//                    }
                }
                JSONArray subDataArray = (JSONArray) dataObj.get("data");
                prevPid = pid;
                for (Object subData : subDataArray) {
                    JSONObject subDataObj = (JSONObject) subData;

                    // 保存细类
                    category = newCategory(subDataObj, deep++);
                    category.setPid(pid);
                    categoryService.add(category);
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
}