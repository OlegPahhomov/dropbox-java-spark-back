import com.google.gson.Gson;
import files.controller.FileController;
import files.dao.FileDao;
import spark.ResponseTransformer;

import java.sql.SQLException;

import static spark.Spark.*;

public class Application {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void main(String[] args) throws SQLException {
        port(8080);

        start();

        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.type("application/json");
        });

        after("/picture/:id", (request, response) -> response.type("image/jpg"));
        after("/picture/small/:id", (request, response) -> response.type("image/jpg"));
    }

    private static void start() throws SQLException {
        options("/*", (request, response) -> "*");
        get("/files", (request, response) -> FileDao.getFiles(), toJson);
        get("/picture/small/:id", FileController::getThumbnail);
        get("/picture/:id", FileController::getPicture);
        post("/add", FileController::addFile, toJson);
        post("/addjson", FileController::addJsonFile, toJson);
        post("/addjsonbulk", FileController::addJsonBulkFile, toJson);
        post("/remove/:id", FileController::deleteFile, toJson);
    }


}
