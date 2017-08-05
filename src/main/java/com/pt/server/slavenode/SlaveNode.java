/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.slavenode;

import com.pt.TempUtils;
import com.pt.exceptions.ServerAlreadyUsePort;
import com.pt.implementation.client.Client;
import com.pt.implementation.server.Server;
import com.pt.utils.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class SlaveNode {

    private String identifier;
    private File mainFolder;
    
    private Client client = new Client();
    private Server server = new Server(1, 50);
    
    private String masterHost;
    private int masterPort;
    
    public void start(int port) throws ServerAlreadyUsePort, Exception{
        //create folder to save chunk if not exist
        mainFolder = new File(identifier+"_chunks");
        
        if (!mainFolder.exists())
            mainFolder.mkdir();
        
        server.startListningRequests(port, (in, out) -> {
            DataInputStream dIn = new DataInputStream(in);
            DataOutputStream dOut = new DataOutputStream(out);
            int fileId;
            int chunkSize;
            switch (dIn.readByte()){
                case 3:
                    fileId = dIn.readInt();
                    chunkSize = dIn.readInt();
                    FileOutputStream fOut = new FileOutputStream(new File(Paths.get(mainFolder.getName(), fileId+".chunk").toString()));
                    //read into file outputStream TODO
                    TempUtils.readWriteOutStream("slave node",in, chunkSize, fOut);
                    fOut.close();
                    System.out.println("Slave node store the file");
                    break;
                case 5:
                    fileId = dIn.readInt();
                    File[] outFiles = mainFolder.listFiles((dir, name) -> name.startsWith(String.valueOf(fileId)));
                    File outFile = outFiles[0]; //assert
                    
                    //send
                    dOut.writeInt((int) outFile.length());//save cast
                    FileInputStream fIn = new FileInputStream(outFile);
                    TempUtils.readWriteOutStream("Slave ", fIn, (int)outFile.length(), dOut);
                    fIn.close();
                    break;
            }
        });
        
        //send I alive
        
        client.sendRequestWithoutResponse(masterHost, masterPort, (out) -> {
            out.write((byte)1);
            out.write("127.000.000.001".getBytes());
            out.write(Utils.intToBytes(port));
            out.write(identifier.getBytes());
        });
        
        System.out.println(identifier+ " send ");
    }
    
    public SlaveNode(String id,String masterHost,int masterPort){
        this.identifier = id;
        this.masterHost = masterHost;
        this.masterPort = masterPort;
    }

    public String getIdentifier() {
        return identifier;
    }


}
