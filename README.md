# dropbox-java-spark-back

####### Goal
Creating a simple file upload and viewing platform using different languages and frameworks

####### Java Spark Back

To run:
* 1) You need front-end project, this is just API
* 2) You need Postgres database (or reconfig more in point 3)
* 3) change Appconfig.java and pom.xml database password (default postgres config)
* 4) run mvn flyway:migrate
* 5) start main in Application.java, spark runs on http://localhost:4567/
* 6) If you have front-end project, reconfigure serverConfig.js



####### In more detail

html - js/jQuery - css - microtemplate.js - fancybox - jquery validation

Simple frontend:
https://github.com/OlegPahhomov/dropbox-simple-front

This is Original joint project (discontinued because it's separated)
https://github.com/OlegPahhomov/dropbox-java