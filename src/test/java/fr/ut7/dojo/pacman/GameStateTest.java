
package fr.ut7.dojo.pacman;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.Move;

public class GameStateTest {

    private String data;
    private int pacManPosition;
    private int traveledDistance;
    private int pillsLeft;
    private int eatenPills;
    private GameState gameState;
    private Move lastMove;

    @Before
    public void setup() {
        this.data = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙⬤┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        this.pacManPosition = 484;
        this.traveledDistance = 0;
        this.pillsLeft = 241;
        this.eatenPills = 0;
        this.lastMove = Move.GO_NOWHERE;
        this.gameState = GameState.from(this.data);
    }

    @Test
    public void testGetBoard() {
        final char[] array1 = this.data.toCharArray();
        array1[this.gameState.getPacmanPosition()] = Constants.SPACE;
        final char[] array2 = this.gameState.getBoard().toCharArray();
        for (int i = 0; i < array1.length; ++i)
            Assert.assertTrue(array1[i] == array2[i]);

    }

    @Test
    public void testGetPacManPosition() {
        Assert.assertTrue(this.gameState.getPacmanPosition() == this.pacManPosition);
    }

    @Test
    public void testGetTraveledDistance() {
        Assert.assertTrue(this.gameState.getTraveledDistance() == this.traveledDistance);
    }

    @Test
    public void testGetPillsLeft() {
        Assert.assertTrue(this.gameState.getPillsLeft() == this.pillsLeft);
    }

    @Test
    public void testGetEatenPills() {
        Assert.assertTrue(this.gameState.getEatenPills() == this.eatenPills);
    }

    @Test
    public void testGetLastMove() {
        Assert.assertTrue(this.gameState.getPacmanLastMove().equals(this.lastMove));
    }

}