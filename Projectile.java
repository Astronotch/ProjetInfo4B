public class Projectile extends EntiteBouge{
    private Joueur joueur;

    public Projectile(int x, int y, String direction, Joueur joueur) {
        super(x, y, "•", direction);
        this.joueur = joueur;
    }

    public Joueur getJoueur(){
        return this.joueur;
    }

    public void bougeHaut(){
        super.entiteX-=1;
        super.setDirection("H");
    }
    public void bougeBas(){
        super.entiteX+=1;
        super.setDirection("B");
    }
    public void bougeGauche(){
        super.entiteY-=1;
        super.setDirection("G");
    }
    public void bougeDroite(){
        super.entiteY+=1;
        super.setDirection("D");
    }

    public void setChara(String dir){}
    
}
