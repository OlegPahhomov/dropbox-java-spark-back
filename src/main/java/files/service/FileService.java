package files.service;

import files.crud.FileCrud;
import files.crud.FileReader;
import files.util.FileUtil;
import spark.Request;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    private static Map<Long, Object> pictureCache = new HashMap<>();
    private static Map<Long, Object> thumbnailCache = new HashMap<>();

    public static void saveFilesToDb(Request request) throws IOException, ServletException, SQLException {
        for (Part file : FileUtil.getParts(request)){
            FileCrud.saveOneFile(file);
        }
        //parts.forEach(FileCrud::saveOneFile); can't do this if exceptions are unfixed
    }

    public static void deleteFileFromDb(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        FileCrud.deleteOneFile(id);
    }

    public static Object getPicture(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        if (pictureCache.containsKey(id)) return pictureCache.get(id);
        Object picture = FileReader.getPicture(id);
        pictureCache.put(id, picture);
        return picture;
    }

    public static Object getThumbnail(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        if (thumbnailCache.containsKey(id)) return thumbnailCache.get(id);
        Object thumbnail = FileReader.getThumbnail(id);
        thumbnailCache.put(id, thumbnail);
        return thumbnail;
    }
}
