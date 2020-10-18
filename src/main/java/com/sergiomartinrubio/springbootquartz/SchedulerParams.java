package com.sergiomartinrubio.springbootquartz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerParams {
    @NotNull
    ZonedDateTime jobDateTime;
}
