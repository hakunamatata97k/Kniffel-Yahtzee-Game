package Kniffel.test;

import Kniffel.Game.Die;
import Kniffel.Game.KniffelBlock;

import static junit.framework.TestCase.*;

/**
 * JUNIT test Class to ONLY test the methods which are important enough to be tested.
 */
public class KniffelTest {

    private final KniffelBlock OBJ =new KniffelBlock();


    @org.junit.Test
    public void test_1(){
        Die[] temp=OBJ.getDICE();
        temp[0].setNumEyesInDie(1);
        temp[1].setNumEyesInDie(1);
        temp[2].setNumEyesInDie(1);
        temp[3].setNumEyesInDie(2);
        temp[4].setNumEyesInDie(2);

        assertTrue(OBJ.isThreeOfAKind());
        assertTrue(OBJ.isFullHouse());
        assertTrue( OBJ.checkPresenceOF(1));
        assertEquals(1, OBJ.getMostFrequentDie(2));
        assertEquals(3, OBJ.sumOfNerElement(1));

        assertFalse(OBJ.isFourOfAKind());
        assertFalse(OBJ.istkleineStrasse());
        assertFalse(OBJ.istGrosseStrasse());
        assertFalse(OBJ.isKniffel());
        assertEquals(7 , OBJ.chance());

    }

    @org.junit.Test
    public void test_2(){
        Die [] temp=OBJ.getDICE();
        temp[0].setNumEyesInDie(1);
        temp[1].setNumEyesInDie(1);
        temp[2].setNumEyesInDie(2);
        temp[3].setNumEyesInDie(2);
        temp[4].setNumEyesInDie(3);
        assertFalse(OBJ.checkPresenceOF(7));
        assertEquals(-1, OBJ.sumOfNerElement(8));
        assertFalse(OBJ.isThreeOfAKind());
        assertFalse(OBJ.isFourOfAKind());
        assertFalse(OBJ.istkleineStrasse());
        assertFalse(OBJ.istGrosseStrasse());
        assertFalse(OBJ.isKniffel());
        assertEquals(9 , OBJ.chance());
        assertFalse(OBJ.isFullHouse());

        assertEquals(-1, OBJ.getMostFrequentDie(1));
    }

    @org.junit.Test
    public void test_3(){
        Die [] temp=OBJ.getDICE();
        temp[0].setNumEyesInDie(1);
        temp[1].setNumEyesInDie(2);
        temp[2].setNumEyesInDie(3);
        temp[3].setNumEyesInDie(4);
        temp[4].setNumEyesInDie(5);

        assertEquals(2, OBJ.sumOfNerElement(2));
        assertFalse(OBJ.isThreeOfAKind());
        assertFalse(OBJ.isFourOfAKind());
        assertTrue(OBJ.istkleineStrasse());
        assertTrue(OBJ.istGrosseStrasse());
        assertFalse(OBJ.isKniffel());
        assertEquals(15 , OBJ.chance());
        assertFalse(OBJ.isFullHouse());
        assertTrue( OBJ.checkPresenceOF(3));

        assertEquals(-1, OBJ.getMostFrequentDie(5));
    }

    @org.junit.Test
    public void test_4(){
        Die [] temp=OBJ.getDICE();
        temp[0].setNumEyesInDie(1);
        temp[1].setNumEyesInDie(2);
        temp[2].setNumEyesInDie(3);
        temp[3].setNumEyesInDie(4);
        temp[4].setNumEyesInDie(4);
        assertFalse(OBJ.checkPresenceOF(0));
        assertEquals(-1, OBJ.sumOfNerElement(0));
        assertFalse(OBJ.isThreeOfAKind());
        assertFalse(OBJ.isFourOfAKind());
        assertTrue(OBJ.istkleineStrasse());
        assertFalse(OBJ.istGrosseStrasse());
        assertFalse(OBJ.isKniffel());
        assertEquals(14 , OBJ.chance());
        assertFalse(OBJ.isFullHouse());

        assertFalse( OBJ.checkSequence(2));
        assertEquals(-1, OBJ.getMostFrequentDie(0));
    }

    @org.junit.Test
    public void test_5(){
        Die [] temp=OBJ.getDICE();
        temp[0].setNumEyesInDie(1);
        temp[1].setNumEyesInDie(1);
        temp[2].setNumEyesInDie(1);
        temp[3].setNumEyesInDie(1);
        temp[4].setNumEyesInDie(1);
        assertFalse(OBJ.checkPresenceOF(-1));
        assertEquals(0, OBJ.sumOfNerElement(4));
        assertTrue(OBJ.isFourOfAKind());
        assertTrue(OBJ.isThreeOfAKind());
        assertFalse(OBJ.istkleineStrasse());
        assertFalse(OBJ.istGrosseStrasse());
        assertTrue(OBJ.isKniffel());
        assertFalse(OBJ.isFullHouse());
        assertEquals(5, OBJ.chance());
        assertEquals(1, OBJ.getMostFrequentDie(4));
    }
}