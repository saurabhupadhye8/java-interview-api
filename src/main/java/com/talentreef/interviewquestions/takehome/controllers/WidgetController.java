package com.talentreef.interviewquestions.takehome.controllers;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/widgets", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class WidgetController {

  private final WidgetService widgetService;

  public WidgetController(WidgetService widgetService) {
    Assert.notNull(widgetService, "widgetService must not be null");
    this.widgetService = widgetService;
  }

  @GetMapping
  public ResponseEntity<List<Widget>> getAllWidgets() {
    return ResponseEntity.ok(widgetService.getAllWidgets());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Widget addWidget(@Valid @RequestBody Widget widget) {
    return widgetService.addWidget(widget);
  }

  @GetMapping("/{widgetName}")
  public ResponseEntity<Widget> getWidget(@PathVariable String widgetName) {
    return ResponseEntity.ok(widgetService.getWidget(widgetName));
  }

  @PutMapping
  public ResponseEntity<Widget> updateWidget(@Valid @RequestBody Widget widget) {
    return ResponseEntity.ok(widgetService.updateWidget(widget));
  }

  @DeleteMapping("/{widgetName}")
  public ResponseEntity<List<Widget>> deleteWidget(@PathVariable String widgetName) {
    return ResponseEntity.ok(widgetService.deleteWidget(widgetName));
  }

}
