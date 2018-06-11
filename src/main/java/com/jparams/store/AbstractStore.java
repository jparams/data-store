package com.jparams.store;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jparams.store.index.Index;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.IndexException;
import com.jparams.store.index.IndexManager;
import com.jparams.store.reference.Reference;
import com.jparams.store.reference.ReferenceManager;

public abstract class AbstractStore<V> extends AbstractCollection<V> implements Store<V>
{
    private final ReferenceManager<V> referenceManager;
    private final IndexManager<V> indexManager;

    protected AbstractStore(final ReferenceManager<V> referenceManager, final IndexManager<V> indexManager)
    {
        this.referenceManager = referenceManager;
        this.indexManager = indexManager;
    }

    @Override
    public <K> Index<V> index(final String indexName, final IndexDefinition<K, V> indexDefinition) throws IndexException
    {
        return indexManager.createIndex(indexName, indexDefinition, referenceManager.getReferences());
    }

    @Override
    public Index<V> getIndex(final String indexName)
    {
        return indexManager.getIndex(indexName);
    }

    @Override
    public Collection<Index<V>> getIndexes()
    {
        return indexManager.getIndexes();
    }

    @Override
    public boolean removeIndex(final Index<V> index)
    {
        return indexManager.removeIndex(index);
    }

    @Override
    public boolean removeIndex(final String indexName)
    {
        return indexManager.removeIndex(indexName);
    }

    @Override
    public void reindex()
    {
        indexManager.reindex(referenceManager.getReferences());
    }

    @Override
    public void reindex(final V item)
    {
        reindex(Collections.singleton(item));
    }

    @Override
    public void reindex(final Collection<V> items)
    {
        final List<Reference<V>> references = items.stream()
                                                   .map(referenceManager::findReference)
                                                   .filter(Optional::isPresent)
                                                   .map(Optional::get)
                                                   .collect(Collectors.toList());

        indexManager.reindex(references);
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
    public Iterator<V> iterator()
    {
        return new StoreIterator(referenceManager.getReferences().iterator());
    }

    @Override
    public boolean addAll(final Collection<? extends V> collection)
    {
        final List<Reference<V>> references = new ArrayList<>();
        boolean changed = false;

        for (final V item : collection)
        {
            final Optional<Reference<V>> existingReference = referenceManager.findReference(item);

            if (existingReference.isPresent())
            {
                references.add(existingReference.get());
                continue;
            }

            references.add(referenceManager.add(item));
            changed = true;
        }

        indexManager.reindex(references);
        return changed;
    }

    @Override
    public boolean add(final V item)
    {
        return addAll(Collections.singleton(item));
    }

    @Override
    public boolean remove(final Object obj)
    {
        final Reference<V> reference = referenceManager.remove(obj);

        if (reference != null)
        {
            indexManager.removeReference(reference);
            return true;
        }

        return false;
    }

    @Override
    public void clear()
    {
        referenceManager.clear();
        indexManager.clear();
    }

    @Override
    public Store<V> copy()
    {
        return createCopy(referenceManager, indexManager);
    }

    protected abstract Store<V> createCopy(final ReferenceManager<V> referenceManager, final IndexManager<V> indexManager);


    protected ReferenceManager<V> getReferenceManager()
    {
        return referenceManager;
    }

    private class StoreIterator implements Iterator<V>
    {
        private final Iterator<Reference<V>> iterator;
        private Reference<V> previous;

        StoreIterator(final Iterator<Reference<V>> iterator)
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public V next()
        {
            previous = iterator.next();
            return previous.get();
        }

        @Override
        public void remove()
        {
            iterator.remove();
            indexManager.removeReference(previous);
        }
    }
}
