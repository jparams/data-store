package com.jparams.store.index;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ReferenceIndexManager<V> extends IndexManager<V>
{
    private ReferenceIndexManager(final Collection<AbstractIndex<?, V>> indexes)
    {
        super(indexes);
    }

    public ReferenceIndexManager()
    {
        this(Collections.emptyList());
    }

    @Override
    protected IndexManager<V> createCopy(final Set<AbstractIndex<?, V>> indexes)
    {
        return new ReferenceIndexManager<>(indexes);
    }

    @Override
    protected <K> AbstractIndex<K, V> createIndex(final String indexName, final IndexDefinition<K, V> indexDefinition)
    {
        return new ReferenceIndex<>(indexName, indexDefinition.getKeyMapper(), indexDefinition.getReducer(), indexDefinition.getComparisonPolicy());
    }
}
