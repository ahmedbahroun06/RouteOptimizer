package Routeoptimizer;

/**
 * Représente une étape dans un itinéraire (une route empruntée).
 * Utilisé pour afficher le détail du trajet calculé par UCS.
 */
public class Segment {
    public final String de;
    public final String vers;
    public final int    distance;
    public final double coutMonetaire;
    public final String type;

    public Segment(String de, String vers, int distance, double coutMonetaire, String type) {
        this.de            = de;
        this.vers          = vers;
        this.distance      = distance;
        this.coutMonetaire = coutMonetaire;
        this.type          = type;
    }
}
