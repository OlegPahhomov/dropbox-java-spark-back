package files.resizer;

import java.awt.image.BufferedImage;

public class Calculator {
    private static final int PICTURE_WIDTH_MAX = 1600;
    private static final int PICTURE_HEIGHT_MAX = 900;
    private static final int FANCY_WIDTH_MAX, FANCY_HEIGHT_MAX;
    static { FANCY_HEIGHT_MAX = FANCY_WIDTH_MAX = 500;}
    private static final int THUMBNAIL_WIDTH_MAX = 400;
    private static final int THUMBNAIL_HEIGHT_MAX = 300;

    private Calculator(){}

    public static Result calculatePictureSize(int width, int height){
        return calculateDimensions(width, height, PICTURE_WIDTH_MAX, PICTURE_HEIGHT_MAX);
    }

    public static Result calculateThumbNailSize(int width, int height){
        return calculateDimensions(width, height, THUMBNAIL_WIDTH_MAX, THUMBNAIL_HEIGHT_MAX);
    }

    public static Result calculateFancySize(int width, int height){
        return calculateDimensions(width, height, FANCY_WIDTH_MAX, FANCY_HEIGHT_MAX);
    }

    public static boolean needsPictureResize(BufferedImage image) {
        return needsResize(image.getWidth(), image.getHeight(), PICTURE_WIDTH_MAX, PICTURE_HEIGHT_MAX);
    }

    public static boolean needsThumbnailResize(BufferedImage image) {
        return needsResize(image.getWidth(), image.getHeight(), THUMBNAIL_WIDTH_MAX, THUMBNAIL_HEIGHT_MAX);
    }

    public static boolean needsFancyResize(BufferedImage image) {
        return needsResize(image.getWidth(), image.getHeight(), FANCY_WIDTH_MAX, FANCY_HEIGHT_MAX);
    }

    static boolean needsResize(int width, int height, double widthMax, double heightMax) {
        return width > widthMax || height > heightMax;
    }

    static Result calculateDimensions(int width, int height, int widthMax, int heightMax){
        double percentOfOriginal = maxResizePercent(width, height, widthMax, heightMax);
        int newWidth = (int) (percentOfOriginal * width);
        int newHeight = (int) (percentOfOriginal * height);
        return new Result(newWidth, newHeight);
    }

    static double maxResizePercent(int width, int height, int widthMax, int heightMax) {
        return 1. - getNeededPercentChange(width, height, widthMax, heightMax);
    }

    private static double getNeededPercentChange(int width, int height, int widthMax, int heightMax) {
        double widthOverPercent = (double) (width - widthMax) / (width);
        double heightOverPercent = (double) (height - heightMax) / (height);
        if (heightOverPercent > widthOverPercent) return heightOverPercent;
        return widthOverPercent;
    }
}
