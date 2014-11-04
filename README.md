# Java Smart Objects

## In a Nut Shell
This project contains various Java components that can be used to define classes that automatically
implement the following canonical methods from the `java.lang.Object` class:

 * `toString` (as JSON)
 * `equals`
 * `compareTo`
 * `hashCode`

## Highlighted Components
The following highlights the various components that are provided by this project:

 * *Censor* - knows how to apply a mask to a string to hide sensitive information
 * *Sensitive* - marks attributes that contain sensitive information and need to be masked
 * *SmartObject* - implements the canonical methods defined in the `java.lang.Object` class

## Quick Links
For more detail on this project click on the following links:

 * [javadocs](http://craterdog.github.io/java-smart-objects/3.0/index.html)
 * [wiki](https://github.com/craterdog/java-smart-objects/wiki)
 * [release notes](https://github.com/craterdog/java-smart-objects/wiki/Release-Notes)
 * [website](http://craterdog.com)

## Getting Started
To get started using these utilities, include the following dependency in your maven pom.xml file:

```xml
    <dependency>
        <groupId>com.craterdog</groupId>
        <artifactId>java-smart-objects</artifactId>
        <version>3.0</version>
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

