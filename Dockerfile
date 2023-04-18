FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY . .

RUN ./gradlew bootJar -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build /workspace/app/build /app/target

EXPOSE 8080

ENTRYPOINT ["java", "-Dcom.amazonaws.sdk.disableEc2Metadata=true", "-Dspring.profiles.active=dev", "-cp", "app:app/lib/*","com.kihong.health.HealthApplication"]