package com.sacumen.demo.salesforceintegrationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sacumen.demo.salesforceintegrationservice.configs.SalesforceConfigurationProperties;
import com.sacumen.demo.salesforceintegrationservice.models.*;
import com.sacumen.demo.salesforceintegrationservice.utils.BearerTokenUtilities;
import com.sacumen.demo.salesforceintegrationservice.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {
    public static final String QUERY_PATH = "/services/data/v52.0/";

    //private final ContactEventPublisher contactEventPublisher;
    private final CloseableHttpClient closeableHttpClient;
    private final ObjectMapper objectMapper;
    private final SalesforceConfigurationProperties salesforceConfigurationProperties;


    @Cacheable("events")
    public List<Event> getEvents() throws Exception {
        SalesforceLoginResult salesforceLoginResult = BearerTokenUtilities.loginToSalesforce(closeableHttpClient, salesforceConfigurationProperties, objectMapper);

        URIBuilder builder = new URIBuilder(salesforceLoginResult.getInstanceUrl());
        builder.setPath(QUERY_PATH + "query").setParameter("q", Event.EVENT_QUERY);

        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + salesforceLoginResult.getAccessToken());

        HttpResponse httpResponse = closeableHttpClient.execute(get);
        System.out.println(httpResponse.getStatusLine());
        HttpUtils.checkResponse(httpResponse);

        SalesforceEventResponse salesforceResponse = objectMapper.readValue(httpResponse.getEntity().getContent(), SalesforceEventResponse.class);

        List<Event> events = salesforceResponse.getRecords();
        System.out.println(events);
        log.debug("events={}", events);
        return events;
    }

}
