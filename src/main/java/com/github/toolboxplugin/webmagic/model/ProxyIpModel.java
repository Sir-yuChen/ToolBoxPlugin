package com.github.toolboxplugin.webmagic.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class ProxyIpModel {

    private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 获取IP的平台域名
     */
    private String platformAddress;

    /**
     * IP详情
     */
    private String ipAddress;

    /**
     * IP类型
     */
    private String ipType;
    /**
     * 端口
     */
    private Long port;

    /**
     * IP状态 1可用 0不可用
     */
    private String ipStatus;

    /**
     * 验证地址
     */
    private String validateUrl;

    /**
     * 延迟时效
     */
    private Integer delatTime;

    /**
     * 验证次数
     */
    private Integer validateCount;

    /**
     * 最后验证时间
     */
    private Date validateDate;
    /***
     *  国家
     * @mock 中国
     * @since
     */
    private String country;
    private String countryCode;
    private String region;
    /***
     * 地区/州
     * @mock California
     * @since
     */
    private String regionName;
    /***
     * 城市
     * @mock 北京
     * @since
     */
    private String city;
    /***
     * 邮政编码
     * @mock 94043
     * @since
     */
    private String zip;
    /***
     *  纬度
     * @mock 37.4192
     * @since
     */
    private float lat;
    /***
     *  经度
     * @mock -122.0574
     * @since
     */
    private float lon;
    /***
     * 时区 (tz)
     * @mock
     * @since
     */
    private String timezone;

}
