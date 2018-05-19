package com.belonk.test.util;

import com.belonk.util.HttpUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sun on 2018/5/19.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class HttpUtilTest {
    @Test
    public void testGet() throws IOException {
        String resp = HttpUtil.me().get("https://www.baidu.com", null);
        System.out.println(resp);
    }

    @Test
    public void testPost() throws IOException {
        String resp = HttpUtil.me().post("http://www.baidu.com", null, null);
        System.out.println(resp);
    }
}