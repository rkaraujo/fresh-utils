package sofware.renato.fresh;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeyGenerator {

    public static void main(String[] args) throws IOException {
//        // decode the base64 encoded string
//        byte[] decodedKey = "9fb261e14ec837aa7907f25b0720d503".getBytes();
//
//        // rebuild key using SecretKeySpec
//        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//
//        byte[] encoded = originalKey.getEncoded();

        // TODO not right yet
        char[] chars = {159, 178, 97, 225, 78, 200, 55, 170, 121, 7, 242, 91, 7, 32, 213, 3};
        Files.write(Paths.get("E:/tmp/program.key"), new String(chars).getBytes(StandardCharsets.US_ASCII));
    }

}
