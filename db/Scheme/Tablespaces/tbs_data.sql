\echo Creating tablespace :MAIN_TABLESPACE (:DATA_TABLESPACE_PATH)
\setenv PG_DATA_TABLESPACE_NAME :DATA_TABLESPACE_PATH
\! mkdir -p $PG_DATA_TABLESPACE_NAME
CREATE TABLESPACE :MAIN_TABLESPACE LOCATION :'DATA_TABLESPACE_PATH';