package com.huacheng.myretrofit.http;

/**
 * Description: 网络工具类
 * created by wangxiaotao
 * 2018/12/5 0005 上午 10:16
 */
public class ApiHttpClient {
    public static String BASE_URL = "http://test.hui-shenghuo.cn/apk41/";

    public static String TOKEN;
    public static String TOKEN_SECRET;

    public static void setTokenInfo(String token, String tokenSecret) {
        TOKEN = token;
        TOKEN_SECRET = tokenSecret;
    }

    //选择房产类型
    public static String GET_HOUSETYPE =  "property/get_pro_housesType";

    //服务首页-更多服务
    public static String GET_SERVICEMORE =  "index/serviceMore";
    //排序
    public static String GET_DEFAULT =  "secondHouseType/getdefault";
    //圈子
    public static String GET_CIRCLE =  "social/get_social";
}
