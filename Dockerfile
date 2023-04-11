FROM ubuntu:23.04 AS BUILD
RUN apt-get -y update && apt-get -y upgrade
RUN apt-get -y install openjdk-19-jdk
RUN mkdir /usr/app
COPY . /usr/app
RUN adduser gradle
RUN chown -R gradle:gradle /usr/app/
RUN rm -rf /home/gradle/.gradle && mkdir /home/gradle/.gradle
RUN chmod -R 755 /home/gradle/.gradle
USER gradle
RUN export GRADLE_USER_HOME=/home/gradle/.gradle/native
RUN cd /usr/app/ && sh ./gradlew clean build
RUN cd /usr/app/ && sh ./gradlew clean assemble

FROM ubuntu:23.04
ARG platform
ENV TOMCAT_VERSION 9.0.73
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV JAVA_HOME /usr/lib/jvm/java-19-openjdk-$platform
ENV APP_HOME=/usr/app/build/libs/businessbank-0.0.1-SNAPSHOT.war

RUN apt-get -y update && apt-get -y upgrade
RUN apt-get -y install openjdk-19-jdk wget
RUN mkdir $CATALINA_HOME
RUN wget https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz
RUN cd /tmp && tar xvfz tomcat.tar.gz
RUN cp -Rv /tmp/apache-tomcat-${TOMCAT_VERSION}/* $CATALINA_HOME
RUN rm -rf /tmp/apache-tomcat-${TOMCAT_VERSION}
RUN rm -rf /tmp/tomcat.tar.gz
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=BUILD $APP_HOME /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]