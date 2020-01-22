package sofware.renato.fresh;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class KeyExtractor {

    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String url = "https://movie.freshlive.tv/manifest/275557/archive.m3u8?token=275557t22125fe85a4c59f950ea0d60e30bb11c7538&version=2&beta4k=";
        String domain = "https://movie.freshlive.tv";
        String outputDir = "/tmp";

        System.out.println("Downloading archive file");
        String archiveData = fetchUrlData(url, client);

        String bestUri = getBestUri(archiveData);
        if (bestUri.startsWith("/")) {
            bestUri = domain + bestUri;
        }
        System.out.println("Best URI found: " + bestUri);

        System.out.println("Fetching playlist file");
        String playlistFile = fetchUrlData(bestUri, client);

        Path path = Paths.get(outputDir + "/original-playlist.m3u8");
        System.out.println("Fetching playlist file: " + path);
        writePlaylistToFile(playlistFile, path);

        System.out.println("Extracting key");
        String keyUri = getKeyUri(playlistFile);

        System.out.println(keyUri);
    }

    private static void writePlaylistToFile(String playlistFile, Path filename) throws IOException {
        Files.write(filename, playlistFile.getBytes());
    }

    private static String getKeyUri(String playlistFile) {
        Scanner scanner = new Scanner(playlistFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#EXT-X-KEY")) {
                return getValue(line, "URI").replace("\"", "");
            }
        }
        return null;
    }

    private static String getBestUri(String archiveData) {
        String bestUri = null;
        int maxBandwith = 0;

        Scanner scanner = new Scanner(archiveData);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#EXT-X-STREAM-INF")) {
                int bandwidth = getBandwidth(line);
                if (bandwidth > maxBandwith) {
                    if (scanner.hasNextLine()) {
                        maxBandwith = bandwidth;
                        bestUri = scanner.nextLine();
                    }
                }
            }
        }

        return bestUri;
    }

    private static int getBandwidth(String line) {
        return Integer.parseInt(getValue(line, "BANDWIDTH"));
    }

    private static String getValue(String line, String key) {
        String[] infos = line.split(",");
        for (String info : infos) {
            if (info.startsWith(key)) {
                String[] keyValue = info.split("=");
                return keyValue[1];
            }
        }
        return null;
    }

    private static String fetchUrlData(String url, OkHttpClient client) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        String data = null;
        try (Response response = client.newCall(request).execute()) {
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
