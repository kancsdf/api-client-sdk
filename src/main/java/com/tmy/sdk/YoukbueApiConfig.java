package com.tmy.sdk;

import com.tmy.sdk.client.YoukbueClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * youkbue api配置
 *
 * @author kancsd
 * @date 2023/06/14
 */

@Configuration
@ConfigurationProperties("youkbue.client")
@Data
@ComponentScan
public class YoukbueApiConfig {

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 解密密钥
     */
    private String secretKey;

    @Bean
    public YoukbueClient youkbueClient(){
        return new YoukbueClient(accessKey,secretKey);
    }

}
