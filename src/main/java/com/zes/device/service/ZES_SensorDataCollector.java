package com.zes.device.service;

import com.zes.device.models.ZES_RegisterRange;
import com.zes.device.models.ZES_SensorSnapshot;
import com.zes.device.modbus.ZES_ModbusTcpClient;
import com.zes.device.repository.ZES_MysqlRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZES_SensorDataCollector
{
 // 주소 범위 정의
 // 10~20: key
 // 100~120: board#1
 // 121~140: board#2
 // 141~160: board#3
 // 161~180: board#4
 // 181~200: board#5
 private static final List<ZES_RegisterRange> ZES_gv_REGISTER_RANGES = List.of(
  new ZES_RegisterRange(10, 20, "KEY"),
  new ZES_RegisterRange(100, 120, "SENSOR_BOARD_1"),
  new ZES_RegisterRange(121, 140, "SENSOR_BOARD_2"),
  new ZES_RegisterRange(141, 160, "SENSOR_BOARD_3"),
  new ZES_RegisterRange(161, 180, "SENSOR_BOARD_4"),
  new ZES_RegisterRange(181, 200, "SENSOR_BOARD_5")
 );

 private final ZES_ModbusTcpClient ZES_gv_modbusClient;
 private final ZES_DataFilterService ZES_gv_filterService;
 private final ZES_DebugPrinter ZES_gv_debugPrinter;
 private final ZES_MysqlRepository ZES_gv_mysqlRepository;

 public ZES_SensorDataCollector(ZES_ModbusTcpClient ZES_lv_modbusClient,
                               ZES_DataFilterService ZES_lv_filterService,
                               ZES_DebugPrinter ZES_lv_debugPrinter,
                               ZES_MysqlRepository ZES_lv_mysqlRepository)
 {
  this.ZES_gv_modbusClient = ZES_lv_modbusClient;
  this.ZES_gv_filterService = ZES_lv_filterService;
  this.ZES_gv_debugPrinter = ZES_lv_debugPrinter;
  this.ZES_gv_mysqlRepository = ZES_lv_mysqlRepository;
 }

 public ZES_SensorSnapshot collect(String ZES_lv_ip, int ZES_lv_port, int ZES_lv_unitId)
 {
  Map<Integer, Integer> ZES_lv_mergedMap = new LinkedHashMap<>();

  for (ZES_RegisterRange ZES_lv_range : ZES_gv_REGISTER_RANGES)
  {
   Map<Integer, Integer> ZES_lv_readMap = ZES_gv_modbusClient.readHoldingRegisters(
    ZES_lv_ip,
    ZES_lv_port,
    ZES_lv_unitId,
    ZES_lv_range.getStartAddress(),
    ZES_lv_range.getLength()
   );

   ZES_lv_mergedMap.putAll(ZES_lv_readMap);
   ZES_gv_debugPrinter.printRegisterMap("READ RANGE: " + ZES_lv_range.getDescription(), ZES_lv_readMap);
  }

  Map<Integer, Integer> ZES_lv_filteredData = ZES_gv_filterService.filterPositiveNonZero(ZES_lv_mergedMap);

  // DB 접근은 현재 비활성화(주석 처리된 repository 메서드 자리만 제공)
  ZES_gv_mysqlRepository.insertRealtimeRow(ZES_lv_filteredData);
  ZES_gv_mysqlRepository.insertLogRows(ZES_lv_filteredData);

  return new ZES_SensorSnapshot(LocalDateTime.now(), ZES_lv_filteredData);
 }
}
