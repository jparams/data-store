package com.jparams.store.comparison.date.time;

import java.time.LocalDateTime;

import com.jparams.store.comparison.Comparison;
import com.jparams.store.comparison.date.TruncateDateTime;

/**
 * Comparison strategy for comparing only the local time element of a local date tome
 */
public class TruncatedLocalDateTimeComparison implements Comparison<LocalDateTime>
{
    private final TruncateDateTime truncatedTo;

    public TruncatedLocalDateTimeComparison(final TruncateDateTime truncatedTo)
    {
        this.truncatedTo = truncatedTo;
    }

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz == LocalDateTime.class;
    }

    @Override
    public Object createComparable(final LocalDateTime localDateTime)
    {
        return LocalDateTime.of(localDateTime.getYear(),
                                truncatedTo.getMonth().orElse(localDateTime.getMonthValue()),
                                truncatedTo.getDay().orElse(localDateTime.getDayOfMonth()),
                                truncatedTo.getHour().orElse(localDateTime.getHour()),
                                truncatedTo.getMinute().orElse(localDateTime.getMinute()),
                                truncatedTo.getSecond().orElse(localDateTime.getSecond()),
                                truncatedTo.getMillis().orElse(localDateTime.getNano()));
    }
}
