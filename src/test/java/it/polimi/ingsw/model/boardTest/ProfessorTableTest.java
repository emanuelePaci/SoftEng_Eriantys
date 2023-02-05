package it.polimi.ingsw.model.boardTest;

import it.polimi.ingsw.model.board.ProfessorTable;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Professor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessorTableTest{
    ProfessorTable professorTable;
    @BeforeEach
    public void setUp(){
        professorTable = new ProfessorTable();
    }
    @AfterEach
    public void tearDown(){
        professorTable = null;
    }
    @Test
    void getProfessors(){
        Professor p = new Professor(0);
        professorTable.addProfessor(p);
        assertEquals(p, professorTable.getProfessors().get(0));
    }
    @Test
    void removeProfessor(){
        Professor p = new Professor(0);
        professorTable.addProfessor(p);
        assertEquals(p, professorTable.getProfessors().get(0));
        professorTable.removeProfessor(p);
        assertEquals(false, professorTable.getProfessors().contains(p));
    }
    @Test
    void find() {
        assertEquals(null, professorTable.find(PawnColor.BLUE));
        Professor prof = new Professor(4);
        professorTable.addProfessor(prof);
        assertEquals(prof, professorTable.find(PawnColor.BLUE));
    }


}