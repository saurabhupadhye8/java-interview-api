package com.talentreef.interviewquestions.takehome.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.respositories.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class WidgetServiceTests {

  private static final String WIDGET_NAME = "Widgette Nielson";

  @Mock
  private WidgetRepository widgetRepository;

  @InjectMocks
  private WidgetService widgetService;

  @Test
  public void when_getAllWidgets_expect_findAllResult() {
    Widget widget = buildWidget();
    List<Widget> response = List.of(widget);

    when(widgetRepository.findAll()).thenReturn(response);

    List<Widget> result = widgetService.getAllWidgets();

    assertThat(result).isEqualTo(response);
  }

  @Test
  public void when_getWidget_expect_findWidget() {
    Widget widget = buildWidget();

    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.of(widget));

    Widget result = widgetService.getWidget(WIDGET_NAME);

    assertThat(result).isEqualTo(widget);
  }

  @Test
  public void when_getWidget_expect_throwsNotFoundException() {
    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> widgetService.getWidget(WIDGET_NAME));
  }

  @Test
  public void when_addWidget_expect_addNewWidget() {
    Widget widget = buildWidget();

    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.empty());
    when(widgetRepository.save(widget)).thenReturn(widget);

    Widget result = widgetService.addWidget(widget);

    assertThat(result).isEqualTo(widget);
  }

  @Test
  public void when_addWidget_expect_throwsEntityExistsException() {
    Widget widget = buildWidget();

    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.of(widget));

    assertThrows(EntityExistsException.class, () -> widgetService.addWidget(widget));
    verify(widgetRepository, never()).save(any(Widget.class));
  }

  @Test
  public void when_updateWidget_expect_updateWidget() {
    Widget widget = buildWidget();
    Widget updatedWidget = buildWidget();
    updatedWidget.setDescription("Updated Description");

    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.of(widget));
    when(widgetRepository.updateWidget(updatedWidget)).thenReturn(updatedWidget);

    Widget result = widgetService.updateWidget(updatedWidget);

    assertThat(result).isEqualTo(updatedWidget);
  }

  @Test
  public void when_updateWidget_expect_throwsNotFoundException() {
    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> widgetService.updateWidget(buildWidget()));
  }

  @Test
  public void when_deleteWidget_expect_removeWidget() {
    Widget widget = buildWidget();
    List<Widget> resultList = new ArrayList<>();

    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.of(widget));
    when(widgetRepository.deleteById(WIDGET_NAME)).thenReturn(resultList);

    List<Widget> result = widgetService.deleteWidget(WIDGET_NAME);

    assertThat(result).isEqualTo(resultList);
  }

  @Test
  public void when_deleteWidget_expect_removesWidget() {
    when(widgetRepository.findById(WIDGET_NAME)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> widgetService.deleteWidget(WIDGET_NAME));
  }

  private static Widget buildWidget() {
    return Widget.builder()
            .name(WIDGET_NAME)
            .description("description")
            .price(new BigDecimal(1))
            .build();
  }

}
