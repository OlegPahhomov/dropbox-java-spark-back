package files.service;

import com.google.gson.JsonObject;
import config.AppDataSource;
import files.dao.ContentDao;
import files.repository.FileRepository;
import files.util.FileUtil;
import spark.Request;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    public static void saveJsonFilesToDb(byte[] content, String name) throws IOException, SQLException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            FileRepository.saveOnePicture(connection, FileUtil.createBufferedImageFrom(content), name);
            connection.commit();
        }
    }

    public static void saveFilesToDb(Request request) throws IOException, ServletException, SQLException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            for (Part file : FileUtil.getParts(request)) {
                FileRepository.saveOnePicture(connection, FileUtil.createBufferedImageFrom(file), FileUtil.getFileName(file));
            }
            connection.commit();
        }
    }

    public static void deleteFileFromDb(String idString) throws SQLException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            Long id = Long.valueOf(idString);
            FileRepository.deleteOneFile(connection, id);
            connection.commit();
        }
    }

    public static Object getPicture(Long id) throws SQLException {
        return ContentDao.getPicture(id);
    }

    public static Object getThumbnail(Long id) throws SQLException {
        return ContentDao.getThumbnail(id);
    }
}
