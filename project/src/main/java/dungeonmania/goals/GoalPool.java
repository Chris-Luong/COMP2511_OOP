package dungeonmania.goals;

import dungeonmania.goals.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import dungeonmania.Level;
//import java.lang.Exception;

public class GoalPool {

    private boolean achieved;

    private List<Goal> goalList = new ArrayList<Goal>();
    private List<String> goalNames = new ArrayList<String>();
    private String operator;
    //private List<Level> levels = new ArrayList<Level>();

    // if single goal
    public GoalPool(String g) {

        this.achieved = false;
        this.operator = "NONE";
        goalNames.add(g);
        Goal goal = new Goal(g);
        goalList.add(goal);

    }

    // creating goal with conjuction
    public GoalPool(String operator, List<String> goals) throws IllegalArgumentException {

        this.achieved = false;
        if (!operator.equals("AND") && !operator.equals("OR")) {
            System.out.println(operator);
            throw new IllegalArgumentException();
            //System.out.println("OPERATOR NOT VALID FOR GOAL!\n");
        }

        this.operator = operator;

        for (String g : goals) {

            Goal goal = new Goal(g);
            goalNames.add(g);
            goalList.add(goal);
        }
    }

    // notifies levels that goal pool has been achieved
    // public void notifyLevels() {
    //     for (Level level : levels) {
    //         level.update(this.achieved);
    //     }
    // }

    // called at every tick to check whether the goal pool has been achieved
    public void tick() {

        if (operator.equals("AND")) {
            this.achieved = true;
            for (Goal goal : goalList) {

                if (!goal.getCompleted()) {
                    this.achieved = false;
                    return;
                }

            }

        } else if (operator.equals("OR")) {

            for (Goal goal : goalList) {

                if (goal.getCompleted()) {
                    this.achieved = true;
                    return;
                }
            }

        } else if (operator.equals("NONE") && goalList.size() == 1) {
            if (goalList.get(0).getCompleted() == true) {
                this.achieved = true;
                return;
            }
        }
        
        // if (isAchieved()) {
        //     notifyLevels();
        // }
    }

    public void setGoalAchieved(String goalName) {
        
        System.out.println("Setting goal : " + goalName + ", as achieved!\n");
        if (!goalNames.contains(goalName)) {
            System.out.println("GOAL DOESN'T EXIST \n");
        }

        for (Goal goal : goalList) {

            if (goal.getGoalName().equals(goalName)) {
                //System.out.println("Found exit goal! \n");
                goal.setCompleted();
                goalNames.remove(goalName);
                //System.out.println("\t goal: " + goal + ", completed: " + goal.getCompleted() + "\n");
            }
        }

    }
    
    public boolean isAchieved() {
        return achieved;
    }

    public String getOperator() {
        return operator;
    }

    public List<String> getGoalNames() {
        return goalNames;
    }

    public List<Goal> getGoalList() {
        return goalList;
    }

    // public void addLevel(Level level) {
    //     levels.add(level);
    // }
}
