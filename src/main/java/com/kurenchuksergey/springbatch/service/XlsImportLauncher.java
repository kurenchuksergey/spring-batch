package com.kurenchuksergey.springbatch.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class XlsImportLauncher {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    @Qualifier("importJob")
    private Job importJob;


    public JobInstance startImport(String path) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("path", path);
        parametersBuilder.addDate("date", new Date());
        JobExecution run = jobLauncher.run(importJob, parametersBuilder.toJobParameters());
        return run.getJobInstance();
    }

    public Set<JobExecution>  getCurrentTasks(){
        return jobExplorer.findRunningJobExecutions(importJob.getName());
    }

    public BatchStatus getStatusOfJobInstance(Long instanceId){
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        JobExecution lastJobExecution = jobExplorer.getLastJobExecution(jobInstance);
        return lastJobExecution.getStatus();
    }
}
