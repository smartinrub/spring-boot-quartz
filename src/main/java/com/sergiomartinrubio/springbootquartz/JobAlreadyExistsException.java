package com.sergiomartinrubio.springbootquartz;

import static java.lang.String.*;

public class JobAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Job with name %s already exists";

    public JobAlreadyExistsException(String jobName) {
        super(format(MESSAGE, jobName));
    }
}
