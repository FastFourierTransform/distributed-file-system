/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt;

import com.pt.exceptions.ServerAlreadyUsePort;
import com.pt.implementation.server.Server;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ServerAlreadyUsePort {
        
        
        
        
        switch (args[0]) {
            case "-sm"://-sm (portnumber)
                launchInServerMasterMode(Integer.parseInt(args[1]));
                break;
            case "-sc"://-sc (portnumber) (server master host) (server master portnumber)
                launchInServerChunkMode(Integer.parseInt(args[1]),args[2],Integer.parseInt(args[3]));
                break;
            case "-c"://-c (server master host) (server master portnumber)
                launchInClientMode(args[1],Integer.parseInt(args[2]));
                break;
            default:
                System.out.println("Invalid argument: " + args[0]);
                break;

        }
    }

    private static void launchInServerMasterMode(int portNumber) throws ServerAlreadyUsePort {
        
    }

    private static void launchInServerChunkMode(int parseInt, String arg, int parseInt0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void launchInClientMode(String arg, int parseInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
