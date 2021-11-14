package com.cookieapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ActiveCookieServiceTest {
    @InjectMocks
    private ActiveCookieService activeCookieService;

    @Test
    public void testFindMostActiveCookies() {
        List<String> result = activeCookieService.findMostActiveCookies(new File("src/test/resources/cookie-log.csv"), "2018-12-09");
        assertNotNull(result);
        assertTrue(result.contains("AtY0laUfhglK3lC7"));
    }
}
