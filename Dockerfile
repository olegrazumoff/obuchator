FROM dockerregistry:5000/base:latest

ARG WORKDIR=/opt
WORKDIR ${WORKDIR}
COPY . .
ENV PATH="${WORKDIR}:${PATH}"
RUN chmod +x gradlew.sh
RUN gradlew.sh bootJar
RUN java -jar build/libs/koroleva_anglii-1.0.jar
