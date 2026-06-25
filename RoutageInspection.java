import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.Solution;

/* ================================================================================
PROBLEM DEFINITION: THE INSPECTION TRAIN ROUTING (TRAVELING SALESMAN PROBLEM)
================================================================================

The Scenario: The Inspection Train

I have to supervise a special maintenance train equipped with lasers to inspect the condition of the tracks. This train departs from Station A, must 
visit Stations B, C, D, and E exactly once to perform its measurements, and then return to its starting point (Station A) to download its data.
- The Nodes: 5 stations.
- The Edges: The railway tracks connecting them, each with a specific travel time in minutes.

Why the Human Brain (and Classical Math) Explode:
If you have 5 stations, there are 4! (factorial of 4) possible paths, which equals 24 routes. That is easy to manage.

However, if SNCF asks to plan the inspection of 15 stations in the Île-de-France region, the number of combinations skyrockets to over 87 billion. A human can no 
longer calculate this mentally, and a simple mathematical equation is no longer enough. It requires an intelligent search algorithm.

This is the classic Traveling Salesman Problem (TSP) applied to rail network. */


public class RoutageInspection {
    public static void main(String[] args) {
        
        Model model = new Model("Inspection des Voies");
        int nbGares = 5;

        // 1. Time matrix (Asymetric)
        // Line = Departure station, Column = Arrival station
        int[][] tempsTrajet = {
            // To: 0,  1,  2,  3,  4
            { 999, 15, 45, 30, 10 }, // From 0 (A)
            { 20, 999, 20, 50, 35 }, // From 1 (B) - Returning to A takes 20, not 15 !
            { 45, 20, 999, 25, 60 }, // From 2 (C)
            { 30, 50, 25, 999, 40 }, // From 3 (D)
            { 10, 35, 60, 40, 999 }  // From (E)
        };
        // Note : 999 on diagonals (0->0) means infinity. 
        // The train shouldn't stay in its place.

        // 2. THE SPATIAL DECISION VARIABLE
        // For each station (0 to 4), the solver must choose the ID of the next station.
        IntVar[] suivants = model.intVarArray("Gare Suivante", nbGares, 0, nbGares - 1);

        // 3. THE GLOBAL CONSTRAINT
        // Forces the "next" array to form a single, continuous loop visiting ALL stations.
        model.circuit(suivants).post();

        // 4. COUPLING WITH TRAVEL COSTS
        IntVar[] tempsParTroncon = model.intVarArray("Temps Troncons", nbGares, 0, 100);
        
        for (int i = 0; i < nbGares; i++) {
            // "For station i, identify the next station, and retrieve the corresponding travel time from the matrix."
            model.element(tempsParTroncon[i], tempsTrajet[i], suivants[i]).post();
        }

        // 5. THE OBJECTIVE: Minimize the total circuit duration
        IntVar tempsTotal = model.intVar("Temps Total", 0, 500);
        model.sum(tempsParTroncon, "=", tempsTotal).post();
        model.setObjective(Model.MINIMIZE, tempsTotal);

        // 6. SOLUTION
        Solution solution = model.getSolver().findOptimalSolution(tempsTotal, false);

        if (solution != null) {
            System.out.println("--- CIRCUIT D'INSPECTION OPTIMAL ---");
            int gareActuelle = 0; // The journey always starts at station 0
            
            // Short loop to display the itinerary in chronological order
            for(int i = 0; i < nbGares; i++) {
                int gareSuivante = solution.getIntVal(suivants[gareActuelle]);
                int temps = solution.getIntVal(tempsParTroncon[gareActuelle]);
                
                System.out.println("Gare " + gareActuelle + " -> Gare " + gareSuivante + " (Temps : " + temps + " min)");
                gareActuelle = gareSuivante; // We origress for the next display
            }
            
            System.out.println("------------------------------------");
            System.out.println(" TOTAL TIME: " + solution.getIntVal(tempsTotal) + " minutes");
        }
    }
}
