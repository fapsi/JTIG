JTIG
====

Java Tree Insertion Grammar


Build - Information
====

1. Installing 3rd party JARs into local maven repository
Navigate to project root folder. And run:
mvn install:install-file -Dfile=lib/morphadorner.jar -DgroupId=edu.northwestern.at -DartifactId=Morphadorner -Dversion=2.0 -Dpackaging=jar
See Also: http://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

2. Build package via maven
mvn package
