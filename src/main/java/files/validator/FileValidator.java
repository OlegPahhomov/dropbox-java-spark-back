package files.validator;

import spark.Request;

import static files.validator.FileValidatorHelper.*;

public class FileValidator {

    public static boolean invalidInsert(Request request) {
        return ! (containsParts(request) && validContent(request));
    }

    public static boolean invalidDelete(String idString) {
        return ! validId(idString);
    }

    public static boolean invalidGetById(String idString) {
        return ! validId(idString);
    }

}
