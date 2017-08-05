/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pt.server.masternode;

import com.pt.client.FSClient;
import com.pt.server.slavenode.SlaveNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Tiago Alexandre Melo Almeida
 */
public class MasterNodeTest {
    
    private MasterNode master;
    private List<SlaveNode> slaves;
    private FSClient client;
    
    @Before
    public void setUp() {
        master = new MasterNode();
        client = new FSClient("localhost", 5555);
        slaves = new ArrayList<>();
        slaves.add(new SlaveNode("slaver_1","localhost", 5555));
        slaves.add(new SlaveNode("slaver_2","localhost", 5555));
        slaves.add(new SlaveNode("slaver_3","localhost", 5555));
        
    }
    
    @After
    public void tearDown() {
        
    }/*
    @Ignore
    @Test
    public void testStart() throws Exception {
        master.start(5555);
        slaves.get(0).start(6666);
        slaves.get(1).start(7777);
        Thread.sleep(5000);//delay for communications... not best way
        Assert.assertEquals(2,master.getListSlaveNodes().size());
    }
    
    @Ignore
    @Test
    public void testSendFile() throws Exception {
        master.start(5555);
        slaves.get(0).start(6666);
        slaves.get(1).start(7777);
        //wait DFS initilization 
        Thread.sleep(1000);
        client.sendFile(new File("bigfile.mkv"));
        
        Thread.sleep(60000);
        System.err.println("Test end");
        
    }*/

    
    @Test
    public void testSendAndRequestFile() throws Exception {
        master.start(5555);
        slaves.get(0).start(6666);
        slaves.get(1).start(7777);
        slaves.get(2).start(8888);
        //wait DFS initilization 
        Thread.sleep(2000);
        client.sendFile(new File("ProjetoFinal.pdf"));       
        Thread.sleep(5000);
        client.requestFile(0);
        Thread.sleep(5000);
        System.err.println("Test end");
        
    }
}
