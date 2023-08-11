package parser.action;

public abstract class Action {
    public act action;
    public int number;

    public Action(act action, int number) {
        this.action = action;
        this.number = number;
    }

    public String toString() {
        return action.toString() + number;
    }
}