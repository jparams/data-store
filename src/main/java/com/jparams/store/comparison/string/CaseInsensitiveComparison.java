package com.jparams.store.comparison.string;

import java.util.Locale;

import com.jparams.store.comparison.Comparison;

/**
 * Case insensitive string comparison strategy
 */
public class CaseInsensitiveComparison implements Comparison<String>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == String.class;
    }

    @Override
    public Object createComparable(final String item)
    {
        return item.toLowerCase(Locale.getDefault());
    }
}
