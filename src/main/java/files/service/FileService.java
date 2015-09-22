package files.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import config.AppDataSource;
import files.dao.ContentDao;
import files.dao.FileDao;
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

    public static void saveJsonFilesToDb(JsonArray files) throws SQLException, IOException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            for (JsonElement file : files) {
                JsonObject object = file.getAsJsonObject();
                byte[] content = getContent(object);
                String name = getName(object);
                FileRepository.saveOnePicture(connection, FileUtil.createBufferedImageFrom(content), name);
            }
            connection.commit();
        }
    }

    public static int saveJsonFileToDb(JsonObject requestJson) throws IOException, SQLException {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            byte[] content = getContent(requestJson);
            String name = getName(requestJson);
            int i = FileRepository.saveOnePicture(connection, FileUtil.createBufferedImageFrom(content), name);
            connection.commit();
            return i;
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

    private static byte[] getContent(JsonObject requestJson) {
        String file = requestJson.get("file").getAsString();
        String contentString = file.substring(file.indexOf(',') + 1);
        return Base64.getDecoder().decode(contentString);
    }

    private static String getName(JsonObject requestJson) {
        return requestJson.get("name").getAsString();
    }
}
