
package fr.ut7.dojo.pacman.model;

import java.util.List;

import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;

public final class Game {

    public int isEmulated = 0;

    private final GameState gameState;

    private final PacmanReferee pacmanReferee;
    private final Pacman pacmanPlayer;

    private final GhostReferee ghostReferee;
    private final Ghost ghostPlayer;

    public static Game from(
            final PacmanReferee pacmanReferee,
            final Pacman pacmanPlayer,
            final GhostReferee ghostReferee,
            final Ghost ghostPlayer,
            final GameState gameState) {
        return new Game(pacmanReferee, pacmanPlayer, ghostReferee, ghostPlayer, gameState);
    }

    public static Game from(final Game game, final Move pacmanMove, final Move ghostMove) {
        final Game newGame = from(
                game.getPacmanReferee(), game.getPacmanPlayer(),
                game.getGhostReferee(), game.getGhostPlayer(),
                GameState.from(game, pacmanMove, ghostMove)
                             );
        newGame.isEmulated = game.isEmulated;
        return newGame;
    }

    private Game(final PacmanReferee pacmanReferee, final Pacman pacmanPlayer, final GhostReferee ghostReferee,
            final Ghost ghostPlayer, final GameState gameState) {
        this.pacmanReferee = pacmanReferee;
        this.pacmanPlayer = pacmanPlayer;
        this.ghostReferee = ghostReferee;
        this.ghostPlayer = ghostPlayer;
        this.gameState = gameState;
    }

    public PacmanReferee getPacmanReferee() {
        return this.pacmanReferee;
    }

    public Pacman getPacmanPlayer() {
        return this.pacmanPlayer;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Game applyPacmanMove(final Move move) {
        return this.isPacmanLegalMove(move) ? Game.from(this, move, Move.GO_NOWHERE) : this;
    }

    public Game applyGhostMove(final Move move) {
        if (move.equals(Move.GO_NOWHERE)) return this;
        return this.isGhostLegalMove(move) ? Game.from(this, Move.GO_NOWHERE, move) : this;
    }

    // façade
    public Board getBoard() {
        return this.gameState.getBoard();
    }

    // façade    
    public List<Direction> getDirections(final int nodeIndex) {
        return this.getBoard().getDirections(nodeIndex);
    }

    // façade
    public Move getPacmanMove() {
        return this.getPacmanPlayer().getMove(this);
    }

    // façade
    public List<Move> getPacmanLegalMoves() {
        return this.getPacmanReferee().getLegalMoves(this);
    }

    // façade
    public int getPacmanPosition() {
        return this.getGameState().getPacmanPosition();
    }

    // façade
    public Move getPacmanLastMove() {
        return this.getGameState().getPacmanLastMove();
    }

    // façade
    public boolean isPacmanLegalMove(final Move move) {
        return this.getPacmanReferee().isLegalMove(this, move);
    }

    // façade
    public int getEatenPills() {
        return this.getGameState().getEatenPills();
    }

    // façade
    public int getPillsLeft() {
        return this.getGameState().getPillsLeft();
    }

    public int getTraveledDistance() {
        return this.getGameState().getTraveledDistance();
    }

    // façade
    public char getNode(final int nodeIndex) {
        return this.getBoard().getCell(nodeIndex);
    }

    // façade
    public boolean isSpaceNode(final int nodeIndex) {
        return this.getBoard().getCell(nodeIndex) == Constants.SPACE;
    }

    // façade
    public boolean isPillNode(final int nodeIndex) {
        return this.getBoard().getCell(nodeIndex) == Constants.PILL;
    }

    // façade
    public int getGhostPosition() {
        return this.getGameState().getGhostPosition();
    }

    private GhostReferee getGhostReferee() {
        return this.ghostReferee;
    }

    // façade
    public List<Move> getGhostLegalMoves() {
        return this.getGhostReferee().getLegalMoves(this);
    }

    // façade    
    private Ghost getGhostPlayer() {
        return this.ghostPlayer;
    }

    // façade    
    public Move getGhostMove() {
        return this.getGhostPosition() > 0 ? this.getGhostPlayer().getMove(this) : Move.GO_NOWHERE;
    }

    // façade    
    private boolean isGhostLegalMove(final Move move) {
        return this.getGhostReferee().isLegalMove(this, move);
    }

    public Move getGhostLastMove() {
        return this.getGameState().getGhostLastMove();
    }

}