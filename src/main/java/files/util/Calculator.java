package files.util;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class Calculator {
    private static final int PICTURE_WIDTH_MAX = 1600;
    private static final int PICTURE_HEIGHT_MAX = 900;
    private static final int THUMBNAIL_WIDTH_MAX = 400;
    private static final int THUMBNAIL_HEIGHT_MAX = 300;

    private Calculator(){}

    public static BufferedImage resize(BufferedImage img, BiFunction<Integer, Integer, Result> calculateResize) {
        Result result = calculateResize.apply(img.getWidth(), img.getHeight());
        img = Scalr.resize(img, Scalr.Method.QUALITY, result.width, result.height);
        return img;
    }

    public static Result calculatePictureSize(int width, int height){
        return calculateDimensions(width, height, PICTURE_WIDTH_MAX, PICTURE_HEIGHT_MAX);
    }

    public static Result calculateThumbNailSize(int width, int height){
        return calculateDimensions(width, height, THUMBNAIL_WIDTH_MAX, THUMBNAIL_HEIGHT_MAX);
    }

    public static boolean needsPictureResize(BufferedImage image) {
        return needsResize(image.getWidth(), image.getHeight(), PICTURE_WIDTH_MAX, PICTURE_HEIGHT_MAX);
    }

    public static boolean needsThumbnailResize(BufferedImage image) {
        return needsResize(image.getWidth(), image.getHeight(), THUMBNAIL_WIDTH_MAX, THUMBNAIL_HEIGHT_MAX);
    }

    private static boolean needsResize(int width, int height, double widthMax, double heightMax) {
        return width > widthMax || height > heightMax;
    }

    private static Result calculateDimensions(int width, int height, int widthMax, int heightMax){
        double percentOfOriginal = maxResizePercent(width, height, widthMax, heightMax);
        width = adjust(width, percentOfOriginal);
        height = adjust(height, percentOfOriginal);
        return new Result(width, height);
    }

    private static int adjust(int param, double percentOfOriginal) {
        return (int) (percentOfOriginal * param);
    }

    private static double maxResizePercent(int width, int height, int widthMax, int heightMax) {
        return 1. - getNeededPercentChange(width, height, widthMax, heightMax);
    }

    private static double getNeededPercentChange(int width, int height, int widthMax, int heightMax) {
        double widthOverPercent = (double) (width - widthMax) / (width);
        double heightOverPercent = (double) (height - heightMax) / (height);
        if (heightOverPercent > widthOverPercent) return heightOverPercent;
        return widthOverPercent;
    }
}
