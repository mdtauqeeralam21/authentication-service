FROM openjdk:17

EXPOSE 9004

ADD /target/g2-authentication-service.jar g2-authentication-service.jar

ENTRYPOINT [ "java","-jar","/g2-authentication-service.jar"]