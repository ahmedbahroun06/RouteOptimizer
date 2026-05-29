package Routeoptimizer;

/**
 * Représente une route entre deux villes.
 * Contient tous les attributs nécessaires pour le calcul de coût.
 */
public class Route {
    public final String vers;
    public final String type;
    public final int    distance;     // en km
    public final double cout;         // en DT

    public Route(String vers, int distance, double cout, String type) {
        this.vers     = vers;
        this.distance = distance;
        this.cout     = cout;
        this.type     = type;
    }

    @Override
    public String toString() {
        return "Route{vers=" + vers + ", dist=" + distance + "km, cout=" + cout + "DT, type=" + type + "}";
    }
}
