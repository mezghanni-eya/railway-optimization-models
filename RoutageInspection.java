import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

public class RoutageInspection {
    public static void main(String[] args) {
        
        Model model = new Model("Inspection des Voies");
        int nbGares = 5;

        // 1. LA MATRICE DES TEMPS (Asymétrique pour plus de réalisme)
        // Ligne = Gare de départ, Colonne = Gare d'arrivée
        int[][] tempsTrajet = {
            // Vers: 0,  1,  2,  3,  4
            { 999, 15, 45, 30, 10 }, // Depuis 0 (Gare A)
            { 20, 999, 20, 50, 35 }, // Depuis 1 (Gare B) - Le retour A vaut 20, pas 15 !
            { 45, 20, 999, 25, 60 }, // Depuis 2 (Gare C)
            { 30, 50, 25, 999, 40 }, // Depuis 3 (Gare D)
            { 10, 35, 60, 40, 999 }  // Depuis 4 (Gare E)
        };
        // Note : On met 999 sur la diagonale (0->0) pour simuler l'infini. 
        // Le train ne doit pas rester sur place.

        // 2. LA VARIABLE DE DÉCISION SPATIALE
        // Pour chaque gare (0 à 4), le solveur doit choisir l'ID de la gare suivante.
        IntVar[] suivants = model.intVarArray("Gare Suivante", nbGares, 0, nbGares - 1);

        // 3. LA CONTRAINTE MAGIQUE (Adieu Dijkstra)
        // Force le tableau "suivants" à former une boucle unique passant par TOUTES les gares.
        model.circuit(suivants).post();

        // 4. LIAISON AVEC LES COÛTS
        IntVar[] tempsParTroncon = model.intVarArray("Temps Troncons", nbGares, 0, 100);
        
        for (int i = 0; i < nbGares; i++) {
            // "Pour la gare i, regarde quelle est la gare suivante, et va chercher le temps dans la matrice"
            model.element(tempsParTroncon[i], tempsTrajet[i], suivants[i]).post();
        }

        // 5. L'OBJECTIF : Minimiser le temps total du circuit
        IntVar tempsTotal = model.intVar("Temps Total", 0, 500);
        model.sum(tempsParTroncon, "=", tempsTotal).post();
        model.setObjective(Model.MINIMIZE, tempsTotal);

        // 6. RÉSOLUTION
        Solution solution = model.getSolver().findOptimalSolution(tempsTotal, false);

        if (solution != null) {
            System.out.println("--- CIRCUIT D'INSPECTION OPTIMAL ---");
            int gareActuelle = 0; // On part toujours de la gare 0
            
            // Petite boucle pour afficher le trajet dans l'ordre de la boucle
            for(int i = 0; i < nbGares; i++) {
                int gareSuivante = solution.getIntVal(suivants[gareActuelle]);
                int temps = solution.getIntVal(tempsParTroncon[gareActuelle]);
                
                System.out.println("Gare " + gareActuelle + " -> Gare " + gareSuivante + " (Temps : " + temps + " min)");
                gareActuelle = gareSuivante; // On avance pour l'affichage suivant
            }
            
            System.out.println("------------------------------------");
            System.out.println("TEMPS TOTAL : " + solution.getIntVal(tempsTotal) + " minutes");
        }
    }
}