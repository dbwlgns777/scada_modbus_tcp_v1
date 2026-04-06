package com.zes.device.server;

import com.zes.device.models.ZES_SensorSnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ZES_ModbusDataServer implements Runnable
{
 private final String ZES_gv_bindIp;
 private final int ZES_gv_bindPort;
 private final AtomicReference<ZES_SensorSnapshot> ZES_gv_latestSnapshotRef;

 public ZES_ModbusDataServer(String ZES_lv_bindIp, int ZES_lv_bindPort, AtomicReference<ZES_SensorSnapshot> ZES_lv_latestSnapshotRef)
 {
  this.ZES_gv_bindIp = ZES_lv_bindIp;
  this.ZES_gv_bindPort = ZES_lv_bindPort;
  this.ZES_gv_latestSnapshotRef = ZES_lv_latestSnapshotRef;
 }

 @Override
 public void run()
 {
  try (ServerSocket ZES_lv_serverSocket = new ServerSocket(ZES_gv_bindPort))
  {
   System.out.println("[SERVER] listening on " + ZES_gv_bindIp + ":" + ZES_gv_bindPort);

   while (true)
   {
    try (Socket ZES_lv_clientSocket = ZES_lv_serverSocket.accept();
         BufferedReader ZES_lv_reader = new BufferedReader(new InputStreamReader(ZES_lv_clientSocket.getInputStream()));
         PrintWriter ZES_lv_writer = new PrintWriter(ZES_lv_clientSocket.getOutputStream(), true))
    {
     String ZES_lv_request = ZES_lv_reader.readLine();

     if ("GET_DATA".equalsIgnoreCase(ZES_lv_request))
     {
      ZES_lv_writer.println(buildResponse());
     }
     else
     {
      ZES_lv_writer.println("UNKNOWN_COMMAND");
     }
    }
   }
  }
  catch (Exception ZES_lv_exception)
  {
   throw new RuntimeException("[SERVER] run 실패: " + ZES_lv_exception.getMessage(), ZES_lv_exception);
  }
 }

 private String buildResponse()
 {
  ZES_SensorSnapshot ZES_lv_snapshot = ZES_gv_latestSnapshotRef.get();

  if (ZES_lv_snapshot == null)
  {
   return "NO_DATA";
  }

  StringBuilder ZES_lv_stringBuilder = new StringBuilder();
  ZES_lv_stringBuilder.append("collectedAt=").append(ZES_lv_snapshot.getCollectedAt()).append("; ");

  for (Map.Entry<Integer, Integer> ZES_lv_entry : ZES_lv_snapshot.getRawRegisterMap().entrySet())
  {
   ZES_lv_stringBuilder.append("[")
    .append(ZES_lv_entry.getKey())
    .append("=")
    .append(ZES_lv_entry.getValue())
    .append("]");
  }

  return ZES_lv_stringBuilder.toString();
 }
}
