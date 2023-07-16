package com.tmy.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * http工具类
 *
 * @author kancsd
 * @date 2023/06/17
 */
public class HttpUtils {
    public static String buildUrlWithQueryParams(String baseUrl, Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        boolean isFirstParam = true;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value != null) {
                String encodedKey = URLEncoder.encode(key, String.valueOf(StandardCharsets.UTF_8));
                String encodedValue = URLEncoder.encode(value.toString(), String.valueOf(StandardCharsets.UTF_8));

                if (isFirstParam) {
                    urlBuilder.append("?");
                    isFirstParam = false;
                } else {
                    urlBuilder.append("&");
                }

                urlBuilder.append(encodedKey).append("=").append(encodedValue);
            }
        }

        return urlBuilder.toString();
    }
}
