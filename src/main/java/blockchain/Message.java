package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Message implements Serializable{
    private long id;
    private String data;
    private List<byte[]> list;

    public Message(long id, String data, String keyFile) throws Exception {
        this.data = data;
        this.id = id;
        list = new ArrayList<>();
        list.add(data.getBytes());
        list.add(sign(id, data, keyFile));
    }

    public long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    private byte[] sign(long id, String data, String keyFile) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(keyFile));
        rsa.update((id + data).getBytes());
        return rsa.sign();
    }

    //Method to retrieve the Private Key from a file
    public PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    //Method to retrieve the Public Key from a file
    public PublicKey getPublic(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

     void writeToFile(String filename) throws FileNotFoundException, IOException {
        File f = new File(filename);
        f.getParentFile().mkdirs();
        try (var fos = new FileOutputStream(filename);
             var out = new ObjectOutputStream(fos)) {
            out.writeObject(list);
        }
//        System.out.println("Your file is ready.");
    }

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(keyFile));
        sig.update(data);

        return sig.verify(signature);
    }


  /*  public static void main(String[] args) throws Exception {
        String data = "Mack: Hello kiddo!";
        new Message(1, data, "KeyPair/privateKey").writeToFile("MyData/SignedData.txt");
    }*/
}
