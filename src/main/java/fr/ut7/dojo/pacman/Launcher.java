
package fr.ut7.dojo.pacman;

import fr.ut7.dojo.pacman.model.BestGhostMoveSearch;
import fr.ut7.dojo.pacman.model.BestPacmanMoveSearch;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameLoop;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;

public final class Launcher {

    private Launcher() {}

    public static void launch(final String data) {

        //final int td1 = new GameLoop().start(Game.from(new PacmanReferee(), new PacmanPlayer(new HumanController()), GameState.from(data)));
        //final int td1 = 0;
        //System.out.println(td1);

        final int td2 = new GameLoop().start(
                Game.from(
                        new PacmanReferee(),
                        new Pacman(new BestPacmanMoveSearch()),
                        new GhostReferee(),
                        new Ghost(new BestGhostMoveSearch()),
                        GameState.from(data))
                );
        System.out.println(td2);

        /*
        if (td1 < td2) {
            System.out.println(td1 + " < " + td2 + " => T'as gagné, pourri !");
        }<
        else if (td1 > td2) {
            System.out.println(td1 + " > " + td2 + " => T'as perdu, gros naze !");
        }
        else {
            System.out.println(td1 + " = " + td2 + " => Bof, pas terrible, franchement...");
        }
        */

    }

    public static void main(final String[] args) {

        /*
        launch(Levels.LEVEL1);
        launch(Levels.LEVEL2);
        launch(Levels.LEVEL3);
        launch(Levels.LEVEL4);
        launch(Levels.LEVEL5);
        launch(Levels.LEVEL6);
        launch(Levels.LEVEL7);
        launch(Levels.LEVEL8);
        launch(Levels.LEVEL9);
        launch(Levels.LEVEL10);
        launch(Levels.LEVEL11);
        launch(Levels.LEVEL12);
        launch(Levels.LEVEL14);
        launch(Levels.LEVEL15);
        launch(Levels.LEVEL13);
        */

        /*
        launch(Levels.DEBUG11);
        launch(Levels.DEBUG112);
        launch(Levels.DEBUG113);
        */

        launch(Levels.LEVEL11);
        /*
        launch(Levels.LEVEL155);
        launch(Levels.DEBUG16);
        //launch(Levels.LEVEL5);
        launch(Levels.DEBUG13);
        launch(Levels.DEBUG14);
        launch(Levels.DEBUG15);
        //launch(Levels.LEVEL12);
        //launch(Levels.LEVEL15);

        //launch(Levels.LEVEL2); // TODO DEBUG12 (si un noeud a 2 options et qu'il ne s'agit pas de directions opposés: en faire un noeud !)
        //launch(Levels.LEVEL14); // TODO DEBUG13
        //launch(Levels.LEVEL10); // TODO DEBUG14 (si un noeud a été visité et une arrete aussi, préférer la plus courte)
        //launch(Levels.LEVEL12); // TODO DEBUG15 (bug boucle)
        launch(Levels.LEVEL17);
        */

        System.gc();

    }
}