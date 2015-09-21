package files.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import files.dao.FileDao;
import files.service.FileService;
import files.service.FileServiceCached;
import files.validator.FileValidator;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.halt;

public class FileController {

    private static JsonParser jsonParser = new JsonParser();

    public static String addFile(Request request, Response response) throws IOException, ServletException, SQLException {
        setRequestMultiPartFile(request);
        if (FileValidator.invalidInsert(request)) return "";
        FileService.saveFilesToDb(request);
        return "";
    }

    public static Object getFile(Request request, Response response) throws SQLException {
        String idString = request.params(":id");
        if (FileValidator.invalidGetById(idString)) return "";
        return FileDao.getPicture(Long.valueOf(idString));
    }

    public static String addJsonBulkFile(Request request, Response response) throws IOException, ServletException, SQLException {
        JsonObject requestJson = jsonParser.parse(request.body()).getAsJsonObject();
        JsonArray files = requestJson.get("files").getAsJsonArray();
        FileService.saveJsonFilesToDb(files);
        return "success";
    }


    public static String addJsonFile(Request request, Response response) throws IOException, ServletException, SQLException {
        JsonObject requestJson = jsonParser.parse(request.body()).getAsJsonObject();
        FileService.saveJsonFileToDb(requestJson);
        return "success";
    }

    public static String deleteFile(Request request, Response response) throws SQLException {
        String idString = request.params(":id");
        if (FileValidator.invalidDelete(idString)) return "failure";
        FileService.deleteFileFromDb(idString);
        return "success";
    }

    public static Object getPicture(Request request, Response response) throws SQLException {
        String idString = request.params(":id");
        if (FileValidator.invalidGetById(idString)) halt();
        return FileServiceCached.getPicture(idString);
    }

    public static Object getThumbnail(Request request, Response response) throws SQLException {
        String idString = request.params(":id");
        if (FileValidator.invalidGetById(idString)) halt();
        return FileServiceCached.getThumbnail(idString);
    }

    private static void setRequestMultiPartFile(Request request) {
        request.raw().setAttribute("org.eclipse.multipartConfig", new MultipartConfigElement("/tmp"));
    }


}
