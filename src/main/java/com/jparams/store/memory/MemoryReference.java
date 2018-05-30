package com.jparams.store.memory;

import com.jparams.store.reference.Reference;

/**
 * Reference to a stored item in memory
 *
 * @param <T> reference type
 */
public class MemoryReference<T> implements Reference<T>
{
    private final T reference;

    public MemoryReference(final T reference)
    {
        this.reference = reference;
    }

    @Override
    public T get()
    {
        return reference;
    }

    @Override
    public String toString()
    {
        return String.valueOf(reference);
    }
}
