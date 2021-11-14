package com.cookieapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
public class ActiveCookieService {
    public List<String> findMostActiveCookies(File file, String dateStr) {
        List<String> mostActiveCookies = new ArrayList<String>();
        Map<String, Integer> cookieCounter = new HashMap<String, Integer>();
        Integer maxOccurrence = 0;
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            // 2018-12-09
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date inputDate = sdf.parse(dateStr);
            OffsetDateTime inputDateUTC = inputDate.toInstant().atOffset(ZoneOffset.UTC);
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, "UTF-8");
            boolean firstRecord = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!firstRecord) {
                    String[] recordValues = line.split(",");
                    String cookieName = recordValues[0];
                    OffsetDateTime cookieTime = OffsetDateTime.parse(recordValues[1]);
                    // Stop reading file if cookie date is old then input date.
                    if (cookieTime.isBefore(inputDateUTC)) {
                        break;
                    }
                    if (cookieTime.getYear() == inputDateUTC.getYear() && cookieTime.getMonth() == inputDateUTC.getMonth() && cookieTime.getDayOfMonth() == inputDateUTC.getDayOfMonth()) {
                        if (cookieCounter.containsKey(cookieName)) {
                            int nextCount = cookieCounter.get(cookieName) + 1;
                            if (maxOccurrence < nextCount)
                                maxOccurrence = nextCount;
                            cookieCounter.put(cookieName, nextCount);
                        } else {
                            cookieCounter.put(cookieName, 1);
                            if (maxOccurrence == 0) maxOccurrence = 1;
                        }
                    }
                }
                firstRecord = false;
            }

            if (!cookieCounter.isEmpty()) {
                for (Map.Entry<String, Integer> cookie : cookieCounter.entrySet()) {
                    if (cookie.getValue() == maxOccurrence)
                        mostActiveCookies.add(cookie.getKey());
                }
            }

            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
            log.error("Given file not found.", e);
        } catch (ParseException e) {
            log.error("Exception occurred while parsing date.", e);
        } catch (IOException e) {
            log.error("Exception occurred while reading input stream.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Exception occurred while closing input stream.", e);
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return mostActiveCookies;
    }
}
