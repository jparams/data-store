package com.jparams.store;

import java.util.Set;

abstract class AbstractIndex<T> implements Index<T>
{
    private final String name;
    private final Transformer<T, ?> transformer;

    AbstractIndex(final String name, final Transformer<T, ?> transformer)
    {
        this.name = name;
        this.transformer = transformer;
    }

    void reindex(final Reference<T> reference)
    {
        remove(reference);
        add(reference);
    }

    @SuppressWarnings("unchecked")
    Set<Object> generateKeys(final Reference<T> reference)
    {
        return (Set<Object>) transformer.transform(reference.get()).getKeys();
    }

    abstract void add(final Reference<T> reference);

    abstract void remove(final Reference<T> reference);

    abstract AbstractIndex<T> copy(boolean withData);

    Transformer<T, ?> getTransformer()
    {
        return transformer;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
