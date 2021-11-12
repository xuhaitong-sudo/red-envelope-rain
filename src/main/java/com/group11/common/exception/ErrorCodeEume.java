package com.group11.common.exception;

/**
 * 错误状态码
 *
 * @author Xu Haitong
 * @since 2021/11/3 20:20
 */
public enum ErrorCodeEume {

    INVALID_UID(1, "uid 无效"),
    MAX_COUNT(2, "用户已达到最大抢到红包次数"),
    FAILURE_SNATCH(3, "未抢到红包"),
    SOLD_OUT(4, "红包发光了"),
    ENVELOPE_NOT_EXIST(5, "红包不存在"),
    SYSTEM_BUSY(6, "系统繁忙"),
    ACCESS_LIMIT(7, "操作过快，请稍后再试");

    private int code;
    private String msg;

    ErrorCodeEume(int code, String msg) {
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
