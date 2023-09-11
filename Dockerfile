FROM eclipse-temurin:17


ARG WORKSPACE=/workspace/heeverse

RUN mkdir -p ${WORKSPACE}

COPY ./build/libs/*.jar ${WORKSPACE}/heeverse-api.jar
COPY ./script/deploy.sh ${WORKSPACE}


USER root

ENTRYPOINT ["sh", "/workspace/heeverse/deploy.sh"]