package com.code.service;

import com.code.model.Field;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @title: CodeService
 * @projectName: code-generator
 * @description: TODO
 * @author: Zack_Tzh
 * @date: 2020/2/12  16:23
 */
@Service
public class CodeService {
    @Autowired
    DataSource dataSource;

    @Autowired
    Configuration configuration;

    @Value("${AutoCode.rootPath}")
    private String rootPath;

    @Value("${AutoCode.useMapper}")
    private String useMapper;

    @Value("${AutoCode.useLombok}")
    private Boolean useLombok;

    @Value("${AutoCode.myAuthor}")
    private String myAuthor;

    @Value("${AutoCode.projectName}")
    private String projectName;


    /**
     * 获取表名和字段
     *
     * @throws SQLException
     */
    public Map<String, Collection<Field>> tableList() throws SQLException {

        Map<String, Collection<Field>> map = new HashMap<>(10);

        Connection connection = dataSource.getConnection();
        ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});

        while (rs.next()) {
            //获取表名
            String table = rs.getString("TABLE_NAME");


            //获取表的字段
            Map<String, Field> fileds = getTableFiled(table);
            List<String> ids = getTableIds(table);

            //标记为主键的字段
            ids.forEach(id -> fileds.get(id).setKey(true));

            map.put(table, fileds.values());
        }
        rs.close();
        connection.close();
        return map;
    }

    /**
     * 将表名转换为类名
     *
     * @param table
     * @return
     */
    private String reName(String table) {

        String[] className = table.split("_");

        StringBuffer newt = new StringBuffer();

        for (int i = 1; i < className.length; i++) {
            char[] chars = className[i].toCharArray();
            if ((((int) chars[0]) - 32) <= 90 && (((int) chars[0]) - 32) >= 65) {
                chars[0] = (char) (((int) chars[0]) - 32);
            }
            newt.append(new String(chars));
        }
        return newt.toString();
    }

    /**
     * 获取表主键
     *
     * @throws SQLException
     */
    private List<String> getTableIds(String table) throws SQLException {

        List<String> list = new ArrayList<>();

        Connection con = dataSource.getConnection();

        ResultSet rs = con.getMetaData().getPrimaryKeys(con.getCatalog(), null, table);

        while (rs.next()) {
            list.add(rs.getString("COLUMN_NAME"));
        }

        rs.close();
        con.close();
        return list;
    }

    /**
     * 获取字段名称
     *
     * @param table
     * @return
     * @throws SQLException
     */
    private Map<String, Field> getTableFiled(String table) throws SQLException {
        Map<String, Field> data = new HashMap<>(10);
        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("desc " + table);
        ResultSetMetaData MetaData = resultSet.getMetaData();
        if (MetaData == null) {
            System.out.println(table + "表没有字段!");
            return null;
        }

        Map<String, String> dataTypeMap = getDataBaseDataType();
        Set<String> keySet = dataTypeMap.keySet();

        while (resultSet.next()) {
            String type = resultSet.getString("Type");
            String filed = resultSet.getString("Field");
            for (String key : keySet) {
                if (type.contains(key)) {
                    data.put(filed, new Field(filed, dataTypeMap.get(key), false));
                    break;
                }
            }
            if (!data.containsKey(filed)) {
                System.out.println(type + "类型暂时无法转换！请添加后使用");
                data.put(filed, new Field(filed, type, false));
            }
        }

        return data;
    }

    /**
     * 类型转换
     *
     * @return
     */
    private Map<String, String> getDataBaseDataType() {
        Map<String, String> typeMap = new HashMap<String, String>(12);
        typeMap.put("int", "Integer");
        typeMap.put("tinyint", "Integer");
        typeMap.put("smallint", "Integer");
        typeMap.put("mediumint", "Integer");
        typeMap.put("bigint", "Integer");
        typeMap.put("float", "Float");
        typeMap.put("double", "Double");
        typeMap.put("decimal", "BigDecimal");
        typeMap.put("varchar", "String");
        typeMap.put("char", "String");
        typeMap.put("datetime", "Date");
        typeMap.put("timestamp", "Date");
        return typeMap;
    }

    /**
     * 输出一个模板文件内容
     *
     * @param packageName 输出的包名
     * @param name        输出的文件名
     * @param content     输出的文件内容
     * @throws IOException
     */
    private void writeFile(String packageName, String name, String content) throws IOException {
        String path = rootPath;
        path += "/" + packageName.replace(".", "/");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        path += "/" + name;
        Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE_NEW);
    }

    /**
     * 创建Mapper文件
     *
     * @param packageName
     * @param table
     * @throws Exception
     */
    public void createMapper(String packageName, String table) throws Exception {
        String mapperPackage = String.format("%s.mapper", packageName);
        //将表名转换为类名，去掉下划线的前缀
        String className = reName(table);
        String fileName = String.format("%sMapper.java", className);

        Map<String, Object> data = new HashMap<>(3);
        data.put("mapperPackage", mapperPackage);
        data.put("modelPackage", String.format("%s.pojo", packageName));
        data.put("modelClass", className);
        data.put("projectName", projectName);
        data.put("author", myAuthor);
        data.put("date", getDateToString());

        StringWriter out = new StringWriter();
        Template template = null;
        if (useMapper.equals("0")) {
            template = configuration.getTemplate("mapper.ftl");
        } else {
            template = configuration.getTemplate("mybatisplus.ftl");
        }
        template.process(data, out);
        writeFile(mapperPackage, fileName, out.toString());
    }

    /**
     * 生成pojo类
     *
     * @param packageName
     * @param table
     * @param fields
     */
    public void createPojo(String packageName, String table, Collection<Field> fields) throws IOException, TemplateException {
        //拼接包名
        String pojoPackage = String.format("%s.pojo", packageName);
        //将表名转换为类名，去掉下划线的前缀
        String className = reName(table);
        //拼接类名
        String fileName = String.format("%s.java", className);
        //拼接导入的包
        String importPackage = getPojoImport(fields);
        //拼接添加的注解
        String annotation = getPojoAnnotation(table);
        //具体属性
        String body = getPojoBody(fields);

        //添加到map中
        Map<String, Object> data = new HashMap<>(5);
        data.put("pojoPackage", pojoPackage);
        data.put("importPackage", importPackage);
        data.put("annotation", annotation);
        data.put("className", className);
        data.put("body", body);
        data.put("projectName", projectName);
        data.put("author", myAuthor);
        data.put("date", getDateToString());

        StringWriter out = new StringWriter();
        Template template = configuration.getTemplate("pojo.ftl");
        template.process(data, out);
        writeFile(pojoPackage, fileName, out.toString());
    }

    /**
     * 填充模版中annotation的数据
     *
     * @param table
     * @return
     */
    private String getPojoAnnotation(String table) {
        StringBuffer annotation = new StringBuffer();
        if (useMapper.equals("1")) {
            //使用mybatisPlus
            annotation.append("@TableName(\"" + table + "\")\n");
        } else {
            //使用通用mapper
            annotation.append("@Table(name=\"" + table + "\")\n");
        }
        if (useLombok == true) {
            //使用lombok
            annotation.append("@Data");
        }
        return annotation.toString();
    }

    /**
     * 填充模版中Import的数据
     *
     * @param fields
     * @return
     */
    private String getPojoImport(Collection<Field> fields) {
        StringBuffer importPackage = new StringBuffer();
        boolean flag = false;
        HashSet<String> set = new HashSet<>();
        for (Field field : fields) {
            set.add(field.getType());
            if (field.getKey()) {
                flag = true;
            }
        }

        for (String s : set) {
            if (s.equals("Date")) {
                importPackage.append("import java.util.Date;\n");
            }
            if (s.equals("BigDecimal")) {
                importPackage.append("import java.math.BigDecimal;\n");
            }
        }

        if (useMapper.equals("1")) {
            //使用mybatisPlus
            importPackage.append("import com.baomidou.mybatisplus.annotations.TableName;\n");
            if (flag == true) {
                importPackage.append("import com.baomidou.mybatisplus.annotations.TableId;\n");
                importPackage.append("import com.baomidou.mybatisplus.enums.IdType\n");
            }
        } else {
            //使用通用mapper
            importPackage.append("import javax.persistence.Table;\n");
            if (flag == true) {
                importPackage.append("import javax.persistence.Id;\n");
            }
        }

        if (useLombok == true) {
            //使用lomobok
            importPackage.append("import lombok.Data;\n");
        }

        return importPackage.toString();
    }

    /**
     * 填充模版中body的数据
     *
     * @param fields
     * @return
     */
    private String getPojoBody(Collection<Field> fields) {
        StringBuffer body = new StringBuffer();
        for (Field field : fields) {
            if (field.getKey()) {
                if (useMapper.equals("1")) {
                    body.append("    @TableId(type = IdType.INPUT)\n");
                }else {
                    body.append("    @Id\n");
                }
            }
            body.append("    private ").append(field.getType() + " ").append(field.getName() + ";\n");
        }
        return body.toString();
    }

    /**
     * 获取当前时间
     * @return
     */
    private String getDateToString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
}
