package com.jparams.store.memory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.jparams.store.reference.Reference;
import com.jparams.store.reference.ReferenceManager;

/**
 * An implementation of reference manager that maintains unique references
 *
 * @param <V> value type
 */
public class MemoryReferenceManager<V> implements ReferenceManager<V>
{
    private final Map<Object, Reference<V>> referenceMap;

    MemoryReferenceManager()
    {
        this.referenceMap = new LinkedHashMap<>();
    }

    private MemoryReferenceManager(final Map<Object, Reference<V>> referenceMap)
    {
        this.referenceMap = new LinkedHashMap<>(referenceMap);
    }

    @Override
    public Collection<Reference<V>> getReferences()
    {
        return referenceMap.values();
    }

    @Override
    public Optional<Reference<V>> findReference(final Object item)
    {
        if (item == null)
        {
            return Optional.empty();
        }

        return Optional.ofNullable(referenceMap.get(item));
    }

    @Override
    public int size()
    {
        return referenceMap.size();
    }

    @Override
    public void clear()
    {
        referenceMap.clear();
    }

    @Override
    public Reference<V> add(final V item)
    {
        if (item == null)
        {
            return null;
        }

        if (referenceMap.containsKey(item))
        {
            return referenceMap.get(item);
        }

        final Reference<V> reference = new MemoryReference<>(item);
        referenceMap.put(item, reference);
        return reference;
    }

    @Override
    public Reference<V> remove(final Object item)
    {
        if (item == null)
        {
            return null;
        }

        return referenceMap.remove(item);
    }

    @Override
    public ReferenceManager<V> copy()
    {
        return new MemoryReferenceManager<>(referenceMap);
    }
}
