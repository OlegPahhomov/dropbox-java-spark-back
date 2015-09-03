package db.migration;

import org.apache.commons.dbutils.QueryRunner;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V2__Add_content_table implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        new QueryRunner().update(connection,
                "CREATE TABLE content (\n" +
                        "  id      BIGSERIAL PRIMARY KEY                  NOT NULL,\n" +
                        "  file_id BIGINT REFERENCES FILE(id)                NOT NULL,\n" +
                        "  type    TEXT CHECK (type <> '')                NOT NULL,\n" +
                        "  content BYTEA                                  NOT NULL\n" +
                        ");"
        );
    }
}
