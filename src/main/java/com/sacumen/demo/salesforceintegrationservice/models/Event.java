/**
 * @author Srinivasula Reddy Koku
 */

package com.sacumen.demo.salesforceintegrationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Event {
    //public static final String EVENT_QUERY = "SELECT Name, Title, Department FROM Contact";

    //public static final String EVENT_QUERY = "SELECT Id, EventType,LogFile ,LogDate,LogFileLength FROM EventLogFile WHERE LogDate > Yesterday AND EventType='API'";
    public static final String EVENT_QUERY = "SELECT Id, EventType,LogFile ,LogDate,LogFileLength FROM EventLogFile ";

    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "EventType")
    private String eventType;

    @JsonProperty(value = "LogFile")
    private String logFile;

    @JsonProperty(value = "LogDate")
    private String logDate;

    @JsonProperty(value = "LogFileLength")
    private String logFileLength;

}
