package ${mapperPackage};

import ${modelPackage}.${modelClass};
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @title: ${modelClass}Mapper
 * @projectName: ${projectName}
 * @description: TODO
 * @author: ${author}
 * @date: ${date}
 */
@Repository
public interface ${modelClass}Mapper extends Mapper<${modelClass}> {}
