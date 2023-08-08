export CLASSPATH=".:./Library/antlr-4.7.1-complete.jar:$CLASSPATH"
rm *.class
rm *.tokens
rm Smoola*.java
java -jar ./Library/antlr-4.7.1-complete.jar Smoola.g4
javac *.java
java org.antlr.v4.gui.TestRig Smoola program -gui < in.sml