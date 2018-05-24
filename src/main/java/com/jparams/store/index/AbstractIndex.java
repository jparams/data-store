package com.jparams.store.index;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.KeyProvider;
import com.jparams.store.comparison.Comparison;
import com.jparams.store.reference.Reference;

public abstract class AbstractIndex<K, V> implements Index<V>
{
    private final String name;
    private final KeyProvider<K, V> keyProvider;
    private final Comparison<K> comparison;

    protected AbstractIndex(final String name, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison)
    {
        this.name = name;
        this.keyProvider = keyProvider;
        this.comparison = comparison;
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

        @SuppressWarnings("unchecked") final Object comparableKey = comparison.createComparable((K) key);
        return comparableKey;
    }

    public abstract void index(final Reference<V> reference) throws IndexCreationException;

    public abstract void removeIndex(final Reference<V> reference);

    public abstract void clear();

    protected abstract AbstractIndex<K, V> copy(String name, KeyProvider<K, V> keyProvider, Comparison<K> comparison);

    public AbstractIndex<K, V> copy()
    {
        return copy(name, keyProvider, comparison);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
