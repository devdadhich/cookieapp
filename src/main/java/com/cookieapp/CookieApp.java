package com.cookieapp;

import com.cookieapp.command.ActiveCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class CookieApp implements CommandLineRunner {

    private ActiveCookie activeCookie;

    @Autowired
    public CookieApp(ActiveCookie activeCookie) {
        this.activeCookie = activeCookie;
    }

    public static void main(String[] args) {
        SpringApplication.run(CookieApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine commandLine = new CommandLine(activeCookie);
        commandLine.parseWithHandler(new CommandLine.RunLast(), args);
    }
}
