package com.jparams.store.comparison.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.jparams.store.comparison.Comparison;

/**
 * Comparison strategy for comparing numbers rounded to a given scale
 */
public class RoundingComparison<T extends Number> implements Comparison<T>
{
    private final int scale;
    private final RoundingMode roundingMode;

    public RoundingComparison(final int scale, final RoundingMode roundingMode)
    {
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return Number.class.isAssignableFrom(clazz);
    }

    @Override
    public Object createComparable(final T number)
    {
        return new BigDecimal(number.toString()).setScale(scale, roundingMode);
    }
}
