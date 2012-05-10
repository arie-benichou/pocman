
package fr.ut7.dojo.pacman.model;

public final class GameState {

    private final Board board;
    private final int pacManPosition;
    private final int ghostPosition;
    private final int eatenPills;
    private final Move pacmanLastMove;
    private final Move ghostLastMove;
    private final int pillsLeft;
    private final int traveledDistance;

    public static GameState from(final String data) {
        final int eatenPills = 0;
        final Move pacmanLastMove = Move.GO_NOWHERE;
        final Move ghostLastMove = Move.GO_NOWHERE;
        final int pacManPosition = data.indexOf(Constants.PACMAN);
        final int ghostPosition = data.indexOf(Constants.GHOST);
        final char[] array = data.toCharArray();
        array[pacManPosition] = Constants.SPACE;
        if (ghostPosition > 0)
            array[ghostPosition] = Constants.SPACE;
        final Board board = Board.from(array);
        final int traveledDistance = 0;
        int pillsLeft = 0;
        for (int i = 0; i < data.length(); ++i)
            if (data.charAt(i) == Constants.PILL) ++pillsLeft;
        return new GameState(board, pacManPosition, ghostPosition, pillsLeft, eatenPills, pacmanLastMove, ghostLastMove, traveledDistance);
    }

    public static GameState from(final Game game, final Move pacmanMove, final Move ghostMove) {

        final Board board = game.getBoard();

        if (!pacmanMove.equals(Move.GO_NOWHERE)) {
            final int newPacManPosition = game.getPacmanPosition() + pacmanMove.getDelta();
            if (game.isPillNode(newPacManPosition)) {
                final char[] array = board.toCharArray();
                array[newPacManPosition] = Constants.SPACE;
                return new GameState(
                        Board.from(array),
                        newPacManPosition,
                        game.getGhostPosition(),
                        game.getPillsLeft() - 1,
                        game.getEatenPills() + 1,
                        pacmanMove,
                        game.getGhostLastMove(),
                        game.getTraveledDistance() + 1);
            }
            return new GameState(
                    board,
                    newPacManPosition,
                    game.getGhostPosition(),
                    game.getPillsLeft(),
                    game.getEatenPills(),
                    pacmanMove,
                    game.getGhostLastMove(),
                    game.getTraveledDistance() + 1);
        }

        if (!ghostMove.equals(Move.GO_NOWHERE)) {
            final int newGhostPosition = game.getGhostPosition() + ghostMove.getDelta();
            return new GameState(
                    board,
                    game.getPacmanPosition(),
                    newGhostPosition,
                    game.getPillsLeft(),
                    game.getEatenPills(),
                    game.getPacmanLastMove(),
                    ghostMove,
                    game.getTraveledDistance());
        }

        //throw new RuntimeException(); //TODO
        return game.getGameState();

    }

    private GameState(
            final Board board,
            final int pacManPosition,
            final int ghostPosition,
            final int pillsLeft,
            final int eatenPills,
            final Move pacmanMove,
            final Move ghostLastMove,
            final int traveledDistance) {
        this.board = board;
        this.pacManPosition = pacManPosition;
        this.ghostPosition = ghostPosition;
        this.pillsLeft = pillsLeft;
        this.eatenPills = eatenPills;
        this.pacmanLastMove = pacmanMove;
        this.ghostLastMove = ghostLastMove;
        this.traveledDistance = traveledDistance;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getPacmanPosition() {
        return this.pacManPosition;
    }

    public int getPillsLeft() {
        return this.pillsLeft;
    }

    public int getEatenPills() {
        return this.eatenPills;
    }

    public Move getPacmanLastMove() {
        return this.pacmanLastMove;
    }

    public int getTraveledDistance() {
        return this.traveledDistance;
    }

    public int getGhostPosition() {
        return this.ghostPosition;
    }

    public Move getGhostLastMove() {
        return this.ghostLastMove;
    }

}
