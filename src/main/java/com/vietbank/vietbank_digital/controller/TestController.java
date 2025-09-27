package com.vietbank.vietbank_digital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@Validated
public class TestController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/test")
    public String test(Locale locale) {
        return messageSource.getMessage("test.message", null, locale);
    }
}
