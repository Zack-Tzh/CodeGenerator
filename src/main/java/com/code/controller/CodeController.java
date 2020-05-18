package com.code.controller;

import com.code.model.Field;
import com.code.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Map;

/**
 * @title: CodeController
 * @projectName: code-generator
 * @description: TODO
 * @author: Zack_Tzh
 * @date: 2020/2/12  16:25
 */
@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;


    public void AutoCode(Integer createPojo, Integer createDao, Integer createServiceAndController) throws Exception {
        //获取数据库中的表
        Map<String, Collection<Field>> tables = codeService.tableList();

        //生成pojo
        if (createPojo == 1) {
            for (String table : tables.keySet()) {
                codeService.createPojo(table,tables.get(table));
            }
        }

        //生成dao
        if (createDao == 1) {
            for (String table : tables.keySet()) {
                codeService.createMapper(table);
            }
        }

        //生成ServiceAndController
        if (createServiceAndController == 1) {
            for (String table : tables.keySet()) {
                codeService.createController(table);
            }
        }


    }
}
