
package fr.designpattern.pocman.game;

public interface TileInterface {

    char toCharacter();

    boolean is(Tile tile);

    boolean isWall();

    boolean isWalkable();

}