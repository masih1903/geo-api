package app.services;

import app.dtos.CountryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CountryService
{
    private static final String BASE_URL_ATLAS = "https://restcountries.com/v3.1/all";
    public List<CountryDTO> fetchAllData() throws IOException, InterruptedException, URISyntaxException
    {
        HttpClient client = HttpClient.newHttpClient();

        // Send request for the current page
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(BASE_URL_ATLAS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200)
        {
            String json = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            List<CountryDTO> countries = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, CountryDTO.class));
            return countries;
        } else
        {
            System.out.println("Failed to fetch data from the API. Status code: " + response.statusCode());
            return null;
        }
    }
}
