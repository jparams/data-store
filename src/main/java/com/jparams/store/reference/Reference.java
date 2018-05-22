package com.jparams.store.reference;

/**
 * Reference a stored item
 *
 * @param <T> type of item referenced
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
