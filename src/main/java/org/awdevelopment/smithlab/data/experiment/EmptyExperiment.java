package org.awdevelopment.smithlab.data.experiment;

import org.awdevelopment.smithlab.data.Condition;
import org.awdevelopment.smithlab.data.Strain;
import org.awdevelopment.smithlab.io.exceptions.NoDaysException;

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

    public EmptyExperiment(Set<Strain> strains, Set<Condition> conditions, byte numReplicates, byte[] days, byte numDays) throws NoDaysException {
        super(conditions, strains);
        this.usingNumConditions = false;
        this.usingNumStrains = false;
        this.numConditions = (byte) conditions.size();
        this.numStrains = (byte) strains.size();
        this.numReplicates = numReplicates;
        if (numDays <= 0 && days.length == 0) throw new NoDaysException();
        else {
            usingNumDays = numDays > 0;
            if (usingNumDays) {
                this.days = new byte[numDays];
                this.numDays = numDays;
            } else {
                this.days = days;
                this.numDays = (byte) days.length;
            }
        }
    }

    public EmptyExperiment(byte numStrains, byte numConditions, byte numReplicates, byte[] days, byte numDays) throws NoDaysException {
        super(null, null);
        this.usingNumConditions = true;
        this.usingNumStrains = true;
        this.numConditions = numConditions;
        this.numStrains = numStrains;
        this.numReplicates = numReplicates;
        if (numDays <= 0 && days.length == 0) throw new NoDaysException();
        else {
            usingNumDays = numDays > 0;
            if (usingNumDays) {
                this.days = new byte[numDays];
                this.numDays = numDays;
            } else {
                this.days = days;
                this.numDays = (byte) days.length;
            }
        }
    }

    public EmptyExperiment(Set<Strain> strains, byte numConditions, byte numReplicates, byte[] days, byte numDays) throws NoDaysException {
        super(null, strains);
        this.usingNumConditions = false;
        this.usingNumStrains = true;
        this.numConditions = numConditions;
        this.numStrains = (byte) strains.size();
        this.numReplicates = numReplicates;
        if (numDays <= 0 && days.length == 0) throw new NoDaysException();
        else {
            usingNumDays = numDays > 0;
            if (usingNumDays) {
                this.days = new byte[numDays];
                this.numDays = numDays;
            } else {
                this.days = days;
                this.numDays = (byte) days.length;
            }
        }
    }

    public EmptyExperiment(byte numStrains, Set<Condition> conditions, byte numReplicates, byte[] days, byte numDays) throws NoDaysException {
        super(conditions, null);
        this.usingNumConditions = true;
        this.usingNumStrains = false;
        this.numConditions = (byte) conditions.size();
        this.numStrains = numStrains;
        this.numReplicates = numReplicates;
        if (numDays <= 0 && days.length == 0) throw new NoDaysException();
        else {
            usingNumDays = numDays > 0;
            if (usingNumDays) {
                this.days = new byte[numDays];
                this.numDays = numDays;
            } else {
                this.days = days;
                this.numDays = (byte) days.length;
            }
        }
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
}
