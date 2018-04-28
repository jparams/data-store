package com.jparams.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In memory reference manager creates, stores and manages {@link MemoryReference}
 *
 * @param <T>
 */
public class MemoryReferenceManager<T> implements ReferenceManager<T>
{
    private final Map<Object, Reference<T>> referenceMap;

    MemoryReferenceManager()
    {
        referenceMap = new LinkedHashMap<>();
    }

    private MemoryReferenceManager(final MemoryReferenceManager<T> referenceManager)
    {
        this.referenceMap = new HashMap<>(referenceManager.referenceMap);
    }

    @Override
    public Collection<Reference<T>> getReferences()
    {
        return referenceMap.values();
    }

    @Override
    public Optional<Reference<T>> findReference(final Object item)
    {
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
    public Reference<T> add(final T obj)
    {
        if (referenceMap.containsKey(obj))
        {
            return referenceMap.get(obj);
        }

        final Reference<T> reference = new MemoryReference<>(obj);
        referenceMap.put(obj, reference);
        return reference;
    }

    @Override
    public ReferenceManager<T> copy()
    {
        return new MemoryReferenceManager<>(this);
    }
}
