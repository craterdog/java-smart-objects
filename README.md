![Java Smart Objects](https://github.com/craterdog/java-smart-objects/blob/master/docs/images/einstein.jpg)

## Java Smart Objects
The `java.lang.Object` class defines a core set of methods that all classes should implement.
Requiring each developer to implement these methods in each of their classes is tedious and
error prone.  Often the implementation of one or more of those methods gets copied from class
to class and if there is a bug in it, the bug is replicated as well. The goal of this project
is to define a "smarter" `SmartObject<S extends SmartObject<S>>` class that implements the
`java.lang.Comparable<T>` interface and provides canonical versions of each of the following
methods:

 * `String toString()` (formatted as a 'censored' JSON string)
 * `S copy()` (a type-safe version of clone())
 * `boolean equals(Object object)`
 * `int compareTo(S object)`
 * `int hashCode()`

Annotations that are part of this project make it possible for developers to annotate
sensitive attributes like passwords or SS numbers so that when the `toString()` method
is called it will _mask_ out the values stored in those sensitive attributes. This ensures
that log files containing the smart objects don't expose the sensitive attributes.

## Highlighted Components
The following highlights the various components that are provided by this project:

 * *Censor* - knows how to apply a mask to a string to hide sensitive information
 * *Sensitive* - marks attributes that contain sensitive information and need to be masked
 * *SmartObject* - implements the canonical methods defined in the `java.lang.Object` class

## Quick Links
For more detail on this project click on the following links:

 * [javadocs](http://craterdog.github.io/java-smart-objects/latest/index.html)
 * [wiki](https://github.com/craterdog/java-smart-objects/wiki)
 * [release notes](https://github.com/craterdog/java-smart-objects/wiki/releases)
 * [website](http://craterdog.com)

## Getting Started
To get started using these utilities, include the following dependency in your maven pom.xml file:

```xml
    <dependency>
        <groupId>com.craterdog</groupId>
        <artifactId>java-smart-objects</artifactId>
        <version>x.y</version>
    </dependency>
```

The source code, javadocs and jar file artifacts for this project are available from the
*Maven Central Repository*. If your project doesn't currently use maven and you would like to,
click [here](https://github.com/craterdog/maven-parent-poms) to get started down that path quickly.

## Recognition
*Crater Dog Technologies™* would like to recognize and thank the following
companies for their contributions to the development and testing of various
components within this project:

 * *Blackhawk Network* (http://blackhawknetwork.com)
