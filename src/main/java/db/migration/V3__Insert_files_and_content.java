package db.migration;

import files.dao.DaoUtil;
import files.resizer.Calculator;
import files.resizer.Resizer;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class V3__Insert_files_and_content implements JdbcMigration {

    public static final String CUTE_KITTENS = "src/main/resources/db/pics/Cute-Kittens.jpg";
    public static final String TERMINATOR = "src/main/resources/db/pics/Terminator.jpeg";
    public static final String HOME = "src/main/resources/db/pics/Home.jpeg";
    public static final String DRAKENSANG = "src/main/resources/db/pics/Drakensang.jpg";


    @Override
    public void migrate(Connection connection) throws Exception {
        try (PreparedStatement filePs = connection.prepareStatement("INSERT INTO FILE(name, ratio) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement contentPs = connection.prepareStatement("INSERT INTO CONTENT(file_id, type, content) VALUES (?, ?, ?)")) {
            insertToDb(filePs, contentPs, CUTE_KITTENS);
            insertToDb(filePs, contentPs, TERMINATOR);
            insertToDb(filePs, contentPs, HOME);
            insertToDb(filePs, contentPs, DRAKENSANG);
        }
    }

    private void insertToDb(PreparedStatement filePs, PreparedStatement contentPs, String fileLocation) throws IOException, SQLException {
        File file = new File(fileLocation);
        BufferedImage img = ImageIO.read(file);
        insertToFile(filePs, file.getName(), DaoUtil.getRatio(img));
        int file_id = DaoUtil.getId(filePs);
        img = Resizer.resizeIfNeeded(img, Calculator::needsPictureResize, Calculator::calculatePictureSize);
        fillToContent(contentPs, file_id, img, "picture");
        img = Resizer.resizeIfNeeded(img, Calculator::needsThumbnailResize, Calculator::calculateThumbNailSize);
        fillToContent(contentPs, file_id, img, "thumbnail");
    }


    public static void fillToContent(PreparedStatement contentPs, int file_id, BufferedImage img, String type) throws IOException, SQLException {
        ByteArrayOutputStream stream = DaoUtil.getOutputStreamOf(img);
        contentPs.setInt(1, file_id);
        contentPs.setString(2, type);
        contentPs.setBinaryStream(3, new ByteArrayInputStream(stream.toByteArray()), stream.size());
        contentPs.executeUpdate();
    }

    public static void insertToFile(PreparedStatement filePs, String fileName, Double ratio) throws SQLException {
        filePs.setString(1, fileName);
        filePs.setDouble(2, ratio);
        filePs.executeUpdate();
    }

}