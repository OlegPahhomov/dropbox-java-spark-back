package files.resizer;

import files.TestUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

public class ResizerTest {


    private static Result getMockCalculator(int width, int height){
        return Calculator.calculateDimensions(width, height, 300, 200);
    }

    /**
     * Drakensang picture dimensions are 900 x 450.
     * it will resize it to 300 x 200
     * 900/300 > 450/200, so it will resize by width
     * So width will be max, which is 300, 3x smaller.
     * Therefore height will be 450/3 = 150
     */
    @Test
    public void resizesImageCorrectly() throws Exception {
        BufferedImage image = TestUtil.getTestImage();
        BufferedImage resizedImage = Resizer.resize(image, ResizerTest::getMockCalculator);
        assertEquals(300, resizedImage.getWidth());
        assertEquals(150, resizedImage.getHeight());
    }

}