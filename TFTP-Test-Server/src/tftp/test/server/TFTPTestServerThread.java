/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.server;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

/**
 *
 * @author Gaming Laptop
 */
public class TFTPTestServerThread extends Thread{
    
    // Initialising Socket
    private DatagramSocket threadSocket = null;
    private DatagramPacket recPacket = null;
    private DatagramPacket ackPacket = null;
    private DatagramPacket dataPacket = null;
    
    // Initialising data buffer
    private byte[] buffer = new byte[512];
    
    // Extracting adress and port
    InetAddress address = null;
    int port;
    
    
    public TFTPTestServerThread(DatagramSocket sck, DatagramPacket Packet){
        //super("TFTPTestServerThread");
        
        // Assign this classes socket
        this.threadSocket = sck;
        
        // Assign the packet recieved
        this.recPacket = Packet;
        
        // Acknowledgement packet
        this.ackPacket = Packet;
        
        // Data packet
        this.dataPacket = new DatagramPacket(buffer, buffer.length);
    }
    
    public void run(){
        
        // Extracting address and port
        address = recPacket.getAddress();
        port = recPacket.getPort();
        
        try{
            switch(recPacket.getData()[1]){
                case 1: // Read Request
            
                    // Reading filename
                    byte[] filenameByte = new byte[512];
                    System.arraycopy(recPacket.getData(), 2, filenameByte, 0, recPacket.getData().length-(3+6));
                    String filename = new String(filenameByte, StandardCharsets.UTF_8);
            
                    // Test file name
                    System.out.println(filename);
                    String directory = "C:\\Users\\PC\\Desktop\\Sending\\";
                    System.out.println(directory);
                    
                    if ("sned.txt" == filename){
                        System.out.println("They're the same");
                    }
                    
                    // Read file
                    FileInputStream stream = new FileInputStream(directory + "IDEinpu");
        
                    // Reading file into buffer
                    stream.read(buffer, 0, buffer.length);

                    // Sending
                    dataPacket.setAddress(address);
                    dataPacket.setPort(port);
                    threadSocket.send(dataPacket);
                    
                    break;
                case 2:
            
                    break;
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
            errorHandler(e.toString());
        }
        
    
    }
    
    private void errorHandler(String error){
        //e.getLocalizedMessage().getBytes();
        byte [] errBuffer = new byte[error.getBytes().length+2];
        DatagramPacket errPacket = new DatagramPacket(errBuffer, errBuffer.length);
        
        System.arraycopy(error.getBytes(), 0, errBuffer, 2, error.getBytes().length);
        
        try{
            errPacket.setAddress(address);
            errPacket.setPort(port);
            threadSocket.send(errPacket);
        }
        catch(IOException f){
            System.out.print(f);
        }
}   
}