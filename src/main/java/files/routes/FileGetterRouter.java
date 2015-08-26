package files.routes;

import com.google.gson.Gson;
import files.controller.FileController;
import spark.ResponseTransformer;

import static spark.Spark.get;

public class FileGetterRouter {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void start() {
        get("/files", FileController::getPictures, toJson);
        get("/picture/:id", FileController::getPicture);
    }
}
