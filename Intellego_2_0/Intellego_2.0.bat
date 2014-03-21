title Intellego_2_0
color 8

start util\intro

DEL *.class

DEL hybrid\*.class
DEL interfaces\*.class
DEL main\*.class
DEL real\*.class
DEL simworldobjects\*.class
DEL simworlds\*.class
DEL util\*.class

set CLASSPATH=.
set RCXTTY=COM1

CALL util\setup

javac intellego\*.java
javac simworlds\*.java


lejosc interfaces\Controller.java
lejosc interfaces\AbstractRobot.java

cls

java intellego.Intellego