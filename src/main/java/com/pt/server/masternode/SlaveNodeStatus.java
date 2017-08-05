/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.masternode;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class SlaveNodeStatus {
    
    private final String hostName;
    private final int portNumber;
    private boolean alive;
    private String identifier;
    
    
    public SlaveNodeStatus(String host,int portNumber,String id){
        this.hostName = host;
        this.portNumber = portNumber;
        this.alive = true;
        this.identifier=id;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isAlive() {
        return alive;
    }
    
    public void kill(){alive=false;}
    
    public void setAlive(){alive=true;}

    @Override
    public String toString() {
        return "SlaveNodeStatus{" +"id=" + identifier+ ", hostName=" + hostName + ", portNumber=" + portNumber + ", alive=" + alive + '}';
    }
    
    
}
