import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.InputMismatchException;

public class CurrencyConverter {
    private static final String API_KEY = "9877736cc28ac45182f4d0dd"; // Reemplaza por tu clave de API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        System.out.println("\033[1;34m"); // Cambia el color del texto a azul
        System.out.println("**************************************************");
        System.out.println("*          Bienvenido al Conversor de Monedas    *");
        System.out.println("**************************************************\033[0m");

        do {
            mostrarMenu();

            try {
                System.out.print("\nSeleccione una opción: ");
                opcion = scanner.nextInt();

                if (opcion >= 1 && opcion <= 6) {
                    System.out.print("Ingrese la cantidad que desea convertir: ");
                    double cantidad = scanner.nextDouble();

                    switch (opcion) {
                        case 1:
                            convertCurrency("USD", "ARS", cantidad);
                            break;
                        case 2:
                            convertCurrency("ARS", "USD", cantidad);
                            break;
                        case 3:
                            convertCurrency("USD", "BRL", cantidad);
                            break;
                        case 4:
                            convertCurrency("BRL", "USD", cantidad);
                            break;
                        case 5:
                            convertCurrency("USD", "COP", cantidad);
                            break;
                        case 6:
                            convertCurrency("COP", "USD", cantidad);
                            break;
                        default:
                            System.out.println("\033[1;31mOpción no válida.\033[0m");
                    }
                } else if (opcion == 7) {
                    System.out.println("\033[1;32mSaliendo del conversor de monedas... ¡Gracias por usar la aplicación!\033[0m");
                } else {
                    System.out.println("\033[1;31mOpción no válida. Intente nuevamente.\033[0m");
                }

            } catch (InputMismatchException e) {
                System.out.println("\033[1;31mError: Por favor ingrese un número válido.\033[0m");
                scanner.next(); // Limpiar el buffer
                opcion = -1; // Volver a mostrar el menú
            }

        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- \033[1;36mConversor de Monedas\033[0m ---");
        System.out.println("1. Dólar a Peso Argentino");
        System.out.println("2. Peso Argentino a Dólar");
        System.out.println("3. Dólar a Real Brasileño");
        System.out.println("4. Real Brasileño a Dólar");
        System.out.println("5. Dólar a Peso Colombiano");
        System.out.println("6. Peso Colombiano a Dólar");
        System.out.println("7. Salir");
    }

    private static void convertCurrency(String from, String to, double amount) {
        try {
            String url = BASE_URL + from + "/" + to;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();

                String searchString = "\"conversion_rate\":";
                int index = responseBody.indexOf(searchString);

                if (index != -1) {
                    int startIndex = index + searchString.length();
                    int endIndex = responseBody.indexOf(",", startIndex);
                    if (endIndex == -1) {
                        endIndex = responseBody.indexOf("}", startIndex);
                    }

                    if (endIndex != -1) {
                        String rateStr = responseBody.substring(startIndex, endIndex).trim();
                        double rate = Double.parseDouble(rateStr);
                        double result = amount * rate;

                        System.out.printf("\n\033[1;32mLa tasa de cambio de %s a %s es: %.4f\033[0m%n", from, to, rate);
                        System.out.printf("\033[1;36m%.2f %s equivalen a %.2f %s\033[0m%n", amount, from, result, to);
                    } else {
                        System.out.println("\033[1;31mNo se pudo encontrar la tasa de conversión en la respuesta.\033[0m");
                    }
                } else {
                    System.out.println("\033[1;31mNo se encontró la tasa de cambio en la respuesta de la API.\033[0m");
                }
            } else {
                System.out.println("\033[1;31mError al obtener los datos de la API. Código de estado: " + response.statusCode() + "\033[0m");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\033[1;31mError al convertir la moneda.\033[0m");
        }
    }
}
