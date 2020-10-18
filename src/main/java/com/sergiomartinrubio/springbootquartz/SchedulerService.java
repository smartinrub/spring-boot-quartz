package com.sergiomartinrubio.springbootquartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private static final String MY_JOB_GROUP = "my-quartz-job";
    private static final String JOB_NAME_KEY = "jobName";

    private final Scheduler scheduler;

    public void scheduleJob(String jobName, SchedulerParams schedulerParams) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, MY_JOB_GROUP);
        MyJobData myJobData = new MyJobData(jobName);

        if (scheduler.checkExists(jobKey)) {
            throw new JobAlreadyExistsException(jobName);
        }

        log.info("Updating job for name {} and time {}", jobName, schedulerParams.getJobDateTime());
        JobDataMap jobDataMap = getJobDataMap(myJobData);
        JobDetail jobDetail = getJobDetail(jobKey, jobDataMap);
        Trigger trigger = getTrigger(schedulerParams.getJobDateTime());

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, MY_JOB_GROUP);
        boolean isDeleted = scheduler.deleteJob(jobKey);

        if (isDeleted) {
            log.info("Delete job for name: {}", jobName);
        } else {
            throw new JobNotFoundException(jobName);
        }
    }

    private Trigger getTrigger(ZonedDateTime jobDateTime) {
        return TriggerBuilder.newTrigger()
                .withIdentity(UUID.randomUUID().toString(), MY_JOB_GROUP)
                .startAt(Date.from(jobDateTime.toInstant()))
                .build();
    }

    private JobDetail getJobDetail(JobKey jobKey, JobDataMap jobDataMap) {
        return JobBuilder.newJob(MyJob.class)
                .withIdentity(jobKey)
                .usingJobData(jobDataMap)
                .requestRecovery(true)
                .build();
    }

    private JobDataMap getJobDataMap(MyJobData jobData) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_NAME_KEY, jobData.getJobName());
        return jobDataMap;
    }
}
