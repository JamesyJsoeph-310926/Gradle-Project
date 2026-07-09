package com.ust.sdet.week3.support;

public final class Config {
    private Config(){}
    public static String baseUrl(){
        return System.getProperty("BaseURL", "http://localhost:5173").replaceAll("/$", "");
    }
    public static String catalogUrl(){
        return baseUrl() + "/catalog";
    }
    public static boolean headless(){
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    public static String loginUrl(){
        return baseUrl() + "/login";
    }
}
