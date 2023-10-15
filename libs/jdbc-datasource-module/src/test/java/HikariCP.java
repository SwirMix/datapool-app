import com.zaxxer.hikari.HikariConfig;
import org.datapool.jdbc.JdbcGatewayImpl;
import org.datapool.jdbc.dto.PgTableCreate;
import org.datapool.jdbc.dto.TableObject;
import org.testng.annotations.Test;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HikariCP {

    @Test
    public void hikariTest() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl( "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool" );
        config.setUsername( "perfcona" );
        config.setPassword( "perfcona" );
        config.setReadOnly(false);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        JdbcGatewayImpl service = new JdbcGatewayImpl(config, "profile");

        Map<String, String> columns = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("id", "1");
        data.put("name", "ponpon");
        data.put("password", "112");

        columns.put("id", "text");
        columns.put("name", "text");
        columns.put("password", "text");
        PgTableCreate request = new PgTableCreate();
        request.setTableName("test222");
        request.setColumns(columns);

        service.createTable(request);

        TableObject tableObject = service.getTableInfo("test222");
        service.insertRequest(tableObject, data);
    }
}
