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

    public static String COOKIE = "t=a048ca1248def5d46e1b99b9dfd3329a; thw=cn; miid=799547686855897634; cna=H7B2E4MdaycCAXZwYRo1ZKJ5; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; hng=CN%7Czh-CN%7CCNY%7C156; _m_h5_tk=24b5b3560546171bd5a4272b8c64541e_1526374832576; _m_h5_tk_enc=c432c10abc5c34fab68623199fd0ea91; UM_distinctid=163710934cbcd-0612e153b7df0e-f373567-e1000-163710934cc809; enc=Ks1OlHbgavv2RgZVt59ClZRCMMoQY5BGgKMG8SsIud3TPFLk45z2l6hlm7SiSpmA%2FCdwnRPe89te0Z5wi9R2Mg%3D%3D; _uab_collina=152663658953196205108952; _umdata=85957DF9A4B3B3E8EA9242D8DA57705DF6C260957D900C846BEE60523F49E91D9B2F8E32F60C367CCD43AD3E795C914C3C0F0F0AE85FA3077A447E6CC0EA0F5C; cookie2=37683e1358457d1ebfc08ea95e509f83; _tb_token_=e07bf55d57de5; JSESSIONID=0B11663F78213B3105B3727DC2F69555; v=0; lgc=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; tracknick=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; dnk=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; publishItemObj=Ng%3D%3D; mt=np=&ci=-1_1; uc3=nk2=tML4zJvlq4bLEw%3D%3D&id2=Vy0QnDnkh%2BXG&vt3=F8dBz494%2FCh9dl5F%2F3s%3D&lg2=W5iHLLyFOGW7aA%3D%3D; existShop=MTUyNjgxMjczOA%3D%3D; sg=%E4%B8%8B7a; csg=db38902a; cookie1=UNky63b4vUvDU75ViU87yTzbGf4Q011kVuzWqsnKdPw%3D; unb=413468597; skt=b0de98028cc829ed; _cc_=U%2BGCWk%2F7og%3D%3D; _l_g_=Ug%3D%3D; _nk_=%5Cu7EB5%5Cu6A2A%5Cu4E28%5Cu5929%5Cu4E0B; cookie17=Vy0QnDnkh%2BXG; l=AsDAvWXlDbSWNhe6bnrnyk7hEEWSDqQT; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=U%2BGCWk%2F7owVBK2Hga0xuWA%3D%3D&cookie15=V32FPkk%2Fw0dUvg%3D%3D&existShop=true&pas=0&cookie14=UoTeOLG50A3zuA%3D%3D&tag=8&lng=zh_CN; apush244d3af8cb013571ea511c2c367b87fd=%7B%22ts%22%3A1526812761004%2C%22parentId%22%3A1526812742952%7D; isg=BKGhmFzxXnH_K_KFIxkuOvcysG175tI6AP89bwN2o6gGasA8S58-EDnsyJ5spK14";

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