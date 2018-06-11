package com.jparams.store.index;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.index.comparison.ComparisonPolicy;
import com.jparams.store.index.reducer.Reducer;
import com.jparams.store.reference.Reference;

public abstract class AbstractIndex<K, V> implements Index<V>
{
    private final String name;
    private final KeyMapper<Collection<K>, V> keyMapper;
    private final Reducer<K, V> reducer;
    private final ComparisonPolicy<K> comparisonPolicy;

    protected AbstractIndex(final String name, final KeyMapper<Collection<K>, V> keyMapper, final Reducer<K, V> reducer, final ComparisonPolicy<K> comparisonPolicy)
    {

        this.name = name;
        this.keyMapper = keyMapper;
        this.reducer = reducer;
        this.comparisonPolicy = comparisonPolicy;
    }

    protected Set<K> generateKeys(final Reference<V> reference) throws IndexCreationException
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
            return keyMapper.map(item)
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

    protected K getComparableKey(final Object key)
    {
        if (key == null || !comparisonPolicy.supports(key.getClass()))
        {
            return null;
        }

        @SuppressWarnings("unchecked") final K comparable = comparisonPolicy.createComparable((K) key);
        return comparable;
    }

    protected Reducer<K, V> getReducer()
    {
        return reducer;
    }

    public abstract void index(final Reference<V> reference) throws IndexCreationException;

    public abstract void removeIndex(final Reference<V> reference);

    public abstract void clear();

    protected abstract AbstractIndex<K, V> copy(String name, KeyMapper<Collection<K>, V> keyMapper, Reducer<K, V> reducer, ComparisonPolicy<K> comparisonPolicy);

    public AbstractIndex<K, V> copy()
    {
        return copy(name, keyMapper, reducer, comparisonPolicy);
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
