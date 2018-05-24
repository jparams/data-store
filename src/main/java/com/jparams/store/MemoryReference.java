package com.jparams.store;

/**
 * Reference to a stored item in memory
 *
 * @param <T>
 */
class MemoryReference<T> implements Reference<T>
{
    private final T reference;

    MemoryReference(final T reference)
    {
        this.reference = reference;
    }

    @Override
    public T get()
    {
        return reference;
    }
}
