package com.ritto.srm;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/19
 * @Description: 本方法将在项目启动后执行
 * @Modified By:
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        System.out.println("本方法将在项目启动后执行");

    }
}
