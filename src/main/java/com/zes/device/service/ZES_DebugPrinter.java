package com.zes.device.service;

import java.time.LocalDateTime;
import java.util.Map;

public class ZES_DebugPrinter
{
 public void printRegisterMap(String ZES_lv_title, Map<Integer, Integer> ZES_lv_map)
 {
  System.out.println("\n========== " + ZES_lv_title + " ==========");
  System.out.println("[DEBUG] time=" + LocalDateTime.now());

  for (Map.Entry<Integer, Integer> ZES_lv_entry : ZES_lv_map.entrySet())
  {
   System.out.println("[DEBUG] address=" + ZES_lv_entry.getKey() + ", value=" + ZES_lv_entry.getValue());
  }
 }

 public void printAddressGuide()
 {
  System.out.println("\n========== MODBUS ADDRESS GUIDE ==========");
  System.out.println("10  ~ 20  : 데이터 KEY 구간");
  System.out.println("100 ~ 120 : 센서보드 #1 데이터 구간");
  System.out.println("121 ~ 140 : 센서보드 #2 데이터 구간");
  System.out.println("141 ~ 160 : 센서보드 #3 데이터 구간");
  System.out.println("161 ~ 180 : 센서보드 #4 데이터 구간");
  System.out.println("181 ~ 200 : 센서보드 #5 데이터 구간");
 }
}
