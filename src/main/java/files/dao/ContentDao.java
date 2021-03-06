package files.dao;

import config.AppDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContentDao {
    static QueryRunner queryRunner = new QueryRunner();

    public static int saveContentRow(Connection connection, int fileId, String type, BufferedImage img) throws SQLException, IOException {
        PreparedStatement contentPs = connection.prepareStatement("INSERT INTO CONTENT(file_id, type, content) VALUES (?, ?, ?)");
        ByteArrayOutputStream stream = DaoUtil.getOutputStreamOf(img);
        contentPs.setInt(1, fileId);
        contentPs.setString(2, type);
        contentPs.setBinaryStream(3, new ByteArrayInputStream(stream.toByteArray()), stream.size());
        return contentPs.executeUpdate();
    }

    public static int deleteContentRowByFileId(Connection connection, Long id) throws SQLException {
        PreparedStatement contentPs = connection.prepareStatement("DELETE FROM CONTENT WHERE FILE_ID=?");
        contentPs.setLong(1, id);
        return contentPs.executeUpdate();
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

    public static Object getFancy(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return queryRunner.query(connection, "select content from content where file_id = ? and type = 'fancy'", new ScalarHandler<>(), id);
        }
    }
}
