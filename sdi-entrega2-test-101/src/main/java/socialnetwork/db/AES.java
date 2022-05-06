package socialnetwork.db;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static String secret="abcdefg";


    public static void main(String[] args) {
        String pass="I love cupcakes";
        System.out.println("\tencriptando: " + pass);
        String encriptado = encrypt(pass);
        System.out.println("Expected");
        System.out.println("c0fa1bc00531bd78ef38c628449c5102aeabd49b5dc3a2a516ea6ea959d6658e");
        System.out.println("Result");
        System.out.println(encriptado);

    }



    public static String encrypt(final String strToEncrypt) {

            try {
                byte[] hmacSha256 = HMAC.calcHmacSha256(secret.getBytes("UTF-8"), strToEncrypt.getBytes("UTF-8"));
                return String.format("%064x", new BigInteger(1, hmacSha256));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        return null;
    }

//    public static String decrypt(final String strToDecrypt) {
//        try {
//            setKey(secret);
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            return new String(cipher.doFinal(Base64.getDecoder()
//                    .decode(strToDecrypt)));
//        } catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return null;
//    }


}
class HMAC {
    static public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }
}