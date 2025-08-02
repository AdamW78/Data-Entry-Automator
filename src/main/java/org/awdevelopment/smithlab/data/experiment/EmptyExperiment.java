package org.awdevelopment.smithlab.data.experiment;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;

import java.util.Set;

public class EmptyExperiment extends AbstractExperiment {

    private final byte numReplicates;
    private final byte numConditions;
    private final byte numStrains;
    private final boolean usingNumConditions;
    private final boolean usingNumStrains;
    private final byte[] days;
    private final byte numDays;
    private final boolean usingNumDays;

    public EmptyExperiment(Set<Condition> conditions, Set<Strain> strains, byte numReplicates, byte numConditions,
                           byte numStrains, boolean usingNumConditions, boolean usingNumStrains, byte[] days,
                           byte numDays, boolean usingNumDays) {
        super(conditions, strains);
        this.numReplicates = numReplicates;
        this.numConditions = numConditions;
        this.numStrains = numStrains;
        this.usingNumConditions = usingNumConditions;
        this.usingNumStrains = usingNumStrains;
        this.days = days;
        this.numDays = numDays;
        this.usingNumDays = usingNumDays;
    }


    public byte getNumReplicates() { return numReplicates; }
    public boolean hasNoDays() { return days.length == 0 && !usingNumDays; }
    public byte[] getDays() { return days; }
    public byte getNumDays() { return numDays; }
    public boolean usingNumDays() { return usingNumDays; }
    public boolean usingNumConditions() { return usingNumConditions; }
    public boolean usingNumStrains() { return usingNumStrains; }
    public byte getNumConditions() { return numConditions; }
    public byte getNumStrains() { return numStrains; }

    public String readableExperiment() {
        StringBuilder sb = new StringBuilder();
        sb.append("Experiment with ");
        if (usingNumStrains) {
            sb.append(numStrains).append(" strains, ");
        } else {
            StringBuilder strainsList = new StringBuilder();
            for (Strain strain : strains()) { strainsList.append(strain.getName()).append(", "); }
            strainsList.setLength(Math.max(strainsList.length() - 2, 0)); // Remove trailing comma and space, if any
            sb.append(strains().size()).append(" strains (").append(strainsList).append("), ");
        }
        if (usingNumConditions) {
            sb.append(numConditions).append(" conditions, ");
        } else {
            StringBuilder conditionsList = new StringBuilder();
            for (Condition condition : conditions()) { conditionsList.append(condition.getName()).append(", "); }
            conditionsList.setLength(Math.max(conditionsList.length() - 2, 0)); // Remove trailing comma and space, if any
            sb.append(conditions().size()).append(" conditions (").append(conditionsList).append("), ");
        }
        sb.append(numReplicates).append(" replicates");
        if (usingNumDays) {
            sb.append(", ").append(numDays).append(" days.");
        } else if (days.length > 0) {
            sb.append(", days: ");
            for (byte day : days) {
                sb.append(day).append(" ");
            }
        } else {
            sb.append(".");
        }
        return sb.toString();
    }
}
