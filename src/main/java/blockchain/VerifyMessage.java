package blockchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class VerifyMessage {
    private List<byte[]> list;

    //The constructor of VerifyMessage class retrieves the byte arrays from the File
    //and prints the message only if the signature is verified.
    public VerifyMessage(String filename) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
        this.list = (List<byte[]>) in.readObject();
        in.close();

        /*System.out.println(verifySignature(list.get(0), list.get(1), keyFile) ? "VERIFIED MESSAGE" +
                "\n----------------\n" + new String(list.get(0)) : "Could not verify the signature.");*/
    }

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(keyFile));
        sig.update(data);

        return sig.verify(signature);
    }

    //Method to retrieve the Public Key from a file
    public PublicKey getPublic(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String printMessage(String publicKey) {
        try {
            return verifySignature(list.get(0), list.get(1), publicKey) ?
                    new String(list.get(0)) :
                    "Could not verify the signature.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


   /* public static void main(String[] args) throws Exception{
        new VerifyMessage("MyData/SignedData.txt", "KeyPair/publicKey");
    }*/
}
