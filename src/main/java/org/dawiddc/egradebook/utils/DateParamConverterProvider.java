package org.dawiddc.egradebook.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

    private static final String FORMAT = "yyyy-MM-dd";

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

        if (rawType != Date.class) {
            return null;
        }

        return (ParamConverter<T>) new ParamConverter<Date>() {

            @Override
            public Date fromString(String value) {
                if (value == null)
                    return null;

                SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
                try {
                    return formatter.parse(value);
                } catch (Exception ex) {
                    throw new WebApplicationException("Badly formatted date", 400);
                }
            }

            @Override
            public String toString(Date date) {
                return new SimpleDateFormat(FORMAT).format(date);
            }
        };
    }
}