import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = 9877736cc28ac45182f4d0dd; // Reemplaza por tu clave de API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Conversor de Monedas ---");
            System.out.println("1. Dólar a Peso Argentino");
            System.out.println("2. Peso Argentino a Dólar");
            System.out.println("3. Dólar a Real Brasileño");
            System.out.println("4. Real Brasileño a Dólar");
            System.out.println("5. Dólar a Peso Colombiano");
            System.out.println("6. Peso Colombiano a Dólar");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    convertCurrency("USD", "ARS");
                    break;
                case 2:
                    convertCurrency("ARS", "USD");
                    break;
                case 3:
                    convertCurrency("USD", "BRL");
                    break;
                case 4:
                    convertCurrency("BRL", "USD");
                    break;
                case 5:
                    convertCurrency("USD", "COP");
                    break;
                case 6:
                    convertCurrency("COP", "USD");
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void convertCurrency(String from, String to) {
        try {
            String url = BASE_URL + from + "/" + to;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parseamos el resultado (dependiendo del formato que devuelva la API, aquí usaremos JSON)
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Parsear la respuesta JSON y extraer la tasa de conversión (puedes usar una librería como Jackson o Gson)
                String searchString = "\"conversion_rate\":";
                int index = responseBody.indexOf(searchString);
                if (index != -1) {
                    int startIndex = index + searchString.length();
                    int endIndex = responseBody.indexOf(",", startIndex);
                    String rateStr = responseBody.substring(startIndex, endIndex).trim();
                    double rate = Double.parseDouble(rateStr);
                    System.out.printf("La tasa de cambio de %s a %s es: %.4f%n", from, to, rate);
                }
            } else {
                System.out.println("Error al obtener los datos de la API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al convertir la moneda.");
        }
    }
}
