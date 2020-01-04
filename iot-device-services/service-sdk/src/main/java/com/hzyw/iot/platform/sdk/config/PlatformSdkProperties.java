package com.hzyw.iot.platform.sdk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/19.
 */
@Data
@ConfigurationProperties(prefix = "sdk")
public class PlatformSdkProperties {

    private final CallBack callBack = new CallBack();
    private final Server server = new Server();
    private final Handlers handlers = new Handlers();
    private boolean preferUrl = true;

    @Data
    public static class Server {
        private String url;
        private String name;
    }

    @Data
    public static class CallBack {
        private String url;
        private String name;
    }

    @Data
    public static class Handlers {
        private String include = "default";

        public String[] getHandlersAsList() {
            return this.include.split(",");
        }
    }

}

