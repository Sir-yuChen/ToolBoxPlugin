package com.github.toolboxplugin.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * RestTemplate工具类
 *
 * @ClassName RestTemplateUtils
 **/
@Component("restTemplateUtils")
public final class RestTemplateUtils {

    private static Logger logger = LogManager.getLogger(RestTemplateUtils.class);
    @Resource
    private RestTemplate httpClientTemplate;

/*    @Resource
    public void setHttpClientTemplate(RestTemplate httpClientTemplate) {
        this.httpClientTemplate = httpClientTemplate;
    }*/


    /**
     * POST请求-JSON参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public <T> T httpPostJson(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        // json方式传参
        return httpClientTemplate.postForObject(url, new HttpEntity(params, httpHeaders), clazz);
    }

    /**
     * POST请求-FROM参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public <T> T httpPostFrom(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new LinkedHashMap<>() : params;
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        MultiValueMap<String, Object> stringObjectLinkedMultiValueMap = new LinkedMultiValueMap<>();
        params.forEach((o1, o2) -> stringObjectLinkedMultiValueMap.add(o1, o2));
        // 表单方式传参
        // json方式传参
        return httpClientTemplate.postForObject(url, new HttpEntity(stringObjectLinkedMultiValueMap, httpHeaders), clazz);
    }

    /**
     * GET请求-?号参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public <T> T httpGetTraditional(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new LinkedHashMap<>() : params;
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        params.forEach((o1, o2) -> sb.append(o1).append("=").append(o2).append("&"));
        url = sb.toString().replaceAll("&$+|\\?$+", "");
        logger.info("REST 请求URL={}", url);
        ResponseEntity<T> exchange = httpClientTemplate.exchange(url, HttpMethod.GET, new HttpEntity(null, httpHeaders), clazz);
        return exchange.getBody();
    }

    /**
     * GET请求-分隔符参数
     *
     * @param url
     * @param params  LinkedHashMap
     * @param headers
     * @return
     */
    public <T> T httpGetPlaceholder(String url, List<String> params, Map<String, String> headers, Class<T> clazz) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            params = params == null ? new ArrayList<>() : params;
            if (headers != null) {
                headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
            }
            StringBuilder sb = new StringBuilder(url);
            params.forEach(o2 -> sb.append("/").append(o2));
            url = sb.toString().replaceAll("&$+|\\?$+", "");
            ResponseEntity<T> exchange = httpClientTemplate.exchange(url, HttpMethod.GET, new HttpEntity(null, httpHeaders), clazz);
            return exchange.getBody();
        } catch (RestClientException e) {
            logger.error("REST 请求URL={} 异常{}", url, e);
        }
        return null;
    }

    /**
     * DELETE请求-?号参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public <T> T httpDeleteTraditional(String url, Map<String, Object> params, Map<String, String> headers, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new LinkedHashMap<>() : params;
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        params.forEach((o1, o2) -> sb.append(o1).append("=").append(o2).append("&"));
        url = sb.toString().replaceAll("&$+|\\?$+", "");
        ResponseEntity<T> exchange = httpClientTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity(null, httpHeaders), clazz);
        return exchange.getBody();
    }

    /**
     * DELETE请求-分隔符参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public <T> T httpDeletePlaceholder(String url, List<String> params, Map<String, String> headers, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new ArrayList<>() : params;
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        StringBuilder sb = new StringBuilder(url);
        params.forEach(o2 -> sb.append("/").append(o2));
        url = sb.toString().replaceAll("&$+|\\?$+", "");
        ResponseEntity<T> exchange = httpClientTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity(null, httpHeaders), clazz);
        return exchange.getBody();
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        /*
        HashMap<String, Object> params = new HashMap<>();
        params.put("q","张艺谋");
        params.put("lang","Cn");

        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new LinkedHashMap<>() : params;
        String url ="http://ip-api.com/json/221.122.91.65";
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        params.forEach((o1, o2) -> sb.append(o1).append("=").append(o2).append("&"));
        url = sb.toString().replaceAll("&$+|\\?$+", "");
        logger.info("REST 请求URL={}",url);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(null, httpHeaders), String.class);

        System.out.println("s ======================>>>>>>>>>> " + exchange);*/
        List<String> params = new ArrayList<>();
        HashMap<String, String> headers = new HashMap<>();
        String url = "http://ip-api.com/json";
        params.add("221.122.91.65");
        HttpHeaders httpHeaders = new HttpHeaders();
        params = params == null ? new ArrayList<>() : params;
        if (headers != null) {
            headers.forEach((o1, o2) -> httpHeaders.add(o1, o2));
        }
        StringBuilder sb = new StringBuilder(url);
        params.forEach(o2 -> sb.append("/").append(o2));
        url = sb.toString().replaceAll("&$+|\\?$+", "");
        logger.info("REST 请求URL={}", url);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(null, httpHeaders), String.class);
        System.out.println("exchange = " + exchange.getBody());
    }

}