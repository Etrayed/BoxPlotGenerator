package de.etrayed.boxplotgen.util;

import java.util.concurrent.Callable;

/**
 * @author Etrayed
 */
public final class EasyCallable<T> implements Callable<T> {

    private final T t;

    public EasyCallable(T t) {
        this.t = t;
    }

    @Override
    public T call() {
        return t;
    }
}
