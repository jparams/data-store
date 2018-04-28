package com.jparams.store;

/**
 * Reference a stored item
 *
 * @param <T>
 */
public interface Reference<T>
{
    /**
     * Get referenced item
     *
     * @return referenced item
     */
    T get();
}
