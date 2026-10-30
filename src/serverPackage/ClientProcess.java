package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess extends Thread {
    private Socket socket;
    private int numero;

    public ClientProcess(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            pw.println("Bienvenue, vous êtes le client n°" + numero);

            String ligne = br.readLine();
            while (ligne != null && !ligne.equals("quit")) {
                System.out.println("[client n°" + numero + "] a envoyé : " + ligne);
                pw.println("Echo : " + ligne);
                ligne = br.readLine();
            }
            System.out.println("Client n°" + numero + " déconnecté");
        } catch (IOException e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du client n°" + numero + " : "
                        + e.getMessage());
            }
        }
    }
}
