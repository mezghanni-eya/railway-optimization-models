import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;
import org.chocosolver.solver.Solution;

public class OrdonnancementTechnicentre {
    public static void main(String[] args) {
        
        Model model = new Model("Ordonnancement TGV");
        int horizon = 100; // On se donne 100 heures maximum pour tout finir

        // 1. Déclaration des variables temporelles (Début, Durée, Fin)
        // TGV 1 (10h)
        IntVar deb1 = model.intVar("Début T1", 0, horizon);
        IntVar fin1 = model.intVar("Fin T1", 0, horizon);
        Task t1 = new Task(deb1, model.intVar(10), fin1);

        // TGV 2 (24h)
        IntVar deb2 = model.intVar("Début T2", 0, horizon);
        IntVar fin2 = model.intVar("Fin T2", 0, horizon);
        Task t2 = new Task(deb2, model.intVar(24), fin2);

        // TGV 3 (15h)
        IntVar deb3 = model.intVar("Début T3", 0, horizon);
        IntVar fin3 = model.intVar("Fin T3", 0, horizon);
        Task t3 = new Task(deb3, model.intVar(15), fin3);

        // TGV 4 (8h)
        IntVar deb4 = model.intVar("Début T4", 0, horizon);
        IntVar fin4 = model.intVar("Fin T4", 0, horizon);
        Task t4 = new Task(deb4, model.intVar(8), fin4);

        // 2. Préparation des tableaux pour la contrainte globale
        Task[] mesTaches = {t1, t2, t3, t4};
        
        // Les consommations respectives (40kW, 70kW, 30kW, 50kW) transformées en IntVar
        IntVar[] consommations = {
            model.intVar(40), 
            model.intVar(70), 
            model.intVar(30), 
            model.intVar(50)
        };
        
        // La capacité maximale de l'atelier (100 kW)
        IntVar capaciteMax = model.intVar(100);

        // 3. LA CONTRAINTE CUMULATIVE
        model.cumulative(mesTaches, consommations, capaciteMax).post();

        // 4. L'Objectif : Minimiser le "Makespan" (le temps total du projet)
        IntVar tempsTotal = model.intVar("Temps Total", 0, horizon);
        
        // Le temps total, c'est simplement la plus grande valeur parmi toutes les dates de fin
        model.max(tempsTotal, new IntVar[]{fin1, fin2, fin3, fin4}).post();
        
        model.setObjective(Model.MINIMIZE, tempsTotal);

        // 5. Résolution
        Solution solution = model.getSolver().findOptimalSolution(tempsTotal, false);

        if (solution != null) {
            System.out.println("--- PLANNING OPTIMAL DU TECHNICENTRE ---");
            System.out.println("TGV 1 : heure " + solution.getIntVal(deb1) + " à " + solution.getIntVal(fin1));
            System.out.println("TGV 2 : heure " + solution.getIntVal(deb2) + " à " + solution.getIntVal(fin2));
            System.out.println("TGV 3 : heure " + solution.getIntVal(deb3) + " à " + solution.getIntVal(fin3));
            System.out.println("TGV 4 : heure " + solution.getIntVal(deb4) + " à " + solution.getIntVal(fin4));
            System.out.println("----------------------------------------");
            System.out.println("TOUTES LES RAMES TERMINÉES EN : " + solution.getIntVal(tempsTotal) + " heures");
        }
    }
}