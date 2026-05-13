package com.web.ecommerce.global.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

  @Override
  public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
    return source -> {
      if (source == null || source.isBlank() || source.equalsIgnoreCase("null")) {
        return null;
      }
      return (T) Enum.valueOf(targetType, source.trim());
    };
  }
}
