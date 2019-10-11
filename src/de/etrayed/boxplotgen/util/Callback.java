package de.etrayed.boxplotgen.util;

/**
 * @author Etrayed
 */
public interface Callback<T> {

    void handleSuccess(T t);

    void handleException(Exception e);
}
