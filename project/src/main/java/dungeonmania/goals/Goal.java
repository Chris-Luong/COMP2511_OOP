package dungeonmania.goals;

public class Goal {
    
    private String goalName;
    private boolean completed;

    public Goal(String goal) {
        
        this.goalName = goal;
        this.completed = false;
    }

    public String getGoalName() {
        return goalName;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }
}
