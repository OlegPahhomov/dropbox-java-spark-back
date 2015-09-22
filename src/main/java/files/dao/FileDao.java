package files.dao;

import config.AppDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class FileDao {
    static QueryRunner queryRunner = new QueryRunner();

    public static int saveFileRow(Connection connection, BufferedImage bufferedImageFrom, String fileName) throws IOException, SQLException {
        PreparedStatement filePs = connection.prepareStatement("INSERT INTO FILE(name, ratio) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        filePs.setString(1, fileName);
        filePs.setDouble(2, DaoUtil.getRatio(bufferedImageFrom));
        filePs.executeUpdate();
        return DaoUtil.getId(filePs);
    }

    public static int deleteFileRow(Connection connection, Long id) throws SQLException {
        PreparedStatement filePs = connection.prepareStatement("DELETE FROM FILE WHERE ID=?");
        filePs.setLong(1, id);
        return filePs.executeUpdate();
    }

    public static Object getFiles() throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "SELECT * FROM file", new MapListHandler());
        }
    }

    public static List<Map<String, Object>> getFile(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "SELECT * FROM file WHERE id=?", new MapListHandler(), id);
        }
    }
}
