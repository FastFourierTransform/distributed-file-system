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
public class Folder extends AbstractFile{

    private List<AbstractFile> includFiles;

    public Folder(String name) {
        super(name);
        includFiles = new ArrayList<>();
    }
    
    public void addFile(AbstractFile file){
        includFiles.add(file);
    }

    public List<AbstractFile> getIncludFiles() {
        return includFiles;
    }
    
}
