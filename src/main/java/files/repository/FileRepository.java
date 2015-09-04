package files.repository;

import config.AppDataSource;
import files.dao.ContentDao;
import files.dao.FileDao;
import files.resizer.Resizer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * CRUD* without R (Read)
 * Create, read, update, delete
 */
public class FileRepository {

    public static final String PICTURE = "picture";
    public static final String THUMBNAIL = "thumbnail";

    public static void saveOnePicture(BufferedImage image, String fileName) throws SQLException, IOException {
        try (Connection connection = AppDataSource.getTransactConnection()) {

            int fileId = FileDao.saveFileRow(connection, image, fileName);
            ContentDao.saveContentRow(connection, fileId, PICTURE, Resizer.getPicture(image));
            ContentDao.saveContentRow(connection, fileId, THUMBNAIL, Resizer.getThumbnail(image));

            connection.commit();
        }
    }

    public static void deleteOneFile(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            ContentDao.deleteContentRowByFileId(connection, id);
            FileDao.deleteFileRow(connection, id);
            connection.commit();
        }
    }

}
