public class Famille extends Entite {
    private boolean estDelivre;
    
    public Famille(int x, int y){
        super(x, y, "F");
        this.estDelivre = false;
    }
    
    public boolean estDelivre(){
        return this.estDelivre;
    }
    public void delivre(){
        this.estDelivre = true;
    }

}
