This is a sample application for a POC. 

Feel free to clone and change as real case specs.

# Hint: how I get the Oracle jdbc driver
downladed oracle jdbc driver from Oracle website, then installed it in 
local maven repo using:

mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc8 \
     -Dversion=10.2.0.3.0 -Dpackaging=jar -Dfile=ojdbc8.jar -DgeneratePom=true