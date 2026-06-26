import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

/*================================================================================
PROBLEM DEFINITION: THE MULTIMODAL FREIGHT TERMINAL SCHEDULING
================================================================================

The Scenario: The Multimodal Freight Terminal

As the Chief Engineer of a major SNCF freight terminal, I must manage the unloading of 3 cargo trains (Train A, Train B, Train C) tonight. The objective 
is to finish the entire operation as quickly as possible (Minimizing the Makespan), subject to strict physical, human, and operational constraints.

1. Equipment Constraints (The Cranes)
The terminal has a maximum capacity of 4 heavy gantry cranes operational simultaneously.
- Train A is heavy: it requires 2 cranes continuously during its unloading.
- Train B is massive: it requires 3 cranes continuously during its unloading.
- Train C is small: it only requires 1 crane.

2. Human Resource Constraints (The Assignment Matrix)
There are 3 stevedore teams (Team 1, Team 2, Team 3). A team can be assigned to exactly one train, and each train must be assigned to exactly one team. 

The unloading duration depends heavily on the team's familiarity with the cargo. The processing time matrix (in hours) is defined as follows:
- Team 1: [Train A: 5h, Train B: 8h, Train C: 2h]
- Team 2: [Train A: 7h, Train B: 4h, Train C: 3h]
- Team 3: [Train A: 6h, Train B: 6h, Train C: 4h]

3. Operational & Business Logic Constraints (Precedence) 
Train C contains ultra-fresh produce. However, due to limited platform and dock space, these goods must be stored exactly where the cargo from Train A is currently 
staged.

Strict Dependency: It is physically impossible to start unloading Train C until the unloading process of Train A is fully completed.*/



public class AffectationCouts {
    public static void main(String[] args) {
        
        Model model = new Model("Optimisation des Temps de Maintenance");

        // 1. Variables (Trains indexed from 0 to 4)
        IntVar[] technicians = model.intVarArray("Techs", 5, 0, 4);
        model.allDifferent(technicians).post();

        // 2. THE PROCESSING TIME MATRIX (The Business Database)
        // Each row = a technician (Marc, Sophie, Ali, Julie, Paul)
        // Each column = the time (in hours) required for trains 0, 1, 2, 3, 4
        int[][] matriceTemps = {
            {4, 2, 5, 3, 9}, // Marc   (Tech 0) - So slow on the train 4 (9 hours !)
            {2, 3, 3, 2, 2}, // Sophie (Tech 1) - Fast and polyvalent
            {3, 4, 2, 4, 3}, // Ali    (Tech 2)
            {5, 1, 4, 3, 4}, // Julie  (Tech 3) - Absolue expert of train 1 (1 heure)
            {3, 3, 3, 3, 3}  // Paul   (Tech 4) - Average speed everywhere
        };

        // 3. Variables to store the individual processing time of each technician
        IntVar[] tempsIndividuels = model.intVarArray("Temps Indiv", 5, 1, 10);

        // 4. THE COUPLING CONSTRAINT
        for (int i = 0; i < 5; i++) {
            // "Retrieve the duration from the cell [chosen train] in the row of technician i"
            model.element(tempsIndividuels[i], matriceTemps[i], techniciens[i]).post();
        }

        // 5. Objective : Minimizing total time
        IntVar tempsTotal = model.intVar("Temps Total", 0, 50);
        model.sum(tempsIndividuels, "=", totalTime).post(); // We add everything
        model.setObjective(Model.MINIMIZE, totalTime);

        // 6. Solution (should be optimal)
        Solution solution = model.getSolver().findOptimalSolution(tempsTotal, false);

        if (solution != null) {
            System.out.println("--- OPTIMAL PLANNING  ---");
            System.out.println("Marc   (T0) -> Train " + solution.getIntVal(techniciens[0]) + " (" + solution.getIntVal(tempsIndividuels[0]) + "h)");
            System.out.println("Sophie (T1) -> Train " + solution.getIntVal(techniciens[1]) + " (" + solution.getIntVal(tempsIndividuels[1]) + "h)");
            System.out.println("Ali    (T2) -> Train " + solution.getIntVal(techniciens[2]) + " (" + solution.getIntVal(tempsIndividuels[2]) + "h)");
            System.out.println("Julie  (T3) -> Train " + solution.getIntVal(techniciens[3]) + " (" + solution.getIntVal(tempsIndividuels[3]) + "h)");
            System.out.println("Paul   (T4) -> Train " + solution.getIntVal(techniciens[4]) + " (" + solution.getIntVal(tempsIndividuels[4]) + "h)");
            System.out.println("------------------------");
            System.out.println("MINIMAL TOTAL TIME : " + solution.getIntVal(totalTime) + " hours");
        }
    }
}

