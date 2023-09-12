FROM eclipse-temurin:17


RUN groupadd -g 1026 heeverse
RUN useradd -r -u 1026 -g heeverse appuser
RUN chown -R :heeverse /tmp

USER appuser

ARG WORKSPACE=/tmp/workspace/heeverse
RUN mkdir -p -m 755 ${WORKSPACE}


COPY --chmod=755 ./build/libs/*.jar ${WORKSPACE}/heeverse-api.jar
COPY ./script/deploy.sh ${WORKSPACE}


ENTRYPOINT ["sh", "tmp/workspace/heeverse/deploy.sh"]