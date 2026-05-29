package Routeoptimizer;

import java.util.*;

/**
 * Modélise le réseau routier sous forme de graphe non orienté pondéré.
 *
 * Structure : liste d'adjacence (HashMap ville -> liste de routes)
 * Chaque route est bidirectionnelle : ajouter(A, B) crée A->B et B->A.
 */
public class GrapheRoutier {

    // adjacence[ville] = liste des routes partant de cette ville
    private final Map<String, List<Route>> adjacence = new LinkedHashMap<>();

    /**
     * Ajoute une route bidirectionnelle entre deux villes.
     */
    public void ajouter(String de, String vers, int distance, double cout, String type) {
        adjacence.computeIfAbsent(de,   k -> new ArrayList<>()).add(new Route(vers, distance, cout, type));
        adjacence.computeIfAbsent(vers, k -> new ArrayList<>()).add(new Route(de,  distance, cout, type));
    }

    /**
     * Retourne les routes disponibles depuis une ville.
     */
    public List<Route> voisins(String ville) {
        return adjacence.getOrDefault(ville, Collections.emptyList());
    }

    /**
     * Retourne la liste triée de toutes les villes du réseau.
     */
    public String[] getVilles() {
        return adjacence.keySet().stream().sorted().toArray(String[]::new);
    }

    /**
     * Charge un réseau routier exemple avec 10 villes tunisiennes.
     */
    public void chargerReseauExemple() {
        ajouter("Tunis",    "Sousse",   140, 18.5, "autoroute");
        ajouter("Tunis",    "Bizerte",   65,  5.0, "nationale");
        ajouter("Tunis",    "Nabeul",    75,  6.5, "nationale");
        ajouter("Sousse",   "Sfax",     130, 16.0, "autoroute");
        ajouter("Sousse",   "Monastir",  22,  2.5, "nationale");
        ajouter("Sousse",   "Kairouan",  57,  5.5, "nationale");
        ajouter("Kairouan", "Sfax",     130, 10.0, "nationale");
        ajouter("Kairouan", "Gafsa",    190, 14.0, "nationale");
        ajouter("Sfax",     "Gabes",    130, 10.5, "nationale");
        ajouter("Sfax",     "Gafsa",    190, 15.0, "secondaire");
        ajouter("Gabes",    "Gafsa",    130, 10.0, "nationale");
        ajouter("Gafsa",    "Tozeur",    95,  7.0, "nationale");
        ajouter("Nabeul",   "Sousse",    95,  7.5, "nationale");
        ajouter("Monastir", "Sfax",     110, 14.0, "autoroute");
    }
}
