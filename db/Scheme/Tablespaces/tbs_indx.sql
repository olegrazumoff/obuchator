\echo Creating tablespace :INDX_TABLESPACE (:INDX_TABLESPACE_PATH)
\setenv PG_INDX_TABLESPACE_NAME :INDX_TABLESPACE_PATH
\! mkdir -p $PG_INDX_TABLESPACE_NAME
CREATE TABLESPACE :INDX_TABLESPACE LOCATION :'INDX_TABLESPACE_PATH';