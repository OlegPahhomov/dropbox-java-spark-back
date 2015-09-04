package files.resizer;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalculatorTest {
    public static final int WIDTH_MAX = 600;
    public static final int HEIGHT_MAX = 400;
    public static final int WIDTH_SMALLER_BY_100 = WIDTH_MAX - 100;
    public static final int HEIGHT_SMALLER_BY_100 = HEIGHT_MAX - 100;
    public static final int WIDTH_BIGGER_BY_300 = WIDTH_MAX + 300;
    public static final int HEIGHT_BIGGER_BY_100 = HEIGHT_MAX + 100;

    @Test
    public void doesntNeedResizeBecauseSmaller() throws Exception{
        assertFalse(Calculator.needsResize(WIDTH_SMALLER_BY_100, HEIGHT_SMALLER_BY_100, WIDTH_MAX, HEIGHT_MAX));
    }
    @Test
    public void doesntNeedResizeBecauseEqual() throws Exception{
        assertFalse(Calculator.needsResize(WIDTH_MAX, HEIGHT_MAX, WIDTH_MAX, HEIGHT_MAX));
    }
    @Test
    public void needsResizeBecauseWidthBigger() throws Exception{
        assertTrue(Calculator.needsResize(WIDTH_BIGGER_BY_300, HEIGHT_MAX, WIDTH_MAX, HEIGHT_MAX));
    }
    @Test
    public void needsResizeBecauseHeightBigger() throws Exception{
        assertTrue(Calculator.needsResize(WIDTH_MAX, HEIGHT_BIGGER_BY_100, WIDTH_MAX, HEIGHT_MAX));
    }

    @Test
    public void shouldResizeHeightOrWidthToItsMaximum() throws Exception {
        int testHeight = HEIGHT_BIGGER_BY_100;
        int testWidth = WIDTH_BIGGER_BY_300;
        double actual = Calculator.maxResizePercent(testWidth, testHeight, WIDTH_MAX, HEIGHT_MAX);
        int resizedWidth = (int) (actual * testWidth);
        int resizedHeight = (int) (actual * testHeight);
        assertTrue(WIDTH_MAX == resizedWidth || HEIGHT_MAX == resizedHeight);
    }

    @Test
    public void shouldResizeHeightAndWidthSmallerOrEqualsToMax() throws Exception {
        int testHeight = HEIGHT_BIGGER_BY_100;
        int testWidth = WIDTH_BIGGER_BY_300;
        double actual = Calculator.maxResizePercent(testWidth, testHeight, WIDTH_MAX, HEIGHT_MAX);
        int resizedWidth = (int) (actual * testWidth);
        int resizedHeight = (int) (actual * testHeight);
        assertTrue(WIDTH_MAX >= resizedWidth);
        assertTrue(HEIGHT_MAX >= resizedHeight);
    }


}