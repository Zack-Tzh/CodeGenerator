package ${controllerPackage};

import ${packageName}.util.Result;
import ${packageName}.pojo.${modelClass};
import ${packageName}.service.${modelClass}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


/**
 * @title: ${modelClass}Mapper
 * @projectName: ${projectName}
 * @description: TODO
 * @author: ${author}
 * @date: ${date}
 */
@RestController
@CrossOrigin
@RequestMapping("/${modelClassMin}")
public class ${modelClass}Controller {


    @Autowired
    private ${modelClass}Service ${modelClassMin}Service;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<${modelClass}> ${modelClassMin}List = ${modelClassMin}Service.findAll();
        return new Result(0,"查询成功",${modelClassMin}List) ;
    }

    /***
    * 根据ID查询数据
    * @param id
    * @return
    */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        ${modelClass} ${modelClassMin} = ${modelClassMin}Service.findById(id);
        return new Result(0,"查询成功",${modelClassMin});
    }


    /***
    * 新增数据
    * @param ${modelClassMin}
    * @return
    */
    @PostMapping
    public Result add(@RequestBody ${modelClass} ${modelClassMin}){
        ${modelClassMin}Service.add(${modelClassMin});
        return new Result(0,"添加成功");
    }


    /***
    * 修改数据
    * @param ${modelClassMin}
    * @param id
    * @return
    */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody ${modelClass} ${modelClassMin},@PathVariable Long id){
        ${modelClassMin}.setId(id);
        ${modelClassMin}Service.update(${modelClassMin});
        return new Result(0,"修改成功");
    }


    /***
    * 根据ID删除数据
    * @param id
    * @return
    */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        ${modelClassMin}Service.delete(id);
        return new Result(0,"删除成功");
    }

}
