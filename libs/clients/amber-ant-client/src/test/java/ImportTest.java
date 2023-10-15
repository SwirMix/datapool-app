import org.datapool.ant.service.AmberWorker;
import org.datapool.ant.service.AmberWorkerConfig;
import org.datapool.ant.service.ExportTask;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ImportTest {
    private String project = "711bff9d-a873-4497-873f-fbfceb220071";
    private String cache = "ticketFlights";
    private String token = "002115f0-3b5c-42a4-8c39-586e84d8ef59";

    @Test
    public void importTest() throws IOException, InterruptedException, ExecutionException {
        ExportTask task = new ExportTask();
        task.setBatch(1000);
        task.setCacheName(cache);
        task.setOutput(new File(cache + ".csv"));
        task.setStartId(0);
        task.setEndId(2360335);

        AmberWorkerConfig config = new AmberWorkerConfig(project, token, "http://192.168.0.8:8084");
        config.setThreads(50);
        config.setTask(task);
        AmberWorker worker = new AmberWorker(config);
        worker.importDataToCSV();
    }
}
