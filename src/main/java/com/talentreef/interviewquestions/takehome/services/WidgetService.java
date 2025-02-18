package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WidgetService {

  private final WidgetRepository widgetRepository;

  @Autowired
  private WidgetService(WidgetRepository widgetRepository) {
    Assert.notNull(widgetRepository, "widgetRepository must not be null");
    this.widgetRepository = widgetRepository;
  }

  public List<Widget> getAllWidgets() {
    return widgetRepository.findAll();
  }

  public Widget getWidget(String name) {
    log.info("Getting widget with name %s".formatted(name));
    return validateAndGetWidget(name);
  }

  public Widget addWidget(Widget widget) {
    log.info("Adding widget with name %s started".formatted(widget.getName()));
    Optional<Widget> existingWidget = widgetRepository.findById(widget.getName());

    if (existingWidget.isPresent()) {
      log.error("Widget with name %s already exists".formatted(widget.getName()));
      throw new EntityExistsException("Widget with name %s already exists".formatted(widget.getName()));
    }

    return widgetRepository.save(widget);
  }

  public Widget updateWidget(Widget widget) {
    log.info("Update widget with name %s started".formatted(widget.getName()));
    validateAndGetWidget(widget.getName());

    return widgetRepository.updateWidget(widget);
  }

  public List<Widget> deleteWidget(String name) {
    log.info("Delete widget with name %s started".formatted(name));
    validateAndGetWidget(name);

    return widgetRepository.deleteById(name);
  }

  private Widget validateAndGetWidget(String name) {
    log.info("Validate widget with name %s started".formatted(name));
    Optional<Widget> widget = widgetRepository.findById(name);

    if (widget.isPresent()) {
      log.info("widget with name %s found".formatted(name));
      return widget.get();
    }

    log.error("Widget with name %s not found".formatted(name));
    throw new EntityNotFoundException("Widget with name %s not found".formatted(name));
  }

}
