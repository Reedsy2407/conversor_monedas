import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    private final ExchangeRateClient client;
    private final String[] suggested = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

    public ConsoleUI(ExchangeRateClient client) {
        this.client = client;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Bienvenido/a al Conversor de Moneda ===");

        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1) Convertir moneda");
            System.out.println("2) Listar monedas sugeridas");
            System.out.println("3) Salir");
            System.out.print("Elija una opción (1-3): ");

            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1":
                    doConversion(scanner);
                    break;
                case "2":
                    listSuggested();
                    break;
                case "3":
                    System.out.println("Saliendo. ¡Hasta luego!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción inválida. Elija 1, 2 o 3.");
            }
        }
    }

    private void listSuggested() {
        System.out.println("Monedas sugeridas:");
        for (String s : suggested) System.out.println("- " + s);
    }

    private void doConversion(Scanner scanner) {
        try {
            System.out.print("Ingrese la moneda origen (ej: USD): ");
            String from = scanner.nextLine().trim().toUpperCase();

            System.out.print("Ingrese la moneda destino (ej: ARS): ");
            String to = scanner.nextLine().trim().toUpperCase();

            System.out.print("Ingrese el monto a convertir: ");
            String amountStr = scanner.nextLine().trim();
            double amount = Double.parseDouble(amountStr);

            Optional<Map<String, Double>> ratesOpt = client.fetchRates(from);

            String baseUsed = from;
            Map<String, Double> rates;
            if (ratesOpt.isEmpty()) {
                System.out.println("No se pudieron obtener las tasas con base " + from + ". Intentando base 'USD' como fallback...");
                Optional<Map<String, Double>> ratesUsd = client.fetchRates("USD");
                if (ratesUsd.isEmpty()) {
                    System.out.println("No se pudieron obtener tasas de la API. Intente más tarde.");
                    return;
                } else {
                    rates = ratesUsd.get();
                    baseUsed = "USD";
                }
            } else {
                rates = ratesOpt.get();
            }

            CurrencyConverter converter = new CurrencyConverter(rates, baseUsed);
            double result = converter.convert(from, to, amount);
            if (Double.isNaN(result)) {
                System.out.println("No se encontró la tasa para una de las monedas. Monedas disponibles: " + converter.availableCurrencies());
                return;
            }

            System.out.printf("%,.2f %s = %,.2f %s (base usada: %s)%n", amount, from, result, to, baseUsed);

        } catch (NumberFormatException e) {
            System.out.println("Monto inválido. Ingrese un número válido.");
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}