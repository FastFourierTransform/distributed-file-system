/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.filesystem;

import com.pt.server.masternode.SlaveNodeStatus;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class Chunk {
    
    private int part;
    private SlaveNodeStatus slaveNode;
    
    public Chunk(int part,SlaveNodeStatus slaveNode){
        this.part = part;
        this.slaveNode = slaveNode;
    }


    public int getPart() {
        return part;
    }

    public SlaveNodeStatus getSlaveNode() {
        return slaveNode;
    }
    
    
}
