import com.google.gson.Gson;
import files.controller.FileController;
import files.crud.FileReader;
import spark.ResponseTransformer;

import java.sql.SQLException;

import static spark.Spark.*;

public class Application {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void main(String[] args) throws SQLException {
        port(8080);

        start();

        after((request, response) -> {// For security reasons do not forget to change "*" to url
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        after("/picture/:id", (request, response) -> response.type("image/jpg"));
    }

    private static void start() throws SQLException {
        get("/files", (request, response) -> FileReader.getPictures(), toJson);
        get("/picture/:id", FileController::getPicture);
        post("/add", FileController::addFile, toJson);
        post("/remove/:id", FileController::deleteFile, toJson);
    }


}
