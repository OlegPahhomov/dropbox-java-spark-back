package files.service;

import files.crud.FileCrud;
import files.util.FileUtil;
import spark.Request;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

public class FileService {


    public static void saveFilesToDb(Request request) throws IOException, ServletException, SQLException {
        for (Part file : FileUtil.getParts(request)){
            FileCrud.saveOneFile(file);
        }
        //parts.forEach(FileCrud::saveOneFile); can't do this if exceptions are unfixed
    }

    public static void deleteFileFromDb(String idString) throws SQLException {
        Long id = Long.valueOf(idString);
        FileCrud.deleteOneFile(id);
    }


}
