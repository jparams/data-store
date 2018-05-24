# Data Store

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jparams/data-store/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jparams/data-store)
 [![Build Status](https://travis-ci.org/jparams/data-store.svg?branch=master)](https://travis-ci.org/jparams/data-store)

## Getting Started

### Get Data Store

Maven:
```
<dependency>
    <groupId>com.jparams</groupId>
    <artifactId>data-store</artifactId>
    <version>1.x.x</version>
</dependency>
```

Gradle:
```
compile 'com.jparams:data-store:1.x.x'
```

### What is Data Store?
Data Store is a full-featured indexed Java Collection capable of ultra-fast data lookup.

### Create a Data Store
Creating a Data Store is easy!

```java
public class Application {
    public static void main(String[] args) {
        final Store<Person> personStore = new MemoryStore<>();
        final Index<T> firstNameIndex = personStore.addIndex((person) -> Keys.create(person.getFirstName()));
        final Index<T> lastNameIndex = personStore.addIndex((person) -> Keys.create(person.getLastName()));
        final Index<T> firstOrLastNameIndex = personStore.addIndex((person) -> Keys.create(person.getFirstName(), person.getLastName()));

        personStore.add(new Person("John", "Smith"));
        personStore.add(new Person("James", "Smith"));
        personStore.add(new Person("Smith", "John"));
        
        firstNameIndex.getFirst("John"); // will return John Smith 
        firstNameIndex.getFirst("James"); // will return James Smith
        firstNameIndex.getFirst("Smith"); // will return Smith John
        
        lastNameIndex.get("Smith"); // will return John Smith and James Smith
        lastNameIndex.getFirst("John"); // will return Smith James

        firstOrLastNameIndex.get("Smith"); // will return John Smith, James Smith and Smith John
    }
    
    public static class Person {
        private String firstName;
        private String lastName;
        
        public Person(final String firstName, final String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
    }
}
```

## Compatibility
This library is compatible with Java 8 and above.

## License
[MIT License](http://www.opensource.org/licenses/mit-license.php)

Copyright 2017 JParams

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
