package com.jparams.store.comparison.date.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jparams.store.comparison.Comparison;

/**
 * Comparison strategy for comparing that two ZonedDateTime’s occured at the same time regardless of date or time zones.
 */
public class ZonedTimeComparison implements Comparison<ZonedDateTime>
{
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == ZonedDateTime.class;
    }

    @Override
    public Object createComparable(final ZonedDateTime item)
    {
        return item.withZoneSameInstant(ZoneId.of("UTC")).toLocalTime();
    }
}
