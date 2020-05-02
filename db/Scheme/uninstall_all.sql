\set AUTOCOMMIT off
\set ON_ERROR_STOP on

\echo Prohibit to create new connections to database: :'DB_NAME'
UPDATE pg_database SET datallowconn = 'false' WHERE datname = :'DB_NAME';

\echo Terminate all existing connections to database: :'DB_NAME'
SELECT pg_terminate_backend( pid ) FROM pg_stat_activity WHERE pid <> pg_backend_pid( ) AND datname = :'DB_NAME';

COMMIT;

\echo Drop Database
DROP DATABASE IF EXISTS :DB_NAME;

\echo Drop Tablespaces
DROP TABLESPACE IF EXISTS :MAIN_TABLESPACE;
DROP TABLESPACE IF EXISTS :INDX_TABLESPACE;

\echo Drop User
DROP ROLE IF EXISTS :SCHEME_USERNAME;

COMMIT;
