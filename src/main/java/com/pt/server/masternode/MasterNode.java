/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.masternode;

import com.pt.TempUtils;
import com.pt.exceptions.ServerAlreadyUsePort;
import com.pt.implementation.client.Client;
import com.pt.implementation.server.Server;
import com.pt.server.filesystem.AbstractFile;
import com.pt.server.filesystem.Chunk;
import com.pt.server.filesystem.ConcreteFile;
import com.pt.server.filesystem.Folder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ProtocolException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class MasterNode {
    private static int maxChunkSize = 52428800;//50MBYTES
    private Server masterCom = new Server(1, 50);
    private Client client = new Client();
    
    private AbstractFile rootFileSystem;
    
    private Folder actualFolder;
    
    private List<SlaveNodeStatus> listSlaveNodes;
    
    public MasterNode(){
        listSlaveNodes = Collections.synchronizedList(new LinkedList<>());
        rootFileSystem = new Folder("root");
        actualFolder = (Folder) rootFileSystem;
    }
    
    public void start(int port) throws ServerAlreadyUsePort{
        masterCom.startListningRequests(port, (in, out) -> {
            DataInputStream dIn = new DataInputStream(in);
            DataOutputStream dOut = new DataOutputStream(out);
            ConcreteFile cFile;
            switch (dIn.readByte()){
                case 1:
                    listSlaveNodes.add(new SlaveNodeStatus(new String(TempUtils.readUntilSize(in, 15)),dIn.readInt(), new String(TempUtils.readUntilSize(in, 8))));
                    break;
                case 2:
                    long fileTotalSize = dIn.readLong();
                    int sizeNameFile = dIn.readInt();
                    String nameFile = new String(TempUtils.readUntilSize(in, sizeNameFile));
                    
                    
                    int chunkNumbers = nChunksFromFile(fileTotalSize);
                    int chunkSize = (int) (fileTotalSize/chunkNumbers);
                    
                    //add file to DFS
                    cFile = new ConcreteFile(chunkSize,nameFile);
                    actualFolder.addFile(cFile);
                    
                    List<SlaveNodeStatus> slaves = chooseSlaveNodes(chunkNumbers);
                    //divide file into chunks
                    for (int i = 0;i<chunkNumbers;i++)
                        cFile.addChunk(new Chunk( i, slaves.get(i)));
                    
                    //send to client numChunk chunkSize and slaves(host,port) list
                    
                    dOut.writeInt(chunkNumbers);
                    dOut.writeInt(chunkSize);
                    dOut.writeInt(cFile.getIdentifier());
                    for (Chunk chunk : cFile.getChunksList()){
                        dOut.write(chunk.getSlaveNode().getHostName().getBytes());
                        dOut.writeInt(chunk.getSlaveNode().getPortNumber());
                    }
                    break;
                case 4:
                    int fileId = dIn.readInt();
                    
                    cFile = findFilebyId(fileId);
                    if (cFile==null)
                        throw new ProtocolException("Bad identifier " + fileId);
                    
                   
                    //send to client numChunk chunkSize and slaves(host,port) list
                    
                    dOut.writeInt(cFile.getChunksList().size());
                    dOut.writeInt(cFile.getName().getBytes().length);
                    dOut.write(cFile.getName().getBytes());
                    dOut.writeInt(cFile.getIdentifier());
                    for (Chunk chunk : cFile.getChunksList()){
                        dOut.write(chunk.getSlaveNode().getHostName().getBytes());
                        dOut.writeInt(chunk.getSlaveNode().getPortNumber());
                    }
                    break;
            
            }
        });
    }

    public Server getMasterCom() {
        return masterCom;
    }

    public AbstractFile getRootFileSystem() {
        return rootFileSystem;
    }

    public List<SlaveNodeStatus> getListSlaveNodes() {
        return listSlaveNodes;
    }

    private int nChunksFromFile(long fileTotalSize) {
        //(int) ((fileTotalSize/(maxChunkSize*(listSlaveNodes.size()<<4)))+1);
        return listSlaveNodes.size();//num chunks = size of slaves
    }

    private List<SlaveNodeStatus> chooseSlaveNodes(int chunkNumbers) {
        //random maybe
        return listSlaveNodes.subList(0, chunkNumbers);
    }

    private ConcreteFile findFilebyId(int fileId) {
        ConcreteFile found=null;
        for (AbstractFile file: actualFolder.getIncludFiles()){
            if (file instanceof ConcreteFile && ((ConcreteFile)file).getIdentifier()==fileId)
                found=(ConcreteFile) file;
        }        
        return found;
    }
    
    
    
}
