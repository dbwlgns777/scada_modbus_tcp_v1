package com.zes.device.util;

public class ZES_SleepUtil
{
 private ZES_SleepUtil()
 {
 }

 public static void sleep(long ZES_lv_millis)
 {
  try
  {
   Thread.sleep(ZES_lv_millis);
  }
  catch (InterruptedException ZES_lv_exception)
  {
   Thread.currentThread().interrupt();
  }
 }
}
