package com.shrona.line_demo.common.utils;

public class StaticVariable {

    /**
     * 로그인 세션
     */
    public final static String LOGIN_USER = "login-session";
    public final static int LOGIN_SESSION_TIME = 60 * 60;

    /**
     * 로그인 view
     */
    public final static String HOME_VIEW = "home";

    /**
     * 쓰레드 스케쥴 최대 크기
     */
    public final static int POOL_SIZE = 2;


    /**
     * 스케쥴 등록할 때 최소 숫자
     */
    public final static int NO_DELAY = 0;
}
