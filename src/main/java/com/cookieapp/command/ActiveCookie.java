package com.cookieapp.command;

import com.cookieapp.service.ActiveCookieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "activecookie", mixinStandardHelpOptions = true, version = "1.0",
        description = "Prints the most active Cookie for a given Date.")
@Component
@Slf4j
public class ActiveCookie implements Callable<Integer> {

    @Autowired
    private ActiveCookieService activeCookieService;

    @Option(names = {"-f", "--file"}, description = "File name to process.", required = true)
    private File file;

    @Option(names = {"-d", "--date"}, description = "Specify the Date", required = true)
    private String dateStr;

    public static void main(String... args) {
        int exitCode = new CommandLine(new ActiveCookie()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Searching most active cookie in file=" + file + " for date=" + dateStr);
        List<String> mostActiveCookies = activeCookieService.findMostActiveCookies(file, dateStr);
        mostActiveCookies.forEach(activeCookie -> System.out.println(activeCookie));
        return 0;
    }
}
