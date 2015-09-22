import config.AppDataSource;
import org.flywaydb.core.Flyway;

public class Migration {

    static Flyway flyway = new Flyway();

    public static void main(String[] args) throws Exception {
        flyway.setDataSource(AppDataSource.getDatasource());
        flyway.clean();
        flyway.migrate();
    }
}
