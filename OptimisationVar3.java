import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

public class OptimisationVar3 {
    public static void main(String[] args) {
        
        Model model = new Model("Optimisation Fret Avancée");

        // 1. Les Variables (A, B et C)
        IntVar x = model.intVar("Wagons Type A", 0, 10);
        IntVar y = model.intVar("Wagons Type B", 0, 10);
        IntVar w = model.intVar("Wagons Type C", 0, 10); // Le petit nouveau

        // 2. Les Contraintes Physiques (Poids et Temps avec 3 éléments)
        model.scalar(new IntVar[]{x, y, w}, new int[]{4, 6, 5}, "<=", 30).post();
        model.scalar(new IntVar[]{x, y, w}, new int[]{1, 2, 1}, "<=", 8).post();

        // 3. LA CONTRAINTE LOGIQUE DE SÉCURITÉ
        // Si w > 0 ALORS x >= 2
        model.ifThen(
            model.arithm(w, ">", 0), 
            model.arithm(x, ">=", 2)
        );

        // 4. L'Objectif
        IntVar z = model.intVar("Benefice Total", 0, 100);
        model.scalar(new IntVar[]{x, y, w}, new int[]{3, 5, 4}, "=", z).post();
        model.setObjective(Model.MAXIMIZE, z);

        // 5. Résolution et Affichage
        Solution solution = model.getSolver().findOptimalSolution(z, true);

        if (solution != null) {
            System.out.println("Wagons A (Tampons) : " + solution.getIntVal(x));
            System.out.println("Wagons B (Frigo)   : " + solution.getIntVal(y));
            System.out.println("Wagons C (Citerne) : " + solution.getIntVal(w));
            System.out.println("Bénéfice max : " + solution.getIntVal(z) + " k€");
            
            // Petit calcul pour vérifier l'optimisation
            int poidsTotal = (solution.getIntVal(x)*4) + (solution.getIntVal(y)*6) + (solution.getIntVal(w)*5);
            System.out.println("Poids utilisé : " + poidsTotal + " / 30 tonnes");
        }
    }
}