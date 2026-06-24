import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

/* The Problem: The Scheduling Nightmare

We have 5 technicians (Marc, Sophie, Ali, Julie, Paul) and 5 train sets (T1, T2, T3, T4, T5) that urgently require inspection tonight.

The Absolute Rule: One technician can only be assigned to one train, and each train must be assigned to exactly one technician.

The Qualification Constraints (Business Rules):
- Marc (Technician 1) does not have the electrical certification required to work on Train 5. 
- Julie (Technician 4) must absolutely be assigned to Train 2, as it matches her specific expertise.*/

public class AffectationMaintenance {
    public static void main(String[] args) {
        
        Model model = new Model("Planning de Maintenance");

        // 1. Variables : table of 5 technicians. 
        // Each must be assigned a value from 1 to 5 (the train ID).
        IntVar[] techniciens = model.intVarArray("Techs", 5, 1, 5);

        // 2. THE GLOBAL CONSTRAINT
        // We enforce that all values in the array must be distinct.
        // This is what prevents 2 technicians from being assigned to the same train.
        model.allDifferent(techniciens).post();

        // 3. Specific business constraints
        // Reminder: in Java, indexes start at 0. Therefore, Marc is technicians[0].

        // Marc (Tech 1) cannot work on train 5
        model.arithm(techniciens[0], "!=", 5).post();
        
        // Julie (Tech 4) SHOULD mwork on train 2
        model.arithm(techniciens[3], "=", 2).post();

        // 4. Solution (we look for ONLY ONE valid solution, no maximisation here)
        Solution solution = model.getSolver().findSolution();

        // 5. Display
        if (solution != null) {
            System.out.println("--- PLANNING DE NUIT ---");
            System.out.println("Marc   (Tech 1) -> Train " + solution.getIntVal(techniciens[0]));
            System.out.println("Sophie (Tech 2) -> Train " + solution.getIntVal(techniciens[1]));
            System.out.println("Ali    (Tech 3) -> Train " + solution.getIntVal(techniciens[2]));
            System.out.println("Julie  (Tech 4) -> Train " + solution.getIntVal(techniciens[3]));
            System.out.println("Paul   (Tech 5) -> Train " + solution.getIntVal(techniciens[4]));
        } else {
            System.out.println("Aucun planning possible avec ces contraintes !");
            System.out.println("No planning possible with these constraints !");
        }
    }
}
