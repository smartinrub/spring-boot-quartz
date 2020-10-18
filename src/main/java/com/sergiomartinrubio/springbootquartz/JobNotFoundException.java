package com.sergiomartinrubio.springbootquartz;

import static java.lang.String.*;

public class JobNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Job not found for name %s";

    public JobNotFoundException(String jobName) {
        super(format(MESSAGE, jobName));
    }
}
