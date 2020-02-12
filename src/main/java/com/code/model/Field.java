package com.code.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    // 字段名称
    @Id
    String name;
    // 字段类型
    String type;
    // 是否主键
    Boolean key;
}
