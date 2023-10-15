package org.datapool.ant;

import org.apache.commons.cli.*;
import org.datapool.ant.service.AmberWorker;
import org.datapool.ant.service.AmberWorkerConfig;
import org.datapool.ant.service.ExportTask;

import javax.naming.NotContextException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConsoleExporter {
    private static AmberWorkerConfig config;
    private static List<ExportTask> tasks = new ArrayList<>();
    private static Configuration configuration;
    public static void main(String[] argv) throws ParseException, NotContextException, IOException, ExecutionException, InterruptedException {
        Options options = new Options();
        options.addOption("tasks", true, "jobs for import.");
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse( options, argv);

        if(cmd.hasOption("tasks")){
            configuration = Utils.readConfiguration(cmd.getOptionValue("tasks"));
        } else throw new NotContextException();
        initCfg(configuration);
        for (ExportTask task : tasks){
            try {
                config.setTask(task);
                config.setThreads(configuration.getThreads());
                AmberWorker worker = new AmberWorker(config);
                System.out.println("Start processing:" + task.getOutput().getAbsolutePath());
                worker.importDataToCSV();
                System.out.println("End processing:" + task.getOutput().getAbsolutePath());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("End processing");
    }

    private static void initCfg(Configuration configuration){
        config = new AmberWorkerConfig(
                configuration.getProject(),
                configuration.getToken(),
                configuration.getDatapool()
        );
        for (Configuration.TaskConfig taskConfig : configuration.getTasks()){
            ExportTask task = new ExportTask();
            task.setBatch(task.getBatch());
            task.setOutput(new File(taskConfig.getOutput()));
            task.setCacheName(taskConfig.getCache());
            task.setStartId(taskConfig.getStartId());
            task.setEndId(taskConfig.getEndId());
            tasks.add(task);
        }
    }
}
