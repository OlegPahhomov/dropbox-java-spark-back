import com.google.gson.Gson;
import files.controller.FileController;
import files.routes.FileGetterRouter;
import files.routes.FileManipulatorRouter;
import spark.*;

import static spark.Spark.*;
import static spark.SparkBase.staticFileLocation;

public class Application {

    static ResponseTransformer toJson = new Gson()::toJson;

    public static void main(String[] args) {
        //port(8080); if you don't like default

        FileGetterRouter.start();
        FileManipulatorRouter.start();

        after((request, response) -> {
            // For security reasons do not forget to change "*" to url
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        after("/picture/:id", (request, response) -> response.type("image/jpeg"));

    }


}
