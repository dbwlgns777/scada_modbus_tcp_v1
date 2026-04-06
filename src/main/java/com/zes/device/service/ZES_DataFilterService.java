package com.zes.device.service;

import java.util.LinkedHashMap;
import java.util.Map;

public class ZES_DataFilterService
{
 public Map<Integer, Integer> filterPositiveNonZero(Map<Integer, Integer> ZES_lv_rawData)
 {
  Map<Integer, Integer> ZES_lv_filteredMap = new LinkedHashMap<>();

  for (Map.Entry<Integer, Integer> ZES_lv_entry : ZES_lv_rawData.entrySet())
  {
   int ZES_lv_value = ZES_lv_entry.getValue();

   if (ZES_lv_value > 0)
   {
    ZES_lv_filteredMap.put(ZES_lv_entry.getKey(), ZES_lv_value);
   }
  }

  return ZES_lv_filteredMap;
 }
}
