package com.jparams.store.index.comparison.string;

import java.util.Locale;

import com.jparams.store.index.comparison.ComparisonPolicy;

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
    public String createComparable(final String item)
    {
        return item.toLowerCase(Locale.getDefault());
    }
}
