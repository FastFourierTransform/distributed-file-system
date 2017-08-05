/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Temp class, all method shlould be move to client-server-library
 * @author Tiago Alexandre Melo Almeida
 */
public class TempUtils {
       public static byte[] readUntilSize(InputStream input,int bytesToRead) throws IOException {
        
        byte[] buffer = new byte[bytesToRead];
        
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        while (output.size() < bytesToRead) {
            bytesRead = input.read(buffer);
            output.write(buffer, 0, bytesRead);
        }
        
        output.flush();
        return output.toByteArray();
    }
       
    public static void readWriteOutStream(String id,InputStream input,int size,OutputStream out) throws IOException {
        int bufferSize = 65536;
        byte[] buffer = new byte[bufferSize];
        
        int bytesRead=0;
        int bytesWrited=0;
        while (bytesWrited < size-bufferSize) {
            
            bytesRead = input.read(buffer);
            //ystem.out.println("bRead "+bytesRead );
            
            out.write(buffer, 0, bytesRead);
            bytesWrited += bytesRead;
        }
        //last read
        out.write(readUntilSize(input, size-bytesWrited));//not effecient
        System.err.println(id+" write bytes " + (bytesWrited+(size-bytesWrited)));
        
        
    }
}
