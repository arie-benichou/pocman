
package fr.ut7.dojo.pacman.model;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

@Deprecated
public class BestGhostMoveSearch implements MoveEmitter {

    private final Random random = new Random();

    public Move getMove(final Game game) {
        final List<Move> legalMoves = game.getGhostLegalMoves();
        if (legalMoves.size() == 1) return legalMoves.get(0);
        return this.getMove(game, modifiedLegalMoves(game, legalMoves));
    }

    public static List<Move> modifiedLegalMoves(final Game game, final List<Move> legalMoves) {

        final List<Move> legalMovesCopy = Lists.newArrayList(legalMoves);
        for (final Move move : legalMoves)
            if (game.getPacmanPosition() == game.getGhostPosition() + move.getDelta()) return Lists.newArrayList(move);

        final List<Move> modifiedLegalMoves = Lists.newArrayList();
        if (legalMovesCopy.contains(game.getGhostLastMove())) {
            modifiedLegalMoves.add(game.getGhostLastMove());
            legalMovesCopy.remove(game.getGhostLastMove());
        }

        legalMovesCopy.remove(game.getGhostLastMove().getOpposite()); // TODO ??

        modifiedLegalMoves.addAll(legalMovesCopy);

        return modifiedLegalMoves;
    }

    public Move getMove(final Game game, final List<Move> legalMoves) {

        if (legalMoves.size() == 1) return legalMoves.get(0);

        final List<EvaluatedMove> bestEvaluatedMoves = Lists.newArrayList();
        final int distance = 1;
        final int maximalDistance = 30;
        int bestDistance = 99999;
        final int worstDistance = 0;
        final int position = game.getGhostPosition();
        for (final Move move : legalMoves) {
            final int localBestDistance = 99999;
            final List<Move> moves = Lists.newArrayList();
            final List<Integer> positions = Lists.newArrayList();
            final EvaluatedMove evaluatedMove = new EvaluatedMove(
                    move,
                    this.evaluate(game, move, position, distance, maximalDistance, bestDistance, worstDistance, moves, positions, localBestDistance)
                    , moves);
            if (evaluatedMove.getScore() < bestDistance) {
                bestDistance = evaluatedMove.getScore();
                bestEvaluatedMoves.clear();
                bestEvaluatedMoves.add(evaluatedMove);
            }
            else {
                if (evaluatedMove.getScore() == bestDistance) {
                    if (!move.equals(game.getGhostLastMove().getOpposite())) bestEvaluatedMoves.add(evaluatedMove);
                }
            }
        }

        /*
        System.out.println(game.getGhostLastMove());
        System.out.println(bestEvaluatedMoves);
        try {
            Thread.sleep(800);
        }
        catch (final InterruptedException e) {}
        */

        if (bestEvaluatedMoves.size() == 1) return bestEvaluatedMoves.get(0).getMove();

        for (final EvaluatedMove evaluatedMove : bestEvaluatedMoves) {
            if (evaluatedMove.getMove().equals(game.getGhostLastMove())) return evaluatedMove.getMove();
        }

        return bestEvaluatedMoves.get(this.random.nextInt(1024) % bestEvaluatedMoves.size()).getMove();
    }

    public int evaluate(
            final Game game,
            final Move move,
            final int position,
            final int distance,
            final int maximalDistance,
            int bestDistance,
            final int worstDistance,
            final List<Move> moves,
            List<Integer> positions,
            int localBestDistance
            ) {

        final Game nextGame = game.applyGhostMove(move);

        moves.add(move);

        //if (nextGame.isPillNode(nextGame.getGhostPosition())) return distance;
        if (nextGame.getGhostPosition() == nextGame.getPacmanPosition()) return distance;
        if (distance > localBestDistance) return 99999;
        if (distance == maximalDistance) return 99999;
        //if (positions.contains(nextGame.getGhostPosition())) return 100;
        positions.add(nextGame.getGhostPosition());

        List<Move> nextLegalMoves = nextGame.getGhostLegalMoves();
        nextLegalMoves = modifiedLegalMoves(nextGame, nextLegalMoves);
        if (nextLegalMoves.isEmpty()) return 99999;

        List<Move> bestMoves = Lists.newArrayList();
        for (final Move nextMove : nextLegalMoves) {
            if (distance == 1) positions.clear();
            final List<Move> nextMoves = Lists.newArrayList();
            if (distance == 1) positions = Lists.newArrayList(position);
            localBestDistance = Math.min(localBestDistance, this.evaluate(
                    nextGame,
                    nextMove,
                    position,
                    distance + 1,
                    maximalDistance,
                    bestDistance,
                    worstDistance,
                    nextMoves,
                    positions,
                    localBestDistance
                    ));

            if (localBestDistance < bestDistance) {
                bestDistance = localBestDistance;
                bestMoves = nextMoves;
            }

        }
        moves.addAll(bestMoves);
        return localBestDistance;
    }

}