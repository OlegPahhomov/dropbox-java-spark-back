package files.crud;

import config.AppDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * CRUD*'s Reader only
 * Create, read, update, delete
 *
 * The idea is to minimise delegation.
 * Keep here only simple methods
 */
public class FileReader {
    static QueryRunner queryRunner = new QueryRunner();

    public static Object getPictures() throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "SELECT ID, NAME, RATIO FROM FILE", new MapListHandler());
        }
    }

    public static Object getPicture(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "select content from content where file_id = ? and type = 'picture'", new ScalarHandler<>(), id);
        }
    }

    public static Object getThumbnail(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "select content from content where file_id = ? and type = 'thumbnail'", new ScalarHandler<>(), id);
        }
    }
}
