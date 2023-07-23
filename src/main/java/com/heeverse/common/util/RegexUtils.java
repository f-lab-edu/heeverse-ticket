package com.heeverse.common.util;

/**
 * @author jeongheekim
 * @date 2023/07/21
 */
public class RegexUtils {

    public static final String MEMBER_ID_REGEX = "^[a-z]+[a-z0-9]{5,15}$";
    public static final String PWD_REGEX = "^(?=.*[a-zA-Z])(?=.*[@\\!\\?$%^*+=-])(?=.*[0-9]).{8,15}$";
    public static final String USER_NAME_REGEX = "^[a-zA-Zㄱ-힣 ]{1,20}$";
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private RegexUtils() {
    }
}
