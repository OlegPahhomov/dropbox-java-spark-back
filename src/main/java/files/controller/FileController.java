package files.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import files.service.FileService;
import files.service.FileServiceCached;
import files.validator.FileValidator;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

import static spark.Spark.halt;

public class FileController {

    private static JsonParser jsonParser = new JsonParser();

    public static String addFile(Request request, Response response) throws IOException, ServletException, SQLException {
        setRequestMultiPartFile(request);
        if (FileValidator.invalidInsert(request)) return "";
        FileService.saveFilesToDb(request);
        return "";
    }

    public static String addJsonFile(Request request, Response response) throws IOException, ServletException, SQLException {
        JsonObject requestJson = jsonParser.parse(request.body()).getAsJsonObject();
        byte[] content = getContent(requestJson);
        String name = getName(requestJson);
        FileService.saveJsonFilesToDb(content, name);
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

    private static byte[] getContent(JsonObject requestJson) {
        String file = requestJson.get("file").getAsString();
        String contentString = file.substring(file.indexOf(',') + 1);
        return Base64.getDecoder().decode(contentString);
    }

    private static String getName(JsonObject requestJson) {
        return requestJson.get("name").getAsString();
    }
}
