package com.zes.device.models;

public class ZES_RegisterRange
{
 private final int ZES_gv_startAddress;
 private final int ZES_gv_endAddress;
 private final String ZES_gv_description;

 public ZES_RegisterRange(int ZES_lv_startAddress, int ZES_lv_endAddress, String ZES_lv_description)
 {
  this.ZES_gv_startAddress = ZES_lv_startAddress;
  this.ZES_gv_endAddress = ZES_lv_endAddress;
  this.ZES_gv_description = ZES_lv_description;
 }

 public int getStartAddress()
 {
  return ZES_gv_startAddress;
 }

 public int getEndAddress()
 {
  return ZES_gv_endAddress;
 }

 public int getLength()
 {
  return (ZES_gv_endAddress - ZES_gv_startAddress) + 1;
 }

 public String getDescription()
 {
  return ZES_gv_description;
 }
}
