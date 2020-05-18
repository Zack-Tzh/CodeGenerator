package ${servicePackage};

import ${packageName}.pojo.${modelClass};
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @title: ${modelClass}Mapper
 * @projectName: ${projectName}
 * @description: TODO
 * @author: ${author}
 * @date: ${date}
 */
public interface ${modelClass}Service {

    /***
     * 查询所有
     * @return
     */
    List<${modelClass}> findAll();

    /**
    * 根据ID查询
    * @param id
    * @return
    */
    ${modelClass} findById(Long id);

    /***
    * 新增
    * @param ${modelClassMin}
    */
    void add(${modelClass} ${modelClassMin});

    /***
    * 修改
    * @param ${modelClassMin}
    */
    void update(${modelClass} ${modelClassMin});

    /***
    * 删除
    * @param id
    */
    void delete(Long id);
}
