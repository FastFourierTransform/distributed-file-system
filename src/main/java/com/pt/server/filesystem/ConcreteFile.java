/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class ConcreteFile extends AbstractFile{

    private static int count=0;
    
    private List<Chunk> fileChunks;
    private int identifier;
    private int chunkSize;
    
    public ConcreteFile(int chunkSize,String name) {
        super(name);
        fileChunks = new ArrayList<>();
        identifier=count++;
        this.chunkSize = chunkSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }
    
    public void addChunk(Chunk c){fileChunks.add(c);}
    
    public List<Chunk> getChunksList(){return fileChunks;}

    public int getIdentifier() {
        return identifier;
    }
    
    
}
