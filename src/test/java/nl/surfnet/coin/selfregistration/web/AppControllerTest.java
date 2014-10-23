package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AppControllerTest {

  AppController appController;
  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    appController = new AppController();
    mockMvc = standaloneSetup(appController).build();
  }

  @Test
  public void testGetsAnEmptyForm() throws Exception {
    mockMvc
      .perform(get("/"))
      .andExpect(model().attributeExists("serviceProvider"));
  }

  @Test
  public void testGetsAnEmptyFormOnGetServiceProvider() throws Exception {
    mockMvc
      .perform(get("/service-provider"))
      .andExpect(model().attributeExists("serviceProvider"));

  }

  @Test
  public void testPostToServiceProvider() throws Exception {
    mockMvc
      .perform(post("/service-provider").sessionAttr("serviceProvider", new ServiceProvider()))
      .andExpect(model().attributeHasFieldErrors("serviceProvider", "entityId"));
  }
}
