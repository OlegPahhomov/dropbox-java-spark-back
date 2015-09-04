package files.dao;

import config.AppDataSource;
import org.junit.Test;

import java.sql.Connection;

import static files.TestUtil.*;
import static org.junit.Assert.assertTrue;

public class ContentDaoTest {

    @Test
    public void saveContentRow() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            int fileId = saveTestFile(connection);
            int affectedRows = ContentDao.saveContentRow(connection, fileId, "picture", getTestImage());
            assertTrue(affectedRows == 1);
            connection.rollback();
        }
    }
    @Test
    public void deleteContentRowByFileId() throws Exception {
        try (Connection connection = AppDataSource.getTransactConnection()) {
            int fileId = saveTestContent(connection);
            int affectedRows = ContentDao.deleteContentRowByFileId(connection, (long) fileId);
            assertTrue(affectedRows == 1);
            connection.rollback();
        }
    }

}