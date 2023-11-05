FROM eclipse-temurin:17


WORKDIR /workspace/heeverse

COPY --chmod=755 ./build/libs/*.jar heeverse-api.jar
COPY ./script/deploy.sh .

RUN groupadd -g 1026 ubuntu
RUN useradd -r -u 1026 -g ubuntu ubuntu
RUN chown -R ubuntu:ubuntu /workspace
USER ubuntu

# ENV CLASSPATH /workspace/heeverse-api.jar:/workspace/heeverse-api-dependencies/*

# CMD ["java", "-cp", "$CLASSPATH", "com.heeverse.Main"]


ENTRYPOINT [ "sh", "deploy.sh", "-Dspring.profiles.active=${PROFILE}"]
