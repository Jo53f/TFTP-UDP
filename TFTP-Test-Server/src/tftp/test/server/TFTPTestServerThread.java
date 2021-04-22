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
    private DatagramPacket ackPacket = null;
    private DatagramPacket dataPacket = null;
    private byte[] buffer = new byte[512];
    
    public TFTPTestServerThread(DatagramSocket sck, DatagramPacket Packet){
        //super("TFTPTestServerThread");
        
        // Assign this classes socket
        this.threadSocket = sck;
        
        // Assign the packet recieved
        this.ackPacket = Packet;
        
        // Data packet
        this.dataPacket = new DatagramPacket(buffer, buffer.length);
        
    }
    
    public void run(){
        
        // Extracting address and port
        InetAddress address = ackPacket.getAddress();
        int port = ackPacket.getPort();
        
        try{
            switch(ackPacket.getData()[1]){
                case 1: // Read Request
            
                    // Reading filename
                    byte[] filenameByte = new byte[512];
                    System.arraycopy(ackPacket.getData(), 2, filenameByte, 0, ackPacket.getData().length-(3+6));
                    String filename = new String(filenameByte, StandardCharsets.UTF_8);
            
                    // Test file name
                    System.out.println(filename);
                    String directory = "C:\\Users\\PC\\Desktop\\Sending\\";
                    System.out.println(directory);
                    
                    if ("sned.txt" == filename){
                        System.out.println("They're the same");
                    }
                    
                    // Read file
                    FileInputStream stream = new FileInputStream(directory + "IDEinput.txt");
        
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
            System.out.println(e);
        }
    
        
    
    }
}
