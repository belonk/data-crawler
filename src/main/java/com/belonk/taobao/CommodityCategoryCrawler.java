package com.belonk.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.belonk.taobao.domain.Category;
import com.belonk.taobao.service.CategoryService;
import com.belonk.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class CommodityCategoryCrawler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(CommodityCategoryCrawler.class);

    public static final String URL = "https://upload.taobao.com/auction/json/reload_cats.htm?customId=&fenxiaoProduct=";

    public static final String COOKIE = "t=a048ca1248def5d46e1b99b9dfd3329a; thw=cn; miid=799547686855897634; cna=H7B2E4MdaycCAXZwYRo1ZKJ5; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; hng=CN%7Czh-CN%7CCNY%7C156; _m_h5_tk=24b5b3560546171bd5a4272b8c64541e_1526374832576; _m_h5_tk_enc=c432c10abc5c34fab68623199fd0ea91; UM_distinctid=163710934cbcd-0612e153b7df0e-f373567-e1000-163710934cc809; enc=Ks1OlHbgavv2RgZVt59ClZRCMMoQY5BGgKMG8SsIud3TPFLk45z2l6hlm7SiSpmA%2FCdwnRPe89te0Z5wi9R2Mg%3D%3D; l=AlhY4-JYNQyuvmaAwtGOfWhRqI3qbLzL; _uab_collina=152663658953196205108952; _umdata=85957DF9A4B3B3E8EA9242D8DA57705DF6C260957D900C846BEE60523F49E91D9B2F8E32F60C367CCD43AD3E795C914C3C0F0F0AE85FA3077A447E6CC0EA0F5C; lgc=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; tracknick=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; mt=np=&ci=0_0; cookie2=37683e1358457d1ebfc08ea95e509f83; v=0; _tb_token_=e07bf55d57de5; dnk=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; publishItemObj=Ng%3D%3D; JSESSIONID=0B11663F78213B3105B3727DC2F69555; uc3=nk2=tML4zJvlq4bLEw%3D%3D&id2=Vy0QnDnkh%2BXG&vt3=F8dBz49%2FN1uDxJ246G8%3D&lg2=UtASsssmOIJ0bQ%3D%3D; existShop=MTUyNjcyODUyNw%3D%3D; sg=%E4%B8%8B7a; csg=8cbc9404; cookie1=UNky63b4vUvDU75ViU87yTzbGf4Q011kVuzWqsnKdPw%3D; unb=413468597; skt=80f5900025bcafd5; _cc_=VT5L2FSpdA%3D%3D; _l_g_=Ug%3D%3D; _nk_=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; cookie17=Vy0QnDnkh%2BXG; uc1=cookie16=V32FPkk%2FxXMk5UvIbNtImtMfJQ%3D%3D&cookie21=U%2BGCWk%2F7owVBK2Hga0xuWA%3D%3D&cookie15=W5iHLLyFOGW7aA%3D%3D&existShop=true&pas=0&cookie14=UoTeOL6Xlg2A6w%3D%3D&tag=8&lng=zh_CN; apush244d3af8cb013571ea511c2c367b87fd=%7B%22ts%22%3A1526728546597%2C%22parentId%22%3A1526728530563%7D; isg=BIWF9AMPcgT0xFemabhsQTfZlMF_6v6uvMuZE4fqC7zOHqSQT5NJpOP4LELoXlGM";

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

    public void run() throws IOException {
        crawl(PATH.ALL, null, null, null, null, 0);
    }

    public static void main(String[] args) throws IOException {
        CommodityCategoryCrawler crawler = new CommodityCategoryCrawler();
        crawler.run();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

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
                    System.out.println(JSON.toJSONString(category));
                    if (++testDeep > 1) {
                        return;
                    }
                }
                JSONArray subDataArray = (JSONArray) dataObj.get("data");
                prevPid = pid;
                for (Object subData : subDataArray) {
                    JSONObject subDataObj = (JSONObject) subData;

                    // 保存细类
                    category = newCategory(subDataObj, deep++);
                    category.setPid(pid);
                    categoryService.add(category);
                    System.out.println(JSON.toJSONString(category));
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
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Inner Class
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public enum PATH {
        ALL, NEXT
    }
}