
package fr.ut7.dojo.pacman.model;

import java.util.List;

import com.google.common.collect.Lists;

public class PacmanReferee {

    public boolean isLegalMove(final Game game, final Move pacmanMove) {
        final int nodeIndex = game.getPacmanPosition() + pacmanMove.getDelta();
        return game.isSpaceNode(nodeIndex) || game.isPillNode(nodeIndex);
    }

    public List<Move> getLegalMoves(final Game game) {
        final List<Move> legalMoves = Lists.newArrayList();
        for (final Direction direction : game.getDirections(game.getPacmanPosition()))
            if (this.isLegalMove(game, Move.from(direction))) legalMoves.add(Move.from(direction));
        return legalMoves;
    }
}