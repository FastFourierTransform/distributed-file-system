/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.client;

import com.pt.TempUtils;
import com.pt.implementation.client.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class FSClient {

    private Client client = new Client();
    private String hostMaster;
    private int portMaster;
    private File mainFolder;
    public FSClient(String hostMaster,int portMaster){
        this.hostMaster = hostMaster;
        this.portMaster = portMaster;
        
        mainFolder = new File("DFS_Downloads");
        if (!mainFolder.exists())
            mainFolder.mkdir();
    }
    
    public void sendFile(File file) throws Exception{
        client.sendRequestWaitAssync(hostMaster, portMaster, (out) -> {
            out.write((byte)2);
            DataOutputStream dOut = new DataOutputStream(out);
            dOut.writeLong(file.length());
            dOut.writeInt(file.getName().getBytes().length);
            dOut.write(file.getName().getBytes());
        }, (in) -> {
            
            //read master response
            DataInputStream dIn = new DataInputStream(in);
            int numChunks = dIn.readInt();
            int chunkSize = dIn.readInt();
            int fileId = dIn.readInt();
            System.out.println("chunk Size "+chunkSize );
            //read file 
            FileInputStream fIn = new FileInputStream(file);
            
            for (int i=0;i<numChunks;i++){
                
                String host = new String(TempUtils.readUntilSize(in, 15));
                int port = dIn.readInt();
                System.err.println("client send to "+host+" port " + port + " chunk " + i);
                //send chunk file to the slave
                client.sendRequestWithoutResponse(host, port, (out) -> {
                    DataOutputStream dOut = new DataOutputStream(out);
                    dOut.write((byte)3);
                    dOut.writeInt(fileId);
                    dOut.writeInt(chunkSize);
                    
                    TempUtils.readWriteOutStream("client",fIn, chunkSize, out);
                    
                });
                System.err.println("Slave "+i+"send");
            }
            
            fIn.close();
                
        });
    }
    
    public void requestFile(int id) throws Exception{
        client.sendRequestWaitAssync(hostMaster, portMaster, (out) -> {
            out.write((byte)4);
            DataOutputStream dOut = new DataOutputStream(out);
            dOut.writeInt(id);
        }, (in) -> {
            
            //read master response
            DataInputStream dIn = new DataInputStream(in);
            int numChunks = dIn.readInt();
            int fileNameSize = dIn.readInt();
            String fileName = new String(TempUtils.readUntilSize(dIn, fileNameSize));
            int fileId = dIn.readInt();
            System.out.println("fileId"+fileId+" nChunk "+numChunks );
            
            //CreateFile
            FileOutputStream fOut = new FileOutputStream(new File(Paths.get(mainFolder.getName(), fileName).toString()));
            
            //ask for the file chunks
            for (int i=0;i<numChunks;i++){
                
                String host = new String(TempUtils.readUntilSize(in, 15));
                int port = dIn.readInt();
                System.err.println("client request chunk to slave "+host+" port " + port + " chunk " + i);
                //Sync for now, can change to multi-thread latter
                InputStream chunkStream = client.sendRequest(host, port, (out) -> {
                    DataOutputStream dOut = new DataOutputStream(out);
                    dOut.write((byte)5);
                    dOut.writeInt(fileId);
                });
                System.err.println("Slave "+i+" request chunk");
                //handle the response sync
                DataInputStream cDIn = new DataInputStream(chunkStream);
                int chunkSize = cDIn.readInt();
                TempUtils.readWriteOutStream("FSClient", cDIn, chunkSize, fOut);
                fOut.flush();
            }
            fOut.close();
        });
    }
    
}
