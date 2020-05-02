FROM dockerregistry:5000/postgres:11

ARG INIT_DIR=/docker-entrypoint-initdb.d

COPY sql/start.sql ${INIT_DIR}
COPY Scheme ${INIT_DIR}/Scheme
COPY sql/settings.sql ${INIT_DIR}/Scheme
