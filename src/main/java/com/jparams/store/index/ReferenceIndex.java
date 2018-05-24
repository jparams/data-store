package com.jparams.store.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.KeyProvider;
import com.jparams.store.comparison.Comparison;
import com.jparams.store.reference.Reference;

/**
 * Maintains indexes against references to stored items
 *
 * @param <V> value type
 */
public class ReferenceIndex<K, V> extends AbstractIndex<K, V>
{
    private final Map<Object, Set<Reference<V>>> keyToReferenceMap;
    private final Map<Reference<V>, Set<Object>> referenceToKeysMap;

    public ReferenceIndex(final String indexName, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison)
    {
        super(indexName, keyProvider, comparison);
        this.keyToReferenceMap = new HashMap<>();
        this.referenceToKeysMap = new HashMap<>();
    }

    private ReferenceIndex(final String indexName, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison, final Map<Object, Set<Reference<V>>> keyToReferenceMap, final Map<Reference<V>, Set<Object>> referenceToKeysMap)
    {
        super(indexName, keyProvider, comparison);
        this.keyToReferenceMap = keyToReferenceMap;
        this.referenceToKeysMap = referenceToKeysMap;
    }

    @Override
    public Optional<V> findFirst(final Object key)
    {
        final Object comparableKey = getComparableKey(key);
        final Set<Reference<V>> references = keyToReferenceMap.get(comparableKey);

        if (references == null)
        {
            return Optional.empty();
        }

        return references.stream().map(Reference::get).findFirst();
    }

    @Override
    public List<V> get(final Object key)
    {
        final Object comparableKey = getComparableKey(key);
        final Set<Reference<V>> references = keyToReferenceMap.get(comparableKey);

        if (references == null)
        {
            return Collections.emptyList();
        }

        return references.stream().map(Reference::get).collect(Collectors.toList());
    }

    @Override
    public Set<Object> getKeys()
    {
        return keyToReferenceMap.keySet();
    }

    @Override
    public void index(final Reference<V> reference) throws IndexCreationException
    {
        final Set<Object> keys = generateKeys(reference);

        removeIndex(reference);

        if (!keys.isEmpty())
        {
            referenceToKeysMap.put(reference, Collections.unmodifiableSet(keys));
            keys.forEach(key -> keyToReferenceMap.computeIfAbsent(key, ignore -> new LinkedHashSet<>()).add(reference));
        }
    }

    @Override
    public void removeIndex(final Reference<V> reference)
    {
        final Set<Object> keys = referenceToKeysMap.get(reference);

        if (keys == null)
        {
            return;
        }

        for (final Object key : keys)
        {
            final Set<Reference<V>> references = keyToReferenceMap.get(key);

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
    protected AbstractIndex<K, V> copy(final String name, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison)
    {
        final Map<Object, Set<Reference<V>>> keyToReferenceMapCopy = keyToReferenceMap.entrySet()
                                                                                      .stream()
                                                                                      .collect(Collectors.toMap(Entry::getKey, references -> new LinkedHashSet<>(references.getValue())));

        final Map<Reference<V>, Set<Object>> referenceToKeysMapCopy = new HashMap<>(referenceToKeysMap);

        return new ReferenceIndex<>(name, keyProvider, comparison, keyToReferenceMapCopy, referenceToKeysMapCopy);
    }

    @Override
    public void clear()
    {
        keyToReferenceMap.clear();
        referenceToKeysMap.clear();
    }
}
