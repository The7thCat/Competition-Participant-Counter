import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppStarter {

    public static void main(String[] args) throws Exception {

        String pageUrl = "https://vorumaaspordiliit.ee/uritused/jooksusari/";

        Document doc = Jsoup.connect(pageUrl).get();

        String googleUrl = null;

        for (Element link : doc.select("a")) {

            String text = link.text().trim();

            if (text.contains("INTERNETIS REGISTREERUNUTE NIMEKIRI")) {

                googleUrl = link.absUrl("href");
                break;
            }
        }

        if (googleUrl == null) {
            System.out.println("Link not found");
            return;
        }

        System.out.println("Google URL: " + googleUrl);

        String csvUrl = googleUrl
                .replace("pubhtml", "pub")
                + "&output=csv";

        System.out.println("\nCSV URL: " + csvUrl);

//        System.out.println("Please enter the Google doc url you want to process.\nURL: ");
//
//        Scanner scanner = new Scanner(System.in);
//        String url = scanner.nextLine().trim();
//
//        if (url.contains("pubhtml")) {
//
//           url = url.replace("pubhtml", "pub") + "&output=csv";
//        }

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(csvUrl))
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "text/csv,text/plain,*/*")
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        String body = response.body();

        System.out.println("STATUS: " + response.statusCode());
        System.out.println("CONTENT-TYPE: "
                + response.headers().firstValue("content-type").orElse("unknown"));


        // html check
        if (body.trim().startsWith("<")) {

            System.out.println("\ngoogle returns html instead of csv.");
            return;
        }

        Map<String, Integer> counts = new HashMap<>();

        CSVParser parser = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(new StringReader(body));

        System.out.println("\nHEADERS:");
        System.out.println(parser.getHeaderNames());

        for (CSVRecord record : parser) {

            String dist = record.get("DISTANTS");

            if (dist == null || dist.isBlank()) {
                continue;
            }

            dist = dist.trim();

            counts.put(
                    dist,
                    counts.getOrDefault(dist, 0) + 1
            );
        }

        System.out.println("\n=== RESULTS ===");

        counts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e ->
                        System.out.println(e.getKey() + ": " + e.getValue())
                );
    }
}
