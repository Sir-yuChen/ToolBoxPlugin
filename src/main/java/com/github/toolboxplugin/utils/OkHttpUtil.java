package com.github.toolboxplugin.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    private static Logger logger = LogManager.getLogger(OkHttpUtil.class);

    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private String transmit;
    private Request.Builder request;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtil() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpClient == null) {
                    TrustManager[] trustManagers = buildTrustManagers();
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(true)
                            .build();
                    addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtil.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static OkHttpUtil builder() {
        return new OkHttpUtil();
    }

    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加参数 Map方式
     */
    public OkHttpUtil addMapParam(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            if (paramMap == null) {
                paramMap = new LinkedHashMap<>(16);
            }
            params.forEach((key, value) -> {
                paramMap.put(key, value.toString());
            });
        }
        return this;
    }

    /**
     * get 带参方式 =  / 两种方式
     */
    public OkHttpUtil addParameter(String key) {
        if (key != null) {
            transmit = key;
        } else {
            transmit = "=";
        }
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return
     */
    public OkHttpUtil get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null && transmit != null) {
            try {
                switch (transmit) {
                    case "/":
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            urlBuilder.append(URLEncoder.encode(entry.getValue(), "utf-8")).
                                    append("/");
                        }
                        break;
                    case "=":
                        urlBuilder.append("?");
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                                    append("=").
                                    append(URLEncoder.encode(entry.getValue(), "utf-8")).
                                    append("&");
                        }
                        break;
                    default:
                        logger.error("未知符合");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw
     *                   false等于普通的表单提交
     * @return
     */
    public OkHttpUtil post(boolean isJsonPost) {
        okhttp3.RequestBody requestBody;
        if (isJsonPost) {
            String json = "";
            if (paramMap != null) {
                json = JSON.toJSONString(paramMap);
            }
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (paramMap != null) {
                paramMap.forEach(formBody::add);
            }
            requestBody = formBody.build();
        }
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "请求失败：" + e.getMessage();
        }
    }
    /**
     * 同步请求 请求文件流
     *
     * @return
     */
    public InputStream syncFileStream() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body());
            }
        });
    }

    public void sync(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, ResponseBody responseBody);

        void onFailure(Call call, String errorMsg);

    }

    public static void main(String[] args) {
        // get请求，方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String sync = OkHttpUtil.builder().url("http://cloud-music.pl-fe.cn/login/qr/key")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                // 可选择是同步请求还是异步请求
                //.async();
                .sync();
        System.out.println("sync = " + sync);
/*
        // post请求，分为两种，一种是普通表单提交，一种是json提交
        OkHttpUtil.builder().url("请求地址，http/https都可以")
                // 有参数的话添加参数，可多个
                .addParam("参数名", "参数值")
                .addParam("参数名", "参数值")
                // 也可以添加多个
                .addHeader("Content-Type", "application/json; charset=utf-8")
                // 如果是true的话，会类似于postman中post提交方式的raw，用json的方式提交，不是表单
                // 如果是false的话传统的表单提交
                .post(true)
                .sync();

        // 选择异步有两个方法，一个是带回调接口，一个是直接返回结果
        OkHttpUtil.builder().url("")
                .post(false)
                .async();

        OkHttpUtil.builder().url("").post(false).async(new OkHttpUtil.ICallBack() {
            @Override
            public void onSuccessful(Call call, String data) {
                // 请求成功后的处理
            }

            @Override
            public void onFailure(Call call, String errorMsg) {
                // 请求失败后的处理
            }
        });*/
    }
}