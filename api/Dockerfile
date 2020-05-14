FROM adoptopenjdk/openjdk11 as build
WORKDIR /app/api

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix mvnw && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN ./mvnw install -DskipTests -Pdocker
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

EXPOSE 8080

FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ARG DEPENDENCY=app/api/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.eirsteir.coffeewithme.CoffeeWithMeApplication"]