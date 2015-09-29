package files.resizer;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Resizer {

    public static BufferedImage getFancy(BufferedImage original) throws IOException {
        return resizeAndCrop(original, Calculator::needsFancyResize, Calculator::calculateFancySize);
    }

    public static BufferedImage getThumbnail(BufferedImage original) throws IOException {
        return resizeIfNeeded(original, Calculator::needsThumbnailResize, Calculator::calculateThumbNailSize);
    }

    public static BufferedImage getPicture(BufferedImage original) throws IOException {
        return resizeIfNeeded(original, Calculator::needsPictureResize, Calculator::calculatePictureSize);
    }

    static BufferedImage resizeAndCrop(BufferedImage img, Predicate<BufferedImage> whenToResize, BiFunction<Integer, Integer, Result> resizeCalculationFunc) {
        BufferedImage resized = resizeIfNeeded(img, whenToResize, resizeCalculationFunc);
        BufferedImage cropxyz = crop(resized);
        return cropxyz;
    }

    static BufferedImage resizeIfNeeded(BufferedImage img, Predicate<BufferedImage> whenToResize, BiFunction<Integer, Integer, Result> resizeCalculationFunc) {
        if (whenToResize.test(img)) return resize(img, resizeCalculationFunc);
        return img;
    }

    static BufferedImage resize(BufferedImage img, BiFunction<Integer, Integer, Result> resizeCalculationFunc) {
        Result result = resizeCalculationFunc.apply(img.getWidth(), img.getHeight());
        return Scalr.resize(img, Scalr.Method.QUALITY, result.width, result.height);
    }

    static BufferedImage crop(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        CropPrep prep = prepareForCrop(width, height);
        BufferedImage crop = Scalr.crop(img, prep.x, prep.y, prep.side, prep.side);
        crop.flush();
        return crop;
    }

    private static CropPrep prepareForCrop(int width, int height) {
        if (width >= height) {
            return cropPrep(width, height);
        }
        return cropPrep2(width, height);
    }

    private static CropPrep cropPrep(int width, int height) {
        int x = (width - height) / 2;
        int y = 0;
        int side = height;
        return new CropPrep(x, y, side);
    }

    private static CropPrep cropPrep2(int width, int height) {
        int x = 0;
        int y = (height - width) / 2;
        int side = width;
        return new CropPrep(x, y, side);
    }
}
