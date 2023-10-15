import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.victoria.ConsumersService;
import org.datapool.victoria.dto.ConsumerCountData;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;

public class VmTest {
    String server = "http://localhost:8428";

    @Test
    public void queryTest() throws IOException {
        String start = "2023-07-26T16:33:47Z";
        String end = "2023-07-26T19:02:05Z";
        ConsumersService client = new ConsumersService(server);
        InternalApiRequest data = client.getDatapoolItem(start, end, "711bff9d-a873-4497-873f-fbfceb220071", "limit-cache", "bob3");
        System.out.println("fin");
    }
}
