package review.demo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import review.demo.controllers.GHController;
import review.demo.exceptions.UserNotFoundException;
import review.demo.services.GHService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GHController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;



    @TestConfiguration
    static class TestConfig {
        @Bean
        public GHService ghService() {
            GHService mock = mock(GHService.class);
            when(mock.getNonForkRepositories(anyString()))
                    .thenThrow(new UserNotFoundException("GitHub user not found"));
            return mock;
        }
    }

    @Test
    void handleUserNotFound_shouldReturn404WithErrorResponse() throws Exception {
        mockMvc.perform(get("/users/nonexistent/repositories"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("GitHub user not found"));
    }
}