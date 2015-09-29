package db.migration;

import files.dao.ContentDao;
import files.dao.FileDao;
import files.resizer.Resizer;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class V3__Insert_files_and_content implements JdbcMigration {

    public static final String CUTE_KITTENS = "src/main/resources/db/pics/Cute-Kittens.jpg";
    public static final String TERMINATOR = "src/main/resources/db/pics/Terminator.jpeg";
    public static final String HOME = "src/main/resources/db/pics/Home.jpeg";
    public static final String DRAKENSANG = "src/main/resources/db/pics/Drakensang.jpg";
    public static final String PICTURE = "picture";
    public static final String THUMBNAIL = "thumbnail";
    public static final String FANCY = "fancy";


    @Override
    public void migrate(Connection connection) throws Exception {
        insertToDb(connection, CUTE_KITTENS);
        insertToDb(connection, TERMINATOR);
        insertToDb(connection, HOME);
        insertToDb(connection, DRAKENSANG);
    }

    void insertToDb(Connection connection, String fileLocation) throws IOException, SQLException {
        File file = new File(fileLocation);
        BufferedImage image = ImageIO.read(file);

        int fileId = FileDao.saveFileRow(connection, image, file.getName());
        ContentDao.saveContentRow(connection, fileId, PICTURE, Resizer.getPicture(image));
        ContentDao.saveContentRow(connection, fileId, THUMBNAIL, Resizer.getThumbnail(image));
        ContentDao.saveContentRow(connection, fileId, FANCY, Resizer.getFancy(image));
    }
}