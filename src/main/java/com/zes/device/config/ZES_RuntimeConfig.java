package com.zes.device.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ZES_RuntimeConfig
{
 private static final String ZES_gv_DEFAULT_CONFIG_PATH = "config/application.properties";
 private final Properties ZES_gv_properties = new Properties();

 public ZES_RuntimeConfig()
 {
  this(ZES_gv_DEFAULT_CONFIG_PATH);
 }

 public ZES_RuntimeConfig(String ZES_lv_configPath)
 {
  try (InputStream ZES_lv_inputStream = new FileInputStream(ZES_lv_configPath))
  {
   ZES_gv_properties.load(ZES_lv_inputStream);
  }
  catch (Exception ZES_lv_exception)
  {
   System.out.println("[WARN] config 파일 로드 실패, 기본값 사용: " + ZES_lv_exception.getMessage());
  }
 }

 public String getString(String ZES_lv_key, String ZES_lv_defaultValue)
 {
  return ZES_gv_properties.getProperty(ZES_lv_key, ZES_lv_defaultValue);
 }

 public int getInt(String ZES_lv_key, int ZES_lv_defaultValue)
 {
  String ZES_lv_value = ZES_gv_properties.getProperty(ZES_lv_key);
  if (ZES_lv_value == null)
  {
   return ZES_lv_defaultValue;
  }

  try
  {
   return Integer.parseInt(ZES_lv_value.trim());
  }
  catch (Exception ZES_lv_exception)
  {
   return ZES_lv_defaultValue;
  }
 }
}
