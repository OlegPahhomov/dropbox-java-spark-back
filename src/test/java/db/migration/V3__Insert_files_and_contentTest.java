package db.migration;

import config.AppDataSource;
import files.TestUtil;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class V3__Insert_files_and_contentTest {

    @Test
    public void testInsertToDb() throws Exception{
        V3__Insert_files_and_content migrationInsert = new V3__Insert_files_and_content();
        try(Connection connection = AppDataSource.getTransactConnection()) {
            migrationInsert.insertToDb(connection, TestUtil.DRAKENSANG);
            connection.rollback();
        }
    }
}