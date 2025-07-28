import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject; // External library (JSON parsing)

public class WeatherApp {

    // Replace YOUR_API_KEY with your OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();

        try {
            String response = fetchWeatherData(city);
            if (response != null) {
                displayWeather(response);
            } else {
                System.out.println("Could not fetch weather data.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    // Method to fetch weather data
    private static String fetchWeatherData(String city) throws Exception {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // Success
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("HTTP error: " + responseCode);
            return null;
        }
    }

    // Method to display weather data
    private static void displayWeather(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);

        String cityName = obj.getString("name");
        JSONObject main = obj.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = obj.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("\n Weather in " + cityName + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Condition: " + description);
    }
}