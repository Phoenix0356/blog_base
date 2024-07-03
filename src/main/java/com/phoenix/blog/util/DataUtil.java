package com.phoenix.blog.util;

import com.phoenix.blog.interfaces.FieldsInjector;

import java.util.*;
import java.util.stream.Collectors;

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
