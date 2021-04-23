/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.client;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.Arrays;

/**
 *
 * @author PC
 */
public class TFTPTestClientThread{
    
    String mode = "octet";
    DatagramSocket srcSocket = null;
    BufferedReader inBufR;
    
    public TFTPTestClientThread( int srcport, int destport, int opcode, String filename, InetAddress address) throws Exception{
        srcSocket = new DatagramSocket(srcport);
        
        if (opcode == 01){
            RRQ(filename, address, destport);
            RD(address, destport);
        }
        else if(opcode == 02){
            WRQ(filename, address, destport);
            WD(address, destport);
        } else {
            System.out.println("No such mode, terminating...");
        };
    }
    
    public void RRQ(String filename, InetAddress address, int port){
        byte[] buffer = new byte[filename.length()+mode.length()+4];
        byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        byte[] opcodeByte = new byte[]{0,1};
        
        System.arraycopy(opcodeByte, 0, buffer, 0, 2);
        System.arraycopy(filenameBytes, 0, buffer, 2, filenameBytes.length);
        
        packet.setAddress(address);
        packet.setPort(port);
        
        try{
        srcSocket.send(packet);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void WRQ(String filename, InetAddress address, int port){
        byte[] buffer = new byte[filename.length()+mode.length()+4];
        byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        byte[] opcodeByte = new byte[]{0,2};
        
        System.arraycopy(opcodeByte, 0, buffer, 0, 2);
        System.arraycopy(filenameBytes, 0, buffer, 2, filenameBytes.length);
        
        packet.setAddress(address);
        packet.setPort(port);
        
        try{
        srcSocket.send(packet);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void RD(InetAddress address, int destPort){
        byte[] fileBuffer1 = new byte[512];
        byte[] buffer = new byte[512];
        byte[] ackBuffer = new byte[4];
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
        
        boolean lastPacket = false;
        
        int RT = 1;
        while(lastPacket == false){
            
            System.out.println("run: " + RT);
            
            // Recieving Data Packet -----------------------------------
            try{
                srcSocket.setSoTimeout(1000);
                srcSocket.receive(packet);
                
            }
            catch(Exception e){
                System.out.println(e);
                System.out.println("Is the server running?");
                break;
            }

            // index ---------------------------------------------------
            int index = 0;
            for (int i = 2; packet.getData()[i] != 0; i++){
                index += 1;
            }
            
            // Copying data into  buffers-------------------------------
            fileBuffer1 = Arrays.copyOfRange(fileBuffer1, 0, RT*512);
            System.arraycopy(packet.getData(),0, fileBuffer1, (512*RT)-512, index);
            
            // Help finding everything ---------------------------------
            System.out.println("The size of index is: " + index);

            if (index < 509){
                System.out.println("Last packet");
            }
            else{
                System.out.println("More packets");
            }
            
            // Acknowlegment Packets -----------------------------------
            try{
                byte[] opcodeByte = new byte[]{0,4};
                System.arraycopy(opcodeByte, 0, ackBuffer, 0, 2);
                
                ackPacket.setAddress(packet.getAddress());
                ackPacket.setPort(packet.getPort());
                
                srcSocket.send(ackPacket);
            } catch(Exception e){
                System.out.println(e);
                System.out.println("Data retrival timed out");
            }
            
            // Last Packet flag ----------------------------------------
            if (index < 509){
                lastPacket = true;
            }
            
            System.out.println("Number of data packets recieved: " + RT);
            RT++;
        }

        // testing last packet -----------------------------------------

        try{
        FileOutputStream fileStream = new FileOutputStream("C:\\Users\\PC\\Desktop\\Recieving\\modeText.txt");
        fileStream.write(fileBuffer1, 0, fileBuffer1.length);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void WD(InetAddress address, int destPort){
        
    }
    
}