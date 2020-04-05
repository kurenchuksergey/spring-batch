package com.kurenchuksergey.springbatch.jobs;

import com.kurenchuksergey.springbatch.entity.Car;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class XlsImportFile {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job importJob(Step importFiles) {
        return jobBuilderFactory.get("importJob")
                .start(importFiles)
                .build();
    }

    @Bean
    public Step importFiles(XlsxReader xlsxReader, RowItemProcessor rowItemProcessor, ItemWriter<Car> databaseItemWriter) {
       return stepBuilderFactory.get("importFile")
                .<XSSFRow, Car> chunk(15)
                .reader(xlsxReader)
                .processor(rowItemProcessor)
                .writer(databaseItemWriter)
                .build();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }


    @Bean
    ItemWriter<Car> databaseItemWriter(DataSource dataSource,
                                                     NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<Car> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql("INSERT INTO car(name, price) VALUES (:name, :price)");

        ItemSqlParameterSourceProvider<Car> paramProvider =
                new BeanPropertyItemSqlParameterSourceProvider<>();
        databaseItemWriter.setItemSqlParameterSourceProvider(paramProvider);

        return databaseItemWriter;
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
