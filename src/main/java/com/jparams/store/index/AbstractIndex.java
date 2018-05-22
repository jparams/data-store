package com.jparams.store.index;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.KeyProvider;
import com.jparams.store.comparison.Comparison;
import com.jparams.store.reference.Reference;

public abstract class AbstractIndex<V> implements Index<V>
{
    private final String name;
    private final KeyProvider<Object, V> keyProvider;
    private final Comparison<Object> comparison;

    protected AbstractIndex(final String indexName, final KeyProvider<Object, V> keyProvider, final Comparison<Object> comparison)
    {
        name = indexName;
        this.keyProvider = keyProvider;
        this.comparison = comparison;
    }

    @SuppressWarnings("unchecked")
    protected Set<Object> generateKeys(final Reference<V> reference) throws IndexCreationException
    {
        final V item;

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
            return keyProvider.provide(item)
                              .getValues()
                              .stream()
                              .map(this::getComparableKey)
                              .filter(Objects::nonNull)
                              .collect(Collectors.toSet());
        }
        catch (final RuntimeException e)
        {
            throw new IndexCreationException("Index: " + name + ". Error generating indexes for item: " + item, e);
        }
    }

    protected Object getComparableKey(final Object key)
    {
        if (key == null || !comparison.supports(key.getClass()))
        {
            return null;
        }

        return comparison.getComparable(key);
    }

    public abstract void index(final Reference<V> reference) throws IndexCreationException;

    public abstract void removeIndex(final Reference<V> reference);

    public abstract void clear();

    protected abstract AbstractIndex<V> copy(String name, KeyProvider<Object, V> keyProvider, Comparison<Object> comparison);

    public AbstractIndex<V> copy()
    {
        return copy(name, keyProvider, comparison);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
