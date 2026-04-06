package com.zes.device;

import com.zes.device.config.ZES_RuntimeConfig;
import com.zes.device.models.ZES_SensorSnapshot;
import com.zes.device.modbus.ZES_ModbusTcpClient;
import com.zes.device.repository.ZES_MysqlRepository;
import com.zes.device.server.ZES_ModbusDataServer;
import com.zes.device.service.ZES_DataFilterService;
import com.zes.device.service.ZES_DebugPrinter;
import com.zes.device.service.ZES_SensorDataCollector;
import com.zes.device.util.ZES_SleepUtil;

import java.util.concurrent.atomic.AtomicReference;

public class ZES_DeviceApplication
{
 public static void main(String[] ZES_lv_args)
 {
  ZES_RuntimeConfig ZES_lv_config = new ZES_RuntimeConfig();

  String ZES_lv_targetIp = ZES_lv_config.getString("zes.client.target.ip", "127.0.0.1");
  int ZES_lv_targetPort = ZES_lv_config.getInt("zes.client.target.port", 502);
  int ZES_lv_unitId = ZES_lv_config.getInt("zes.client.unit.id", 1);
  int ZES_lv_pollIntervalMs = ZES_lv_config.getInt("zes.client.poll.interval.ms", 3000);

  String ZES_lv_bindIp = ZES_lv_config.getString("zes.server.bind.ip", "0.0.0.0");
  int ZES_lv_bindPort = ZES_lv_config.getInt("zes.server.bind.port", 9600);

  AtomicReference<ZES_SensorSnapshot> ZES_lv_latestSnapshotRef = new AtomicReference<>();

  ZES_DebugPrinter ZES_lv_debugPrinter = new ZES_DebugPrinter();
  ZES_lv_debugPrinter.printAddressGuide();

  ZES_SensorDataCollector ZES_lv_collector = new ZES_SensorDataCollector(
   new ZES_ModbusTcpClient(),
   new ZES_DataFilterService(),
   ZES_lv_debugPrinter,
   new ZES_MysqlRepository()
  );

  Thread ZES_lv_serverThread = new Thread(
   new ZES_ModbusDataServer(ZES_lv_bindIp, ZES_lv_bindPort, ZES_lv_latestSnapshotRef)
  );
  ZES_lv_serverThread.setDaemon(true);
  ZES_lv_serverThread.start();

  while (true)
  {
   try
   {
    ZES_SensorSnapshot ZES_lv_snapshot = ZES_lv_collector.collect(ZES_lv_targetIp, ZES_lv_targetPort, ZES_lv_unitId);
    ZES_lv_latestSnapshotRef.set(ZES_lv_snapshot);
    System.out.println("[COLLECTOR] filtered size=" + ZES_lv_snapshot.getRawRegisterMap().size());
   }
   catch (Exception ZES_lv_exception)
   {
    System.out.println("[ERROR] collect 실패: " + ZES_lv_exception.getMessage());
   }

   ZES_SleepUtil.sleep(ZES_lv_pollIntervalMs);
  }
 }
}
