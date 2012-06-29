
package pocman.maze;

public interface TileInterface {

    char toCharacter();

    boolean is(Tile tile);

    boolean isWall();

    boolean isWalkable();

    boolean isCollectable();

}