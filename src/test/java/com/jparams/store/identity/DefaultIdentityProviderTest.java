package com.jparams.store.identity;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultIdentityProviderTest
{
    private DefaultIdentityProvider subject;

    @Before
    public void setUp()
    {
        subject = new DefaultIdentityProvider();
    }

    @Test
    public void testGetIdentity()
    {
        assertThat(subject.getIdentity(null)).isNull();

        final Object object = new Object();
        assertThat(subject.getIdentity(object)).isEqualTo(object);
    }
}