package com.jparams.store.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jparams.store.persistence.manager.PersistenceManager;
import com.jparams.store.reference.Reference;
import com.jparams.store.reference.ReferenceManager;

/**
 * An implementation of reference manager that maintains unique references
 *
 * @param <V> value type
 */
public class PersistenceReferenceManager<V> implements ReferenceManager<V>
{
    private final PersistenceManager<V> persistenceManager;
    private final Map<Object, PersistenceReference<V>> referenceMap;

    PersistenceReferenceManager(final PersistenceManager<V> persistenceManager)
    {
        this.persistenceManager = persistenceManager;
        this.referenceMap = buildReferenceMap();
    }

    private PersistenceReferenceManager(final PersistenceManager<V> persistenceManager, final Map<Object, PersistenceReference<V>> referenceMap)
    {
        this.persistenceManager = persistenceManager;
        this.referenceMap = referenceMap;
    }

    @Override
    public Collection<Reference<V>> getReferences()
    {
        return new ArrayList<>(referenceMap.values());
    }

    @Override
    public Optional<Reference<V>> findReference(final Object item)
    {
        final Object identity = persistenceManager.identify(item);

        if (identity == null)
        {
            return Optional.empty();
        }

        return Optional.ofNullable(referenceMap.get(identity));
    }

    @Override
    public int size()
    {
        return referenceMap.size();
    }

    @Override
    public void clear()
    {
        referenceMap.entrySet()
                    .removeIf(entry -> {
                        persistenceManager.remove(entry.getKey());
                        return true;
                    });
    }

    @Override
    public Reference<V> add(final V item)
    {
        final Object identity = persistenceManager.persist(item);

        if (identity == null)
        {
            return null;
        }

        final PersistenceReference<V> reference = new PersistenceReference<>(persistenceManager, identity, item);
        referenceMap.put(identity, reference);
        return reference;
    }

    @Override
    public Reference<V> remove(final Object item)
    {
        final Object identity = persistenceManager.identify(item);

        if (identity == null)
        {
            return null;
        }

        persistenceManager.remove(identity);

        return referenceMap.remove(identity);
    }

    @Override
    public ReferenceManager<V> copy()
    {
        return new PersistenceReferenceManager<>(persistenceManager, new LinkedHashMap<>(referenceMap));
    }

    private Map<Object, PersistenceReference<V>> buildReferenceMap()
    {
        return persistenceManager.getIdentities()
                                 .stream()
                                 .collect(Collectors.toMap(Function.identity(), identity -> new PersistenceReference<>(persistenceManager, identity, null)));
    }
}
