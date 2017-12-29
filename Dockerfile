FROM openjdk:8-jre-alpine

COPY driver/target /usr/src/dostavca/driver

WORKDIR /usr/src/dostavca/driver

EXPOSE 8080

CMD ["java", "-server", "-cp", "classes:dependency/*", "com.kumuluz.ee.EeApplication"]