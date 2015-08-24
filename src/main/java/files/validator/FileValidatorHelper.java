package files.validator;

import files.util.FileUtil;
import spark.Request;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;

public class FileValidatorHelper {

    public static boolean containsParts(Request request){
        try {
            if(FileUtil.getParts(request).isEmpty()) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean validContent(Request request){
        return onlyImages(request);
    }

    /**
     * All are non-null pictures
     */
    private static boolean onlyImages(Request request) {
        try {
            for (Part part : FileUtil.getParts(request)) {
                BufferedImage img = ImageIO.read(part.getInputStream());
                img.getWidth();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * There is a non-null integer id
     */
    public static boolean validId(Request request) {
        try {
            Integer id = Integer.valueOf(request.params(":id"));
            id = id + 1;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
