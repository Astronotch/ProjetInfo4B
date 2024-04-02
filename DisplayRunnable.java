import java.util.ArrayList;

class DisplayRunnable extends Thread {
    public boolean arret = false;

    public void run() {
        while (!arret) {
            DrolGame.moveAdversaire();
            DrolGame.moveProjectile();
            if(!arret){
                displayGrid();
                try {
                    Thread.sleep(200); // Actualisation toutes les 1 seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void displayGrid() {
        for (int i = 0; i < DrolGame.getWidth(); i++) {
            for (int j = 0; j < DrolGame.getHeight(); j++) {
                System.out.print(DrolGame.getGrid()[i][j]);
            }
            System.out.println();
        }
        ArrayList<Joueur> joueurs = DrolGame.getJoueurs();

        for(Joueur joueur:joueurs){
            System.out.println("Score " + joueur.getPseudo() + " :"+ Integer.toString(joueur.getScore()));
        }
        System.out.println("\n\n\n\n\n\n\n");
    }

    public void arret(){
        arret = true;
    }
}
