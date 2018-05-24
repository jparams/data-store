package com.jparams.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maintains indexes against references to stored items
 *
 * @param <T>
 */
class ReferenceIndex<T> extends AbstractIndex<T>
{
    private final Map<Object, Set<Reference<T>>> keyToReferenceMap;
    private final Map<Reference<T>, Set<Object>> referenceToKeysMap;

    ReferenceIndex(final String name, final Transformer<T, ?> transformer)
    {
        super(name, transformer);
        this.keyToReferenceMap = new HashMap<>();
        this.referenceToKeysMap = new HashMap<>();
    }

    private ReferenceIndex(final String name, final Transformer<T, ?> transformer, final Map<Object, Set<Reference<T>>> keyToReferenceMap, final Map<Reference<T>, Set<Object>> referenceToKeysMap)
    {
        super(name, transformer);
        this.keyToReferenceMap = keyToReferenceMap;
        this.referenceToKeysMap = referenceToKeysMap;
    }

    @Override
    public Optional<T> findFirst(final Object key)
    {
        final Set<Reference<T>> references = keyToReferenceMap.get(key);

        if (references == null)
        {
            return Optional.empty();
        }

        return references.stream().map(Reference::get).findFirst();
    }

    @Override
    public List<T> get(final Object key)
    {
        final Set<Reference<T>> references = keyToReferenceMap.get(key);

        if (references == null)
        {
            return Collections.emptyList();
        }

        return references.stream().map(Reference::get).collect(Collectors.toList());
    }

    @Override
    void add(final Reference<T> reference)
    {
        final Set<Object> keys = generateKeys(reference);

        remove(reference);

        if (!keys.isEmpty())
        {
            referenceToKeysMap.put(reference, Collections.unmodifiableSet(keys));
            keys.forEach(key -> keyToReferenceMap.computeIfAbsent(key, ignore -> new LinkedHashSet<>()).add(reference));
        }
    }

    @Override
    void remove(final Reference<T> reference)
    {
        final Set<Object> keys = referenceToKeysMap.get(reference);

        if (keys == null)
        {
            return;
        }

        for (final Object key : keys)
        {
            final Set<Reference<T>> references = keyToReferenceMap.get(key);

            if (reference != null)
            {
                references.remove(reference);

                if (references.isEmpty())
                {
                    keyToReferenceMap.remove(key);
                }
            }
        }

        referenceToKeysMap.remove(reference);
    }

    @Override
    ReferenceIndex<T> copy(final boolean withData)
    {
        final Map<Object, Set<Reference<T>>> keyToReferenceMapCopy = withData ? keyToReferenceMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, references -> new HashSet<>(references.getValue()))) : new HashMap<>();
        final Map<Reference<T>, Set<Object>> referenceToKeysMapCopy = withData ? new HashMap<>(referenceToKeysMap) : new HashMap<>();
        return new ReferenceIndex<>(getName(), getTransformer(), keyToReferenceMapCopy, referenceToKeysMapCopy);
    }
}
