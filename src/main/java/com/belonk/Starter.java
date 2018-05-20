package com.belonk;

import com.belonk.taobao.service.CommodityCategoryCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by sun on 2018/5/19.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@ImportResource({"classpath:config-*.xml"})
public class Starter implements ApplicationRunner {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(Starter.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    @Qualifier("dynamicCommodityCategoryCrawler")
    private CommodityCategoryCrawler commodityCategoryCrawler;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Starter.class);
        builder.run(args);
//        SpringApplication.run(Starter.class, args);
    }

    /**
     * 如果您需要在SpringApplication启动之后运行一些特定的代码，您可以实现ApplicationRunner
     * 或CommandLineRunner接口。这两个接口都以相同的方式工作，并提供了一个单独的运行方法，
     * 在SpringApplication.run(…)完成之前就会调用。
     * <p>
     * CommandLineRunner接口提供对应用程序参数的访问作为一个简单的字符串数组，而ApplicationRunner
     * 使用上面讨论的ApplicationArguments接口。
     * <p>
     * 另外，还可以实现org.springframework.core.Ordered接口或使用org.springframework.core.annotation.Order注解，
     * 如果定义了多个CommandLineRunner或ApplicationRunner bean，则必须按特定顺序调用它。
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
//       Set<String> names =  args.getOptionNames();
//        for (String name : names) {
//            List<String> vs = args.getOptionValues(name);
//            System.out.println(name);
//            System.out.println(vs);
//            System.out.println("=================================");
//        }
        commodityCategoryCrawler.run();
//        log.info("xxxxxxxxxxxxxxxxx");
//        log.error("xxxxxxxxxxxxxxxxx");
//        log.debug("xxxxxxxxxxxxxxxxx");
//        log.warn("xxxxxxxxxxxxxxxxx");
//        log.trace("xxxxxxxxxxxxxxxxx");
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
}