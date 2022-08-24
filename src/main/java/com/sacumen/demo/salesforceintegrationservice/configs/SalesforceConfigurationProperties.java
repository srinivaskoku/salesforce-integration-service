/**
 * @author Srinivasula Reddy Koku
 */

package com.sacumen.demo.salesforceintegrationservice.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration("salesforceConfigurationProperties")
@ConfigurationProperties(prefix = "salesforce")
public class SalesforceConfigurationProperties {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String consumerKey;

    @NotNull
    private String consumerSecret;
}
