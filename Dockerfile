FROM openjdk:17

WORKDIR /usrapp/bin

ENV PORT=35000
ENV STATIC=/usrapp/bin/classes/webroot

EXPOSE 35000

COPY target/classes /usrapp/bin/classes

CMD ["java", "-cp", "./classes", "co.edu.escuelaing.httpserver.HttpServer", "co.edu.escuelaing.controller.GreetingController"]
