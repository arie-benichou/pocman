
package fr.ut7.dojo.pacman.model;

import java.util.List;

import com.google.common.collect.Lists;

public class GhostReferee {

    public boolean isLegalMove(final Game game, final Move move) {
        final int nodeIndex = game.getGhostPosition() + move.getDelta();
        return game.isSpaceNode(nodeIndex) || game.isPillNode(nodeIndex);
    }

    public List<Move> getLegalMoves(final Game game) {
        final List<Move> legalMoves = Lists.newArrayList();
        for (final Direction direction : game.getDirections(game.getGhostPosition()))
            if (this.isLegalMove(game, Move.from(direction))) legalMoves.add(Move.from(direction));
        return legalMoves;
    }
}