package br.com.fechaki.telephone.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomHeaderInterceptorTest {
    CustomHeaderInterceptor interceptor = new CustomHeaderInterceptor();

    @Test
    @DisplayName("Validate API Header")
    void validateAPIHeader() throws Exception {
        ReflectionTestUtils.setField(interceptor, "serverHeader", "API_HEADER");
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();

        interceptor.postHandle(httpServletRequest, httpServletResponse, new Object(), null);

        assertEquals(200, httpServletResponse.getStatus());
        assertNotNull(httpServletResponse.getHeader("Server"));
    }

}