package com.utku.invoice_upload_system.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class TcpConnections {
        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;

    public String QueryInvoice(String ip, int port, String type, String value) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connected");

            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            byte typeMessage;

            if(type.trim().equals("Seri Numarası")){
                typeMessage = 1;
            }else{
                typeMessage = 2;
            }

            byte command = 2;
            String data = value;
            byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

            out.writeShort(dataInBytes.length);
            out.writeByte(command);
            out.writeByte(typeMessage);
            out.write(dataInBytes);

            short length = input.readShort();
            byte responseCommand = input.readByte();
            byte responseType= input.readByte();

            byte[] messageByte = new byte[length];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(length);
            int totalBytesRead = 0;
            while (!end) {
                int currentBytesRead = input.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if (totalBytesRead <= length) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                } else {
                    dataString
                            .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                    StandardCharsets.UTF_8));
                }
                if (dataString.length() >= length) {
                    end = true;
                }
            }

            input.close();
            out.close();
            socket.close();

            return dataString.toString();
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
        return "İstek gerçekleştirilirken hata meydana geldi!";
    }



    public String uploadInvoice(String ip, int port, String type, String value){
        try {
            socket = new Socket(ip, port);
            System.out.println("Connected");

            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            byte typeMessage;

            System.out.println(type);

            if(type.toLowerCase(Locale.ROOT).equals("xml")){
                typeMessage = 1;
            }else{
                typeMessage = 2;
            }

            byte command = 1;
            String data = value;
            byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

            out.writeShort(dataInBytes.length);
            out.writeByte(command);
            out.writeByte(typeMessage);
            out.write(dataInBytes);

            short length = input.readShort();
            byte responseCommand = input.readByte();
            byte responseType= input.readByte();

            byte[] messageByte = new byte[length];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(length);
            int totalBytesRead = 0;
            while (!end) {
                int currentBytesRead = input.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if (totalBytesRead <= length) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                } else {
                    dataString
                            .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                    StandardCharsets.UTF_8));
                }
                if (dataString.length() >= length) {
                    end = true;
                }
            }

            input.close();
            out.close();
            socket.close();

            return dataString.toString();
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
        return "İstek gerçekleştirilirken hata meydana geldi!";
    }

}
