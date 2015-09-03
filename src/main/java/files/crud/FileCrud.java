package files.crud;

import config.AppDataSource;
import files.util.FileResizer;
import files.util.FileUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * CRUD* without R (Read)
 * Create, read, update, delete
 */
public class FileCrud {

    public static void saveOneFile(Part file) throws SQLException, IOException {
        try (Connection connection = AppDataSource.getTransactConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO FILE(name, content, image_width, image_height) VALUES (?, ?, ?, ?)")) {
            resizePictureIfNeededAndFillPS(ps, file);
            connection.commit();
        }
    }

    public static void deleteOneFile(Long id) throws SQLException {
        try (Connection connection = AppDataSource.getTransactConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM FILE WHERE ID=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            connection.commit();
        }
    }

    private static void resizePictureIfNeededAndFillPS(PreparedStatement ps, Part part) throws IOException, SQLException {
        BufferedImage img = ImageIO.read(part.getInputStream());
        String fileName = FileUtil.getFileName(part);
        if (FileResizer.needsResize(img)) {
            BufferedImage resizedImage = FileResizer.dynamicResize(img);
            ByteArrayOutputStream os = FileResizer.getByteArrayOutputStream(resizedImage);
            fillPsAndExecute(ps, fileName, resizedImage, new ByteArrayInputStream(os.toByteArray()), os.size());
        } else {
            fillPsAndExecute(ps, fileName, img, part.getInputStream(), (int) part.getSize());
        }
    }

    private static void fillPsAndExecute(PreparedStatement ps, String fileName, BufferedImage img, InputStream inputStream, int size) throws SQLException {
        ps.setString(1, fileName);
        ps.setBinaryStream(2, inputStream, size);
        ps.setInt(3, img.getWidth());
        ps.setInt(4, img.getHeight());
        ps.executeUpdate();
    }


}
