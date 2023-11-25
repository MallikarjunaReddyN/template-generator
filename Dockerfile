FROM amazoncorretto:17.0.7-alpine

# Set the working directory
WORKDIR /app

# Install necessary packages
RUN apk add --update --no-cache python3=~3.10 && ln -sf python3 /usr/bin/python
RUN apk add git
RUN python3 -m ensurepip
RUN pip3 install --no-cache --upgrade pip setuptools
RUN pip3 install copier

EXPOSE 3662
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]