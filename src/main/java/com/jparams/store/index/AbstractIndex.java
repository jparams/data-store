package com.jparams.store.index;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.KeyProvider;
import com.jparams.store.comparison.ComparisonPolicy;
import com.jparams.store.reference.Reference;

public abstract class AbstractIndex<K, V> implements Index<V>
{
    private final String name;
    private final KeyProvider<Collection<K>, V> keyProvider;
    private final ComparisonPolicy<K> comparisonPolicy;

    protected AbstractIndex(final String name, final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy)
    {
        this.name = name;
        this.keyProvider = keyProvider;
        this.comparisonPolicy = comparisonPolicy;
    }

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
        if (key == null || !comparisonPolicy.supports(key.getClass()))
        {
            return null;
        }

        @SuppressWarnings("unchecked") final Object comparableKey = comparisonPolicy.createComparable((K) key);
        return comparableKey;
    }

    public abstract void index(final Reference<V> reference) throws IndexCreationException;

    public abstract void removeIndex(final Reference<V> reference);

    public abstract void clear();

    protected abstract AbstractIndex<K, V> copy(String name, KeyProvider<Collection<K>, V> keyProvider, ComparisonPolicy<K> comparisonPolicy);

    public AbstractIndex<K, V> copy()
    {
        return copy(name, keyProvider, comparisonPolicy);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "Index[name='" + name + "\']";
    }
}
