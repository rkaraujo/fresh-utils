package sofware.renato.fresh;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PlaylistWriter {

    public static void main(String[] args) throws Exception {
        String domain = "https://movie.freshlive.tv";
        String workdir = "E:/tmp";

        Path originalPlaylistFile = Paths.get(workdir + "/original-playlist.m3u8");
        Path rewritePlaylistFile = Paths.get(workdir + "/playlist.m3u8");

        StringBuilder output = new StringBuilder();
        List<String> fileLines = Files.readAllLines(originalPlaylistFile);
        for (String fileLine : fileLines) {
            if (fileLine.startsWith("#EXT-X-KEY")) {
                output.append(fileLine.replaceFirst("\".*\"", "\"" + workdir + "/program.key\""));
            } else if (fileLine.startsWith("/")) {
                output.append(domain).append(fileLine);
            } else {
                output.append(fileLine);
            }
            output.append("\n");
        }

        Files.write(rewritePlaylistFile,
                output.toString().getBytes(StandardCharsets.UTF_8));
    }

}
