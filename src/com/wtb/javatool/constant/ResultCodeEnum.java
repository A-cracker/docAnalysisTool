package com.wtb.javatool.constant;

public enum ResultCodeEnum {
    // 调试状态码
    DEBUG_SUCCESS(10000, "剧本调试成功"),
    DEBUG_ERROR_BANDNAME(10001, "剧本调试失败，无效帮区名称"),
    DEBUG_ERROR_USERNAME(10002, "剧本调试失败，无效用户名称"),
    DEBUG_ERROR_TOOLNAME(10003, "剧本调试失败，无效工具名称"),
    DEBUG_ERROR_UNITNAME(10004, "剧本调试失败，无效部件名称"),
    DEBUG_ERROR_PARAMNAME(10005, "剧本调试失败，无效参数名称"),
    DEBUG_ERROR_PARAMTYPENAME(10006, "剧本调试失败，无效参数类型"),
    DEBUG_ERROR_PERMISSION(10007, "剧本调试失败，当前用户缺少工具运行权限"),
    DEBUG_ERROR_CHATROOM(10008, "剧本调试失败，消息板获取失败"),

    // 运行状态码
    RUN_SUCCESS(20000, "剧本运行成功"),
    RUN_ERROR_BANDNAME(20001, "剧本运行失败，无效帮区名称"),
    RUN_ERROR_USERNAME(20002, "剧本运行失败，无效用户名称"),
    RUN_ERROR_TOOLNAME(20003, "剧本运行失败，无效工具名称"),
    RUN_ERROR_UNITNAME(20004, "剧本运行失败，无效部件名称"),
    RUN_ERROR_PARAMNAME(20005, "剧本运行失败，无效参数名称"),
    RUN_ERROR_PARAMTYPENAME(20006, "剧本运行失败，无效参数类型"),
    RUN_ERROR_PERMISSION(20007, "剧本运行失败，当前用户缺少工具运行权限"),
    RUN_ERROR_CHATROOM(10008, "剧本运行失败，消息板获取失败"),

    // 剧本解析状态码
    PARSER_ERROR(30001, "剧本解析失败，请按照规则形成剧本"),

    // 草稿剧本相关
    DRAFT_DELETE_EXCEPTION(40001, "草稿剧本正在运行中，请停用后删除"),




    // 分析剧本相关
    ANALYSE_DELETE_EXCEPTION(50001, "分析剧本删除失败"),

    // 核心接口请求
    BAND_BY_ID_EXCEPTION(60001, "/core/v4/band/{?} 接口存在问题，获取帮区名称失败"),
    USER_BY_ID_EXCEPTION(60002, "/core/v4/user/{?} 接口存在问题，获取用户名称失败"),


    // 草稿章节相关,
    DRAFT_CHAPTER_UPDATE_EXCEPTION(70001, "草稿章节关联分析剧本失败"),


    UNIMPLEMENT_EXCEPTION(80001,"未实现父类接口异常"),

    UNAUTHORISED_EXCEPTION(90001,"该用户没有权限执行当前工具"),

    // 状态码
    SUCCESS(200, "请求成功"),
    ERROR(400, "请求出错");

    private int code;
    private String msg;
    ResultCodeEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
