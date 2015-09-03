package files.controller;

import files.service.FileService;
import files.validator.FileValidator;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.halt;

public class FileController {

    public static String addFile(Request request, Response response) throws IOException, ServletException, SQLException {
        setRequestMultiPartFile(request);
        if (FileValidator.invalidInsert(request)) return "failure";
        FileService.saveFilesToDb(request);
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
        return FileService.getPicture(idString);
    }

    public static Object getThumbnail(Request request, Response response) throws SQLException {
        String idString = request.params(":id");
        if (FileValidator.invalidGetById(idString)) halt();
        return FileService.getThumbnail(idString);
    }

    private static void setRequestMultiPartFile(Request request) {
        request.raw().setAttribute("org.eclipse.multipartConfig", new MultipartConfigElement("/tmp"));
    }
}
