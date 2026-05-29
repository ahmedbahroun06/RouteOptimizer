package Routeoptimizer;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Interface graphique principale — Projet 7 : Optimisation d'Itinéraire
 */
public class RouteOptimizer extends JFrame {

    // ── Nouvelle Palette de couleurs (Inspirée Flat Design) ──────────
    static final Color BG_MAIN       = new Color(243, 244, 246); // Gris très clair
    static final Color BG_PANEL      = Color.WHITE;              // Blanc pur pour les cartes
    static final Color PRIMARY       = new Color(59, 130, 246);  // Bleu moderne
    static final Color PRIMARY_DARK  = new Color(37, 99, 235);   // Bleu au survol
    static final Color SECONDARY     = new Color(16, 185, 129);  // Vert émeraude (succès)
    static final Color SECONDARY_DARK= new Color(5, 150, 105);   // Vert au survol
    static final Color TEXT_DARK     = new Color(31, 41, 55);    // Gris anthracite (lisible)
    static final Color BORDER_COLOR  = new Color(229, 231, 235); // Gris bordure doux
    static final Color ACCENT_RED    = new Color(239, 68, 68);   // Rouge d'erreur clair

    // ── Composants de l'interface ─────────────────────────────────
    private JComboBox<String> cmbDepart, cmbArrivee, cmbCritere;
    private JCheckBox         chkAutoroute;
    private JTextField        txtInterdites, txtBudget;
    private JTextArea         aireResultat;
    private DefaultTableModel modeleTableau;
    private JLabel            lblStatut;

    // ── Données métier ────────────────────────────────────────────
    private final GrapheRoutier graphe;

    // =============================================================
    public RouteOptimizer() {
        graphe = new GrapheRoutier();
        graphe.chargerReseauExemple();

        setTitle("Projet 7 — Optimisation d'Itinéraire Routier (UCS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 720); // Légèrement plus grand pour laisser respirer
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);

        construireInterface();
        setVisible(true);
    }

    // CONSTRUCTION DE L'INTERFACE

    private void construireInterface() {
        setLayout(new BorderLayout(10, 10)); // Espacement global
        
        // Un panel principal pour ajouter une marge autour de toute l'application
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(BG_MAIN);
        mainContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        mainContainer.add(creerPanneauParametres(), BorderLayout.WEST);
        mainContainer.add(creerPanneauResultats(),  BorderLayout.CENTER);

        add(creerEnTete(), BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);
        add(creerBarreStatut(), BorderLayout.SOUTH);
    }

