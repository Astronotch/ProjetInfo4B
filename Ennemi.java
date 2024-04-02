public class Ennemi extends EntiteBouge{

    public Ennemi(int x, int y){
        super(x, y, "►", "D");
    }

    public void bougeHaut(){
        super.entiteX-=1;
        super.direction = "H";
    }
    public void bougeBas(){
        super.entiteX+=1;
        direction = "B";
    }
    public void bougeGauche(){
        super.entiteY-=1;
        super.setDirection("G");
    }
    public void bougeDroite(){
        super.entiteY+=1;
        super.setDirection("D");
    }

    public void setChara(String dir){
        switch(dir){
            case "G":
                super.chara = "◄";
                break;
            case "D":
                super.chara = "►";
                break;
        }
    }

}
