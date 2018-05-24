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

    @SuppressWarnings("unchecked")
    Set<Object> generateKeys(final Reference<T> reference) throws IndexCreationException
    {
        final T item;

        try
        {
            item = reference.get();
        }
        catch (final RuntimeException e)
        {
            throw new IndexCreationException("Index: " + name + ". Unable to retrieve item to index", e);
        }

        try
        {
            return (Set<Object>) transformer.transform(item).getKeys();
        }
        catch (final RuntimeException e)
        {
            throw new IndexCreationException("Index: " + name + ". Error generating indexes for item: " + item, e);
        }
    }

    abstract void index(final Reference<T> reference) throws IndexCreationException;

    abstract void removeIndex(final Reference<T> reference);

    abstract AbstractIndex<T> copy();

    abstract void clear();

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
