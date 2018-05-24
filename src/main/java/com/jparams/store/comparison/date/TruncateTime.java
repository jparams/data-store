//
// Copyright (c) 2018 Resonate Group Ltd.  All Rights Reserved.
//

package com.jparams.store.comparison.date;

import java.util.Optional;

/**
 * Provides unit for date time truncation
 */
public enum TruncateTime
{
    HOUR(null, 0, 0, 0),
    MINUTE(null, null, 0, 0),
    SECOND(null, null, null, 0);

    private final Integer hour;
    private final Integer minute;
    private final Integer second;
    private final Integer millis;

    TruncateTime(final Integer hour, final Integer minute, final Integer second, final Integer millis)
    {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millis = millis;
    }

    public Optional<Integer> getMonth()
    {
        return Optional.ofNullable(month);
    }

    public Optional<Integer> getDay()
    {
        return Optional.ofNullable(day);
    }

    public Optional<Integer> getHour()
    {
        return Optional.ofNullable(hour);
    }

    public Optional<Integer> getMinute()
    {
        return Optional.ofNullable(minute);
    }

    public Optional<Integer> getSecond()
    {
        return Optional.ofNullable(second);
    }

    public Optional<Integer> getMillis()
    {
        return Optional.ofNullable(millis);
    }
}
