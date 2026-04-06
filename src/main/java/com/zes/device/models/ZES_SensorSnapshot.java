package com.zes.device.models;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZES_SensorSnapshot
{
 private final LocalDateTime ZES_gv_collectedAt;
 private final Map<Integer, Integer> ZES_gv_rawRegisterMap;

 public ZES_SensorSnapshot(LocalDateTime ZES_lv_collectedAt, Map<Integer, Integer> ZES_lv_rawRegisterMap)
 {
  this.ZES_gv_collectedAt = ZES_lv_collectedAt;
  this.ZES_gv_rawRegisterMap = new LinkedHashMap<>(ZES_lv_rawRegisterMap);
 }

 public LocalDateTime getCollectedAt()
 {
  return ZES_gv_collectedAt;
 }

 public Map<Integer, Integer> getRawRegisterMap()
 {
  return new LinkedHashMap<>(ZES_gv_rawRegisterMap);
 }
}
