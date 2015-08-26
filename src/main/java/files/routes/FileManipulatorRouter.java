package files.routes;

import com.google.gson.Gson;
import files.controller.FileController;
import spark.ResponseTransformer;

import static spark.Spark.post;

public class FileManipulatorRouter {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void start() {

        post("/add", FileController::addFile, toJson);
        post("/remove/:id", FileController::deleteFile, toJson);
    }
}
