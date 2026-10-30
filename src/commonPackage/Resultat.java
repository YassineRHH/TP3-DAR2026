package commonPackage;

import java.io.Serializable;

public class Resultat implements Serializable {

    private static final long serialVersionUID = 1L;

    private double valeur;
    private String erreur; // null si l'opération a réussi

    public Resultat(double valeur) {
        this.valeur = valeur;
        this.erreur = null;
    }

    public Resultat(String erreur) {
        this.erreur = erreur;
    }

    public double getValeur() {
        return valeur;
    }

    public String getErreur() {
        return erreur;
    }

    public boolean estErreur() {
        return erreur != null;
    }
}
