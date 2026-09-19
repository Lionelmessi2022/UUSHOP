package com.test.service.impl;

import com.test.exception.ShopException;
import com.test.result.ResponseEnum;
import com.test.service.SmsService;
import com.test.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    //互亿无线短信接口地址
    private static final String SMS_URL = "https://106.ihuyi.com/webservice/sms.php?method=Submit";

    @Override
    public boolean send(String mobile, String code) {
        //content格式必须与后台报备的默认模板一致，签名由平台自动附加，不要拼在内容里
        String content = "您的验证码是：" + code + "。请不要把验证码泄露给其他人。";
        try {
            String result = post(mobile, content);
            log.info("短信返回结果：" + result);
            //code=2表示提交成功
            Matcher matcher = Pattern.compile("<code>(.*?)</code>").matcher(result);
            if (matcher.find() && "2".equals(matcher.group(1))) {
                return true;
            }
        } catch (Exception e) {
            log.error("短信调用失败！", e);
            throw new ShopException(ResponseEnum.SMS_SEND_ERROR.getMsg());
        }
        return false;
    }

    private String post(String mobile, String content) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(SMS_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        //password参数为APIKEY的MD5值（32位小写）
        String body = "account=" + URLEncoder.encode(SmsUtil.Account, "UTF-8")
                + "&password=" + URLEncoder.encode(md5(SmsUtil.Password), "UTF-8")
                + "&mobile=" + URLEncoder.encode(mobile, "UTF-8")
                + "&content=" + URLEncoder.encode(content, "UTF-8");
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        writer.write(body);
        writer.flush();
        writer.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        conn.disconnect();
        return sb.toString();
    }

    private String md5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(str.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) sb.append("0");
            sb.append(hex);
        }
        return sb.toString();
    }
}
