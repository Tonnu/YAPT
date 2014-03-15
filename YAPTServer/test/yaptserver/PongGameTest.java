/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yaptserver;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import yapt.RMI.IPong;
import yapt.RMI.ISession;

/**
 *
 * @author Toon
 */
public class PongGameTest {
    ISession a, b;
    PongGame pg;
    YAPTServer server;
    
    public PongGameTest() throws RemoteException, AccessException, NotBoundException {
        server = new YAPTServer(null);
        pg = new PongGame(server, a, b, 1);
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
}
