package com.highnes.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: Result
 * @projectName: supply
 * @description: TODO
 * @author: Zack_Tzh
 * @date: 2020/3/25  17:53
 */
@Data
@ApiModel("响应信息")
public class Result<T> {

    @ApiModelProperty("返回状态码 0:请求成功 -1:请求失败 -2:商品已删除")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
