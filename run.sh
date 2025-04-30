#!/bin/bash

# Clear any existing compiled classes
mvn clean

# Run with dev profile and show SQL
mvn spring-boot:run -Dspring-boot.run.profiles=dev \
    -Dlogging.level.org.hibernate.SQL=DEBUG \
    -Dlogging.level.org.hibernate.type=TRACE