package files.crud;

import config.AppDataSource;
import files.util.Calculator;
import files.util.FileUtil;
import files.util.Result;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * CRUD* without R (Read)
 * Create, read, update, delete
 */
public class FileCrud {

    public static void saveOneFile(Part file) throws SQLException, IOException {
        try (Connection connection = AppDataSource.getTransactConnection();
             PreparedStatement filePs = connection.prepareStatement("INSERT INTO FILE(name, ratio) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement contentPs = connection.prepareStatement("INSERT INTO CONTENT(file_id, type, content) VALUES (?, ?, ?)")) {
            insertFileAndContent(filePs, contentPs, file);
            connection.commit();
        }
    }

    public static void deleteOneFile(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getTransactConnection();
             PreparedStatement contentPs = connection.prepareStatement("DELETE FROM CONTENT WHERE FILE_ID=?");
             PreparedStatement filePs = connection.prepareStatement("DELETE FROM FILE WHERE ID=?")) {
            deleteContentAndFile(id, contentPs, filePs);
            connection.commit();
        }
    }

    private static void deleteContentAndFile(Long id, PreparedStatement contentPs, PreparedStatement filePs) throws SQLException {
        contentPs.setLong(1, id);
        contentPs.executeUpdate();
        filePs.setLong(1, id);
        filePs.executeUpdate();
    }

    private static void insertFileAndContent(PreparedStatement filePs, PreparedStatement contentPs, Part part) throws IOException, SQLException {
        BufferedImage img = ImageIO.read(part.getInputStream());
        String fileName = FileUtil.getFileName(part);
        insertToFile(filePs, fileName, FileUtil.getRatio(img));
        int file_id = FileUtil.getId(filePs);
        img = resizeIfNeeded(img, Calculator::needsPictureResize, Calculator::calculatePictureSize);
        fillToContent(contentPs, file_id, img);
        img = resizeIfNeeded(img, Calculator::needsThumbnailResize, Calculator::calculateThumbNailSize);
        fillToContent(contentPs, file_id, img);
    }

    private static void fillToContent(PreparedStatement contentPs, int file_id, BufferedImage img) throws IOException, SQLException {
        ByteArrayOutputStream stream = FileUtil.getOutputStreamOf(img);
        contentPs.setInt(1, file_id);
        contentPs.setString(2, "picture");
        contentPs.setBinaryStream(3, new ByteArrayInputStream(stream.toByteArray()), stream.size());
        contentPs.executeUpdate();
    }

    public static void insertToFile(PreparedStatement filePs, String fileName, Double ratio) throws SQLException {
        filePs.setString(1, fileName);
        filePs.setDouble(2, ratio);
        filePs.executeUpdate();
    }

    public static void insertToContent(PreparedStatement contentPs, int fileId, BufferedImage img, Predicate<BufferedImage> checker, BiFunction<Integer, Integer, Result> functionCalc, String type) throws IOException, SQLException {
        img = resizeIfNeeded(img, checker, functionCalc);
        ByteArrayOutputStream stream = FileUtil.getOutputStreamOf(img);
        contentPs.setInt(1, fileId);
        contentPs.setString(2, type);
        contentPs.setBinaryStream(3, new ByteArrayInputStream(stream.toByteArray()), stream.size());
        contentPs.executeUpdate();
    }

    private static BufferedImage resizeIfNeeded(BufferedImage img, Predicate<BufferedImage> checker, BiFunction<Integer, Integer, Result> functionCalc) {
        if (checker.test(img)) img = Calculator.resize(img, functionCalc);
        return img;
    }

}
