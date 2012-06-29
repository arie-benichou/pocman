
package pocman.maze;

import junit.framework.Assert;

import org.junit.Test;

import pocman.maze.Tile;
import pocman.maze.Tiles;

public class TilesTest {

    private static final Tile[] TILES = Tile.values();

    @Test
    public void testFromCharacter() {
        for (final Tile tile : TILES)
            Assert.assertTrue(Tiles.fromCharacter(tile.toCharacter()).equals(tile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromCharacterWithNullReference() {
        Tiles.fromCharacter(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromCharacterWithUnknownTile() {
        Tiles.fromCharacter('?');
    }

}