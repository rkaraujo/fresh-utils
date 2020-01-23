package sofware.renato.fresh;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyGenerator {

    private static final Pattern AMEBA_PATTERN = Pattern.compile("abemafresh://abemafresh/(.*)/(.*)");

    public static void main(String[] args) throws Exception {
        String workdir = "/tmp";
        String amebafresh = "abemafresh://abemafresh/206437t2806498f02ad88f4aecd7aa2083a6374edef/2928e09b0fa322020bbe5d854eb92984";

        // app.js
        int[] abemaKey = { 1413502068, 2104980084, 1144534056, 1967279194, 2051549272, 860632952, 1464353903, 1212380503 };

        Matcher matcher = AMEBA_PATTERN.matcher(amebafresh);
        if (matcher.find()) {
            String token = matcher.group(1);
            String encryptedKey = matcher.group(2);
            System.out.println("Token: " + token);
            System.out.println("EncryptedKey: " + encryptedKey);

            ByteBuffer keyByteBuffer = ByteBuffer.allocate(abemaKey.length * 4);
            for (int abemaKeyElement : abemaKey) {
                keyByteBuffer.putInt(abemaKeyElement);
            }
            byte[] hmacSHA256KeyBytes = keyByteBuffer.array();

            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec hmacSHA256Key = new SecretKeySpec(hmacSHA256KeyBytes, "HmacSHA256");
            hmacSHA256.init(hmacSHA256Key);
            byte[] aesKey = hmacSHA256.doFinal(token.getBytes());

            SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKeySpec);
            byte[] aesBytes = cipher.doFinal(Hex.decodeHex(encryptedKey));

            Path path = Paths.get(workdir + "/program.key");
            Files.write(path, aesBytes);
        } else {
            System.out.println("Keys not found");
        }
    }

}
