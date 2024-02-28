package ch.uzh.ifi.hase.soprafs22.utils;

import ch.uzh.ifi.hase.soprafs22.constant.Category;
import ch.uzh.ifi.hase.soprafs22.entity.ValidationResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Validator {
    private Validator() {
        throw new IllegalStateException("Do not instantiate - Utility class");
    }

    public static ValidationResult validate(Integer category, String answer, Character letter) throws IOException, InterruptedException {
        answer = answer.toLowerCase();
        if (!answer.startsWith(String.valueOf(letter))) {
            return null;
        }
        if (answer.length() < 4) {
            return null;
        }
        switch (category) {
            case Category.CITIES -> {return validateCities(answer);}
            case Category.COUNTRIES -> {return validateCountries(answer);}
            case Category.RIVERS -> {return validateRivers(answer);}
            case Category.MUNICIPALITY -> {return validateMunicipalities(answer);}
        }
        return null;
    }
    private static ValidationResult validateFromURL(String requestURL) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestURL)).build();
        HttpClient client = HttpClient.newHttpClient();
        String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        JSONObject response = new JSONObject(jsonResponse);
        ValidationResult validationResult = new ValidationResult();
        if (response.getInt("totalResultsCount") > 0) {
            JSONArray records = response.getJSONArray("geonames");
            // we iterate over the results to find a result with a wikipedia link (in other words: is relevant).
            // otherwise, there would be a lot of useless false positives (like "hans" or "hund" is a river and "haus"
            // or "garten" are cities near oslo, while being technically true, would ruin the fun in the game ...
            JSONObject record;
            JSONArray alternateNames;
            JSONObject alternateName;
            // iterate over all the given records
            for (int i = 0; i < records.length(); i++) {
                record = records.getJSONObject(i);
                // check if different languages exist / wikipedia links are found under language - "link"
                if (record.has("alternateNames")) {
                    alternateNames = record.getJSONArray("alternateNames");
                    for (int j = 0; j < alternateNames.length(); j++) {
                        alternateName = alternateNames.getJSONObject(j);
                        if (Objects.equals(alternateName.getString("lang"), "link")) {
                            validationResult.setWikipediaLink(alternateName.getString("name"));
                            validationResult.setGeoNameID(record.getInt("geonameId"));
                            validationResult.setValid(true);
                            return validationResult;
                        }
                    }
                }
            }
            // no wikipedia links found - results probably irrelevant - return null
            return null;
        } else {
            return null;
        }
    }
    private static final String apiBaseURL = "http://api.geonames.org/searchJSON?style=FULL&";
    private static ValidationResult validateCities(String answer) throws IOException, InterruptedException {
        // Accepted Feature Classes:
        // featureClass P stands for "city, village,..." - a populated place
        String citiesBaseURL = apiBaseURL +
                "username=tnaescher&" +
                "featureClass=P&" +
                "name=%s";
        String citiesURL = String.format(citiesBaseURL, URLEncoder.encode(answer, StandardCharsets.UTF_8));
        return validateFromURL(citiesURL);
    }

    private static ValidationResult validateCountries(String answer) throws IOException, InterruptedException {
        // Accepted Feature Classes:
        // featureCode PCLI stands for independent political entity
        String countriesBaseURL = apiBaseURL +
                "username=tnaescher&" +
                "featureCode=PCLI&" +
                "name=%s";
        String countriesURL = String.format(countriesBaseURL, URLEncoder.encode(answer, StandardCharsets.UTF_8));
        return validateFromURL(countriesURL);

    }

    private static ValidationResult validateRivers(String answer) throws IOException, InterruptedException {
        // Accepted Feature Classes:
        // featureClass H stands for "stream, lake, ..."
        // featureCode STM stands for "stream - a body of running water moving to a lower level in a channel on land"
        // featureCode STMC stands for "canalized stream - a stream that has been substantially ditched, diked, or straightened"
        // featureCode STMI stands for "intermittent stream"
        // featureCode STMIX stands for "section of intermittent stream"
        // featureCode STMS stands for "section of intermittent stream - bodies of running water moving to a lower level in a channel on land"
        // featureCode WTRC stands for "a natural, well-defined channel produced by flowing water, or an artificial channel designed to carry flowing water"
        // style = FULL returns way more information (like alternate names / links)
        String riversBaseURL = apiBaseURL +
                "username=tnaescher&" +
                "featureClass=H&" +
                "featureCode=STM&" +
                "featureCode=STMC&" +
                "featureCode=STMI&" +
                "featureCode=STMIX&" +
                "featureCode=STMS&" +
                "featureCode=WTRC&" +
                "name=%s";
        String riversURL = String.format(riversBaseURL, URLEncoder.encode(answer, StandardCharsets.UTF_8));
        return validateFromURL(riversURL);
    }

    private static ValidationResult validateMunicipalities(String answer) throws IOException, InterruptedException {
        // Accepted Feature Classes:
        // featureCode ADM3 stands for third-order administrative division:
        // in Switzerland:
        // ADM1: Kanton (zB. Kanton Zürich)
        // ADM2: Bezirk / Wahlkreis ... (z.B.Bezirk Horgen)
        // ADM3: Gemeinde (z.B. Gde. Thalwil)
        // require country=CH for only swiss municipalities
        String citiesBaseURL = apiBaseURL +
                "username=tnaescher&" +
                "country=CH&" +
                "countryBias=CH&" +
                "featureCode=ADM3&" +
                "name=%s";
        String citiesURL = String.format(citiesBaseURL, URLEncoder.encode(answer, StandardCharsets.UTF_8));
        return validateFromURL(citiesURL);
    }

}
