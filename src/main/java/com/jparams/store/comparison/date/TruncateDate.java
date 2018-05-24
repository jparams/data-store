package com.jparams.store.comparison.date;

import java.util.Optional;

/**
 * Provides unit for date truncation
 */
public enum TruncateDate
{
    YEAR(1, 1),
    MONTH(null, 1);

    private final Integer month;
    private final Integer day;

    TruncateDate(final Integer month, final Integer day)
    {
        this.day = day;
        this.month = month;
    }

    public Optional<Integer> getDay()
    {
        return Optional.ofNullable(day);
    }

    public Optional<Integer> getMonth()
    {
        return Optional.ofNullable(month);
    }
}
