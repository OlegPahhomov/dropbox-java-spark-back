package config;

import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class AppDataSource {
    private static DataSource dataSource = initLocalDbSource();
    public static final String HOME_PW = "12345";
    public static final String OFFICE_PW = "postgres";

    private static DataSource initLocalDbSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword(HOME_PW);
        //dataSource.setPassword(OFFICE_PW);
        dataSource.setServerName("localhost");
        return dataSource;
    }

    public static DataSource getDatasource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getTransactConnection() throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

}