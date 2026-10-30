package clientPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import commonPackage.Operation;
import commonPackage.Resultat;

public class Client_3_2 {
    // "localhost" pour les tests locaux ; IP de la machine serveur en réseau (ex : 192.168.1.10)
    static final String IP_SERVEUR = "localhost";
    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté…");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(IP_SERVEUR), PORT), 5000);
            System.out.println("je suis un client connecté");

            // Même ordre qu'au serveur : flux de sortie d'abord, puis flux d'entrée
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner clavier = new Scanner(System.in);

            System.out.println("Serveur : " + ois.readObject());

            while (true) {
                System.out.print("Opérande 1 (ou 'quit' pour arrêter) : ");
                String saisie = clavier.nextLine().trim();
                if (saisie.equals("quit")) {
                    oos.writeObject("quit");
                    oos.flush();
                    break;
                }

                try {
                    double a = Double.parseDouble(saisie);

                    System.out.print("Opérateur (+, -, *, /) : ");
                    String op = clavier.nextLine().trim();
                    if (op.length() != 1) {
                        System.out.println("Opérateur invalide.");
                        continue;
                    }

                    System.out.print("Opérande 2 : ");
                    double b = Double.parseDouble(clavier.nextLine().trim());

                    oos.writeObject(new Operation(a, op.charAt(0), b));
                    oos.flush();

                    Resultat res = (Resultat) ois.readObject();
                    if (res.estErreur()) {
                        System.out.println("Serveur : erreur - " + res.getErreur());
                    } else {
                        System.out.println("Serveur : résultat = " + res.getValeur());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez saisir un nombre valide.");
                }
            }
            clavier.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
