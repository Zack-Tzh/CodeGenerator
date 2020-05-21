package ${serviceImplPackage};

import ${packageName}.pojo.${modelClass};
import ${packageName}.dao.${modelClass}Mapper;
import ${packageName}.service.${modelClass}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: ${modelClass}Mapper
 * @projectName: ${projectName}
 * @description: TODO
 * @author: ${author}
 * @date: ${date}
 */
@Service
public class ${modelClass}ServiceImpl implements ${modelClass}Service {

    @Autowired
    private ${modelClass}Mapper ${modelClassMin}Mapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<${modelClass}> findAll() {
    return ${modelClassMin}Mapper.selectAll();
    }

    /**
    * 根据ID查询
    * @param id
    * @return
    */
    @Override
    public ${modelClass} findById(Long id){
    return  ${modelClassMin}Mapper.selectByPrimaryKey(id);
    }


    /**
    * 增加
    * @param ${modelClassMin}
    */
    @Override
    public void add(${modelClass} ${modelClassMin}){
    ${modelClassMin}Mapper.insert(${modelClassMin});
    }


    /**
    * 修改
    * @param ${modelClassMin}
    */
    @Override
    public void update(${modelClass} ${modelClassMin}){
    ${modelClassMin}Mapper.updateByPrimaryKey(${modelClassMin});
    }

    /**
    * 删除
    * @param id
    */
    @Override
    public void delete(Long id){
    ${modelClassMin}Mapper.deleteByPrimaryKey(id);
    }

}