    // ── En-tête ───────────────────────────────────────────────────
    private JPanel creerEnTete() {
        JPanel entete = new JPanel(new BorderLayout());
        entete.setBackground(Color.WHITE);
        // Bordure en bas pour séparer l'en-tête du reste
        entete.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(15, 25, 15, 25)
        ));

        JLabel lblTitre = new JLabel("Optimisation d'Itinéraire");
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitre.setForeground(PRIMARY_DARK);

        JLabel lblSousTitre = new JLabel("Algorithme : Uniform Cost Search (UCS) | FST — Fondements de l'IA");
        lblSousTitre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSousTitre.setForeground(Color.GRAY);

        JPanel textes = new JPanel(new GridLayout(2, 1, 0, 5));
        textes.setOpaque(false);
        textes.add(lblTitre);
        textes.add(lblSousTitre);
        entete.add(textes, BorderLayout.WEST);
        
        return entete;
    }

    // ── Panneau gauche : paramètres ───────────────────────────────
    private JPanel creerPanneauParametres() {
        JPanel panneau = new JPanel();
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
        panneau.setBackground(BG_PANEL);
        panneau.setPreferredSize(new Dimension(320, 0));
        
        // Bordure "carte" (fond blanc avec bordure grise douce)
        panneau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        String[] villes   = graphe.getVilles();
        String[] criteres = {"Distance (km)", "Coût monétaire (DT)", "Mixte (distance + coût)"};

        cmbDepart  = new JComboBox<>(villes);
        cmbArrivee = new JComboBox<>(villes);
        cmbArrivee.setSelectedIndex(villes.length - 1);
        cmbCritere = new JComboBox<>(criteres);

        txtInterdites = new JTextField();
        txtInterdites.setToolTipText("Ex : Sfax, Gabes");
        txtBudget     = new JTextField();
        txtBudget.setToolTipText("Ex : 50");

        chkAutoroute = new JCheckBox("Éviter les autoroutes");
        chkAutoroute.setBackground(BG_PANEL);
        chkAutoroute.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chkAutoroute.setFocusPainted(false);

        // Ajout des sections
        panneau.add(creerSection("📍 Trajet", new String[]{"Départ", "Arrivée"}, new JComponent[]{cmbDepart, cmbArrivee}));
        panneau.add(Box.createVerticalStrut(15));
        
        panneau.add(creerSection("🎯 Objectif", new String[]{"Critère"}, new JComponent[]{cmbCritere}));
        panneau.add(Box.createVerticalStrut(15));
        
        JPanel secContraintes = creerSection("🚧 Contraintes", new String[]{"Villes interdites", "Budget max (DT)"}, new JComponent[]{txtInterdites, txtBudget});
        JPanel ligneCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        ligneCheck.setBackground(BG_PANEL);
        ligneCheck.add(chkAutoroute);
        secContraintes.add(ligneCheck);
        panneau.add(secContraintes);

        panneau.add(Box.createVerticalStrut(25));

        // Boutons d'action
        JButton btnCalculer = creerBouton("Calculer l'itinéraire", PRIMARY, PRIMARY_DARK, Color.WHITE);
        JButton btnComparer = creerBouton("Comparer les critères", SECONDARY, SECONDARY_DARK, Color.WHITE);
        JButton btnReset    = creerBouton("Réinitialiser", new Color(243, 244, 246), new Color(229, 231, 235), TEXT_DARK);

        btnCalculer.addActionListener(e -> actionCalculer());
        btnComparer.addActionListener(e -> actionComparer());
        btnReset.addActionListener(e -> actionReset());

        for (JButton btn : new JButton[]{btnCalculer, btnComparer, btnReset}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 45)); // Boutons plus hauts
            panneau.add(btn);
            panneau.add(Box.createVerticalStrut(10));
        }

        panneau.add(Box.createVerticalGlue());
        return panneau;
    }

    // ── Panneau droit : résultats ─────────────────────────────────
    private JPanel creerPanneauResultats() {
        JPanel panneau = new JPanel(new BorderLayout(0, 15));
        panneau.setBackground(BG_MAIN);

        // --- Zone de texte résultat ---
        aireResultat = new JTextArea();
        aireResultat.setFont(new Font("Consolas", Font.PLAIN, 14)); // Police monospaced plus lisible
        aireResultat.setEditable(false);
        aireResultat.setBackground(BG_PANEL);
        aireResultat.setForeground(TEXT_DARK);
        aireResultat.setBorder(new EmptyBorder(15, 15, 15, 15));
        aireResultat.setText(
            "Bienvenue dans le système d'optimisation d'itinéraire.\n\n" +
            "Comment utiliser :\n" +
            "  1. Choisissez une ville de départ et d'arrivée.\n" +
            "  2. Sélectionnez un critère d'optimisation.\n" +
            "  3. Ajoutez des contraintes si nécessaire.\n" +
            "  4. Cliquez sur 'Calculer l'itinéraire'.\n\n" +
            "Réseau chargé : 10 villes tunisiennes, 14 routes."
        );

        JScrollPane scrollTexte = new JScrollPane(aireResultat);
        scrollTexte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Détails de l'itinéraire", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), TEXT_DARK),
            BorderFactory.createLineBorder(BORDER_COLOR)
        ));
        scrollTexte.getViewport().setBackground(BG_PANEL);

        // --- Tableau comparaison ---
        String[] colonnes = {"Critère", "Itinéraire", "Distance (km)", "Coût (DT)", "Nb étapes"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tableau = new JTable(modeleTableau);
        tableau.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableau.setRowHeight(30); // Lignes plus hautes
        tableau.setShowVerticalLines(false);
        tableau.setGridColor(BORDER_COLOR);
        tableau.setFillsViewportHeight(true);
        tableau.setSelectionBackground(PRIMARY);
        tableau.setSelectionForeground(Color.WHITE);

        // Style de l'en-tête du tableau
        JTableHeader enteteTab = tableau.getTableHeader();
        enteteTab.setFont(new Font("SansSerif", Font.BOLD, 13));
        enteteTab.setBackground(BG_PANEL);
        enteteTab.setForeground(TEXT_DARK);
        enteteTab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        enteteTab.setReorderingAllowed(false);

        JScrollPane scrollTableau = new JScrollPane(tableau);
        scrollTableau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Comparatif des critères", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), TEXT_DARK),
            BorderFactory.createLineBorder(BORDER_COLOR)
        ));
        scrollTableau.getViewport().setBackground(BG_PANEL);

        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTexte, scrollTableau);
        splitV.setResizeWeight(0.65);
        splitV.setDividerSize(8);
        splitV.setBorder(null);
        splitV.setBackground(BG_MAIN);

        panneau.add(splitV, BorderLayout.CENTER);
        return panneau;
    }

    // ── Barre de statut ───────────────────────────────────────────
    private JPanel creerBarreStatut() {
        JPanel barre = new JPanel(new BorderLayout());
        barre.setBackground(Color.WHITE);
        barre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(6, 15, 6, 15)
        ));

        lblStatut = new JLabel("Prêt.");
        lblStatut.setForeground(PRIMARY);
        lblStatut.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel lblInfo = new JLabel("Réseau : 10 villes | 14 routes | UCS");
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        barre.add(lblStatut, BorderLayout.WEST);
        barre.add(lblInfo,   BorderLayout.EAST);
        return barre;
    }

    // HELPERS DE CONSTRUCTION UI

    private JPanel creerSection(String titre, String[] labels, JComponent[] champs) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BG_PANEL);
        
        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitre.setForeground(TEXT_DARK);
        lblTitre.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel enteteSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        enteteSection.setBackground(BG_PANEL);
        enteteSection.add(lblTitre);
        section.add(enteteSection);

        for (int i = 0; i < labels.length; i++) {
            JPanel ligne = new JPanel(new BorderLayout(10, 0));
            ligne.setBackground(BG_PANEL);
            ligne.setBorder(new EmptyBorder(0, 0, 8, 0));

            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(Color.DARK_GRAY);
            lbl.setPreferredSize(new Dimension(110, 28));

            appliquerStyleChamp(champs[i]);

            ligne.add(lbl, BorderLayout.WEST);
            ligne.add(champs[i], BorderLayout.CENTER);
            section.add(ligne);
        }
        return section;
    }

    private void appliquerStyleChamp(JComponent c) {
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        if (c instanceof JTextField tf) {
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(5, 8, 5, 8)
            ));
        }
        if (c instanceof JComboBox<?> cb) {
            cb.setBackground(Color.WHITE);
            cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 30));
        }
    }

    private JButton creerBouton(String texte, Color bg, Color bgHover, Color fg) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Retire la bordure native 3D
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bordure vide pour donner du volume au bouton
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Effet de survol simple
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgHover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    // ACTIONS DES BOUTONS (Logique intacte)
   
    private void actionCalculer() {
        String dep = (String) cmbDepart.getSelectedItem();
        String arr = (String) cmbArrivee.getSelectedItem();

        if (dep.equals(arr)) {
            afficherErreur("La ville de départ et d'arrivée sont identiques.");
            return;
        }

        String       critere    = getCritereCode();
        List<String> exclus     = getTypesExclus();
        List<String> interdites = getVillesInterdites();
        double       budgetMax  = getBudgetMax();

        if (budgetMax < 0) {
            afficherErreur("Budget max invalide. Entrez un nombre positif.");
            return;
        }

        Resultat res = new UCSAlgo(graphe, critere).rechercher(dep, arr, exclus, interdites, budgetMax);
        afficherResultat(res, cmbCritere.getSelectedItem().toString());
        lblStatut.setText("Recherche terminée : " + dep + " -> " + arr);
    }

    private void actionComparer() {
        String dep = (String) cmbDepart.getSelectedItem();
        String arr = (String) cmbArrivee.getSelectedItem();

        if (dep.equals(arr)) {
            afficherErreur("La ville de départ et d'arrivée sont identiques.");
            return;
        }

        modeleTableau.setRowCount(0);
        StringBuilder sb = new StringBuilder();

        String[][] criteres = {
            {"distance", "Distance (km)"},
            {"cout",     "Coût (DT)"},
            {"mixte",    "Mixte"}
        };

        for (String[] crit : criteres) {
            Resultat res = new UCSAlgo(graphe, crit[0]).rechercher(
                dep, arr, new ArrayList<>(), new ArrayList<>(), Double.MAX_VALUE
            );

            sb.append(formaterResultat(res, crit[1])).append("\n");

            if (res.succes) {
                String trajet = String.join(" -> ", res.chemin);
                if (trajet.length() > 38) trajet = trajet.substring(0, 35) + "...";
                modeleTableau.addRow(new Object[]{
                    crit[1],
                    trajet,
                    res.distanceTotale(),
                    String.format("%.1f", res.coutMonetaireTotal()),
                    res.chemin.size() - 1
                });
            } else {
                modeleTableau.addRow(new Object[]{crit[1], "Aucun chemin", "-", "-", "-"});
            }
        }

        aireResultat.setText(sb.toString());
        aireResultat.setCaretPosition(0);
        aireResultat.setForeground(TEXT_DARK);
        lblStatut.setText("Comparaison effectuée : " + dep + " -> " + arr);
    }

    private void actionReset() {
        cmbDepart.setSelectedIndex(0);
        cmbArrivee.setSelectedIndex(cmbArrivee.getItemCount() - 1);
        cmbCritere.setSelectedIndex(0);
        chkAutoroute.setSelected(false);
        txtInterdites.setText("");
        txtBudget.setText("");
        modeleTableau.setRowCount(0);
        aireResultat.setForeground(TEXT_DARK);
        aireResultat.setText(
            "Interface réinitialisée.\n\n" +
            "Choisissez un départ, une arrivée, puis cliquez sur 'Calculer'."
        );
        lblStatut.setText("Réinitialisé.");
    }

    private void afficherResultat(Resultat res, String critereLabel) {
        aireResultat.setText(formaterResultat(res, critereLabel));
        aireResultat.setCaretPosition(0);
        aireResultat.setForeground(res.succes ? TEXT_DARK : ACCENT_RED);
    }

    private String formaterResultat(Resultat res, String critereLabel) {
        if (!res.succes)
            return "Aucun itinéraire trouvé avec les contraintes données.\n" +
                   "Essayez de retirer certaines contraintes.";

        String sep = "------------------------------------------------------------\n";
        StringBuilder sb = new StringBuilder();

        sb.append("============================================================\n");
        sb.append(" ITINÉRAIRE OPTIMAL   |   Critère : ").append(critereLabel).append("\n");
        sb.append("============================================================\n");
        sb.append(" Trajet  : ").append(String.join(" -> ", res.chemin)).append("\n");
        sb.append(" Score   : ").append(String.format("%.2f", res.coutTotal)).append("\n\n");

        sb.append(String.format(" %-14s %-14s %-11s %-11s %-12s\n", "De", "Vers", "Dist (km)", "Coût (DT)", "Type"));
        sb.append(sep);

        for (Segment seg : res.details) {
            sb.append(String.format(" %-14s %-14s %-11d %-11.1f %-12s\n",
                seg.de, seg.vers, seg.distance, seg.coutMonetaire, seg.type));
        }

        sb.append(sep);
        sb.append(String.format(" %-14s %-14s %-11d %-11.1f\n", "TOTAL", "", res.distanceTotale(), res.coutMonetaireTotal()));

        return sb.toString();
    }

    private void afficherErreur(String msg) {
        aireResultat.setForeground(ACCENT_RED);
        aireResultat.setText("Erreur : " + msg);
        lblStatut.setText("Erreur.");
    }

    // LECTURE DES PARAMÈTRES SAISIS
    private String getCritereCode() {
        return switch (cmbCritere.getSelectedIndex()) {
            case 1  -> "cout";
            case 2  -> "mixte";
            default -> "distance";
        };
    }

    private List<String> getTypesExclus() {
        List<String> exclus = new ArrayList<>();
        if (chkAutoroute.isSelected()) exclus.add("autoroute");
        return exclus;
    }

    private List<String> getVillesInterdites() {
        List<String> interdites = new ArrayList<>();
        String saisie = txtInterdites.getText().trim();
        if (!saisie.isEmpty())
            for (String v : saisie.split(","))
                interdites.add(v.trim());
        return interdites;
    }

    private double getBudgetMax() {
        String saisie = txtBudget.getText().trim();
        if (saisie.isEmpty()) return Double.MAX_VALUE;
        try {
            double val = Double.parseDouble(saisie);
            return val >= 0 ? val : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // POINT D'ENTRÉE
    public static void main(String[] args) {
        try {
            // Appliquer l'aspect du système d'exploitation de base
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(RouteOptimizer::new);
    }
}