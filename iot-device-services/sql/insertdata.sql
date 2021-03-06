insert  into `DEVICE_ACCESS_T`(`DEVICE_ID`,`DEVICE_DOMAIN`,`REGISTRATION`,`PROTOCOL`,`PROTOCOL_VERSION`,`DEVICE_IPV4`,`DEVICE_IPV6`,`DEVICE_PORT`,`LONGITUDE`,`LATITUDE`,`LOCATION_TYPE`,`ACCESS_TIME`,`LEAVE_TIME`,`CREATE_TIME`) values

('1030-0621babd82add79e-200d-ffff-05bf',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:31:06','2019-09-10 18:31:06','2019-09-10 18:31:06'),

('1030-38f6e082eb3fb313-200d-ffff-4819',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:26:56','2019-09-10 18:26:56','2019-09-10 18:26:56'),

('1030-3c474cf77e1a0376-200d-ffff-74a8',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:27:24','2019-09-10 18:27:24','2019-09-10 18:27:24'),

('1030-5a00228291d2497a-200d-ffff-5bae',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:31:10','2019-09-10 18:31:10','2019-09-10 18:31:10'),

('1030-5fc692ab16143487-200d-ffff-13b5',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:31:07','2019-09-10 18:31:07','2019-09-10 18:31:07'),

('1030-85e87393b1b44870-200d-ffff-61e7',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:33:31','2019-09-10 18:33:31','2019-09-10 18:33:31'),

('1030-8aacf7381352c8cf-200d-ffff-5c02',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:33:31','2019-09-10 18:33:31','2019-09-10 18:33:31'),

('1030-bd5d721b3f8a409f-200d-ffff-0392',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:33:31','2019-09-10 18:33:31','2019-09-10 18:33:31'),

('1030-ca0eac3c009f4cd8-200d-ffff-cdb0',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:33:31','2019-09-10 18:33:31','2019-09-10 18:33:31'),

('1030-d393f460ec55c1f3-200d-ffff-9de3',4144,2,NULL,NULL,'192.168.3.161','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-10 18:31:04','2019-09-10 18:31:04','2019-09-10 18:31:04'),

('10308f00b204e9800998200dffff70a0',4144,2,NULL,NULL,'192.168.3.104','2001:0db8:85a3:08d3:1319:8a2e:0370:7344',NULL,NULL,NULL,NULL,'2019-09-09 16:21:06','2019-09-09 16:21:06','2019-09-09 16:21:06');


insert  into DEVICE_ATTRIBUTE_T(`ATTR_KEY`,`ATTR_NAME`,`VALUE_TYPE`,`UNIT`,`METADATA`) values

('air_pressure','气压','java.lang.String','hPa',NULL),

('brightness',NULL,'java.lang.String','cd',NULL),

('humidity','湿度','java.lang.String','rh',NULL),

('mem_free',NULL,'java.lang.String','byte',NULL),

('noise','噪声','java.lang.String','db',NULL),

('on_off',NULL,'java.lang.String','',NULL),

('pm10','PM10','java.lang.String','ug/m³',NULL),

('pm_2_5','PM2.5','java.lang.String','ug/m³',NULL),

('rainfall','雨量','java.lang.String','mm',NULL),

('screen_height',NULL,'java.lang.String','pix',NULL),

('screen_width',NULL,'java.lang.String','pix',NULL),

('temperature','温度','java.lang.String','C',NULL),

('uptime',NULL,'java.lang.String','ms',NULL),

('wind_direction','风向','java.lang.String','°',NULL),

('wind_speed','风速','java.lang.String','m/s',NULL),

('www','气压','java.lang.String','hPa',NULL);