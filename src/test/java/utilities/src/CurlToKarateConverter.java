package utilities.src;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CurlToKarateConverter {

    public static void main(String[] args) {
        try {
            System.out.println("Reading cURL from file...");
            String curlCommand = readCurlCommand("src\\test\\java\\utilities\\curl_input.txt");
            System.out.println("Parsing cURL...");
            ParsedCurl parsed = parseCurl(curlCommand);

            System.out.println("Generating Karate...");
            String karateFeature = generateKarateFeature(parsed);

            System.out.println("Writing to file...");
            writeToFile("src\\test\\java\\utilities\\output\\generated.feature", karateFeature);

            System.out.println("Done. Karate file generated!");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static String readCurlCommand(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    static ParsedCurl parseCurl(String curlCommand) {
        String method = "GET";
        String url = "";
        Map<String, String> headers = new LinkedHashMap<>();
        String body = "";

        String curl = curlCommand.replace("\\\n", "").replace("\\\r\n", "").replace("\r", "").trim();

        Pattern methodPattern = Pattern.compile("-X\\s+(\\w+)");
        Matcher methodMatcher = methodPattern.matcher(curl);
        if (methodMatcher.find()) {
            method = methodMatcher.group(1);
        }

        Pattern urlPattern = Pattern.compile("curl\\s+-X\\s+\\w+\\s+(\\S+)");
        Matcher urlMatcher = urlPattern.matcher(curl);
        if (urlMatcher.find()) {
            url = urlMatcher.group(1);
        }

        Pattern headerPattern = Pattern.compile("-H\\s+\"(.*?):\\s*(.*?)\"");
        Matcher headerMatcher = headerPattern.matcher(curl);
        while (headerMatcher.find()) {
            headers.put(headerMatcher.group(1), headerMatcher.group(2));
        }

        Pattern dataPattern = Pattern.compile("(-d|--data|--data-raw)\\s+('|\")(.*?)\\2", Pattern.DOTALL);
        Matcher dataMatcher = dataPattern.matcher(curl);
        if (dataMatcher.find()) {
            body = dataMatcher.group(3).trim();
        }

        return new ParsedCurl(method, url, headers, body);
    }

    static String generateKarateFeature(ParsedCurl curl) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature: Generated from cURL\n\n");

        sb.append("Background:\n");
        sb.append("  Given url '").append(curl.url).append("'\n");

        for (Map.Entry<String, String> entry : curl.headers.entrySet()) {
            sb.append("  And header ").append(entry.getKey()).append(" = '").append(entry.getValue()).append("'\n");
        }

        sb.append("\nScenario: ").append(curl.method).append(" request to ").append(curl.url).append("\n");

        if (!curl.body.isEmpty()) {
            sb.append("  And request\n");
            sb.append("    \"\"\"\n");
            sb.append(indentJsonPretty(curl.body));
            sb.append("\n    \"\"\"\n");
        }

        sb.append("  When method ").append(curl.method.toLowerCase()).append("\n");
        sb.append("  Then status ").append(getExpectedStatusCode(curl.method)).append("\n");
        sb.append("  And match response == {}  # TODO: Replace with expected response body\n");

        return sb.toString();
    }

    // Basic JSON formatter using indentation logic
    static String indentJsonPretty(String json) {
        StringBuilder result = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            switch (c) {
                case '{':
                case '[':
                    result.append(c);
                    if (!inQuotes) {
                        result.append("\n");
                        indentLevel++;
                        result.append("      ".repeat(indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        result.append("\n");
                        indentLevel--;
                        result.append("      ".repeat(indentLevel));
                    }
                    result.append(c);
                    break;
                case ',':
                    result.append(c);
                    if (!inQuotes) {
                        result.append("\n");
                        result.append("      ".repeat(indentLevel));
                    }
                    break;
                case ':':
                    result.append(c);
                    if (!inQuotes) result.append(" ");
                    break;
                case '"':
                    result.append(c);
                    if (i == 0 || json.charAt(i - 1) != '\\') {
                        inQuotes = !inQuotes;
                    }
                    break;
                default:
                    result.append(c);
            }
        }
        return result.toString();
    }

    static int getExpectedStatusCode(String method) {
        switch (method.toUpperCase()) {
            case "GET": return 200;
            case "POST": return 201;
            case "PUT": return 200;
            case "DELETE": return 204;
            default: return 200;
        }
    }

    static void writeToFile(String path, String content) throws IOException {
        Files.createDirectories(Path.of(path).getParent());
        Files.writeString(Path.of(path), content);
    }

    static class ParsedCurl {
        String method;
        String url;
        Map<String, String> headers;
        String body;

        ParsedCurl(String method, String url, Map<String, String> headers, String body) {
            this.method = method;
            this.url = url;
            this.headers = headers;
            this.body = body;
        }
    }
}
