package com.jparams.store.index;

import com.jparams.store.reference.Reference;

public final class Element<V>
{
    private final Reference<V> reference;
    private boolean removed;

    public Element(final Reference<V> reference)
    {
        this.reference = reference;
    }

    public V get()
    {
        return reference.get();
    }

    public void remove()
    {
        removed = true;
    }

    Reference<V> getReference()
    {
        return reference;
    }

    boolean isRemoved()
    {
        return removed;
    }
}
