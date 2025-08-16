public class Main {
    public static void main(String[] args) {
        String keyFromEnv = System.getenv("EXCHANGE_API_KEY");
        final String API_KEY = (keyFromEnv != null && !keyFromEnv.isBlank())
                ? keyFromEnv
                : "df96b2040de8841f9609e5f4";

        ExchangeRateClient client = new ExchangeRateClient(API_KEY);
        ConsoleUI ui = new ConsoleUI(client);
        ui.start();
    }
}