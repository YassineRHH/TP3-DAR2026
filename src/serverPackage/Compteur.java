package serverPackage;

/**
 * Compteur global partagé entre tous les threads ClientProcess.
 * Ressource critique : l'accès est synchronisé.
 */
public class Compteur {
    private static int valeur = 0;

    // synchronized static : verrou sur la classe, un seul thread à la fois
    public static synchronized int incrementer() {
        valeur++;
        return valeur;
    }

    public static synchronized int getValeur() {
        return valeur;
    }
}
