package Routeoptimizer;

import java.util.List;

/**
 * Contient le résultat retourné par l'algorithme UCS.
 * Si succes = false, aucun chemin n'a été trouvé avec les contraintes données.
 */
public class Resultat {
    public final boolean       succes;
    public final List<String>  chemin;
    public final double        coutTotal;
    public final List<Segment> details;

    public Resultat(boolean succes, List<String> chemin, double coutTotal, List<Segment> details) {
        this.succes    = succes;
        this.chemin    = chemin;
        this.coutTotal = coutTotal;
        this.details   = details;
    }

    // Calcule la distance totale du trajet
    public int distanceTotale() {
        return details.stream().mapToInt(s -> s.distance).sum();
    }

    // Calcule le coût monétaire total du trajet
    public double coutMonetaireTotal() {
        return details.stream().mapToDouble(s -> s.coutMonetaire).sum();
    }
}
