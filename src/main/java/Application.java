import com.google.gson.Gson;
import files.controller.FileController;
import spark.ResponseTransformer;

import static spark.Spark.*;

public class Application {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void main(String[] args) {
        port(8080);

        get("/files", FileController::getPictures, toJson);
        get("/picture/:id", FileController::getPicture);
        post("/add", FileController::addFile, toJson);
        post("/remove/:id", FileController::deleteFile, toJson);

        after((request, response) -> {
            // For security reasons do not forget to change "*" to url
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        after("/picture/:id", (request, response) -> response.type("image/jpeg"));

    }


}
