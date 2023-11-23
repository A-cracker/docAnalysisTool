package com.wtb.javatool.util;

import com.wtb.javatool.constant.DataType;
import com.wtb.javatool.constant.ResultCodeEnum;
import com.wtb.javatool.constant.ShowType;
import lombok.Data;

/**
 * 返回数据
 */
@Data
public class R {

    // 是否成功
    private Boolean success;

    // 返回码
    private Integer code;

    // 返回消息
    private String message;

    // 数据类型 1.string（result: string）；2. map（result: {}）；；3. list（result: list）
    private String dataType;

    // 处理方式 1.print（控制台打印输入）；2.putToContext（放入语境）；3.button（页面链接；操作链接）
    private String showType;

    // 返回数据
    // private Map<String, Object> data = new HashMap<String, Object>();

    // 返回结果
    private Object result = new Object();

    // 私有构造方法
    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMsg());
        r.setShowType(ShowType.PRINT.getName());
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCodeEnum.ERROR.getCode());//请求出错
        r.setMessage(ResultCodeEnum.ERROR.getMsg());
        r.setShowType(ShowType.PRINT.getName());
        return r;
    }

    public R STR() {
        this.setDataType(DataType.STR.getName());
        return this;
    }

    public R LIST() {
        this.setDataType(DataType.LIST.getName());
        return this;
    }

    public R MAP() {
        this.setDataType(DataType.MAP.getName());
        return this;
    }

    // 返回this 可以链式编程
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 可以传入Map,或者直接一个键值对
//    public R put(String key, Object value) {
//        this.data.put(key, value);
//        return this;
//    }
//
//    public R put(Map<String, Object> map) {
//        this.setData(map);
//        return this;
//    }

    public R result(Object result) {
        this.setResult(result);
        return this;
    }

    public R PRINT() {
        this.setShowType(ShowType.PRINT.getName());
        return this;
    }

    public R PUT() {
        this.setShowType(ShowType.PUT.getName());
        return this;
    }

    public R LINK() {
        this.setShowType(ShowType.LINK.getName());
        return this;
    }
}