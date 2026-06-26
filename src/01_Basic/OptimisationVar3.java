import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

/* The PROBLEM : THE ADVANCED FREIGHT TRAIN COMPOSITION OPTIMIZATION
This problem models a freight train assembly challenge where the objective is to 
maximize the total profit of a cargo journey under physical, operational, and 
safety constraints.

We have 3 Wagon types. Each wagon type brings a specific profit but consumes limited layout resources:

- Type A (Standard):     Profit = 3 k€  |  Weight = 4 tons  |  Inspection = 1 hour
- Type B (Refrigerated): Profit = 5 k€  |  Weight = 6 tons  |  Inspection = 2 hours
- Type C (Tanker):       Profit = 4 k€  |  Weight = 5 tons  |  Inspection = 1 hour

Safety & Business Logic Constraint:
For braking safety reasons, IF at least one Tanker wagon (Type C) is attached, 
THEN you are required to attach at least 2 Standard wagons (Type A) at the front 
to act as a buffer.
*/

public class OptimisationVar3 {
    public static void main(String[] args) {
        
        Model model = new Model("Optimisation Fret Avancée");

        // 1. Variables (A, B et C)
        IntVar x = model.intVar("Wagons Type A", 0, 10);
        IntVar y = model.intVar("Wagons Type B", 0, 10);
        IntVar w = model.intVar("Wagons Type C", 0, 10); // Le petit nouveau

        // 2. Physical constraints (weight and time)
        model.scalar(new IntVar[]{x, y, w}, new int[]{4, 6, 5}, "<=", 30).post();
        model.scalar(new IntVar[]{x, y, w}, new int[]{1, 2, 1}, "<=", 8).post();

        // 3. Security logic constraint
        // If w > 0 Then x >= 2
        model.ifThen(
            model.arithm(w, ">", 0), 
            model.arithm(x, ">=", 2)
        );

        // 4. Objectif
        IntVar z = model.intVar("Total profit", 0, 100);
        model.scalar(new IntVar[]{x, y, w}, new int[]{3, 5, 4}, "=", z).post();
        model.setObjective(Model.MAXIMIZE, z);

        // 5. Solution and display
        Solution solution = model.getSolver().findOptimalSolution(z, true);

        if (solution != null) {
            System.out.println("Wagons A (Stabndard) : " + solution.getIntVal(x));
            System.out.println("Wagons B (Refrigerated)   : " + solution.getIntVal(y));
            System.out.println("Wagons C (Tanker) : " + solution.getIntVal(w));
            System.out.println("maximal profit : " + solution.getIntVal(z) + " k€");
            
            // Little test to verify weight constraint satisfaction
            int poidsTotal = (solution.getIntVal(x)*4) + (solution.getIntVal(y)*6) + (solution.getIntVal(w)*5);
            System.out.println("Weight used : " + poidsTotal + " / 30 tonnes");
        }
    }
}
