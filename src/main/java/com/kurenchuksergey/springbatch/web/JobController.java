package com.kurenchuksergey.springbatch.web;

import com.kurenchuksergey.springbatch.service.XlsImportLauncher;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {

    @Autowired
    private XlsImportLauncher xlsImportLauncher;

    @PostMapping("/")
    public JobInstance run(@RequestBody String path) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return xlsImportLauncher.startImport(path);
    }

    @GetMapping("/{instanceId}")
    public BatchStatus getStatus(@PathVariable("instanceId") Long instanceId){
        return xlsImportLauncher.getStatusOfJobInstance(instanceId);
    }

}
