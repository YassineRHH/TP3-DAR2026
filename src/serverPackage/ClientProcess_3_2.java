package serverPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commonPackage.Operation;
import commonPackage.Resultat;

public class ClientProcess_3_2 extends Thread {
    private Socket socket;
    private int numero;

    public ClientProcess_3_2(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            // Important : créer l'ObjectOutputStream en premier et faire flush()
            // pour éviter un blocage (interblocage) avec l'ObjectInputStream du client
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();

            while (true) {
                Object recu = ois.readObject();

                // Le client envoie la chaîne "quit" pour se déconnecter
                if (recu instanceof String && recu.equals("quit")) {
                    break;
                }

                if (recu instanceof Operation) {
                    Operation op = (Operation) recu;
                    Resultat res = calculer(op);

                    if (!res.estErreur()) {
                        int total = Compteur.incrementer();
                        System.out.println("[client n°" + numero + "] "
                                + op.getOperande1() + " " + op.getOperateur() + " "
                                + op.getOperande2() + " = " + res.getValeur()
                                + "  | Compteur global = " + total);
                    } else {
                        System.out.println("[client n°" + numero + "] erreur : " + res.getErreur());
                    }

                    oos.writeObject(res);
                    oos.flush();
                } else {
                    oos.writeObject(new Resultat("Objet non reconnu"));
                    oos.flush();
                }
            }
            System.out.println("Client n°" + numero + " déconnecté");
        } catch (EOFException e) {
            System.out.println("Client n°" + numero + " a fermé la connexion");
        } catch (IOException | ClassNotFoundException e) {
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

    private Resultat calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        switch (op.getOperateur()) {
            case '+':
                return new Resultat(a + b);
            case '-':
                return new Resultat(a - b);
            case '*':
                return new Resultat(a * b);
            case '/':
                if (b == 0) {
                    return new Resultat("Division par zéro impossible");
                }
                return new Resultat(a / b);
            default:
                return new Resultat("Opérateur inconnu : " + op.getOperateur());
        }
    }
}
