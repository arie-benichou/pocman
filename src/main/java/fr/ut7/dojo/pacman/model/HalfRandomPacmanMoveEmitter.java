
package fr.ut7.dojo.pacman.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Deprecated
public class HalfRandomPacmanMoveEmitter implements MoveEmitter {

    private final Random random = new Random();

    public Move getMove(final Game game) {
        final List<Move> legalMoves = game.getPacmanLegalMoves();
        if (legalMoves.size() == 1) return legalMoves.get(0);
        //return this.getMove(game, modifiedLegalMoves(game, legalMoves));
        final List<Move> modifiedLegalMoves = modifiedLegalMoves(game, legalMoves);
        return modifiedLegalMoves.get(this.random.nextInt(1024) % modifiedLegalMoves.size());
    }

    public static List<Move> modifiedLegalMoves(final Game game, final List<Move> legalMoves) {

        final List<Move> legalMovesCopy = Lists.newArrayList(legalMoves);

        final List<Move> oneMoveAwayPill = Lists.newArrayList();
        for (final Move move : legalMoves) {
            if (game.getGhostPosition() == game.getPacmanPosition() + move.getDelta()) continue; // TODO
            if (game.isPillNode(game.getPacmanPosition() + move.getDelta())) {
                oneMoveAwayPill.add(move);
                legalMovesCopy.remove(move);
            }
        }

        final List<Move> modifiedLegalMoves = Lists.newArrayList();

        if (oneMoveAwayPill.contains(game.getPacmanLastMove())) {
            modifiedLegalMoves.add(game.getPacmanLastMove());
            oneMoveAwayPill.remove(game.getPacmanLastMove());
        }
        modifiedLegalMoves.addAll(oneMoveAwayPill);

        legalMovesCopy.remove(game.getPacmanLastMove().getOpposite());
        modifiedLegalMoves.addAll(legalMovesCopy);

        return modifiedLegalMoves;
    }

    public Move getMove(final Game game, final List<Move> legalMoves) {

        final List<EvaluatedMove> evaluatedMoves = Lists.newArrayList();

        if (legalMoves.size() == 1) return legalMoves.get(0);

        final int distance = 1;
        final int maximalDistance = 27 * 19;
        int bestDistance = 99999;
        final int worstDistance = 0;
        final int pacmanPosition = game.getPacmanPosition();

        for (final Move pacmanMove : legalMoves) {
            final int localBestDistance = 99999;
            final List<Move> moves = Lists.newArrayList();
            //final Map<Integer, Integer> positions = Maps.newHashMap();
            final List<Integer> positions = Lists.newArrayList();
            //positions.put(pacmanPosition, 1);
            final EvaluatedMove evaluatedMove =
                                                new EvaluatedMove(pacmanMove,
                                                        this.evaluate(game, pacmanMove, pacmanPosition, distance, maximalDistance, bestDistance, worstDistance,
                                                                moves, positions, localBestDistance), moves);

            if (evaluatedMove.getScore() < bestDistance)
                bestDistance = evaluatedMove.getScore();

            // TODO trier ici !

            //System.out.println(evaluatedMove);
            evaluatedMoves.add(evaluatedMove);
            //System.out.println();
        }
        Collections.sort(evaluatedMoves);

        ///System.out.println(evaluatedMoves);

        final int bestEvaluatedScore = evaluatedMoves.get(0).getScore();

        ///System.out.println(evaluatedMoves);
        final List<EvaluatedMove> filtered = Lists.newArrayList(Iterables.filter(evaluatedMoves, new Predicate<EvaluatedMove>() {

            public boolean apply(final EvaluatedMove input) {
                return input.getScore() == bestEvaluatedScore;
            }
        }));

        if (filtered.size() == 1) {
            ///System.out.println("ONLY ONE BEST MOVE");
            return filtered.get(0).getMove();

        }
        else {
            ///System.out.println("MORE THAN ONE BEST MOVE");
        }

        final List<Move> bestMoves = Lists.newArrayList();
        for (final EvaluatedMove evaluatedMove : filtered) {
            if (evaluatedMove.getMove().equals(game.getPacmanLastMove())) {
                ///System.out.println("PRIORITY TO LAST MOVE");
                //return evaluatedMove.getMove();
            }
            bestMoves.add(evaluatedMove.getMove());
        }

        if (bestMoves.contains(game.getPacmanLastMove().getOpposite())) {
            //bestMoves.remove(game.getPacmanLastMove().getOpposite());
            ///System.out.println("OPPOSITE MOVE REMOVED");
        }

        if (game.isEmulated > -1 && game.isEmulated < 1) {

            System.out.println("emulation...");
            /*
            try {
                System.out.println(bestMoves);
                Thread.sleep(1000);
            }
            catch (final InterruptedException e) {}
            */

            ++game.isEmulated;

            final List<EvaluatedMove> emulatedMoves = Lists.newArrayList();

            int best = 99999;

            for (final EvaluatedMove evaluatedMove : evaluatedMoves) {
                final int depth = 400;//120 / game.isEmulated + 10;
                //System.out.println(depth);
                final Game emulatedGame = game.applyPacmanMove(evaluatedMove.getMove());
                final int score = new GameLoop().emulate(emulatedGame, depth, best);
                if (score < best) best = score;
                emulatedMoves.add(new EvaluatedMove(evaluatedMove.getMove(), score, null));
            }

            Collections.sort(emulatedMoves);

            /*
            System.out.println(emulatedMoves);
            try {
                System.out.println(bestMoves);
                Thread.sleep(1);
            }
            catch (final InterruptedException e) {}
            */

            --game.isEmulated;
            System.out.println(emulatedMoves);
            return emulatedMoves.get(0).getMove();
        }

        /*
        if (bestMoves.size() == 1) return bestMoves.get(0);

        System.out.println("RANDOM"); // TODO 3eme function déterminante
        try {
            System.out.println(bestMoves);
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {}
        */

        return evaluatedMoves.get(this.random.nextInt(1024) % evaluatedMoves.size()).getMove();

    }

