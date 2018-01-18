/*
 * PayConstants     2016/12/2 17:11
 * Copyright (c) 2016 Koterwong All right reserved
 */
package com.justwayward.book;

/**
 * Created by Koterwong on 2016/12/2 17:11
 */
public interface Constants {
    interface AliPay {
        /**
         * 支付宝AppID
         */
        String APPID = "2017092208865791";

        /**
         * 支付宝回调后台的NotifyUrl
         */
        String NOTIFY_URL = AppConfig.BaseUrl + "/pay/alipayNotify.php";

        /**
         * 商户私钥，pkcs8格式
         */

        String RSA_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGqJhHNrExtUiNor5jlkWAo4zKVrsDRTiAxuJ6QGBdhIZLJR7M+UD8zGnGVvgTcGIvqrtdJYHdQqvmr6ENzF9khK9GVh9L2/tO7ZAmJGqrxx3c9YCVcmpoqDM+n0g37xcIHUln5hTYGnCldkkgUVV6JIKg18pV5Mmo1n6Zk8YqbeK7GssJP9LhP4G98vMFCE3jL2ivfkOXiYB+7QyuGyQtSNz9cMV8HRZHo8gGt9Lxo6sLHjRQApftnyRaVesO/+iD6ur/RVlaW0zSAW683hCnTIZcDk2/3tP1jMFFXtOuyaRqOuZ1wAB1OOvG/PljZsfxJkRqVBrdBmKisKqT19qBAgMBAAECggEAdCmGS5CDpQTklMI6iUA9rq3nqzjMVRLmnjhzcNCOEL5c9wALpPi0Nh/Ec4PctzAwkzwuKzlK4Os9zV/eiD+wXs8TQJLUpqUbEshenHi3yH2ZZl4mUUWFQ8ktg6z2KMJI1QgmpclWW+HbIPF8PbPMD/Tk9wmQEy6tQ8fdKHDQ9xHZln2D+Cur3SVo6twW2XwtKgLcW8NSTCOQCWfcEfYOGJf5CeSiiWFDxC+IdH1gyIBGp9dCCILa+JeL4jcUwlPW0NNsdzZdMWwLkjEQfjB9/EWTcXzsB4krIxVUAToUcRWbpMjfKs567J8cYjBVNBTRDlX2oAD9VYqbCGHmh+DVUQKBgQC/Qm5GosgoQFKfamD8yheAQ2ZeyUhI1YJWxL676bvsBuv/OxKlEWyw/FFDmXtHGHnpbxXGmW549nuozpBE23UrEWKt696j9Hdbz1nV3dEsXjqyz53NdfJAenDDi0ttnkz9VSv0+PxWcF85VEvr4fluZJ75cru8eoq8oW4dZMeHxQKBgQC0PWr199iOOpCZh8eofhkIdltjLH53Vt6YJr1c2oOd0ol4ajgkdLAqAT/LgVjL2ZfQDcfpa1WjnIzdUJNSwkkEfciZw7cb4Wd3dtXmK6YfyWCU4OI0o5zXT8zBv65nZ2xKAi2YPj7YWcfR4G20GqRkwo76MaAbFawFMQzxYlf3jQKBgBXbAID/3wGCgC6JWHXqng7hJNSWCbWQ+Gjz6JvOlNPe4p7y4CmOuS3sV1jzM/wm1t+O+x0LsW0PNYhPunqz3UrDnNQlDPcNW4pZQnnG9D0us2R1hyHQZZNqqnId+8uV+FRGpFRFE4jJJIiHknltBDTSOLxmmP9rvWjaxZuERazFAoGAMjUppnr0CCqoPRxlsbnunzzZGDc8w2GfgCLKfSMhpsUPJkvwd7dJm2ndq4LMiXyDnaE5HvyXTow9cSkQ4OzeCjI/vxWV3F5dxBmGXCesjw45qT87xA4PkqrYDuCmZIea0sLokJBGUV4xG3W5GYKSaL8JtQg94U2YLZoMUPFkL8ECgYARmi+/zEl9cLEnIXyjZ6F4jd49v4tvdOFvoM+CNpP+I+3WkeSuq0fvcSevF4xc2IFJrfRivTefSO4eezX5f7+xgy1Qqib2cAweV/XxiJz4a6JK1V5jHLdGiDxCKJwztSc/xM8n9qpm0S8tSlL+0mO2Y6J1Vf2mLSazDyr9gDsguQ==";
    }

    interface WxPay {
        /**
         * 微信 AppID，在微信开放平台创建应用，并开通支付能力
         */
        String APP_ID = "wx6dd167b7879fa266";

        /**
         * 商户号
         */
        String WX_SHOP_NUM = "1490174142";
        /**
         * 微信应用密钥
         */
        String WX_API_KEUSTORE = "xianyuexiaoshuoxianyuexiaoshuoxi";

        String NOTIFY_URL = AppConfig.BaseUrl + "pay/wxpayNotify.php";
    }
}
