package nl.adainf.fittrack.model;

/*
 * WorkoutEntry.java
 *
 * Dit is een modelklasse voor één trainingregel.
 *
 * Een Workout is de dag/training zelf.
 * Een WorkoutEntry is de activiteit die daarbij hoort.
 *
 * Bijvoorbeeld:
 * - workout op maandag
 * - activiteit: hardlopen
 * - minuten: 30
 * - calorieën: 250
 *
 * Hier staat geen SQL-code.
 * SQL staat in WorkoutEntryDao.
 */

public class WorkoutEntry {

    // id is het unieke nummer van deze trainingregel in de database.
    private int id;

    // workoutId geeft aan bij welke workout deze regel hoort.
    private int workoutId;

    // activityTypeId geeft aan welk soort activiteit gekozen is.
    private int activityTypeId;

    // activityName gebruik ik om de naam van de activiteit te tonen in het overzicht.
    // Deze naam komt meestal uit een JOIN in de DAO.
    private String activityName;

    // Aantal minuten dat de gebruiker actief was.
    private int minutes;

    // Aantal calorieën dat de gebruiker heeft verbrand.
    private int calories;

    // Constructor voor een nieuwe trainingregel.
    // Het id is dan nog 0, omdat de database het echte id later maakt.
    public WorkoutEntry(int workoutId, int activityTypeId, int minutes, int calories) {
        this(0, workoutId, activityTypeId, null, minutes, calories);
    }

    // Constructor voor een trainingregel die al alle gegevens heeft.
    // Deze gebruik ik bijvoorbeeld als data uit de database komt.
    public WorkoutEntry(int id, int workoutId, int activityTypeId, String activityName, int minutes, int calories) {
        this.id = id;
        this.workoutId = workoutId;
        this.activityTypeId = activityTypeId;
        this.activityName = activityName;
        this.minutes = minutes;
        this.calories = calories;
    }

    // Getter: hiermee haal ik het id op.
    public int getId() {
        return id;
    }

    // Getter: hiermee haal ik het workoutId op.
    public int getWorkoutId() {
        return workoutId;
    }

    // Getter: hiermee haal ik het activityTypeId op.
    public int getActivityTypeId() {
        return activityTypeId;
    }

    // Getter: hiermee haal ik de naam van de activiteit op.
    public String getActivityName() {
        return activityName;
    }

    // Getter: hiermee haal ik het aantal minuten op.
    public int getMinutes() {
        return minutes;
    }

    // Getter: hiermee haal ik het aantal calorieën op.
    public int getCalories() {
        return calories;
    }

    // Setter: hiermee kan ik het id later aanpassen.
    public void setId(int id) {
        this.id = id;
    }

    // Setter: hiermee kan ik het workoutId aanpassen.
    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    // Setter: hiermee kan ik het activityTypeId aanpassen.
    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    // Setter: hiermee kan ik de activiteitsnaam aanpassen.
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    // Setter: hiermee kan ik het aantal minuten aanpassen.
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    // Setter: hiermee kan ik het aantal calorieën aanpassen.
    public void setCalories(int calories) {
        this.calories = calories;
    }

    // Dit zorgt ervoor dat een trainingregel netjes zichtbaar is in de ListView.
    @Override
    public String toString() {
        String name = activityName != null ? activityName : ("type#" + activityTypeId);
        return name + " - " + minutes + " min, " + calories + " kcal";
    }
}