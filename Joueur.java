import java.io.PrintWriter;

public class Joueur extends EntiteBouge{
    private String pseudo;
    private String couleur;
    private int score;
    private PrintWriter out;


    public Joueur(int x, int y, String couleur, String direction, PrintWriter out, String pseudo){
        super(x, y, "→", direction);
        this.setDirection(direction);
        this.couleur = couleur;
        this.score = 0;
        this.setCouleur(couleur);
        this.out = out;
        this.pseudo = pseudo;
    }

    public void setCouleur(String couleur){
        switch(couleur){
            case "rouge":
                super.chara = "\u001B[31m" + this.getChara() + "\u001B[0m";
            case "vert":
                super.chara = "\u001B[32m" + this.getChara() + "\u001B[0m";
            case "jaune":
                super.chara = "\u001B[33m" + this.getChara() + "\u001B[0m";
            case "bleu":
                super.chara = "\u001B[34m" + this.getChara() + "\u001B[0m";
        }
    }

    public String getCouleur(){
        return this.couleur;
    }

    public int getScore(){
        return this.score;
    }

    public void ajScore(int ajout){
        this.score+=ajout;
    }

    public PrintWriter getOut(){
        return this.out;
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public void bougeHaut(){
        super.entiteX-=1;
        super.setDirection("H");
        this.setCouleur(couleur);
    }
    public void bougeBas(){
        super.entiteX+=1;
        super.setDirection("B");
        this.setCouleur(couleur);
    }
    public void bougeGauche(){
        super.entiteY-=1;
        super.setDirection("G");
        this.setCouleur(couleur);
    }
    public void bougeDroite(){
        super.entiteY+=1;
        super.setDirection("D");
        this.setCouleur(couleur);
    }

    public void setChara(String dir){
        switch(dir){
            case "H":
                super.chara = "↑";
                break;
            case "B":
                super.chara = "↓";
                break;
            case "G":
                super.chara = "←";
                break;
            case "D":
                super.chara = "→";
                break;
        }
    }
}
