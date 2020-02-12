package com.code.config;

import com.code.controller.CodeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @title: MyCommandLineRunner
 * @projectName: code-generator
 * @description: TODO
 * @author: Zack_Tzh
 * @date: 2020/2/12  16:33
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner, Ordered {
    @Autowired
    private CodeController codeController;

    @Value("${AutoCode.createPojo}")
    private Integer createPojo;

    @Value("${AutoCode.createDao}")
    private Integer createDao;

    @Value("${AutoCode.createServiceAndController}")
    private Integer createServiceAndController;

    /**
     * 调用程序执行
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        codeController.AutoCode(createPojo,createDao,createServiceAndController);
    }

    /**
     * 值越小越先执行
     * @return
     */
    @Override
    public int getOrder() {
        return 100;
    }
}
