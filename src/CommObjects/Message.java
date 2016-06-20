/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommObjects;

import java.io.Serializable;

/**
 *
 * @author Aleks
 */
public abstract class Message implements Serializable {

    private static int counter = 0;
    private final int id;

    public Message() {
        id = counter++;
    }

    public int getId() {
        return id;
    }
}
