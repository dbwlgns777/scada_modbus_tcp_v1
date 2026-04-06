package com.zes.device.modbus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZES_ModbusTcpClient
{
 private static final int ZES_gv_READ_HOLDING_FUNCTION_CODE = 0x03;

 public Map<Integer, Integer> readHoldingRegisters(String ZES_lv_ip, int ZES_lv_port, int ZES_lv_unitId, int ZES_lv_startAddress, int ZES_lv_quantity)
 {
  Map<Integer, Integer> ZES_lv_resultMap = new LinkedHashMap<>();

  try (Socket ZES_lv_socket = new Socket(ZES_lv_ip, ZES_lv_port);
       DataOutputStream ZES_lv_outputStream = new DataOutputStream(ZES_lv_socket.getOutputStream());
       DataInputStream ZES_lv_inputStream = new DataInputStream(ZES_lv_socket.getInputStream()))
  {
   int ZES_lv_transactionId = (int) (System.currentTimeMillis() % 65535);

   byte[] ZES_lv_request = new byte[12];
   ZES_lv_request[0] = (byte) ((ZES_lv_transactionId >> 8) & 0xFF);
   ZES_lv_request[1] = (byte) (ZES_lv_transactionId & 0xFF);
   ZES_lv_request[2] = 0x00;
   ZES_lv_request[3] = 0x00;
   ZES_lv_request[4] = 0x00;
   ZES_lv_request[5] = 0x06;
   ZES_lv_request[6] = (byte) (ZES_lv_unitId & 0xFF);
   ZES_lv_request[7] = (byte) (ZES_gv_READ_HOLDING_FUNCTION_CODE & 0xFF);
   ZES_lv_request[8] = (byte) ((ZES_lv_startAddress >> 8) & 0xFF);
   ZES_lv_request[9] = (byte) (ZES_lv_startAddress & 0xFF);
   ZES_lv_request[10] = (byte) ((ZES_lv_quantity >> 8) & 0xFF);
   ZES_lv_request[11] = (byte) (ZES_lv_quantity & 0xFF);

   ZES_lv_outputStream.write(ZES_lv_request);
   ZES_lv_outputStream.flush();

   byte[] ZES_lv_mbapHeader = new byte[7];
   ZES_lv_inputStream.readFully(ZES_lv_mbapHeader);

   int ZES_lv_functionCode = ZES_lv_inputStream.readUnsignedByte();
   if (ZES_lv_functionCode != ZES_gv_READ_HOLDING_FUNCTION_CODE)
   {
    throw new RuntimeException("Modbus 예외 응답 수신, functionCode=" + ZES_lv_functionCode);
   }

   int ZES_lv_byteCount = ZES_lv_inputStream.readUnsignedByte();
   byte[] ZES_lv_payload = new byte[ZES_lv_byteCount];
   ZES_lv_inputStream.readFully(ZES_lv_payload);

   for (int ZES_lv_index = 0; ZES_lv_index < ZES_lv_quantity; ZES_lv_index++)
   {
    int ZES_lv_hi = ZES_lv_payload[ZES_lv_index * 2] & 0xFF;
    int ZES_lv_lo = ZES_lv_payload[(ZES_lv_index * 2) + 1] & 0xFF;
    int ZES_lv_value = (ZES_lv_hi << 8) | ZES_lv_lo;
    int ZES_lv_address = ZES_lv_startAddress + ZES_lv_index;
    ZES_lv_resultMap.put(ZES_lv_address, ZES_lv_value);
   }
  }
  catch (Exception ZES_lv_exception)
  {
   throw new RuntimeException("Modbus TCP read 실패: " + ZES_lv_exception.getMessage(), ZES_lv_exception);
  }

  return ZES_lv_resultMap;
 }
}
