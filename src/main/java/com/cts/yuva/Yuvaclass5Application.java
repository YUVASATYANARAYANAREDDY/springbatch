package com.cts.yuva;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Yuvaclass5Application {

    public static void main(String[] args) {
       // SpringApplication.run(Yuvaclass5Application.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(Yuvaclass5Application.class, args);

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job importJob = context.getBean("importJob", Job.class);

        try {
            jobLauncher.run(importJob, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
