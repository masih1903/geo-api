package app.services;

import app.dtos.CountryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class CountryService {
    private static final String BASE_URL_ATLAS = "https://restcountries.com/v3.1/all";

    public List<CountryDTO> fetchAllData() throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL_ATLAS))
                .header("Accept-Encoding", "gzip")
                .header("User-Agent", "Java HttpClient")
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() == 200) {
            byte[] compressedBody = response.body();

            // Decompress gzip content
            String json;
            try (GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedBody))) {
                json = new String(gzipStream.readAllBytes());
            }

            // Map JSON to DTO
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, CountryDTO.class));
        } else {
            throw new IOException("Failed to fetch data: " + response.statusCode());
        }
    }
}
