/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.filesystem;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public abstract class AbstractFile {
    
    private final String name;
    
    public AbstractFile(String name){
        this.name = name;
    }
    
    public String getName(){return name;}
}
