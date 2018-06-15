package com.jparams.store.memory;

import java.util.Arrays;
import java.util.Iterator;

import com.jparams.store.Store;
import com.jparams.store.SynchronizedStore;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.IndexException;
import com.jparams.store.index.KeyMapper;
import com.jparams.store.index.ReferenceIndex;
import com.jparams.store.index.SynchronizedIndex;
import com.jparams.store.index.comparison.string.CaseInsensitiveComparisonPolicy;
import com.jparams.store.model.Person;
import com.jparams.store.query.Query;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MemoryStoreTest
{
    private Store<Person> subject;
    private Index<Person> firstNameIndex;
    private Person person1;
    private Person person2;
    private Person person3;
    private boolean throwException = false;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();

        final KeyMapper<String, Person> keyMapper = (person) ->
        {
            if (throwException)
            {
                throw new RuntimeException("error indexing " + person.getFirstName());
            }

            return person.getFirstName();
        };

        firstNameIndex = subject.index("firstName", IndexDefinition.withKeyMapping(keyMapper).withComparisonPolicy(new CaseInsensitiveComparisonPolicy()));

        person1 = createPerson("John", "Smith");
        subject.add(person1);

        person2 = createPerson("James", "Johnson");
        subject.add(person2);

        person3 = createPerson("James", "Johnson");
        subject.add(person3);
    }

    @Test
    public void testVarArgConstructor()
    {
        subject = new MemoryStore<>(person1, person2);
        assertThat(subject).containsExactlyInAnyOrder(person1, person2);
    }

    @Test
    public void testAddMultiIndexWithProvider()
    {
        final Index<Person> anyNameIndex = subject.index(IndexDefinition.withKeyMappings(person -> Arrays.asList(person.getFirstName(), person.getLastName())));
        assertThat(anyNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(anyNameIndex.getFirst("Smith")).isSameAs(person1);
        assertThat(anyNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(anyNameIndex.get("Johnson")).containsExactly(person2, person3);
        assertThat(anyNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testAddIndexWithProvider()
    {
        final Index<Person> lastNameIndex = subject.index(Person::getLastName);
        assertThat(lastNameIndex.getFirst("Smith")).isSameAs(person1);
        assertThat(lastNameIndex.get("Johnson")).containsExactly(person2, person3);
        assertThat(lastNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testAddIndexWithNameAndProvider()
    {
        subject.index("lastName", Person::getLastName);

        final Index<Person> lastNameIndex = subject.getIndex("lastName");
        assertThat(lastNameIndex.getFirst("Smith")).isSameAs(person1);
        assertThat(lastNameIndex.get("Johnson")).containsExactly(person2, person3);
        assertThat(lastNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testAddIndexWithNameAndProviderAndPolicy()
    {
        subject.index("lastName", IndexDefinition.withKeyMapping(Person::getLastName)
                                                 .withComparisonPolicy(new CaseInsensitiveComparisonPolicy()));

        final Index<Person> lastNameIndex = subject.getIndex("lastName");
        assertThat(lastNameIndex.getFirst("smith")).isSameAs(person1);
        assertThat(lastNameIndex.get("JOHNson")).containsExactly(person2, person3);
        assertThat(lastNameIndex.get("Random")).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIndex()
    {
        assertThat(subject.index(firstNameIndex.getName(), Person::getFirstName));
    }

    @Test
    public void testGetSize()
    {
        assertThat(subject.size()).isEqualTo(3);
    }

    @Test
    public void testGetAll()
    {
        assertThat(subject).containsExactly(person1, person2, person3);
    }

    @Test
    public void testGetIndex()
    {
        assertThat(subject.getIndex("firstName")).isEqualTo(firstNameIndex);
        assertThat(subject.getIndex("lastName")).isNull();
    }

    @Test
    public void testFindIndex()
    {
        assertThat(subject.findIndex("firstName")).hasValue(firstNameIndex);
        assertThat(subject.findIndex("lastName")).isEmpty();
    }

    @Test
    public void testRemoveIndex()
    {
        assertThat(subject.removeIndex(firstNameIndex));
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testRemoveIndexHandlesInvalidIndex()
    {
        assertThat(subject.removeIndex(new ReferenceIndex<>(firstNameIndex.getName(), null, null, null)));
        assertThat(subject.getIndexes()).isNotEmpty();
    }

    @Test
    public void testRemoveIndexByName()
    {
        assertThat(subject.removeIndex(firstNameIndex.getName()));
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testGetIndexes()
    {
        assertThat(subject.getIndexes()).containsExactly(firstNameIndex);
    }

    @Test
    public void testGetIndexedData()
    {
        subject.index("lastName", Person::getLastName);

        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(subject.getFirst("firstName", "John")).isSameAs(person1);

        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(subject.get("firstName", "James")).containsExactly(person2, person3);
        assertThat(subject.get("firstName", "James", 1)).containsExactly(person2);

        assertThat(firstNameIndex.get("Random")).isEmpty();
        assertThat(subject.get("firstName", "Random")).isEmpty();

        assertThat(subject.get(Query.where("firstName", "John").or("firstName", "James"))).containsExactly(person1, person2, person3);
        assertThat(subject.get(Query.where("firstName", "John").or("firstName", "James"), 2)).containsExactly(person1, person2);
        assertThat(subject.get(Query.where("firstName", "John").and("lastName", "Smith"))).containsExactly(person1);
        assertThat(subject.get(Query.where("firstName", "John").and("lastName", "Stewart"))).isEmpty();
    }

    @Test
    public void testFindIndexedData()
    {
        assertThat(firstNameIndex.findFirst("John")).hasValue(person1);
        assertThat(subject.findFirst("firstName", "John")).hasValue(person1);

        assertThat(firstNameIndex.findFirst("Random")).isEmpty();
        assertThat(subject.findFirst("firstName", "Random")).isEmpty();

        assertThat(subject.findFirst(Query.where("firstName", "John"))).hasValue(person1);
        assertThat(subject.findFirst(Query.where("firstName", "John").or("firstName", "Random"))).hasValue(person1);
    }

    @Test
    public void testReindex()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.reindex();

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);
        assertThat(firstNameIndex.get("Richard")).containsExactly(person2);
        assertThat(firstNameIndex.get("James")).containsExactly(person3);
    }

    /**
     * testReindexHandlesExceptions
     */
    @Test
    public void testReindexHandlesExceptions()
    {
        throwException = true;

        try
        {
            subject.reindex();
            fail("expected exceptions");
        }
        catch (final IndexException e)
        {
            assertThat(e.getSuppressed()).hasSize(3);
        }

        // test original data remains unchanged
        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(firstNameIndex.getFirst("john")).isSameAs(person1);
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("jaMEs")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("Random")).isEmpty();
        assertThat(firstNameIndex.get(1223)).isEmpty();
    }

    @Test
    public void testClear()
    {
        subject.clear();
        assertThat(subject.getIndex("firstName").getFirst("John")).isNull();
        assertThat(subject).isEmpty();
    }

    @Test
    public void testAddAll()
    {
        subject.clear();
        subject.addAll(Arrays.asList(person1, person2, person3));
        assertThat(firstNameIndex.getFirst("John")).isSameAs(person1);
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
        assertThat(firstNameIndex.get("Random")).isEmpty();
    }

    @Test
    public void testAddReIndexesIfItemAlreadyInStore()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.add(person1);

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);

        // person 2 isn't re-indexed
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
    }

    @Test
    public void testReindexSingleItem()
    {
        person1.setFirstName("John-1");
        person2.setFirstName("Richard");

        subject.reindex(person1);

        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.getFirst("John-1")).isSameAs(person1);

        // person 2 isn't re-indexed
        assertThat(firstNameIndex.get("James")).containsExactly(person2, person3);
    }

    @Test
    public void testRemove()
    {
        assertThat(subject.remove(person1)).isTrue();
        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(subject).hasSize(2);
    }

    @Test
    public void testIterator()
    {
        final Iterator<Person> iterator = subject.iterator();
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person1);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person2);
        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next()).isEqualTo(person3);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testRemoveUsingIterator()
    {
        final Iterator<Person> iterator = subject.iterator();
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();

        assertThat(subject).hasSize(1);
        assertThat(firstNameIndex.getFirst("John")).isNull();
        assertThat(firstNameIndex.get("James")).containsExactly(person3);
    }

    @Test
    public void testRemoveItemThatDoesNotExist()
    {
        assertThat(subject.remove(createPerson("", ""))).isFalse();
    }

    @Test
    public void testContains()
    {
        assertThat(subject.contains(person1)).isTrue();
        assertThat(subject.contains(createPerson("", ""))).isFalse();
    }

    @Test
    public void testContainsAll()
    {
        assertThat(subject.containsAll(Arrays.asList(person1, person2))).isTrue();
        assertThat(subject.containsAll(Arrays.asList(createPerson("", ""), person3))).isFalse();
    }

    @Test
    public void testRemoveAllIndexes()
    {
        subject.removeAllIndexes();
        assertThat(subject.getIndexes()).isEmpty();
    }

    @Test
    public void testCreateCopy()
    {
        final Store<Person> copy = subject.copy();
        assertThat(copy).isNotSameAs(subject);

        final Index<Person> copyIndex = copy.getIndex(firstNameIndex.getName());
        assertThat(copyIndex).isNotSameAs(firstNameIndex);

        final Person newPerson = createPerson("Jim", "Jaf");
        copy.add(newPerson);

        assertThat(firstNameIndex.getFirst("Jim")).isNull();
        assertThat(copyIndex.getFirst("Jim")).isEqualTo(newPerson);

        assertThat(subject).hasSize(3);
        assertThat(copy).hasSize(4);

        assertThat(copyIndex.getFirst("John")).isSameAs(person1);
        assertThat(copyIndex.get("James")).containsExactly(person2, person3);
        assertThat(copyIndex.get("Random")).isEmpty();
    }

    @Test
    public void testUnmodifiableStore()
    {
        final Store<Person> unmodifiable = subject.unmodifiableStore();
        assertThat(unmodifiable).isNotSameAs(subject);

        assertThat(unmodifiable.getIndex("firstName").getFirst("John")).isSameAs(person1);
        assertThat(unmodifiable.getIndex("firstName").get("James")).containsExactly(person2, person3);
        assertThat(unmodifiable.getIndex("firstName").get("Random")).isEmpty();

        final Person newPerson = createPerson("Jim", "Jaf");
        subject.add(newPerson);

        assertThat(unmodifiable.getIndex("firstName").getFirst("Jim")).isEqualTo(newPerson);

        try
        {
            unmodifiable.add(createPerson("Jafy", "Jim"));
            fail("expected UnsupportedOperationException");
        }
        catch (final UnsupportedOperationException e)
        {
            // expected
        }
    }

    @Test
    public void testSynchronizedStore()
    {
        final Store<Person> synchronizedStore = subject.synchronizedStore();
        assertThat(synchronizedStore).isExactlyInstanceOf(SynchronizedStore.class);
        assertThat(synchronizedStore.getIndex("firstName")).isExactlyInstanceOf(SynchronizedIndex.class);
        assertThat(((SynchronizedStore<?>) synchronizedStore).getStore()).isSameAs(subject);
    }

    @Test
    public void testSynchronizeSynchronizedStore()
    {
        final Store<Person> synchronizedStore = subject.synchronizedStore().synchronizedStore();
        assertThat(synchronizedStore).isExactlyInstanceOf(SynchronizedStore.class);
        assertThat(synchronizedStore.getIndex("firstName")).isExactlyInstanceOf(SynchronizedIndex.class);
        assertThat(((SynchronizedStore<?>) synchronizedStore).getStore()).isSameAs(subject);
    }

    @Test
    public void toToString()
    {
        assertThat(subject.toString()).isEqualTo("[Person{id=null, firstName='John', lastName='Smith', email='null', gender=null, ipAddress='null'}, Person{id=null, firstName='James', lastName='Johnson', email='null', gender=null, ipAddress='null'}, Person{id=null, firstName='James', lastName='Johnson', email='null', gender=null, ipAddress='null'}]");
    }

    private Person createPerson(final String firstName, final String lastName)
    {
        return new Person(null, firstName, lastName, null, null, null);
    }
}