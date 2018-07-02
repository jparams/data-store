package com.jparams.store.persistence.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import com.jparams.file.DataFile;

public class DataFilePersistenceManager<V> extends AbstractPersistenceManager<V>
{
    private final DataFile<V> dataFile;
    private final Map<Object, String> identityMap;

    public DataFilePersistenceManager(final DataFile<V> dataFile, final Class<V> valueType, final Function<V, Object> identityFn, final long maxCacheSize)
    {
        super(valueType, identityFn, maxCacheSize);
        this.dataFile = dataFile;
        this.identityMap = new HashMap<>();
    }

    @Override
    protected void persist(final Object identify, final V item)
    {
        final String derivedIdentity = UUID.randomUUID().toString();
        identityMap.put(identify, derivedIdentity);
        dataFile.add(derivedIdentity, item);
    }

    @Override
    protected void removeByIdentity(final Object identity)
    {
        final String derivedIdentity = identityMap.remove(identity);

        if (derivedIdentity != null)
        {
            dataFile.remove(derivedIdentity);
        }
    }

    @Override
    protected V getByIdentity(final Object identity)
    {
        if (identityMap.containsKey(identity))
        {
            return dataFile.get(identityMap.get(identity));
        }

        return null;
    }

    @Override
    public Collection<Object> getIdentities()
    {
        return identityMap.keySet();
    }
}
