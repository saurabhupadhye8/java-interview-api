package com.talentreef.interviewquestions.takehome.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetControllerTests {

  private static final String WIDGET_NAME = "Widget von Hammersmark";

  final private ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private WidgetService widgetService;

  @InjectMocks
  private WidgetController widgetController;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(widgetController).build();
  }

  @Test
  public void when_getAllWidgets_expect_allWidgets() throws Exception {
    Widget widget = buildWidget();
    List<Widget> allWidgets = List.of(widget);
    when(widgetService.getAllWidgets()).thenReturn(allWidgets);

    MvcResult result = mockMvc.perform(get("/v1/widgets"))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();

    List<Widget> parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<Widget>>(){});
    assertThat(parsedResult).isEqualTo(allWidgets);
  }

  @Test
  public void when_getWidget_expect_getWidget() throws Exception {
    Widget widget = buildWidget();
    when(widgetService.getWidget(WIDGET_NAME)).thenReturn(widget);

    MvcResult result = mockMvc.perform(get("/v1/widgets/" + WIDGET_NAME))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    Widget parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<Widget>(){});
    assertThat(parsedResult).isEqualTo(widget);
  }

  @Test
  public void when_addWidget_expect_addNewRecord() throws Exception {
    Widget widget = buildWidget();
    when(widgetService.addWidget(widget)).thenReturn(widget);

    MvcResult result = mockMvc.perform(post("/v1/widgets/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(widget)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

    Widget parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<Widget>(){});
    assertThat(parsedResult).isEqualTo(widget);
  }

  @Test
  public void when_updateWidget_expect_update() throws Exception {
    Widget widget = buildWidget();
    Widget updatedWidget = buildWidget();
    updatedWidget.setDescription("Updated description");
    when(widgetService.updateWidget(widget)).thenReturn(updatedWidget);

    MvcResult result = mockMvc.perform(put("/v1/widgets/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(widget)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    Widget parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<Widget>(){});
    assertThat(parsedResult).isEqualTo(updatedWidget);
  }

  @Test
  public void when_deleteWidget_expect_removeWidget() throws Exception {
    List<Widget> allWidgets = List.of(buildWidget());
    when(widgetService.deleteWidget(WIDGET_NAME)).thenReturn(allWidgets);

    MvcResult result = mockMvc.perform(delete("/v1/widgets/" + WIDGET_NAME))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    List<Widget> parsedResult = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<List<Widget>>(){});
    assertThat(parsedResult).isEqualTo(allWidgets);
  }

  private static Widget buildWidget() {
    return Widget.builder()
            .name(WIDGET_NAME)
            .description("description")
            .price(new BigDecimal(1))
            .build();
  }

}
