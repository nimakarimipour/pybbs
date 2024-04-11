package co.yiiu.pybbs.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
// aaaaaaaaa https://github.com/zl736732419/spring-boot-i18n/blob/master/src/main/java/com/zheng/utils
// /LocaleMessageSourceUtil.java
@Component
public class LocaleMessageSourceUtil {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * @param code ：aamessagesaaakey.
     * @param args : aaaa.
     * @return
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    /**
     * @param code           ：aamessagesaaakey.
     * @param args           : aaaa.
     * @param defaultMessage : aaaakeyaaaaaaa.
     * @return
     */
    public String getMessage(String code, Object[] args, String defaultMessage) {
        //aaaaaaaaaaa，aaarequest.
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
