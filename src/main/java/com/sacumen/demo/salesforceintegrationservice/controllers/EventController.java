/**
 * @author Srinivasula Reddy Koku
 */

package com.sacumen.demo.salesforceintegrationservice.controllers;

import com.sacumen.demo.salesforceintegrationservice.models.Event;
import com.sacumen.demo.salesforceintegrationservice.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping(value = "/events")
    public ResponseEntity<List<Event>> getEvents() {
        try {
            return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
