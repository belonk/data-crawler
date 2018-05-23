package com.belonk.taobao.service.impl;

import com.belonk.taobao.service.CategoryService;
import com.belonk.taobao.service.CommodityCategoryCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 商品类目爬取。
 * <p>
 * Created by sun on 2018/5/19.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractCommodityCategoryCrawler implements CommodityCategoryCrawler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(AbstractCommodityCategoryCrawler.class);

    public static final String URL = "https://upload.taobao.com/auction/json/reload_cats.htm?customId=&fenxiaoProduct=";

    protected static String COOKIE = "t=a048ca1248def5d46e1b99b9dfd3329a; thw=cn; miid=799547686855897634; cna=H7B2E4MdaycCAXZwYRo1ZKJ5; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; hng=CN%7Czh-CN%7CCNY%7C156; _m_h5_tk=24b5b3560546171bd5a4272b8c64541e_1526374832576; _m_h5_tk_enc=c432c10abc5c34fab68623199fd0ea91; UM_distinctid=163710934cbcd-0612e153b7df0e-f373567-e1000-163710934cc809; enc=Ks1OlHbgavv2RgZVt59ClZRCMMoQY5BGgKMG8SsIud3TPFLk45z2l6hlm7SiSpmA%2FCdwnRPe89te0Z5wi9R2Mg%3D%3D; _uab_collina=152663658953196205108952; _umdata=85957DF9A4B3B3E8EA9242D8DA57705DF6C260957D900C846BEE60523F49E91D9B2F8E32F60C367CCD43AD3E795C914C3C0F0F0AE85FA3077A447E6CC0EA0F5C; cookie2=320f890da621bd10aa607ae9eb8cd7ca; _tb_token_=5a333be5e3361; v=0; uc3=nk2=tML4zJvlq4bLEw%3D%3D&id2=Vy0QnDnkh%2BXG&vt3=F8dBz494%2FCUQe5wXYnI%3D&lg2=URm48syIIVrSKA%3D%3D; existShop=MTUyNjgxNzEwNw%3D%3D; lgc=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; tracknick=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; dnk=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; sg=%E4%B8%8B7a; csg=050e503f; mt=np=&ci=0_0; cookie1=UNky63b4vUvDU75ViU87yTzbGf4Q011kVuzWqsnKdPw%3D; unb=413468597; skt=8af9a618ed9cfaaa; publishItemObj=Ng%3D%3D; _cc_=U%2BGCWk%2F7og%3D%3D; _l_g_=Ug%3D%3D; _nk_=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; cookie17=Vy0QnDnkh%2BXG; l=AmhoxXcjJTw-zu-yVrK/Uuc5uF16kcyb; uc1=cookie16=W5iHLLyFPlMGbLDwA%2BdvAGZqLg%3D%3D&cookie21=W5iHLLyFfXb%2BYu1IQCuW1w%3D%3D&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D&existShop=true&pas=0&cookie14=UoTeOLG51d2HBw%3D%3D&tag=8&lng=zh_CN; apush244d3af8cb013571ea511c2c367b87fd=%7B%22ts%22%3A1526817123647%2C%22parentId%22%3A1526817120640%7D; isg=BOLiXWaGzTypw9FgAnW2V6FqM2iEmyE3pwpeSix7YNUC_4N5FcIwXf0ZKzsDaV7l";

    private static int testDeep = 0;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



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

    @Override
    public abstract void run() throws IOException;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



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