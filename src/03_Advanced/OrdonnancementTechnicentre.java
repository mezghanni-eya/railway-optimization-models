import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;
import org.chocosolver.solver.Solution;

/*
================================================================================
PROBLEM DEFINITION: THE TRAIN MAINTENANCE DEPOT SCHEDULING (MAKESPAN OPTIMIZATION)
================================================================================

The Scenario: The Maintenance Depot

I will schedule heavy maintenance for 4 TGV train sets in a workshop over the course of next week. I know exactly how long the overhaul of each train set takes 
(e.g., TGV 1 takes 10 hours, TGV 2 takes 24 hours, etc.).

The bottleneck is the infrastructure. The workshop has a limited power capacity (let's say a maximum of 100 kW available at any given time t).
- Maintaining TGV 1 continuously consumes 40 kW during its 10-hour window.
- Maintaining TGV 2 consumes 70 kW during its 24-hour window, and so on.

The Trap:
If we start TGV 1 and TGV 2 at the same time, the combined power draw reaches 110 kW, and the workshop's circuit breaker will trip. Therefore, we must shift 
the interventions over time to smooth out the power consumption, while finishing as early as possible.

Decision Variables & Logic:
For each TGV, the actual unknown variable that the solver must determine is its "Start Time".

The algorithm will virtually scan every single second of the schedule. If it detects that at hour H, the sum of the power draws from the TGVs simultaneously 
present in the workshop exceeds 100 kW, it rejects that combination and shifts the start time of one of the TGVs.

Objective Function:
To find the optimum, we define a "Total Time" variable (the Makespan), which is simply equal to the completion time (End Time) of the very last TGV to leave the 
workshop. We then instruct the solver to minimize this Makespan.*/



public class OrdonnancementTechnicentre {
    public static void main(String[] args) {
        
        Model model = new Model("Ordonnancement TGV");
        int horizon = 100; // We have 100 hours maximum to finish everything

        // 1. DECISION VARIABLES: START TIMES
        // For each TGV, the solver must determine its scheduled start time.
        // TGV 1 (10h)
        IntVar deb1 = model.intVar("Start T1", 0, horizon);
        IntVar fin1 = model.intVar("End T1", 0, horizon);
        Task t1 = new Task(deb1, model.intVar(10), fin1);

        // TGV 2 (24h)
        IntVar deb2 = model.intVar("Start T2", 0, horizon);
        IntVar fin2 = model.intVar("End T2", 0, horizon);
        Task t2 = new Task(deb2, model.intVar(24), fin2);

        // TGV 3 (15h)
        IntVar deb3 = model.intVar("Start T3", 0, horizon);
        IntVar fin3 = model.intVar("End T3", 0, horizon);
        Task t3 = new Task(deb3, model.intVar(15), fin3);

        // TGV 4 (8h)
        IntVar deb4 = model.intVar("Start T4", 0, horizon);
        IntVar fin4 = model.intVar("Finish T4", 0, horizon);
        Task t4 = new Task(deb4, model.intVar(8), fin4);

        // 2. Preparing the schedule
        Task[] mesTaches = {t1, t2, t3, t4};
        
        // Consumptions of every train (40kW, 70kW, 30kW, 50kW)
        IntVar[] consommations = {
            model.intVar(40), 
            model.intVar(70), 
            model.intVar(30), 
            model.intVar(50)
        };
        
        // Maximal capacity of the workshop (100 kW)
        IntVar capaciteMax = model.intVar(100);

        // Ensure that at any given time t, the cumulative power consumption 
        // of all active maintenance tasks does not exceed the 100 kW threshold.
        model.cumulative(mesTaches, consommations, capaciteMax).post();

        // 3. OBJECTIVE VARIABLE: MAKESPAN (Total Time)
        IntVar totalTime = model.intVar("Temps Total", 0, horizon);
        
        // The completion time of the very last TGV to leave the workshop.
        model.max(totalTime, new IntVar[]{fin1, fin2, fin3, fin4}).post();
        
        model.setObjective(Model.MINIMIZE, totalTime);

        // 5. Solution
        Solution solution = model.getSolver().findOptimalSolution(totalTime, false);

        if (solution != null) {
            System.out.println("--- OPTIMAL WORKSHOP SCHEDULE---");
            System.out.println("TGV 1 : from " + solution.getIntVal(deb1) + " to " + solution.getIntVal(fin1));
            System.out.println("TGV 2 : from " + solution.getIntVal(deb2) + " to " + solution.getIntVal(fin2));
            System.out.println("TGV 3 : from " + solution.getIntVal(deb3) + " to " + solution.getIntVal(fin3));
            System.out.println("TGV 4 : from " + solution.getIntVal(deb4) + " to " + solution.getIntVal(fin4));
            System.out.println("----------------------------------------");
            System.out.println("ALL TRAIN SETS COMPLETED IN : " + solution.getIntVal(totalTime) + " hours");
        }
    }
}
