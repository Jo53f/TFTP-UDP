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
        
        // Sockets
        int srcSocket = 5000;
        int dstSocket = 4000;
        InetAddress address = InetAddress.getByName(args[0]);
        
        // Packet
        
        // Selecting Read or Write
        System.out.println("1 for READ, 2 for WRITE");
        int INopcode = scan.nextInt();
        int opcode;
        
        if (INopcode == 1){
            opcode = 01;
        }
        else if (INopcode == 2){
            opcode = 02;
        }
        else{
            opcode = -1;
        }
        
        // Filename being recieved or sent
        System.out.println("Filename: ");
        String filename = scan.next();
        
        new TFTPTestClientThread(srcSocket, dstSocket, opcode, filename, address);
        
        // Write filename into buffer
        
        // Sending RRQ or WRQ
        
        // ------
        
        // Sending RRQ/WRQ packet to server
        
        // Reading Data
        //buffer = p.getData(); // Interchangable
        
        // File Output
        //String sned = "sned.txt";
        //String direct = "C:\\Users\\PC\\Desktop\\Recieving\\";
        
        //FileOutputStream fileStream = new FileOutputStream(direct+sned);
        //fileStream.write(p.getData(), 0, p.getData().length);
        
        //System.out.println(buffer);
        
    }
    
}