    public int evaluate(
            final Game game,
            final Move move,
            final int pacmanPosition,
            final int distance,
            final int maximalDistance,
            int bestDistance,
            final int worstDistance,
            final List<Move> moves,
            List<Integer> positions,
            int localBestDistance
            ) {

        final Game nextGame = game.applyPacmanMove(move);

        moves.add(move);

        if (nextGame.getGhostPosition() == nextGame.getPacmanPosition()) return Integer.MAX_VALUE;
        if (nextGame.getEatenPills() > game.getEatenPills()) return distance;
        if (distance > localBestDistance) return 997;
        if (distance == maximalDistance) return 998;
        if (positions.contains(nextGame.getPacmanPosition())) return 996;
        positions.add(nextGame.getPacmanPosition());

        List<Move> nextLegalMoves = nextGame.getPacmanLegalMoves();
        nextLegalMoves = modifiedLegalMoves(nextGame, nextLegalMoves);
        if (nextLegalMoves.isEmpty()) return 999;

        List<Move> bestMoves = Lists.newArrayList();
        for (final Move nextPacmanMove : nextLegalMoves) {
            if (distance == 1) {
                /*
                final HashMap<Integer, Integer> newHashMap = Maps.newHashMap();
                final HashSet<Integer> newHashSet = Sets.newHashSet(positions);
                for (final Integer integer : newHashSet) {
                    newHashMap.put(integer, Collections.frequency(positions, integer));
                }
                System.out.println();
                System.out.println(newHashMap.size());
                System.out.println(newHashMap);
                System.out.println();
                */
                positions.clear();
            }
            final List<Move> nextMoves = Lists.newArrayList();
            //if (distance == 0) positions = Lists.newArrayList(pacmanPosition, nextGame.getPacmanPosition());
            if (distance == 1) positions = Lists.newArrayList(pacmanPosition);
            localBestDistance = Math.min(localBestDistance, this.evaluate(
                    nextGame,
                    nextPacmanMove,
                    pacmanPosition,
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