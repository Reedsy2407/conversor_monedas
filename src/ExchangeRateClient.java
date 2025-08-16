import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class ExchangeRateClient {
    private final HttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final String baseUrl; // se arma con la clave


    public ExchangeRateClient(String apiKey) {
        this.apiKey = apiKey;
        this.baseUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/";
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }


    public Optional<Map<String, Double>> fetchRates(String baseCurrency) {
        String url = baseUrl + baseCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String body = response.body();

            if (status >= 200 && status < 300) {
                ExchangeRatesResponse resp = gson.fromJson(body, ExchangeRatesResponse.class);
                Map<String, Double> ratesMap = resp.getRatesMap();
                if (ratesMap == null) {
                    System.err.println("Respuesta JSON no contiene tasas en 'conversion_rates' o 'rates'. JSON: " + body);
                    return Optional.empty();
                }
                return Optional.of(ratesMap);
            } else {
                System.err.printf("La API devolvió estado %d. Cuerpo: %s%n", status, body);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al conectar con la API: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Respuesta JSON inválida: " + e.getMessage());
        }
        return Optional.empty();
    }
}