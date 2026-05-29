package Routeoptimizer;

import java.util.*;

/**
 * Implémentation de l'algorithme Uniform Cost Search (UCS).
 *
 * UCS explore les nœuds par ordre croissant de coût cumulé.
 * Il garantit l'optimalité si tous les coûts d'arcs sont >= 0.
 *
 * La fonction de coût est paramétrable via le critère choisi :
 *   - "distance" : minimise les kilomètres
 *   - "cout"     : minimise le coût monétaire (DT)
 *   - "mixte"    : combinaison pondérée des deux
 */
public class UCSAlgo {

    private final GrapheRoutier graphe;
    private final String        critere;

    // Pondérations pour le critère mixte
    private static final double POIDS_DISTANCE = 0.6;
    private static final double POIDS_COUT     = 0.4;

    public UCSAlgo(GrapheRoutier graphe, String critere) {
        this.graphe   = graphe;
        this.critere  = critere;
    }

    /**
     * Calcule le coût d'un arc selon le critère choisi.
     */
    private double calculerCout(Route r) {
        return switch (critere) {
            case "cout"  -> r.cout;
            // On divise la distance par 10 pour équilibrer l'échelle avec le coût en DT
            case "mixte" -> POIDS_DISTANCE * (r.distance / 10.0) + POIDS_COUT * r.cout;
            default      -> r.distance;   // "distance"
        };
    }

    /**
     * Lance la recherche UCS du chemin optimal entre dep et arr.
     *
     * @param dep         ville de départ
     * @param arr         ville d'arrivée
     * @param exclus      types de routes à éviter (ex: "autoroute")
     * @param interdites  villes à ne pas traverser
     * @param coutMax     budget maximum autorisé
     * @return            Resultat avec chemin, coût total et détail des étapes
     */
    public Resultat rechercher(String dep, String arr,
                               List<String> exclus,
                               List<String> interdites,
                               double coutMax) {

        // File de priorité triée par coût cumulé croissant
        // Chaque élément : [coutCumule, ville, chemin, segments]
        PriorityQueue<Object[]> frontiere = new PriorityQueue<>(
            Comparator.comparingDouble(a -> (double) a[0])
        );

        frontiere.offer(new Object[]{
            0.0,
            dep,
            new ArrayList<>(List.of(dep)),
            new ArrayList<Segment>()
        });

        // Ensemble des villes déjà explorées (coût optimal connu)
        Set<String> explores = new HashSet<>();

        while (!frontiere.isEmpty()) {
            Object[] etat = frontiere.poll();

            double        coutCum = (double)       etat[0];
            String        ville   = (String)        etat[1];
            @SuppressWarnings("unchecked")
            List<String>  chemin  = (List<String>)  etat[2];
            @SuppressWarnings("unchecked")
            List<Segment> segs    = (List<Segment>) etat[3];

            // Objectif atteint
            if (ville.equals(arr))
                return new Resultat(true, chemin, coutCum, segs);

            // Déjà exploré avec un coût optimal
            if (explores.contains(ville)) continue;
            explores.add(ville);

            // Expansion des voisins
            for (Route r : graphe.voisins(ville)) {

                // Contrainte : ville interdite
                if (interdites.contains(r.vers)) continue;

                // Contrainte : type de route exclu
                if (exclus.contains(r.type)) continue;

                double nouveauCout = coutCum + calculerCout(r);

                // Contrainte : budget maximum
                if (nouveauCout > coutMax) continue;

                if (!explores.contains(r.vers)) {
                    List<String>  nouveauChemin = new ArrayList<>(chemin);
                    nouveauChemin.add(r.vers);

                    List<Segment> nouveauxSegs = new ArrayList<>(segs);
                    nouveauxSegs.add(new Segment(ville, r.vers, r.distance, r.cout, r.type));

                    frontiere.offer(new Object[]{nouveauCout, r.vers, nouveauChemin, nouveauxSegs});
                }
            }
        }

        // Aucun chemin trouvé
        return new Resultat(false, new ArrayList<>(), Double.MAX_VALUE, new ArrayList<>());
    }
}
