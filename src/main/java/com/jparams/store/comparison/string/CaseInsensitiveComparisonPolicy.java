package com.jparams.store.comparison.string;

import java.util.Locale;

import com.jparams.store.comparison.ComparisonPolicy;

/**
 * Comparison policy for comparing two string elements regardless of case.
 */
public class CaseInsensitiveComparisonPolicy implements ComparisonPolicy<String>
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
