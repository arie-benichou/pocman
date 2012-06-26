
package fr.designpattern.pocman.game;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class TileTest {

    @Test
    public void testToCharacter() {
        final Tile[] tiles = Tile.values();
        final Set<Tile> setOfTiles = Sets.newHashSet(tiles);
        assertTrue(setOfTiles.size() == tiles.length);
    }

    @Test
    public void testIs() {
        assertTrue(Tile.SPACE.is(Tile.SPACE));
        assertTrue(!Tile.SPACE.is(Tile.PILL));

        assertTrue(Tile.PILL.is(Tile.PILL));
        assertTrue(!Tile.PILL.is(Tile.POCMAN));

        assertTrue(Tile.POCMAN.is(Tile.POCMAN));
        assertTrue(!Tile.POCMAN.is(Tile.HORIZONTAL_WALL));

        assertTrue(Tile.HORIZONTAL_WALL.is(Tile.HORIZONTAL_WALL));
        assertTrue(!Tile.HORIZONTAL_WALL.is(Tile.VERTICAL_WALL));

        assertTrue(Tile.VERTICAL_WALL.is(Tile.VERTICAL_WALL));
    }

    @Test
    public void testIsWall() {
        assertTrue(!Tile.SPACE.isWall());
        assertTrue(!Tile.PILL.isWall());
        assertTrue(!Tile.POCMAN.isWall());
        assertTrue(Tile.HORIZONTAL_WALL.isWall());
        assertTrue(Tile.VERTICAL_WALL.isWall());
    }

    @Test
    public void testIsWalkable() {
        assertTrue(Tile.SPACE.isWalkable());
        assertTrue(Tile.PILL.isWalkable());
        assertTrue(Tile.POCMAN.isWalkable());
        assertTrue(!Tile.HORIZONTAL_WALL.isWalkable());
        assertTrue(!Tile.VERTICAL_WALL.isWalkable());
    }

}