/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects;

/**
 *
 * @author Sasa
 */
public class RaptorCloneException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>ExchangeException</code> without detail
     * message.
     */
    public RaptorCloneException() {
    }

    /**
     * Constructs an instance of <code>ExchangeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RaptorCloneException(String msg) {
        super(msg);
    }
}
