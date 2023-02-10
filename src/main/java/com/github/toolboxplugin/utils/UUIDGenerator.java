package com.github.toolboxplugin.utils;

import java.util.UUID;

public class UUIDGenerator {


    public UUIDGenerator() {
    }

    /**
     * 获得一个唯一性UUID
     */
    public static String getUUID() {
        //不去掉"-"
        return  UUID.randomUUID().toString();
    }
    /**
     * 获得一个唯一性UUID
     */
    public static String getUUIDReplace() {
        return  UUID.randomUUID().toString().replaceAll("-","");
    }


    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
    public static void main(String[] args){
        String[] ss = getUUID(10);
        for(int i=0;i<ss.length;i++){
            System.out.println(ss[i]);
        }
    }
}
