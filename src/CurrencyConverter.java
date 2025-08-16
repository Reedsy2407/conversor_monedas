import java.util.Map;
import java.util.Set;

public class CurrencyConverter {
    private final Map<String, Double> rates;
    private final String baseCurrency;

    public CurrencyConverter(Map<String, Double> rates, String baseCurrency) {
        this.rates = rates;
        this.baseCurrency = baseCurrency;
    }

    public double convert(String from, String to, double amount) {
        if (from.equalsIgnoreCase(to)) return amount;

        Double rateFrom = rates.get(from.toUpperCase());
        Double rateTo = rates.get(to.toUpperCase());

        if (rateFrom == null || rateTo == null) {
            return Double.NaN;
        }

        if (from.equalsIgnoreCase(baseCurrency)) {
            return amount * rateTo;
        }


        double amountInBase = amount / rateFrom;
        return amountInBase * rateTo;
    }

    public Set<String> availableCurrencies() {
        return rates.keySet();
    }
}