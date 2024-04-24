import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class PlayerHandler extends Thread {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private Joueur joueur;
    private final String grid[][];

    public PlayerHandler(Socket socket, PrintWriter out, BufferedReader in, String[][] grid, Joueur joueur) throws IOException {
        this.clientSocket = socket;
        this.joueur = joueur;
        this.out = out;
        this.in = in;
        this.grid = grid;
    }

    public void run() {
        try {
            String choixEquipe = "-1";
            if(DrolGame.getEquipes().size()>1){
                out.println("Choisissez votre équipe:");
                switch(DrolGame.getEquipes().size()){
                    case 4:
                        out.println("1-Equipe rouge");
                        out.println("2-Equipe bleue");
                        out.println("3-Equipe verte");
                        out.println("4-Equipe jaune");
                        break;
                    case 3:
                        out.println("1-Equipe rouge");
                        out.println("2-Equipe bleue");
                        out.println("3-Equipe verte");
                        break;
                    case 2:
                        out.println("1-Equipe rouge");
                        out.println("2-Equipe bleue");
                        break;
                }
            }

            while(!DrolGame.equipePleine() && (((choixEquipe = (String)in.readLine()) != null) && !choixEquipe.equals("-1") && !DrolGame.getSolo())){
                switch(choixEquipe){
                    case "1":
                        if(!joueur.getEquipe().getNom().equals("None")){
                            joueur.getEquipe().supprimerJoueur(joueur);
                        }
                        DrolGame.getEquipes().get(0).ajoutJoueur(joueur);
                        joueur.setEquipe(DrolGame.getEquipes().get(0));
                        out.println("Vous êtes dans l'équipe " + joueur.getEquipe().getNom());
                        System.out.println(joueur.getPseudo() + " est dans l'équipe " + joueur.getEquipe().getNom());
                        break;
                    case "2":
                        if(!joueur.getEquipe().getNom().equals("None")){
                            joueur.getEquipe().supprimerJoueur(joueur);
                        }
                        joueur.setEquipe(DrolGame.getEquipes().get(1));
                        DrolGame.getEquipes().get(1).ajoutJoueur(joueur);
                        out.println("Vous êtes dans l'équipe " + joueur.getEquipe().getNom());
                        System.out.println(joueur.getPseudo() + " est dans l'équipe " + joueur.getEquipe().getNom());
                        break;
                    case "3":
                        if(!joueur.getEquipe().getNom().equals("None")){
                            joueur.getEquipe().supprimerJoueur(joueur);    
                        }
                        joueur.setEquipe(DrolGame.getEquipes().get(2));
                        DrolGame.getEquipes().get(2).ajoutJoueur(joueur);
                        out.println("Vous êtes dans l'équipe " + joueur.getEquipe().getNom());
                        System.out.println(joueur.getPseudo() + " est dans l'équipe " + joueur.getEquipe().getNom());
                        break;
                    case "4":
                        if(!joueur.getEquipe().getNom().equals("None")){
                            joueur.getEquipe().supprimerJoueur(joueur);    
                        }
                        joueur.setEquipe(DrolGame.getEquipes().get(3));
                        DrolGame.getEquipes().get(3).ajoutJoueur(joueur);
                        out.println("Vous êtes dans l'équipe " + joueur.getEquipe().getNom());
                        System.out.println(joueur.getPseudo() + " est dans l'équipe " + joueur.getEquipe().getNom());
                        break;
                }
            }

            String move;
            while ((move = (String) in.readLine()) != null) {
                if (DrolGame.isValidMove(move)) {
                    DrolGame.moveJoueur(move, joueur);
                    if(DrolGame.toucheFamille(joueur)){
                        out.println("Vous venez de sauver une famille");
                        out.println("+50 points");
                        DrolGame.ajNbFamille();
                        joueur.ajScore(50);
                        Famille famDeli = DrolGame.familleTouche(joueur);
                        famDeli.delivre();
                        grid[famDeli.getX()][famDeli.getY()] = joueur.getChara();
                        if(DrolGame.getNbFamilleDelivre() == DrolGame.getNbFamille()){
                            for(Joueur AutreJoueur:DrolGame.getJoueurs()){
                                if(!AutreJoueur.estMort() && AutreJoueur!=joueur){
                                    AutreJoueur.getOut().println("Dommage, un autre robot a sauvé la dernière famille");
                                    AutreJoueur.getOut().println("END");
                                }
                            }
                            out.println("Bravo, toutes les familles ont été sauvées");
                            out.println("END");

                            try{
                                FileOutputStream outputStream = new FileOutputStream("score.txt", true);
                                PrintWriter printStream = new PrintWriter(outputStream, true, StandardCharsets.UTF_8);
                                for(Joueur joueur:DrolGame.getJoueurs()){
                                    printStream.print(joueur.getPseudo() + ": " + joueur.getScore() + "\n");
                                }
                                printStream.close();
                            }catch(IOException e){e.printStackTrace();}
                            break;
                        }
                    }                    
                } else {
                    out.println("Invalid move! Try again (Z/Q/S/D)");
                }

                if (move.equals("END")) {
                    break;
                }
            }

            if(DrolGame.getNbJoueurVivant() == 0 || DrolGame.getNbFamilleDelivre() == DrolGame.getNbFamille()){
                DisplayRunnable.arret();
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}