# Maven Installing JavaBot

You will need the source code for javabot, as found under http://github.com/Vincentyification/JavaBot

After you pulled it to your machine, open CLI and navigate to the JavaBot root directory.
Run following command:

    mvn clean compile package install:install

All Tests in the JavaBot source folder need to pass for the build to succeed.
When the build succeeded it should finish with the output:

    [INFO] --- maven-install-plugin:2.4:install (default-cli) @ javabot ---
    [INFO] Installing C:\fakepath\JavaBot\target\javabot-0.0.1.jar to C:\Users\username\.m2\repository\com\gmail\inverseconduit\javabot\0.0.1\javabot-0.0.1.jar
    [INFO] Installing C:\fakepath\JavaBot\pom.xml to C:\Users\username\.m2\repository\com\gmail\inverseconduit\javabot\0.0.1\javabot-0.0.1.pom
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 9.100 s
    [INFO] Finished at: 2015-01-06T16:13:08+01:00
    [INFO] Final Memory: 27M/174M
    [INFO] ------------------------------------------------------------------------