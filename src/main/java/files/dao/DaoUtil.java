package files.dao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoUtil {

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
