package com.phoenix.blog.util;

import com.phoenix.blog.interfaces.FieldsInjector;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class DataUtil{
    public static boolean isEmptyData(String s){
        return s == null || s.isEmpty();
    }

    public static <T,E> void setFields(T t, E e, FieldsInjector fieldsInjector){
        fieldsInjector.inject();
    }
    public static boolean isOptionChosen(int input,int option){
        return (input&option)>0;
    }
}
