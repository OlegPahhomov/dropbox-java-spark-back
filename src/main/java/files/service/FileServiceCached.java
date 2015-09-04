package files.service;

import files.dao.ContentDao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FileServiceCached extends FileService{

    private static Map<Long, Object> pictureCache = new HashMap<>();
    private static Map<Long, Object> thumbnailCache = new HashMap<>();

    public static Object getPicture(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        if (pictureCache.containsKey(id)) return pictureCache.get(id);
        Object picture = FileService.getPicture(id);
        pictureCache.put(id, picture);
        return picture;
    }

    public static Object getThumbnail(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        if (thumbnailCache.containsKey(id)) return thumbnailCache.get(id);
        Object thumbnail = FileService.getThumbnail(id);
        thumbnailCache.put(id, thumbnail);
        return thumbnail;
    }
}
