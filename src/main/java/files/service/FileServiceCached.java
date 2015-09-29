package files.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FileServiceCached{

    private static Map<Long, Object> pictureCache = new HashMap<>();
    private static Map<Long, Object> thumbnailCache = new HashMap<>();
    private static Map<Long, Object> fancyCache = new HashMap<>();

    public static Object getPicture(String idString) throws SQLException {
        return getCachedContent(idString, pictureCache, FileService::getPicture);
    }

    public static Object getThumbnail(String idString) {
        return getCachedContent(idString, thumbnailCache, FileService::getThumbnail);
    }

    public static Object getFancy(String idString) throws SQLException {
        return getCachedContent(idString, fancyCache, FileService::getFancy);
    }

    private static Object getCachedContent(String idString, Map<Long, Object> cache, Function<Long, Object> serviceFunction){
        Long id = Long.valueOf(idString);
        if (cache.containsKey(id)) return cache.get(id);
        Object thumbnail = serviceFunction.apply(id);
        cache.put(id, thumbnail);
        return thumbnail;
    }
}
