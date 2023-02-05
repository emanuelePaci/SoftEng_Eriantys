package it.polimi.ingsw.model.playerTest;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Deck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck= new Deck();
    }

    @AfterEach
    void tearDown(){
        deck=null;
    }
    @Test
    void removeAssistant() {
        Assistant assistant1 = deck.removeAssistant(7);
        for (Assistant a: deck.getAssistant()){
            assertNotEquals(a.getWeight(), 8);
        }
        assertEquals(assistant1.getWeight(), 8);
        assertEquals(assistant1.getNumMovement(), 4);
        assertEquals(deck.getSize(), 9);
        assertEquals(deck.getAssistant(7).getWeight(), 9);

        for(int i=0; i<9;i++){
            deck.removeAssistant(0);
        }
        assertTrue(deck.isEmpty());
    }

}