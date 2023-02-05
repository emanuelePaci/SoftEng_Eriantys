package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnumTest {

    @Test
    void lookup(){
        assertEquals(PawnColor.lookup("BLUE"), PawnColor.BLUE);
        assertEquals(PawnColor.lookup("YELLOW"), PawnColor.YELLOW);
        assertEquals(PawnColor.lookup("GREEN"), PawnColor.GREEN);
        assertNull(PawnColor.lookup("grey"));
    }

    @Test
    void getColor(){
        assertEquals(TowerColor.getColor(0), TowerColor.WHITE);
        assertEquals(TowerColor.getColor(2), TowerColor.GREY);
        assertEquals(TowerColor.getColor("BLACK"), TowerColor.BLACK);
        assertEquals(TowerColor.getColor("White"), TowerColor.WHITE);
        assertNull(TowerColor.getColor(3));
        assertNull(TowerColor.getColor("blue"));
    }

    @Test
    void getIndex(){
        assertEquals(TowerColor.BLACK.getIndex(), 1);
    }
}
