FROM eclipse-temurin:17


WORKDIR /workspace/heeverse

COPY --chmod=755 ./build/libs/*.jar heeverse-api.jar
COPY ./script/deploy.sh .

RUN groupadd -g 1026 heeverse
RUN useradd -r -u 1026 -g heeverse appuser
RUN chown -R appuser:heeverse /workspace
USER appuser

ENTRYPOINT [ "sh", "deploy.sh", "-Dspring.profiles.active=${PROFILE}"]