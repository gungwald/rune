#!/bin/sh
JAVA_VERSION=1.4
APPLICATION_JAR=hreodwrit.jar
javac -d target/classes -sourcepath src/main/java \
    -source $JAVA_VERSION -target $JAVA_VERSION \
    src/main/java/com/alteredmechanism/notepad/*.java \
    src/main/java/com/alteredmechanism/java/awt/*.java \
    src/main/java/com/alteredmechanism/javax/swing/*.java || exit $?
jar -cvf target/$APPLICATION_JAR -C target/classes . || exit $?
jar -uvf target/$APPLICATION_JAR -C src/main/resources . || exit $?
# Still need to create correct manifest for runnable jar

