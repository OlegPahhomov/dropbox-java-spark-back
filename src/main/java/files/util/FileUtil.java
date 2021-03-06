package files.util;

import spark.Request;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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


    public static BufferedImage createBufferedImageFrom(Part file) throws IOException {
        return createBufferedImageFrom(file.getInputStream());
    }

    private static BufferedImage createBufferedImageFrom(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    public static BufferedImage createBufferedImageFrom(byte[] content) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(content));
    }

}
