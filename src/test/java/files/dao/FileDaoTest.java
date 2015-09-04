package files.dao;

import config.AppDataSource;
import org.junit.Test;

import java.sql.Connection;

import static files.TestUtil.saveTestFile;
import static org.junit.Assert.assertTrue;

public class FileDaoTest {

    @Test
    public void shouldAddTestFile() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            int fileId = saveTestFile(connection);
            assertTrue(fileId > 0);
            connection.rollback();
        }
    }

    @Test
    public void shouldDeleteTestFile() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            int fileId = saveTestFile(connection);
            int affectedRows = FileDao.deleteFileRow(connection, (long) fileId);
            assertTrue(affectedRows == 1);
            connection.rollback();
        }
    }

}
