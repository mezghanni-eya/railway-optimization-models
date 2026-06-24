import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

public class AffectationMaintenance {
    public static void main(String[] args) {
        
        Model model = new Model("Planning de Maintenance");

        // 1. Les Variables : Un tableau de 5 techniciens. 
        // Chacun doit se voir assigner une valeur de 1 à 5 (l'ID du train)
        IntVar[] techniciens = model.intVarArray("Techs", 5, 1, 5);

        // 2. LA CONTRAINTE GLOBALE
        // Magie : on ordonne que toutes les valeurs du tableau soient différentes.
        // C'est ce qui empêche 2 techniciens d'être sur le même train.
        model.allDifferent(techniciens).post();

        // 3. Les contraintes métiers spécifiques
        // Rappel : en Java, l'index commence à 0. Donc Marc est techniciens[0].
        
        // Marc (Tech 1) ne peut pas faire le train 5
        model.arithm(techniciens[0], "!=", 5).post();
        
        // Julie (Tech 4) DOIT faire le train 2
        model.arithm(techniciens[3], "=", 2).post();

        // 4. Résolution (On cherche juste UNE solution valide, pas de maximisation ici)
        Solution solution = model.getSolver().findSolution();

        // 5. Affichage
        if (solution != null) {
            System.out.println("--- PLANNING DE NUIT ---");
            System.out.println("Marc   (Tech 1) -> Train " + solution.getIntVal(techniciens[0]));
            System.out.println("Sophie (Tech 2) -> Train " + solution.getIntVal(techniciens[1]));
            System.out.println("Ali    (Tech 3) -> Train " + solution.getIntVal(techniciens[2]));
            System.out.println("Julie  (Tech 4) -> Train " + solution.getIntVal(techniciens[3]));
            System.out.println("Paul   (Tech 5) -> Train " + solution.getIntVal(techniciens[4]));
        } else {
            System.out.println("Aucun planning possible avec ces contraintes !");
        }
    }
}