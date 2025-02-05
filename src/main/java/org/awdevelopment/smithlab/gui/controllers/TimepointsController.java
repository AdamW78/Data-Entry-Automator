package org.awdevelopment.smithlab.gui.controllers;

import java.util.HashSet;
import java.util.Set;

public class TimepointsController extends AbstractLabelController {

    private boolean usingNumDays = false;
    private final Set<Byte> days;

    public TimepointsController() {
        super();
        days = new HashSet<>();
    }

    public boolean usingNumDays() { return usingNumDays; }
    private void addDay(byte day) { days.add(day); }
    private void removeDay(byte day) { days.remove(day); }
}
