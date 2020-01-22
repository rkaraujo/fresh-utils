package sofware.renato.fresh;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class PlaylistWriter {

    public static void main(String[] args) throws Exception {
        String domain = "https://movie.freshlive.tv";

        Path originalPlaylistFile = Paths.get("/tmp/original-playlist.m3u8");
        Path rewritePlaylistFile = Paths.get("/tmp/playlist.m3u8");

        List<String> fileLines = Files.readAllLines(originalPlaylistFile);
        for (int i = 0; i < fileLines.size(); i++) {
            String fileLine = fileLines.get(i);

            StandardOpenOption[] options = { StandardOpenOption.APPEND };
            if (i == 0) {
                options = new StandardOpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };
            }

            String outputLine = null;
            if (fileLine.startsWith("#EXT-X-KEY")) {
                outputLine = fileLine.replaceFirst("\".*\"", "\"/tmp/program.key\"");
            } else if (fileLine.startsWith("/")) {
                outputLine = domain + fileLine;
            } else {
                outputLine = fileLine;
            }
            outputLine = outputLine + "\n";

            Files.write(rewritePlaylistFile,
                    outputLine.getBytes(StandardCharsets.UTF_8),
                    options);
        }
    }

}
