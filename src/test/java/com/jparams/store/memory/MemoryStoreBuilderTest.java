package com.jparams.store.memory;

import java.util.Collections;

import com.jparams.store.Store;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.comparison.string.CaseInsensitiveComparisonPolicy;
import com.jparams.store.model.Person;
import com.jparams.store.model.PersonBuilder;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryStoreBuilderTest
{
    @Test
    public void testBuilder()
    {
        final Person person1 = PersonBuilder.aPerson().withFirstName("firstName1").withLastName("lastName1").build();
        final Person person2 = PersonBuilder.aPerson().withFirstName("firstName2").withLastName("lastName2").build();
        final Person person3 = PersonBuilder.aPerson().withFirstName("firstName3").withLastName("lastName3").build();
        final Person person4 = PersonBuilder.aPerson().withFirstName("firstName4").withLastName("lastName4").build();

        final Store<Person> store = MemoryStore.<Person>newStore()
            .withIndex("firstName", Person::getFirstName)
            .withIndex("lastName", IndexDefinition.withKeyMapping(Person::getLastName).withComparisonPolicy(new CaseInsensitiveComparisonPolicy()))
            .withValue(person1)
            .withValues(person2, person3)
            .withValues(Collections.singleton(person4))
            .build();

        assertThat(store.getFirst("firstName", "firstName1")).isSameAs(person1);
        assertThat(store.getFirst("firstName", "firstName2")).isSameAs(person2);
        assertThat(store.getFirst("firstName", "firstName3")).isSameAs(person3);
        assertThat(store.getFirst("firstName", "firstName4")).isSameAs(person4);

        assertThat(store.getFirst("lastName", "lastNAME1")).isSameAs(person1);
        assertThat(store.getFirst("lastName", "lastname2")).isSameAs(person2);
        assertThat(store.getFirst("lastName", "LASTNAME3")).isSameAs(person3);
        assertThat(store.getFirst("lastName", "lastName4")).isSameAs(person4);
    }
}