\set AUTOCOMMIT off
\set ON_ERROR_STOP on

\echo
\echo Install tablespaces, user and database
\i :SCHEME_PATH/install_user.sql

\echo
\echo Install Objects
\i :SCHEME_PATH/install_obj.sql

\echo
\echo Schema objects installation is finished
COMMIT;
