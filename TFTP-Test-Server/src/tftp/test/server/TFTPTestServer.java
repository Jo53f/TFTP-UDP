/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftp.test.server;

import java.io.*;
import java.net.*;

/**
 *
 * @author 181307
 */
public class TFTPTestServer {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        // Data Buffer
        byte[] buffer = new byte[512];
        
         // Packet
        DatagramPacket p = new DatagramPacket(buffer, buffer.length); // Setting up the packet
        
        // Socket
        DatagramSocket srcSocket = new DatagramSocket(4000); // Setting up the socket
        System.out.println("Server set up and running");
        srcSocket.receive(p); // Recieving from socket
        
        // Read method
        new TFTPTestServerThread(srcSocket, p).start();
        
        // ---------------------------------------------------------------------------------------------------
        
    }
    
    private void RRQ (DatagramPacket Rpacket, int threadInt) throws Exception{
        
        // Socket
         DatagramSocket threadSocket = new DatagramSocket(threadInt);
        
        // Buffer
         byte[] buffer = new byte[512];
         
        // Packet
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
         
        // Reads the name of file to send
         //FileInputStream stream = new FileInputStream("D:\\Sending\\"+filename);
         
    }
    
}
