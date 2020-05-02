\set ENTRYPOINT /docker-entrypoint-initdb.d/Scheme
\set PGDATA `echo $PGDATA`

\i :ENTRYPOINT/settings.sql
\i :ENTRYPOINT/install.sql
