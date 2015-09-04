package files.repository;

import config.AppDataSource;
import files.TestUtil;
import org.junit.Test;

import java.sql.Connection;

import static files.TestUtil.*;

public class FileRepositoryTest {

    @Test
    public void testSaveOnePicture() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            FileRepository.saveOnePicture(connection, getTestImage(), getTestImageName());
            connection.rollback();
        }
    }

    @Test
    public void testDeleteOneFile() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            int fileId = FileRepository.saveOnePicture(connection, getTestImage(), getTestImageName());
            FileRepository.deleteOneFile(connection, (long) fileId);
            connection.rollback();
        }
    }
}