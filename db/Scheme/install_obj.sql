\echo Connecting to :SCHEME_USERNAME
\connect :DB_NAME :SCHEME_USERNAME

--Schemas
\i :SCHEME_PATH/Schemas/main_schema.sql

SET search_path = :SCHEME_USERNAME;

--Tables
\i :SCHEME_PATH/Tables/model.sql
