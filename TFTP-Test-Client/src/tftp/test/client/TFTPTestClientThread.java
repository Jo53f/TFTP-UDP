/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.client;

import java.io.FileOutputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.*;

/**
 *
 * @author PC
 */
public class TFTPTestClientThread{
    
    String mode = "octet";
    DatagramSocket srcSocket = null;
    
    public TFTPTestClientThread( int srcport, int destport, int opcode, String filename, InetAddress address) throws Exception{
        srcSocket = new DatagramSocket(srcport);
        
        if (opcode == 01){
            RRQ(filename, address, destport);
            RD();
        }
        else if(opcode == 02){
            WRQ(filename);
        } else {
            System.out.println("No such mode, terminating...");
        };
    }
    
    public void RRQ(String filename, InetAddress address, int port){
        byte[] buffer = new byte[filename.length()+mode.length()+4];
        byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
        //int opcode = 01;
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        byte[] opcodeByte = new byte[]{0,1};
        
        System.out.println(opcodeByte);
        
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
    
    public void WRQ(String filename){
        
    }
    
    public void RD(){
        byte[] buffer = new byte[512];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        try{
            srcSocket.setSoTimeout(500);
            srcSocket.receive(packet);
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Is the server running?");
        }
        
        if (packet.getData().length < 512){
            System.out.print("Last packet");
        }
        else{
            System.out.print("More packets");
        }
        
        try{
        FileOutputStream fileStream = new FileOutputStream("C:\\Users\\PC\\Desktop\\Recieving\\modeText.txt");
        fileStream.write(packet.getData(), 0, packet.getData().length);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
}