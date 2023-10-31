package com.heeverse.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.heeverse.common.Constants.*;

/**
 * @author jeongheekim
 * @date 10/31/23
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Profile({PROD})
public @interface MultiDataSourceProfile {
}
