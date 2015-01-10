# What this is:

This is a minimal "Chat-Client" to allow offline testing and troubleshooting the javabot under development at http://github.com/Vincentyification/JavaBot

# What this is not:

A test suite. This is a minimal "implementation" to allow **manual** troubleshooting and testing the JavaBot, which will be referred to as "Junior" in the chat.

Issues for the JavaBot himself should be submitted to the JavaBot repository. 
Only issues concerning the TestClient will be accepted here.

# Setup:

This currently requires an existing and `mvn install`-ed JavaBot (instructions can be found in SETUP.MD) on your local machine to run, mostly since Junior is not accessible in any public maven repo yet.
It can be run simply by executing `mvn exec:java` to start up with the currently availably state of Junior

# Special Thanks:

- Codereview user [rolfl](http://codereview.stackexchange.com/users/31503/rolfl), for providing an excellent review and reimplementation of MessageTracker.java (see it's javadoc)