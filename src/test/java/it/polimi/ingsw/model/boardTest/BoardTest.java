package it.polimi.ingsw.model.boardTest;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private LinkedList<Student> stud;
    @BeforeEach
    void setUp() {
        stud = new LinkedList<>();
        stud.add(new Student(0));
        stud.add(new Student(1));
        stud.add(new Student(1));
        stud.add(new Student(3));
        board = new Board(stud, 2, TowerColor.BLACK);
    }

    @AfterEach
    void tearDown() {
        stud = null;
        board = null;
    }

    @Test
    void getEntrance() {
        assertEquals(board.getEntrance().getStudent(), stud);
    }

    @Test
    void getDiningRoom() {
        Student s = new Student(0);
        board.getDiningRoom().addStudent(s);
        assertEquals(board.getDiningRoom().find(PawnColor.GREEN), s);
    }

    @Test
    void getProfessorTable() {
        Professor p = new Professor(0);
        board.getProfessorTable().addProfessor(p);
        assertEquals(board.getProfessorTable().find(PawnColor.GREEN), p);
    }

    @Test
    void getTowerCourt() {
        assertEquals(board.getTowerCourt().getTower().size(), 8);
    }
}