import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class DrolGame {
    private static final int HEIGHT = 50;
    private static int WIDTH;

    private static int nbEnnemi;
    private static int nbFamille;
    private static int nbFamilleDelivre = 0;
    private static int nbJoueur;
    private static int nbJoueurVivant;
    private static int nbPassage;

    private static final String EMPTY = " ";
    private static final String WALL = "#";
    private static final String FAMILY = "F";
    private static final String ENNEMY1 = "◄";
    private static final String ENNEMY2 = "►";

    private static final ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private static final ArrayList<Ennemi> ennemis = new ArrayList<Ennemi>();
    private static final ArrayList<Famille> familles = new ArrayList<Famille>();
    private static final ArrayList<Joueur> joueurs = new ArrayList<Joueur>();

    private static String[][] grid;

    private static Socket socket;
    private static OutputStream outputStream;
    private static PrintWriter out;
    private static InputStream inputStream;
    private static BufferedReader stdIn;
    private static DisplayRunnable displayThread;    

    public static void main(String[] args) {
        int option;
        int tempOption;
        int tempJoueur;

        initOption();

        Scanner reponse = new Scanner(System.in);
        do{
            ///////////////////////////////////////////////////////////////////FAIRE LE MENU ICI/////////////////////////////////////////////////////////////////// 
            System.out.println();
            System.out.println("Bienvenue dans le jeu de Drol");
            System.out.println("0-Quitter le jeu");
            System.out.println("1-Un joueur");
            System.out.println("2-Multijoueur");
            System.out.println("3-Score");
            System.out.println("4-Options");
            System.out.println("5-Règles du jeu");

            option = reponse.nextInt();

            switch(option){
                case 0:
                    System.exit(1);
                case 1:
                    nbJoueur = 1;
                    break;
                case 2:
                    do{
                        System.out.println("Choisissez un nombre de joueur entre 2 et 4:");
                        tempJoueur = reponse.nextInt();
                    }while(tempJoueur>4 || tempJoueur<2);
                    nbJoueur = tempJoueur;
                    break;
                case 3:
                    afficheScore();
                    System.out.println();
                    break;
                case 4:
                    do{
                        System.out.println();
                        System.out.println("0-Retourner à l'écran précédent");
                        System.out.println("1-Choisir le nombre d'étage");
                        System.out.println("2-Choisir le nombre d'objectif");
                        System.out.println("3-Choisir le nombre d'ennemi");
                        System.out.println("4-Choisir le nombre de passage");
                        tempOption = reponse.nextInt();
                        switch(tempOption){
                            case 1:
                                int tempOptionEtage;
                                do{
                                    System.out.println();
                                    System.out.println("Choisissez le nombre d'étage (3-10)");
                                    System.out.println("Nombre d'étage actuellement: " + ((int)(WIDTH/2)));
                                    tempOptionEtage = reponse.nextInt();
                                }while(tempOptionEtage>10 || tempOptionEtage<3);
                                WIDTH = 2*tempOptionEtage + 1;
                                grid = new String[WIDTH][HEIGHT];
                                actuOption();
                                break;
                            case 2:
                                int tempOptionObjectif;
                                do{
                                    System.out.println();
                                    System.out.println("Choisissez le nombre d'objectif (1-100)");
                                    System.out.println("Nombre d'objectif actuellement: " + nbFamille);
                                    tempOptionObjectif = reponse.nextInt();
                                    if(tempOptionObjectif+nbEnnemi>100){
                                        System.out.println("Il y a trop d'entités sur le plateau, pour ajouter plus d'objectifs, reduisez le nombre d'ennemis");
                                        System.out.println("(Limite max: 1OO)");
                                    }
                                }while(tempOptionObjectif>100 || tempOptionObjectif<1 ||(tempOptionObjectif+nbEnnemi>100));
                                nbFamille = tempOptionObjectif;
                                actuOption();
                                break;

                            case 3:
                                int tempOptionEnnemi;
                                do{
                                    System.out.println();
                                    System.out.println("Choisissez le nombre d'ennemi (1-100)");
                                    System.out.println("Nombre d'ennemi actuellement: " + nbEnnemi);
                                    tempOptionEnnemi = reponse.nextInt();
                                    if(tempOptionEnnemi+nbFamille>100){
                                        System.out.println("Il y a trop d'entités sur le plateau, pour ajouter plus d'ennemis, reduisez le nombre d'objectifs");
                                        System.out.println("(Limite max: 1OO)");
                                    }
                                }while(tempOptionEnnemi>100 || tempOptionEnnemi<1 || (tempOptionEnnemi+nbFamille>100));
                                nbEnnemi = tempOptionEnnemi;
                                actuOption();
                                break;

                            case 4:
                                int tempOptionPassage;
                                do{
                                    System.out.println();
                                    System.out.println("Choisissez le nombre d'ennemi (1-48)");
                                    System.out.println("Nombre de passage actuellement compris entre 1 et " + nbPassage);
                                    tempOptionPassage = reponse.nextInt();
                                }while(tempOptionPassage>48 || tempOptionPassage<1);
                                nbPassage = tempOptionPassage;
                                actuOption();
                                break;
                        }
                    }while(tempOption != 0);
                    break;
                case 5:
                    System.out.println("\n");
                    System.out.println("Ce programme est une adaptation du jeu de Drol créé en 1983 par Benny Aik Beng Ngo et publié par BroderBund.");
                    System.out.println("Il y a plusieurs mode de jeu, un mode de jeu en solo, en mode de jeu en multi coopératif et un multi en mode affrontement.");
                    System.out.println("Dans le mode solo, on contrôle un personnage (représenté par une flèche vide de couleur \u001B[31m→\u001B[0m) qui se bat contre des ennemis (représentés par une flèche remplie ►) et l'objectif du jeu est de sauver des familles (représentées par la lettre F), tout en faisant le plus haut score.");
                    System.out.println("Pour se faire, on peut se déplacer dans les quatres directions en utilisant les lettres Z/Q/S/D et en tirant un projectile à l'aide de la barre espace.");
                    System.out.println("Dans le mode multijoueur, il est possible de créer des équipes. Le score de chaque joueur est commun à l'équipe et il est impossible d'éliminer son coéquipier avec ses projectiles.");                    
                    System.out.println("\n");

            }
        }while(option!=1 && option!=2);
        reponse.close();
        
        
        
        
        
        initializeGrid();

        try (ServerSocket serverSocket = new ServerSocket(1234)){
            System.out.println("Waiting for player(s)...");
            displayThread = new DisplayRunnable();


            do{
                socket = serverSocket.accept();
                System.out.println("Nouveau client connecté");

                outputStream = socket.getOutputStream();
                out = new PrintWriter( new BufferedWriter(new OutputStreamWriter(outputStream)),true) ;

                inputStream = socket.getInputStream();
                stdIn = new BufferedReader(new InputStreamReader(inputStream));

                String pseudo = stdIn.readLine(); 
                out.println("Bienvenue, " + pseudo + "!");
    

                Joueur j = ajoutJoueur(out, pseudo);
                new PlayerHandler(socket, out, stdIn, grid, j).start();
            }while(joueurs.size()<nbJoueur);
            placeEntities();
            displayThread.start();
            nbJoueurVivant = nbJoueur;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initOption(){
        System.out.println();
        try{
            FileReader s = new FileReader("options.txt");
            BufferedReader br = new BufferedReader(s);
            String line;
            ArrayList<Integer> tabOption= new ArrayList<Integer>();
            while((line = br.readLine()) != null){
                String[] tempTab = line.split(": ");
                tabOption.add(Integer.parseInt(tempTab[1]));
            }
            WIDTH = 2*tabOption.get(0)+1;
            nbFamille = tabOption.get(1);
            nbEnnemi = tabOption.get(2);
            grid = new String[WIDTH][HEIGHT];
            nbPassage =  tabOption.get(3);
            s.close();
        }catch(IOException e){e.printStackTrace();}
    }

    private static void actuOption(){
        try{
            FileOutputStream outputStream = new FileOutputStream("options.txt", false);
            PrintWriter printStream = new PrintWriter(outputStream, true, StandardCharsets.UTF_8);

            printStream.print("nbEtage: " + ((int)(WIDTH/2)) + "\n");
            printStream.print("nbFamille: " + nbFamille + "\n");
            printStream.print("nbEnnemi: " + nbEnnemi + "\n");
            printStream.print("nbPassage: " + nbPassage + "\n");

            printStream.close();
        }catch(IOException e){e.printStackTrace();}
    }

    private static void initializeGrid() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (j == 0 || j == HEIGHT - 1 || i%2==0) {
                    grid[i][j] = WALL;
                } else {
                    grid[i][j] = EMPTY;
                }
            }
        }

        for(int i=0; i<(int)(WIDTH/2)-1; i++){
            Random random = new Random();
            int nbPassages = random.nextInt(nbPassage) + 1;
            for(int j=0; j<nbPassages; j++){
                grid[2*(i+1)][2*random.nextInt(((int)(HEIGHT/2)) - 1) + 1] = EMPTY;
            }
        }
    }

    private static int[] randomCoord(){
        Random random = new Random();
        int x = random.nextInt(((int)(WIDTH/2)+1)-1);
        int y = random.nextInt(HEIGHT - 2) + 1;
        
        int[] tab = {2*x+1, y};

        return(tab);
    }

    public static Joueur ajoutJoueur(PrintWriter out, String pseudo){
        Joueur joueur;
        switch(joueurs.size()){
            case 0:
                joueur = new Joueur(1, 1, "rouge", "D", out, pseudo);
                joueurs.add(joueur);
                grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                return joueur;
            case 1:
                joueur = new Joueur(1, HEIGHT-2, "bleu", "G", out, pseudo);
                joueurs.add(joueur);
                grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                return joueur;
            case 2:
                joueur = new Joueur(WIDTH-2, 1, "vert", "D", out, pseudo);
                joueurs.add(joueur);
                grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                return joueur;
            case 3:
                joueur = new Joueur(WIDTH-2, HEIGHT-2, "jaune", "G", out, pseudo);
                joueurs.add(joueur);
                grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                return joueur;
        }
        return new Joueur(-1, -1, "blanc", "H", out, "Null");
    }

    private static void placeEntities() {
        for(int i=0; i<nbFamille; i++){
            int[] tabFamille;
            do{
                tabFamille = randomCoord();
            }while(grid[tabFamille[0]][tabFamille[1]]!=EMPTY);
            Famille famille = new Famille(tabFamille[0], tabFamille[1]);
            grid[famille.getX()][famille.getY()] = famille.getChara();
            familles.add(famille);
        }

        for(int i=0; i<nbEnnemi; i++){
            int[] tabEnnemi;
            do{
                tabEnnemi = randomCoord();
            }while(estProcheJoueur(tabEnnemi) || grid[tabEnnemi[0]][tabEnnemi[1]]!=EMPTY);
            Ennemi ennemi = new Ennemi(tabEnnemi[0], tabEnnemi[1]);
            grid[ennemi.getX()][ennemi.getY()] = ennemi.getChara();
            ennemis.add(ennemi);
        }
    }

    public static boolean isValidMove(String move) {
        return move.equals("Z") || move.equals("Q") || move.equals("S") || move.equals("D") || move.equals(" ");
    }

    public static boolean estProcheJoueur(int[] coord){
        boolean bool = false;
        for(Joueur joueur:joueurs){
            if((Math.abs(joueur.getX()-coord[0])<1) && (Math.abs(joueur.getY()-coord[1])<10)){
                bool = true;
            }
        }
        return bool;
    }    

    public static void moveJoueur(String move, Joueur joueur) {
        synchronized(grid){
            switch (move) {
                case "Z":
                    if (grid[joueur.getX() - 1][joueur.getY()] != WALL) {
                        joueur.bougeHaut();
                        grid[joueur.getX()+1][joueur.getY()] = EMPTY;
                        grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                    }
                    break;
                case "Q":
                    if (grid[joueur.getX()][joueur.getY() - 1] != WALL) {
                        joueur.bougeGauche();
                        grid[joueur.getX()][joueur.getY()+1] = EMPTY;
                        grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                    }
                    break;
                case "S":
                    if (grid[joueur.getX() + 1][joueur.getY()] != WALL) {
                        joueur.bougeBas();
                        grid[joueur.getX()-1][joueur.getY()] = EMPTY;
                        grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                    }
                    break;
                case "D":
                    if (grid[joueur.getX()][joueur.getY() + 1] != WALL) {
                        joueur.bougeDroite();
                        grid[joueur.getX()][joueur.getY()-1] = EMPTY;
                        grid[joueur.getX()][joueur.getY()] = joueur.getChara();
                    }
                    break;
                case " ":
                    synchronized(projectiles){
                        Projectile proj;
                        switch(joueur.getDirection()){
                            case "H":
                                if (grid[joueur.getX() - 1][joueur.getY()] != WALL) {
                                    proj = new Projectile(joueur.getX()-1, joueur.getY(), "H", joueur);
                                    projectiles.add(proj);
                                    grid[proj.getX()][proj.getY()] = proj.getChara();
                                }
                                break;

                            case "B":
                                if (grid[joueur.getX() + 1][joueur.getY()] != WALL) {
                                    proj = new Projectile(joueur.getX()+1, joueur.getY(), "B", joueur);
                                    projectiles.add(proj);
                                    grid[proj.getX()][proj.getY()] = proj.getChara();
                                }
                                break;

                            case "G":
                                if (grid[joueur.getX()][joueur.getY()-1] != WALL) {
                                    proj = new Projectile(joueur.getX(), joueur.getY()-1, "G", joueur);
                                    projectiles.add(proj);
                                    grid[proj.getX()][proj.getY()] = proj.getChara();
                                }
                                break;

                            case "D":
                                if (grid[joueur.getX()][joueur.getY()+1] != WALL) {
                                    proj = new Projectile(joueur.getX(), joueur.getY()+1, "D", joueur);
                                    projectiles.add(proj);
                                    grid[proj.getX()][proj.getY()] = proj.getChara();
                                }
                                break;
                        }
                        displayThread.displayGrid();
                    }
            }
        }
    }

    public static void moveAdversaire(){
        synchronized(grid){
            synchronized(ennemis){
                for(Ennemi ennemi : ennemis){
                    if(!ennemi.estMort()){    
                        if(ennemi.getDirection() == "D" && (grid[ennemi.getX()][ennemi.getY()+1] != WALL && grid[ennemi.getX()][ennemi.getY()+1] != FAMILY && grid[ennemi.getX()][ennemi.getY()+1] != ENNEMY1 && grid[ennemi.getX()][ennemi.getY()+1] != ENNEMY2)){
                            grid[ennemi.getX()][ennemi.getY()] = EMPTY;
                            grid[ennemi.getX()][ennemi.getY() + 1] = ennemi.getChara();
                            ennemi.bougeDroite();
                        }else if(ennemi.getDirection() == "G" && (grid[ennemi.getX()][ennemi.getY()-1] != WALL && grid[ennemi.getX()][ennemi.getY()-1] != FAMILY && grid[ennemi.getX()][ennemi.getY()-1] != ENNEMY1 && grid[ennemi.getX()][ennemi.getY()-1] != ENNEMY2)){
                            grid[ennemi.getX()][ennemi.getY()] = EMPTY;
                            grid[ennemi.getX()][ennemi.getY() - 1] = ennemi.getChara();
                            ennemi.bougeGauche();
                        }else if(ennemi.getDirection() == "D" && (grid[ennemi.getX()][ennemi.getY()+1] == WALL || grid[ennemi.getX()][ennemi.getY()+1] == FAMILY || grid[ennemi.getX()][ennemi.getY()+1] == ENNEMY1 || grid[ennemi.getX()][ennemi.getY()+1] == ENNEMY2)){
                            ennemi.setDirection("G");
                        }else if(ennemi.getDirection() == "G" && (grid[ennemi.getX()][ennemi.getY()-1] == WALL || grid[ennemi.getX()][ennemi.getY()-1] == FAMILY || grid[ennemi.getX()][ennemi.getY()-1] == ENNEMY1 || grid[ennemi.getX()][ennemi.getY()-1] == ENNEMY2)){
                            ennemi.setDirection("D");
                        }
                    }

                    //FAIRE EN SORTE DE TUER JUSTE UN ROBOT ET D'ARRETER QUAND PLUS AUCUN ROBOT N'EST DISPO
                    if(ennemiToucheJoueur(ennemi)){
                        nbJoueurVivant--;
                        Joueur j = ennemiJoueurTouche(ennemi);
                        j.meurt();
                        j.getOut().println("Le robot est mort");
                        j.getOut().println("END");
                        if(nbJoueurVivant == 0){
                            displayThread.displayGrid();
                            displayThread.arret();
                            try{
                                FileOutputStream outputStream = new FileOutputStream("score.txt", true);
                                PrintWriter printStream = new PrintWriter(outputStream, true, StandardCharsets.UTF_8);
                                for(Joueur joueur:DrolGame.getJoueurs()){
                                    printStream.print(joueur.getPseudo() + ": " + joueur.getScore() + "\n");
                                }
                                printStream.close();
                            }catch(IOException e){e.printStackTrace();}
                        }
                    }
                }
            }
        }
    }

    public static void moveProjectile(){
        synchronized(grid){
            synchronized(projectiles){
                for(Projectile projectile : projectiles){
                    if(!projectile.estMort()){
                        switch(projectile.getDirection()){
                            case "H":
                                if (grid[projectile.getX() - 1][projectile.getY()] == EMPTY || grid[projectile.getX() - 1][projectile.getY()] == ENNEMY1 || grid[projectile.getX() - 1][projectile.getY()] == ENNEMY2) {
                                    if(!projectileToucheEnnemi(projectile)){
                                        projectile.bougeHaut();
                                        grid[projectile.getX()+1][projectile.getY()] = EMPTY;
                                        grid[projectile.getX()][projectile.getY()] = projectile.getChara();
                                    }else{
                                        tueEnnemi(projectile);
                                    }
                                }else{
                                    grid[projectile.getX()][projectile.getY()] = EMPTY;
                                    projectile.meurt();
                                }
                                break;
                            case "G":
                                if(grid[projectile.getX()][projectile.getY()-1] == EMPTY || grid[projectile.getX()][projectile.getY()-1] == ENNEMY1 || grid[projectile.getX()][projectile.getY()-1] == ENNEMY2) {
                                    if(!projectileToucheEnnemi(projectile)){
                                        projectile.bougeGauche();
                                        grid[projectile.getX()][projectile.getY()+1] = EMPTY;
                                        grid[projectile.getX()][projectile.getY()] = projectile.getChara();
                                    }else{
                                        tueEnnemi(projectile);
                                    }
                                }else{
                                    grid[projectile.getX()][projectile.getY()] = EMPTY;
                                    projectile.meurt();
                                }
                                break;
                            case "B":
                                if (grid[projectile.getX()+1][projectile.getY()] == EMPTY || grid[projectile.getX()+1][projectile.getY()] == ENNEMY1 || grid[projectile.getX()+1][projectile.getY()] == ENNEMY2) {
                                    if(!projectileToucheEnnemi(projectile)){
                                        projectile.bougeBas();
                                        grid[projectile.getX()-1][projectile.getY()] = EMPTY;
                                        grid[projectile.getX()][projectile.getY()] = projectile.getChara();
                                    }else{
                                        tueEnnemi(projectile);
                                    }
                                }else{
                                    grid[projectile.getX()][projectile.getY()] = EMPTY;
                                    projectile.meurt();
                                }
                                break;
                            case "D":
                                 if (grid[projectile.getX()][projectile.getY()+1] == EMPTY || grid[projectile.getX()][projectile.getY()+1] == ENNEMY1 || grid[projectile.getX()][projectile.getY()+1] == ENNEMY2) {
                                    if(!projectileToucheEnnemi(projectile)){
                                        projectile.bougeDroite();
                                        grid[projectile.getX()][projectile.getY()-1] = EMPTY;
                                        grid[projectile.getX()][projectile.getY()] = projectile.getChara();
                                    }else{
                                        tueEnnemi(projectile);
                                    }
                                }else{
                                    grid[projectile.getX()][projectile.getY()] = EMPTY;
                                    projectile.meurt();
                                }
                                break;
                        }
                    }
                }  
            }
        }
    }

    public static void tueEnnemi(Projectile projectile){
        Ennemi ennemi = ennemiToucheProjectile(projectile);
        Joueur j = projectile.getJoueur();
        j.ajScore(10);                                        
        ennemi.meurt();
        projectile.meurt();
        grid[projectile.getX()][projectile.getY()] = EMPTY;
        grid[ennemi.getX()][ennemi.getY()] = EMPTY;
        j.getOut().println("Vous avez éliminé un ennemi");
        j.getOut().println("+10 points");
    }

    public static boolean ennemiToucheJoueur(Ennemi ennemi){
        for(Joueur joueur:joueurs){
            if(joueur.getX() == ennemi.getX() && joueur.getY() == ennemi.getY() && !ennemi.estMort() && !joueur.estMort() ){
                return true;
            }
        }
        return false;
    }

    public static Joueur ennemiJoueurTouche(Ennemi ennemi){
        for(Joueur joueur:joueurs){
            if(joueur.getX() == ennemi.getX() && joueur.getY() == ennemi.getY() && !ennemi.estMort && !joueur.estMort()){
                return joueur;
            }
        }
        return new Joueur(-1, -1, "blanc", "H", out, "Null");
    }

    public static boolean projectileToucheEnnemi(Projectile projectile){
        for(Ennemi ennemi : ennemis){
            if(!ennemi.estMort() && !projectile.estMort() && (ennemi.getX() == projectile.getX() || ennemi.getX()-1 == projectile.getX() || ennemi.getX()+1 == projectile.getX()) && (ennemi.getY()+1 == projectile.getY() || ennemi.getY()-1 == projectile.getY() || ennemi.getY() == projectile.getY())){
                return true;
            }
        }
        return false;
    }

    public static Ennemi ennemiToucheProjectile(Projectile projectile){
        for(Ennemi ennemi : ennemis){
            if(!ennemi.estMort() && !projectile.estMort() && (ennemi.getX() == projectile.getX() || ennemi.getX()-1 == projectile.getX() || ennemi.getX()+1 == projectile.getX()) && (ennemi.getY()+1 == projectile.getY() || ennemi.getY()-1 == projectile.getY() || ennemi.getY() == projectile.getY())){
                return ennemi;
            }
        }
        return new Ennemi(-1, -1);
    }

    public static boolean toucheFamille(Joueur joueur){
        for(Famille famille : familles){
            if(!famille.estDelivre() && famille.getX() == joueur.getX() && famille.getY() == joueur.getY()){
                return true;
            }
        }
        return false;
    }

    public static Famille familleTouche(Joueur joueur){
        for(Famille famille : familles){
            if(!famille.estDelivre() && famille.getX() == joueur.getX() && famille.getY() == joueur.getY()){
                return famille;
            }
        }
        return new Famille(-1, -1);
    }

    private static void afficheScore(){
        System.out.println("\n");
        try{
            FileReader s = new FileReader("score.txt");
            BufferedReader br = new BufferedReader(s);
            String line;
            ArrayList<String> tabString = new ArrayList<String>();
            ArrayList<Integer> tabInt = new ArrayList<Integer>();
            while((line = br.readLine()) != null){
                String[] tempTab = line.split(": ");
                tabString.add(tempTab[0]);
                tabInt.add(Integer.parseInt(tempTab[1]));
            }
            boolean swapped;
            int temp;
            String tempS;
            for(int i=0; i<tabInt.size(); i++){
                swapped = false;
                for (int j = 0; j < tabInt.size() - i - 1; j++) {
                    if (tabInt.get(j) < tabInt.get(j+1)){
                        temp = tabInt.get(j);
                        tempS = tabString.get(j);
                        tabInt.set(j,tabInt.get(j+1));
                        tabString.set(j,tabString.get(j+1));
                        tabInt.set(j+1, temp);
                        tabString.set(j+1, tempS);
                        swapped = true;
                    }
                }
 
                if (swapped == false)
                    break;
            }
            int n;
            if(tabInt.size()>10){
                n = 10;
            }else{
                n = tabInt.size();
            }
            for(int i=0; i<n; i++){
                System.out.println(tabString.get(i) + " :" + tabInt.get(i));
            }
            s.close();
        }catch(IOException e){e.printStackTrace();}
    }

    public static int getWidth(){
        return WIDTH;
    }

    public static int getHeight(){
        return HEIGHT;
    }

    public static String[][] getGrid(){
        return grid;
    }

    public static ArrayList<Joueur> getJoueurs(){
        return joueurs;
    }

    public static int getNbFamille(){
        return nbFamille;
    }

    public static int getNbFamilleDelivre(){
        return nbFamilleDelivre;
    }

    public static void ajNbFamille(){
        nbFamilleDelivre++;
    }
}
