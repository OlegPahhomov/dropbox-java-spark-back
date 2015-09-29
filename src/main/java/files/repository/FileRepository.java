package files.repository;

import files.dao.ContentDao;
import files.dao.FileDao;
import files.resizer.Resizer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class FileRepository {

    public static final String PICTURE = "picture";
    public static final String THUMBNAIL = "thumbnail";
    public static final String FANCY = "fancy";

    public static int saveOnePicture(Connection connection, BufferedImage image, String fileName) throws SQLException, IOException {
        int fileId = FileDao.saveFileRow(connection, image, fileName);
        ContentDao.saveContentRow(connection, fileId, PICTURE, Resizer.getPicture(image));
        ContentDao.saveContentRow(connection, fileId, THUMBNAIL, Resizer.getThumbnail(image));
        ContentDao.saveContentRow(connection, fileId, FANCY, Resizer.getFancy(image));
        return fileId;
    }

    public static void deleteOneFile(Connection connection, Long id) throws SQLException {
        ContentDao.deleteContentRowByFileId(connection, id);
        FileDao.deleteFileRow(connection, id);
    }
}
