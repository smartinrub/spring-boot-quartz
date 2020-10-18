package com.sergiomartinrubio.springbootquartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping("/create-job/{job-name}")
    public void createJob(@PathVariable("job-name") String jobName,
                          @RequestBody @Valid SchedulerParams schedulerParams) throws SchedulerException {
        schedulerService.scheduleJob(jobName, schedulerParams);
    }

    @DeleteMapping("/delete-job/{job-name}")
    public void deleteJob(@PathVariable("job-name") String jobName) throws SchedulerException {
        schedulerService.deleteJob(jobName);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JobNotFoundException.class)
    public void handleJobNotFoundException(JobNotFoundException exception) {
        log.info("Could not find the job", exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JobAlreadyExistsException.class)
    public void handleJobAlreadyExistsException(JobAlreadyExistsException exception) {
        log.info("Job already exists", exception);
    }
}
