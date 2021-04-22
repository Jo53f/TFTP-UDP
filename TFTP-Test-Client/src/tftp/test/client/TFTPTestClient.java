/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.charset.*;

/**
 *
 * @author 18307
 */
public class TFTPTestClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        // Setting up Input Scanner
        Scanner scan = new Scanner(System.in);
        
        // Setting up the buffer
        byte[] buffer = new byte[512];
        
        // Sockets
        DatagramSocket srcSocket = new DatagramSocket(5000);
        int dstSocket = 4000;
        
        // Packet
        DatagramPacket p = new DatagramPacket(buffer,buffer.length);
        
        // Selecting Read or Write
        System.out.println("1 for READ, 2 for WRITE");
        int opcode = scan.nextInt();
        
        switch(opcode){
            case 1: // Read
                System.out.println("Read is selected");
                buffer[0] = (byte)0;
                buffer[1] = (byte) opcode;
                break;
            case 2: // Write
                System.out.println("Write is selected");
                buffer[0] = (byte)0;
                buffer[1] = (byte) opcode;
                break;
        }
        
        // Filename being recieved or sent
        System.out.print("Filename: ");
        String filename = scan.next();
        System.out.println(filename);
        
        new TFTPTestClientThread(srcSocket, dstSocket, opcode, filename);
        
        // Write filename into buffer
        System.arraycopy(filename.getBytes(), 0, buffer, 2, filename.length());
        
        // Sending RRQ or WRQ
        
        // ------
        
        // Sending RRQ/WRQ packet to server
        InetAddress address = InetAddress.getByName(args[0]);
        p.setAddress(address);
        p.setPort(dstSocket);
        srcSocket.send(p);
        
        // Recieving data
        srcSocket.receive(p);
        
        
        
        // Reading Data
        //buffer = p.getData(); // Interchangable
        
        // File Output
        String sned = "sned.txt";
        String direct = "C:\\Users\\PC\\Desktop\\Recieving\\";
        
        FileOutputStream fileStream = new FileOutputStream(direct+sned);
        fileStream.write(p.getData(), 0, p.getData().length);
        
        System.out.println(buffer);
        
    }
    
    public void RRQHandler(DatagramPacket p){
        
    }
    
    public void WRQHandler(){
        
    }
    
}
