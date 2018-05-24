package com.jparams.store;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractStore<T> extends AbstractCollection<T> implements Store<T>
{
    private final ReferenceManager<T> referenceManager;
    private Map<String, AbstractIndex<T>> indexMap;

    AbstractStore(final ReferenceManager<T> referenceManager)
    {
        this.referenceManager = referenceManager;
        this.indexMap = new HashMap<>();
    }

    AbstractStore(final ReferenceManager<T> referenceManager, final Set<AbstractIndex<T>> indexes)
    {
        this.referenceManager = referenceManager;
        this.indexMap = indexes.stream().collect(Collectors.toMap(AbstractIndex::getName, Function.identity()));
    }

    @Override
    public <K> Index<T> addIndex(final String indexName, final Transformer<T, K> transformer)
    {
        if (indexMap.containsKey(indexName))
        {
            throw new IllegalArgumentException("An index already exists with this name");
        }

        final AbstractIndex<T> abstractIndex = createIndex(indexName, transformer);
        indexMap.put(indexName, abstractIndex);
        return abstractIndex;
    }

    @Override
    public <K> Index<T> addIndex(final Transformer<T, K> transformer)
    {
        return addIndex(UUID.randomUUID().toString(), transformer);
    }

    @Override
    public Index<T> getIndex(final String indexName)
    {
        return indexMap.get(indexName);
    }

    @Override
    public boolean removeIndex(final Index<T> index)
    {
        return indexMap.values().removeIf(abstractIndex -> abstractIndex.equals(index));
    }

    @Override
    public boolean removeIndex(final String indexName)
    {
        return indexMap.remove(indexName) != null;
    }

    @Override
    public void reindex()
    {
        final Collection<Reference<T>> references = referenceManager.getReferences();
        indexMap = indexMap.values()
                           .stream()
                           .map(index -> index.copy(false))
                           .peek(index -> references.forEach(index::add))
                           .collect(Collectors.toMap(AbstractIndex::getName, Function.identity()));
    }

    @Override
    public void reindex(final T item)
    {
        referenceManager.findReference(item)
                        .ifPresent((reference) -> indexMap.values().forEach(index -> index.reindex(reference)));
    }

    @Override
    public int size()
    {
        return referenceManager.size();
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    @Override
    public boolean contains(final Object obj)
    {
        return referenceManager.findReference(obj).isPresent();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new StoreIterator(referenceManager.getReferences().iterator());
    }

    @Override
    public boolean add(final T obj)
    {
        final Optional<Reference<T>> existingReference = referenceManager.findReference(obj);

        if (existingReference.isPresent())
        {
            indexMap.values().forEach(index -> index.reindex(existingReference.get()));
            return false;
        }

        final Reference<T> newIndex = referenceManager.add(obj);
        indexMap.values().forEach(index -> index.add(newIndex));
        return true;
    }

    @Override
    public boolean remove(final Object obj)
    {
        final Optional<Reference<T>> reference = referenceManager.findReference(obj);

        if (reference.isPresent())
        {
            indexMap.values().forEach(index -> index.remove(reference.get()));
            return true;
        }

        return false;
    }

    @Override
    public void clear()
    {
        referenceManager.clear();
        reindex();
    }

    @Override
    public Store<T> copy()
    {
        return createCopy(referenceManager, indexMap.values());
    }

    protected abstract Store<T> createCopy(final ReferenceManager<T> referenceManager, final Collection<AbstractIndex<T>> indexes);

    protected abstract AbstractIndex<T> createIndex(final String indexName, final Transformer<T, ?> transformer);

    private class StoreIterator implements Iterator<T>
    {
        private final Iterator<Reference<T>> iterator;
        private Reference<T> previous;

        StoreIterator(final Iterator<Reference<T>> iterator)
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public T next()
        {
            previous = iterator.next();
            return previous.get();
        }

        @Override
        public void remove()
        {
            iterator.remove();
            indexMap.values().forEach(index -> index.remove(previous));
        }
    }
}
