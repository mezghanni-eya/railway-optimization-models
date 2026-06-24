import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

public class AffectationCouts {
    public static void main(String[] args) {
        
        Model model = new Model("Optimisation des Temps de Maintenance");

        // 1. Les Variables (Trains numérotés de 0 à 4)
        IntVar[] techniciens = model.intVarArray("Techs", 5, 0, 4);
        model.allDifferent(techniciens).post();

        // 2. LA MATRICE DES TEMPS (La base de données métier)
        // Chaque ligne = un technicien (Marc, Sophie, Ali, Julie, Paul)
        // Chaque colonne = le temps (en heures) qu'il met pour les trains 0, 1, 2, 3, 4
        int[][] matriceTemps = {
            {4, 2, 5, 3, 9}, // Marc   (Tech 0) - Très lent sur le train 4 (9 heures !)
            {2, 3, 3, 2, 2}, // Sophie (Tech 1) - Rapide et polyvalente
            {3, 4, 2, 4, 3}, // Ali    (Tech 2)
            {5, 1, 4, 3, 4}, // Julie  (Tech 3) - Experte absolue du train 1 (1 heure)
            {3, 3, 3, 3, 3}  // Paul   (Tech 4) - Vitesse moyenne partout
        };

        // 3. Variables pour stocker le temps individuel de chaque technicien
        IntVar[] tempsIndividuels = model.intVarArray("Temps Indiv", 5, 1, 10);

        // 4. LA CONTRAINTE DE LIAISON (La magie de CHOCO)
        for (int i = 0; i < 5; i++) {
            // "Prends le temps de la case [train choisi] dans la ligne du technicien i"
            model.element(tempsIndividuels[i], matriceTemps[i], techniciens[i]).post();
        }

        // 5. L'Objectif : Minimiser le temps total
        IntVar tempsTotal = model.intVar("Temps Total", 0, 50);
        model.sum(tempsIndividuels, "=", tempsTotal).post(); // On additionne tout
        
        // Attention : cette fois on MINIMISE !
        model.setObjective(Model.MINIMIZE, tempsTotal);

        // 6. Résolution (findOptimalSolution au lieu de findSolution)
        Solution solution = model.getSolver().findOptimalSolution(tempsTotal, false);

        if (solution != null) {
            System.out.println("--- PLANNING OPTIMAL ---");
            System.out.println("Marc   (T0) -> Train " + solution.getIntVal(techniciens[0]) + " (" + solution.getIntVal(tempsIndividuels[0]) + "h)");
            System.out.println("Sophie (T1) -> Train " + solution.getIntVal(techniciens[1]) + " (" + solution.getIntVal(tempsIndividuels[1]) + "h)");
            System.out.println("Ali    (T2) -> Train " + solution.getIntVal(techniciens[2]) + " (" + solution.getIntVal(tempsIndividuels[2]) + "h)");
            System.out.println("Julie  (T3) -> Train " + solution.getIntVal(techniciens[3]) + " (" + solution.getIntVal(tempsIndividuels[3]) + "h)");
            System.out.println("Paul   (T4) -> Train " + solution.getIntVal(techniciens[4]) + " (" + solution.getIntVal(tempsIndividuels[4]) + "h)");
            System.out.println("------------------------");
            System.out.println("TEMPS TOTAL MINIMUM : " + solution.getIntVal(tempsTotal) + " heures");
        }
    }
}

