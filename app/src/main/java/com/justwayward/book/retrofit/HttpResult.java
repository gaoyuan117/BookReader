package com.justwayward.book.retrofit;

/**
 * Created by admin on 2017/3/27.
 */

public class HttpResult<T> {
    public int code;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
