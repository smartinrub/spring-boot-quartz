package com.sergiomartinrubio.springbootquartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static java.lang.String.format;

@Slf4j
public class MyJob implements Job {
    private static final String JOB_NAME_KEY = "jobName";
    private static final int MAX_RETRIES = 3;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        int refireCount = jobExecutionContext.getRefireCount();
        MyJobData jobData = new MyJobData(jobDataMap.getString(JOB_NAME_KEY));

        try {
            if (refireCount > MAX_RETRIES) {
                throw new JobExecutionException(format("Job execution retries exceeded for job name %s",
                        jobData.getJobName()));
            }

            log.info("Job execution for name: {}", jobData.getJobName());
        } catch (Exception e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);

            // fire it again
            jobExecutionException.setRefireImmediately(refireCount <= MAX_RETRIES);
            throw jobExecutionException;
        }

    }
}
