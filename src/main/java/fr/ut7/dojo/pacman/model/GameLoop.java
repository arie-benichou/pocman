
package fr.ut7.dojo.pacman.model;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import fr.ut7.dojo.pacman.view.GameView;

public final class GameLoop {

    private final GameView gameView = new GameView();

    private static void delay(final int n) {
        try {
            Thread.sleep(n);
        }
        catch (final InterruptedException e) {}
    }

    public int start(Game game) {

        System.out.println(this.gameView.render(game));

        delay(500);

        final Stopwatch stopwatch = new Stopwatch().start();
        int i = 0;
        while (game.getGameState().getPillsLeft() != 0) {

            ++i;

            stopwatch.stop();
            delay(80);
            stopwatch.start();

            final Move pacmanMove = game.getPacmanMove();
            game = game.applyPacmanMove(pacmanMove);

            if (i % 2 == 0) {
                final Move ghostMove = game.getGhostMove();
                game = game.applyGhostMove(ghostMove);
            }

            if (game.getGhostPosition() == game.getPacmanPosition()) {
                delay(1000);
            }

            System.out.println(this.gameView.render(game) + "\n");

        }

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " ms");
        delay(1000);

        return game.getTraveledDistance();
    }

    public int emulate(Game game, final int limit, final int cuttoff) { // TODO injecter l'agent pour la vue
        int i = -1;
        int score = 99999;
        while (++i < limit) {

            game = game.applyPacmanMove(game.getPacmanMove());
            game = game.applyGhostMove(game.getGhostMove());
            if (game.getGhostPosition() == game.getPacmanPosition()) return 999999;

            if (game.getPillsLeft() == 0) return game.getTraveledDistance() - 1000;

            score = game.getTraveledDistance() + game.getPillsLeft();
            if (score >= cuttoff) { return 997; }
        }
        return score;
    }
}