import java.io.*;
import java.net.*;


public class PlayerInput {
    private static boolean arret = false;
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if(args.length!=2){
            System.err.println("Utilisation: java PlayerInput <adresse du serveur> <pseudo>");
            System.exit(1);
        }

        int portNumber = 8080;
        String hostName = args[0];
        String pseudo = args[1];


        try (
            Socket clientSocket = new Socket(hostName, portNumber);

            OutputStream outputStream = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter out = new PrintWriter(outputStream, true);

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            out.println(pseudo);
            // Création d'un thread dédié pour la réception des messages du serveur
            Thread receiverThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        if(serverMessage.equals("END")){
                            arret = true;
                            break;
                        }else{
                            System.out.println(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiverThread.start();

            // Envoi des messages saisis par l'utilisateur au serveur
            
            String userInput;
            while (!arret) {
                if((userInput = stdIn.readLine()) != null){
                    out.println(userInput);
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Impossible d'établir une connexion");
            System.exit(1);
        }
    }
}
