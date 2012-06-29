
package pocman.maze;

public enum Tile implements TileInterface {

    SPACE(' '),
    COIN('∙'),
    POCMAN('⬤'),
    HORIZONTAL_WALL('⬛'),
    VERTICAL_WALL('┃');

    private final char character;

    private Tile(final char character) {
        this.character = character;
    }

    @Override
    public char toCharacter() {
        return this.character;
    }

    @Override
    public boolean is(final Tile tile) {
        return this.equals(tile);
    }

    @Override
    public boolean isWall() {
        return this.is(VERTICAL_WALL) || this.is(HORIZONTAL_WALL);
    }

    @Override
    public boolean isWalkable() {
        return this.is(SPACE) || this.is(COIN) || this.is(Tile.POCMAN);
    }

    @Override
    public boolean isCollectable() {
        return this.is(COIN);
    }

}