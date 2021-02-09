FROM egovio/8-openjdk-alpine

MAINTAINER Senthil<senthil.kalimuthu@tarento.com>


# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"
COPY /target/bookings-0.0.2-SNAPSHOT.jar  /opt/egov/bookings-0.0.2-SNAPSHOT.jar

COPY start.sh /opt/egov/

RUN chmod +x /opt/egov/start.sh

CMD ["/opt/egov/start.sh"]

# NOTE: the two 'RUN' commands can probably be combined inside of a single
# script (i.e. RUN build-and-install-app.sh) so that we can also clean up the
# extra files created during the `mvn package' command. that step inflates the
# resultant image by almost 1.0GB.
