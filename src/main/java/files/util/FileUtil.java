package files.util;

import spark.Request;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {
    public static final String UNKNOWN = "unknown";
    public static final String FILENAME = "filename";


    /**
     * It is magical, I cast to List, (it's ArrayList),
     * all objects are <Part> yet cannot cast on the spot
     */
    public static List<Part> getParts(Request request) throws ServletException, IOException {
        return (List) request.raw().getParts();
    }

    /**
     * hack from the web, rewritten to java8
     */
    public static String getFileName(Part part) {
        String[] split = part.getHeader("content-disposition").split(";");
        return Stream.of(split)
                .filter(cd -> cd.trim().startsWith(FILENAME))
                .map(FileUtil::getFileName)
                .findFirst()
                .orElse(UNKNOWN);
    }

    /**
     * Raw is "filename="Mypic.jpg""
     * We need to return: Mypic.jpg
     */
    private static String getFileName(String rawHeader) {
        return rawHeader
                .substring(rawHeader.indexOf('=') + 1)
                .trim()
                .replace("\"", "");
    }

    public static double getRatio(BufferedImage img){
        return (double) img.getWidth() / (double) img.getHeight();
    }

    public static int getId(PreparedStatement filePs) throws SQLException {
        ResultSet generatedKeys = filePs.getGeneratedKeys();
        if (generatedKeys.next()){
            return generatedKeys.getInt(1);
        }
        return 0;
    }

    public static ByteArrayOutputStream getOutputStreamOf(BufferedImage img) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", os);
        return os;
    }
}
