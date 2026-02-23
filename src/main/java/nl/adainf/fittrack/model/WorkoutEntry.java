package nl.adainf.fittrack.model;



/*
 * WorkoutEntry.java - Model/Entity: 1 object = 1 rij uit de database
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

// Dit is een "plain" model: velden + constructor + getters/setters.
// Geen SQL hier, dat zit in de DAO.
public class WorkoutEntry {
    // Velden (kolommen uit de DB)

    private int id;
    private int workoutId;
    private int activityTypeId;
    private String activityName; // from JOIN for display
    private int minutes;
    private int calories;

    public WorkoutEntry(int workoutId, int activityTypeId, int minutes, int calories) {
        this(0, workoutId, activityTypeId, null, minutes, calories);
    }

    public WorkoutEntry(int id, int workoutId, int activityTypeId, String activityName, int minutes, int calories) {
        this.id = id;
        this.workoutId = workoutId;
        this.activityTypeId = activityTypeId;
        this.activityName = activityName;
        this.minutes = minutes;
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getCalories() {
        return calories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        String name = activityName != null ? activityName : ("type#" + activityTypeId);
        return name + " - " + minutes + " min, " + calories + " kcal";
    }
}
