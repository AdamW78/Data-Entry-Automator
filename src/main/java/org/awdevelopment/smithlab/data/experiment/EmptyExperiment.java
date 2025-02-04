package org.awdevelopment.smithlab.data.experiment;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;

import java.util.Set;

public class EmptyExperiment extends AbstractExperiment {

    private final short numReplicates;
    private final short[] days;
    private final short numDays;
    private final boolean usingNumDays;

    public EmptyExperiment(Set<Strain> strains, Set<Condition> conditions, short numReplicates, short[] days, short numDays) throws NoDaysException {
        super(conditions, strains);
        this.numReplicates = numReplicates;
        if (numDays <= 0 && days.length == 0) throw new NoDaysException();
        else {
            usingNumDays = numDays > 0;
            if (usingNumDays) {
                this.days = new short[numDays];
                this.numDays = numDays;
            } else {
                this.days = days;
                this.numDays = (short) days.length;
            }
        }
    }

    public short getNumReplicates() { return numReplicates; }
    public boolean hasNoDays() { return days.length == 0 && !usingNumDays; }
    public short[] getDays() { return days; }
    public short getNumDays() { return numDays; }
    public boolean usingNumDays() { return usingNumDays; }
}
