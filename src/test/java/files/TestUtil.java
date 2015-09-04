package files;

import files.dao.ContentDao;
import files.dao.FileDao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class TestUtil {
    public static final String DRAKENSANG = "src/main/resources/db/pics/Drakensang.jpg";
    public static File cachedFile = null;

    public static int saveTestFile(Connection connection) throws IOException, SQLException {
        return FileDao.saveFileRow(connection, getTestImage(), getTestImageName());
    }

    public static int saveTestContent(Connection connection) throws IOException, SQLException {
        int fileId = saveTestFile(connection);
        ContentDao.saveContentRow(connection, fileId, "picture", getTestImage());
        return fileId;
    }

    public static BufferedImage getTestImage() throws IOException {
        return ImageIO.read(getFile());
    }

    public static String getTestImageName() throws IOException {
        return getFile().getName();
    }

    private static File getFile() {
        if (cachedFile == null) cachedFile = new File(DRAKENSANG);
        return cachedFile;
    }
}
