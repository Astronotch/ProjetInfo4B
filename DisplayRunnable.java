import java.util.*;

class DisplayRunnable extends Thread {
    private static boolean arret = false;

    public void run() {
        while (!arret) {
            DrolGame.moveAdversaire();
            DrolGame.moveProjectile();
            if(!arret){
                displayGrid();
                try {
                    Thread.sleep(200); // Actualisation toutes les 0.2 seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        displayGrid();
        System.out.println("Fin du jeu!");
        if(DrolGame.getSolo()){
            System.out.println("Le joueur a amassé " + DrolGame.getJoueurs().get(0).getScore() + " points");
        }else{
            Equipe equipeGagne = DrolGame.getEquipeGagnante();
            System.out.println("L'équipe avec le plus de point est l'équipe " + equipeGagne.getNom() + " avec " + equipeGagne.getScore() + " points");
        }
    }

    public static void displayGrid() {
        for (int i = 0; i < DrolGame.getWidth(); i++) {
            for (int j = 0; j < DrolGame.getHeight(); j++) {
                System.out.print(DrolGame.getGrid()[i][j]);
            }
            System.out.println();
        }
        ArrayList<Joueur> joueurs = DrolGame.getJoueurs();
        if(!DrolGame.getSolo()){
            for(Joueur joueur:joueurs){
                System.out.println("[" + joueur.getEquipe().getNom() + "] " + joueur.getPseudo() + " :"+ Integer.toString(joueur.getScore()));
            }
        }else{
            System.out.println(joueurs.get(0).getPseudo() + " :" + joueurs.get(0).getScore());
        }
        System.out.println("\n\n\n\n\n");
    }

    public static void arret(){
        arret = true;
    }
}
