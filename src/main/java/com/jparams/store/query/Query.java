package com.jparams.store.query;

public interface Query
{
    /**
     * Build query definition
     *
     * @return definition
     */
    QueryDefinition build();

    /**
     * Create a new query
     *
     * @param indexName    index name
     * @param valueToMatch value to match
     * @return query
     */
    static BasicQuery where(final String indexName, final Object valueToMatch)
    {
        final QueryImpl query = new QueryImpl();
        query.and(indexName, valueToMatch);
        return query;
    }
}
