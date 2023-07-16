package com.tmy.sdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.tmy.sdk.utils.HttpUtils;
import com.tmy.sdk.utils.SignatureUtils;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * youkbue客户端 调用开放接口
 *
 * @author kancsd
 * @date 2023/06/14
 */
public class YoukbueClient {

    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 解密密钥
     */
    private String secretKey;

    /**
     * 执行get请求
     *
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    public String doGetByParams(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        return doHttp(url, params, GET_METHOD);
    }

    /**
     * 执行post请求
     *
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    public String doPostByParams(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        return doHttp(url, params, POST_METHOD);
    }


    /**
     * 发送post 请求 json-body
     *
     * @param url  url
     * @param json 身体
     * @return {@link String}
     */
    public String doPostByJson(String url, String json) {
        return HttpRequest.post(url)
                .body(json)
                .charset(CharsetUtil.UTF_8)
                .contentType("application/json;charset=UTF-8")
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .addHeaders(getHeaderMap(String.valueOf(json)))
                .execute().body();
    }

    /**
     * 执行post请求
     *
     * @param url    url
     * @param json   json
     * @param params 参数个数
     * @return {@link String}
     */
    public String doPost(String url, String json, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpRequest httpRequest = HttpRequest.post(url);

        // 判断是否有请求体
        if (StrUtil.isNotEmpty(json)) {
            httpRequest.body(json);
            httpRequest.contentType("application/json;charset=UTF-8")
                    .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8");
        }

        // 判断是否有请求参数
        if (params != null && !params.isEmpty()) {
            String paramsUrl = HttpUtils.buildUrlWithQueryParams(url, params);
            httpRequest.setUrl(paramsUrl);
        }

        httpRequest.addHeaders(getHeaderMap(""));

        HttpResponse response = httpRequest.execute();
        return response.body();
    }


    /**
     * 执行请求
     *
     * @param url    url
     * @param params 参数个数
     * @param method 方法
     * @return {@link String}
     */
    protected String doHttp(String url, Map<String, Object> params, String method) throws UnsupportedEncodingException {
        String result = "";
        if (method.equals(GET_METHOD)) {
            return doGet(url, params);
        } else if (method.equals(POST_METHOD)) {
            return doPost(url, params);
        }
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> headerMap = new HashMap<>(16);
        headerMap.put("accessKey", accessKey);
        headerMap.put("once", RandomUtil.randomNumbers(4));
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("body", body);
        headerMap.put("sign", SignatureUtils.generateSignature(headerMap, secretKey));
        return headerMap;
    }

    /**
     * 执行Get方法
     *
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    private String doGet(String url, Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params)) {
            return HttpRequest.get(url).addHeaders(getHeaderMap(" "))
                    .contentType("application/json;charset=UTF-8")
                    .charset(CharsetUtil.UTF_8)
                    .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                    .timeout(20000)
                    .execute().body();
        }
        return HttpRequest.get(url)
                .contentType("application/json;charset=UTF-8")
                .charset(CharsetUtil.UTF_8)
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .form(params)
                .addHeaders(getHeaderMap(String.valueOf(params)))
                .timeout(20000)
                .execute().body();
    }

    /**
     * 执行Post请求
     *
     * @param url    url
     * @param params 参数个数
     * @return {@link String}
     */
    private String doPost(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        if (ObjectUtils.isEmpty(params)) {
            return HttpRequest.post(url).addHeaders(getHeaderMap(" "))
                    .contentType("application/json;charset=UTF-8")
                    .charset(CharsetUtil.UTF_8)
                    .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                    .timeout(20000)
                    .execute().body();
        }
        String paramsUrl = HttpUtils.buildUrlWithQueryParams(url, params);
        return HttpRequest.post(paramsUrl).contentType("application/json;charset=UTF-8")
                .charset(CharsetUtil.UTF_8)
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .form(params)
                .addHeaders(getHeaderMap(String.valueOf(params)))
                .timeout(20000)
                .execute().body();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public YoukbueClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public YoukbueClient() {

    }
}
