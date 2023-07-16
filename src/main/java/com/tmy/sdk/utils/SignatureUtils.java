package com.tmy.sdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 生成签名
 *
 * @author kancsd
 * @date 2023/06/14
 */
public class SignatureUtils {
    public static String generateSignature(Map<String, String> parameters, String secretKey) {
        // 将参数按照字典顺序排序
        List<String> sortedKeys = new ArrayList<>(parameters.keySet());
        Collections.sort(sortedKeys);

        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys) {
            String value = parameters.get(key);
            sb.append(key).append(value);
        }

        sb.append(secretKey);

        // 使用MD5进行签名计算
        String signature = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(sb.toString().getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            signature = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return signature;
    }

}
