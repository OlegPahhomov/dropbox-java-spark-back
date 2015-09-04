package files.resizer;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Resizer {

    public static BufferedImage getThumbnail(BufferedImage original) throws IOException {
        return resizeIfNeeded(original, Calculator::needsThumbnailResize, Calculator::calculateThumbNailSize);
    }

    public static BufferedImage getPicture(BufferedImage original) throws IOException {
        return resizeIfNeeded(original, Calculator::needsPictureResize, Calculator::calculatePictureSize);
    }

    public static BufferedImage resizeIfNeeded(BufferedImage img, Predicate<BufferedImage> whenToResize, BiFunction<Integer, Integer, Result> resizeCalculationFunc) {
        if (whenToResize.test(img)) return resize(img, resizeCalculationFunc);
        return img;
    }

    static BufferedImage resize(BufferedImage img, BiFunction<Integer, Integer, Result> resizeCalculationFunc) {
        Result result = resizeCalculationFunc.apply(img.getWidth(), img.getHeight());
        return Scalr.resize(img, Scalr.Method.QUALITY, result.width, result.height);
    }
}
