package com.jparams.store.comparison.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.jparams.store.comparison.ComparisonPolicy;

/**
 * Comparison policy for comparing two numbers to a fixed scale. A scale being the number of digits to the right of a decimal point.
 */
public class ScalingComparisonPolicy implements ComparisonPolicy<Number>
{
    private final int scale;
    private final RoundingMode roundingMode;

    public ScalingComparisonPolicy(final int scale, final RoundingMode roundingMode)
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
    public Object createComparable(final Number number)
    {
        return new BigDecimal(number.toString()).setScale(scale, roundingMode);
    }
}
