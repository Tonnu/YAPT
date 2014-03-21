/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yapt.GAME;

import java.awt.Graphics;
import java.awt.Rectangle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import yapt.RMI.IPongGame;
import yapt.RMI.ISession;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class SessionTest {
    
    public SessionTest() {
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

    /**
     * Test of setIsSpectating method, of class Session.
     */
    @Test
    public void testSetIsSpectating() {
        System.out.println("setIsSpectating");
        boolean isSpectating = false;
        Session instance = null;
        instance.setIsSpectating(isSpectating);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSpectating method, of class Session.
     */
    @Test
    public void testIsSpectating() {
        System.out.println("isSpectating");
        Session instance = null;
        boolean expResult = false;
        boolean result = instance.isSpectating();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsername method, of class Session.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        Session instance = null;
        String expResult = "";
        String result = instance.getUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGameClient method, of class Session.
     */
    @Test
    public void testGetGameClient() {
        System.out.println("getGameClient");
        Session instance = null;
        GameClient expResult = null;
        GameClient result = instance.getGameClient();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerPosition method, of class Session.
     */
    @Test
    public void testGetPlayerPosition() {
        System.out.println("getPlayerPosition");
        Session instance = null;
        Vector2f expResult = null;
        Vector2f result = instance.getPlayerPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerRectangle method, of class Session.
     */
    @Test
    public void testGetPlayerRectangle() {
        System.out.println("getPlayerRectangle");
        Session instance = null;
        Rectangle expResult = null;
        Rectangle result = instance.getPlayerRectangle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWinner method, of class Session.
     */
    @Test
    public void testSetWinner() {
        System.out.println("setWinner");
        boolean won = false;
        Session instance = null;
        instance.setWinner(won);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPosition method, of class Session.
     */
    @Test
    public void testSetPosition() {
        System.out.println("setPosition");
        Vector2f _position = null;
        Session instance = null;
        instance.setPosition(_position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of draw method, of class Session.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        Graphics g = null;
        Session instance = null;
        instance.draw(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Session.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        int direction = 0;
        Session instance = null;
        instance.update(direction);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onMessage method, of class Session.
     */
    @Test
    public void testOnMessage() throws Exception {
        System.out.println("onMessage");
        String message = "";
        Object o = null;
        Session instance = null;
        instance.onMessage(message, o);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isGameStarted method, of class Session.
     */
    @Test
    public void testIsGameStarted() {
        System.out.println("isGameStarted");
        Session instance = null;
        boolean expResult = false;
        boolean result = instance.isGameStarted();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPongGame method, of class Session.
     */
    @Test
    public void testGetPongGame() {
        System.out.println("getPongGame");
        Session instance = null;
        IPongGame expResult = null;
        IPongGame result = instance.getPongGame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPongGame method, of class Session.
     */
    @Test
    public void testSetPongGame() {
        System.out.println("setPongGame");
        IPongGame pongGame = null;
        Session instance = null;
        instance.setPongGame(pongGame);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPlayerNumber method, of class Session.
     */
    @Test
    public void testSetPlayerNumber() {
        System.out.println("setPlayerNumber");
        int _number = 0;
        Session instance = null;
        instance.setPlayerNumber(_number);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerNumber method, of class Session.
     */
    @Test
    public void testGetPlayerNumber() {
        System.out.println("getPlayerNumber");
        Session instance = null;
        int expResult = 0;
        int result = instance.getPlayerNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGamePongNumber method, of class Session.
     */
    @Test
    public void testSetGamePongNumber() {
        System.out.println("setGamePongNumber");
        int _number = 0;
        Session instance = null;
        instance.setGamePongNumber(_number);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGamePongNumber method, of class Session.
     */
    @Test
    public void testGetGamePongNumber() {
        System.out.println("getGamePongNumber");
        Session instance = null;
        int expResult = 0;
        int result = instance.getGamePongNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClientRectangle method, of class Session.
     */
    @Test
    public void testGetClientRectangle() throws Exception {
        System.out.println("getClientRectangle");
        Session instance = null;
        Rectangle expResult = null;
        Rectangle result = instance.getClientRectangle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getplayers method, of class Session.
     */
    @Test
    public void testGetplayers() throws Exception {
        System.out.println("getplayers");
        String _username = "";
        Session instance = null;
        ISession expResult = null;
        ISession result = instance.getplayers(_username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of challengePlayer method, of class Session.
     */
    @Test
    public void testChallengePlayer() {
        System.out.println("challengePlayer");
        ISession _opponent = null;
        Session instance = null;
        int expResult = 0;
        int result = instance.challengePlayer(_opponent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recieveChallengeRequest method, of class Session.
     */
    @Test
    public void testRecieveChallengeRequest() throws Exception {
        System.out.println("recieveChallengeRequest");
        ISession _opponent = null;
        Session instance = null;
        int expResult = 0;
        int result = instance.recieveChallengeRequest(_opponent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChallengeMode method, of class Session.
     */
    @Test
    public void testGetChallengeMode() {
        System.out.println("getChallengeMode");
        Session instance = null;
        boolean expResult = false;
        boolean result = instance.getChallengeMode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setChallengeMode method, of class Session.
     */
    @Test
    public void testSetChallengeMode() {
        System.out.println("setChallengeMode");
        boolean challengeMode = false;
        Session instance = null;
        instance.setChallengeMode(challengeMode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
