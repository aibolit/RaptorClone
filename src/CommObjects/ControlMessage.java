/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommObjects;

import Objects.ControlType;

/**
 *
 * @author Aleks
 */
public class ControlMessage extends Message {

    private final ControlType control;
    private final boolean on;

    public ControlMessage(ControlType control, boolean on) {
        this.control = control;
        this.on = on;
    }

    public ControlType getControl() {
        return control;
    }

    public boolean isOn() {
        return on;
    }
}
