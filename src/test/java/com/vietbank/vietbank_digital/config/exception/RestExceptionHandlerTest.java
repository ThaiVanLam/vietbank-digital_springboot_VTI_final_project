package com.vietbank.vietbank_digital.config.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RestExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleAllException() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testHandleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/not-found-url")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(2));
    }

    @Test
    void testHandleHttpRequestMethodNotSupported() throws Exception {
        mockMvc.perform(post("/test/method-not-supported"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(3));
    }

    @Test
    void testHandleHttpMediaTypeNotSupported() throws Exception {
        mockMvc.perform(post("/test/media-not-supported")
                        .content("plain text")
                        .contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value(4));
    }

    @Test
    void testHandleMethodArgumentNotValid() throws Exception {
        mockMvc.perform(post("/test/body-validation")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(5))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        mockMvc.perform(get("/test/validation")
                        .param("name", "abc")) // invalid: size < 5
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(5))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void testHandleMissingServletRequestParameter() throws Exception {
        mockMvc.perform(get("/test/missing-param"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(6));
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() throws Exception {
        mockMvc.perform(get("/test/arg-type-mismatch")
                        .param("number", "abc")) // invalid: cannot parse int
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(7));
    }
}
