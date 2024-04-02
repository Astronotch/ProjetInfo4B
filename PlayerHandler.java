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
            String move;
            out.println("Z/Q/S/D");
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
                            //Faire un arrêt pour tout les player input ou alors juste y mettre en pause jusqu'au prochain niveau
                            for(Joueur AutreJoueur:DrolGame.getJoueurs()){
                                if(!AutreJoueur.estMort() && AutreJoueur!=joueur){
                                    AutreJoueur.getOut().println("Dommage, un autre robot au sauvé toutes les familles");
                                    AutreJoueur.getOut().println("END");
                                }
                            }
                            out.println("END");
                            out.println("Bravo, vous avez sauvé toutes les familles");
                            joueur.ajScore(50);

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

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}