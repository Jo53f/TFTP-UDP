/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.server;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
    
    // Initialising buffers
    private byte[] buffer = new byte[512];
    private byte[] ackBuffer = new byte[4];
    
    // Extracting adress and port
    InetAddress address = null;
    int port;
    
    
    public TFTPTestServerThread(DatagramSocket sck, DatagramPacket Packet) throws SocketException{
        //super("TFTPTestServerThread");
        
        // Assign this classes socket
        this.threadSocket = sck;
        threadSocket.setSoTimeout(10000);
        
        // Assign the packet recieved
        this.recPacket = Packet;
        
        // Acknowledgement packet
        this.ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
        
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
                    byte[] opcode = new byte[]{0,3};
            
                    // Reading filename
                    byte[] filenameByte = new byte[508];
                    System.arraycopy(recPacket.getData(), 2, filenameByte, 0, recPacket.getData().length-(3+6));
                    String filename = new String(filenameByte, StandardCharsets.UTF_8);
                    
                    // Test file name
                    System.out.println(filename);
                    String directory = "C:\\Users\\PC\\Desktop\\Sending\\";
                    System.out.println(directory);
                    
                    if ("sned.txt".equals(filename)){
                        System.out.println("They're the same");
                    }
                    
                    // Read file
                    FileInputStream stream = new FileInputStream(directory + "In.txt");
                    
                    byte[] buf = new byte[stream.available()];
                    
                    stream.read(buf);
                    
                    
                    int x = 512;
                    int index = 0;
                    int RT = 1;
                    boolean ack = true;
                    int dataRemaining = buf.length;
                    
                    while( x <= dataRemaining && ack == true){
                       
                        System.arraycopy(buf, index+2, buffer, 0, x);
                        
                        System.arraycopy(opcode, 0, buffer, 0, 2);
                        
                        byte[] extra = new byte[]{0};
                        System.arraycopy(extra, 0, buffer, x-1, 1);
                        
                        dataPacket.setAddress(address);
                        dataPacket.setPort(port);
                        threadSocket.send(dataPacket);
                        
                        index = (x*RT);
                        
                        try{
                        threadSocket.receive(ackPacket);
                        } catch(SocketException e){
                            threadSocket.send(dataPacket);
                            System.out.print("no Ack response");
                            ack = false;
                        }
                        System.out.println("run: " + RT);
                        
                        System.out.println("The buffer size is: " + buffer.length);
                        
                        RT++;
                        dataRemaining-=x;
                    }
                    
                    buffer = Arrays.copyOfRange(buf, index+2, buf.length);
                    System.arraycopy(opcode, 0, buffer, 0, 2);
        
                    System.out.println("The buf size is: " + buf.length);
                    System.out.println("The buffer size is: " + buffer.length);

                    // Sending
                    dataPacket.setAddress(address);
                    dataPacket.setPort(port);
                    threadSocket.send(dataPacket);
                    
                    // Recieving last ack --------------------------------------
                    threadSocket.receive(ackPacket);
                    
                    break;
                case 2:
                    byte[] fileBuffer1 = new byte[512];
                    byte[] buffer = new byte[512];
                    byte[] ackBuffer = new byte[4];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);

                    boolean lastPacket = false;

                    int Rs = 1;
                    while(lastPacket == false){

                        System.out.println("run: " + Rs);

                        // Recieving Data Packet -----------------------------------
                        try{
                            threadSocket.receive(packet);

                        }
                        catch(Exception e){
                            System.out.println(e);
                            System.out.println("Is the server running?");
                            break;
                        }

                        // index ---------------------------------------------------
                        int indexA = 0;
                        for (int i = 2; packet.getData()[i] != 0; i++){
                            indexA += 1;
                        }

                        // Copying data into  buffers-------------------------------
                        fileBuffer1 = Arrays.copyOfRange(fileBuffer1, 0, Rs*512);
                        System.arraycopy(packet.getData(),0, fileBuffer1, (512*Rs)-512, indexA);

                        // Help finding everything ---------------------------------
                        System.out.println("The size of index is: " + indexA);

                        if (indexA < 509){
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

                            threadSocket.send(ackPacket);
                        } catch(Exception e){
                            System.out.println(e);
                            System.out.println("Data retrival timed out");
                        }

                        // Last Packet flag ----------------------------------------
                        if (indexA < 509){
                            lastPacket = true;
                        }

                        System.out.println("Number of data packets recieved: " + Rs);
                        Rs++;
                    }

                    // testing last packet -----------------------------------------

                    try{
                    FileOutputStream fileStream = new FileOutputStream("C:\\Users\\PC\\Desktop\\Recieving\\modeText.txt");
                    fileStream.write(fileBuffer1, 0, fileBuffer1.length);
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                    break;
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
            errorHandler(e.toString());
        }
        
    
    }
    
    private void errorHandler(String error){
        
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