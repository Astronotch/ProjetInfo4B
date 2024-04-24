import java.util.ArrayList;

public class Equipe {
    private ArrayList<Joueur> joueurs;
    private String nom;

    public Equipe(String nom){
        this.nom = nom;
        this.joueurs = new ArrayList<Joueur>();
    }

    public synchronized void ajoutJoueur(Joueur joueur){
        this.joueurs.add(joueur);
    }

    public void supprimerJoueur(Joueur joueur){
        this.joueurs.remove(joueur);
    }

    public ArrayList<Joueur> getJoueurs(){
        return this.joueurs;
    }

    public String getNom(){
        return this.nom;
    }

    public int getSize(){
        return this.joueurs.size();
    }

    public int getScore(){
        return this.joueurs.get(0).getScore();
    }
}
