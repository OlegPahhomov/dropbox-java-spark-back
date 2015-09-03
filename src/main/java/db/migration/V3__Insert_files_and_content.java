package db.migration;

import files.crud.FileCrud;
import files.util.Calculator;
import files.util.FileUtil;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        FileCrud.insertToFile(filePs, file.getName(), FileUtil.getRatio(img));
        int file_id = FileUtil.getId(filePs);
        img = FileCrud.resizeIfNeeded(img, Calculator::needsPictureResize, Calculator::calculatePictureSize);
        FileCrud.fillToContent(contentPs, file_id, img, "picture");
        img = FileCrud.resizeIfNeeded(img, Calculator::needsThumbnailResize, Calculator::calculateThumbNailSize);
        FileCrud.fillToContent(contentPs, file_id, img, "thumbnail");
    }


}