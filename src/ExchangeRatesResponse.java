import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class ExchangeRatesResponse {
    private String result;
    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("conversion_rates")
    private Map<String, Double> conversionRates;

    @SerializedName("rates")
    private Map<String, Double> rates;

    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    // Método utilitario para obtener el mapa real de tasas
    public Map<String, Double> getRatesMap() {
        if (conversionRates != null) return conversionRates;
        return rates;
    }

    @Override
    public String toString() {
        return "ExchangeRatesResponse{" +
                "result='" + result + '\'' +
                ", baseCode='" + baseCode + '\'' +
                ", rates=" + getRatesMap() +
                '}';
    }
}