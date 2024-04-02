public abstract class EntiteBouge extends Entite {
    protected String direction;
    protected boolean estMort;

    public EntiteBouge(int x, int y, String chara, String direction){
        super(x, y, chara);
        this.estMort = false;
        this.direction = direction;
    }

    public String getDirection(){
        return this.direction;
    }

    public void setDirection(String newDir){
        this.direction = newDir;
        this.setChara(newDir);
    }

    public boolean estMort(){
        return this.estMort;
    }

    public void meurt(){
        this.estMort = true;
        this.chara = " ";
    }

    public abstract void setChara(String dir);

    public abstract void bougeHaut();
    public abstract void bougeBas();
    public abstract void bougeGauche();
    public abstract void bougeDroite();
    
}
