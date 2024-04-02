public class Entite {
    protected int entiteX, entiteY;
    protected String chara;

    public Entite(int x, int y, String chara){
        this.entiteX = x;
        this.entiteY = y;
        this.chara = chara;
    }

    public int getX(){
        return this.entiteX;
    }

    public int getY(){
        return this.entiteY;
    }

    public String getChara(){
        return this.chara;
    }
}
