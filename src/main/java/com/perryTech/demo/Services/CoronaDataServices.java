package com.perryTech.demo.Services;

import com.perryTech.demo.Models.CoronaStats;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
//@Data
public class CoronaDataServices {
    private static final String coronaDataURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<CoronaStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "10 * * * * *")
    public void fetchCoronaData() throws IOException, InterruptedException {

        List<CoronaStats> newStats = new ArrayList<>();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create(coronaDataURL))
                .build();

        HttpResponse<String> stringHttpResponse = httpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader csvReader = new StringReader(stringHttpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvReader);

        for (CSVRecord record : records) {
            CoronaStats coronaStats = new CoronaStats();
            coronaStats.setProvince(record.get("Province/State"));
            coronaStats.setCountry(record.get("Country/Region"));
            int newTotalCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            coronaStats.setNewTotalCases(newTotalCases);
            coronaStats.setDifferenceFromPreviousDay(newTotalCases - prevDayCases);
            newStats.add(coronaStats);
        }

        this.allStats = newStats;
    }
    public List<CoronaStats> getAllStats() {
        return allStats;
    }
}

