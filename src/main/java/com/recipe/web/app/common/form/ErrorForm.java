package com.recipe.web.app.common.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ErrorForm {
    private String errorMessage;
    private String stackTrace;
    private Map<String, Object> sessionAttributes;
}