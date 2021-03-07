package blockchain;

import blockchain.security.GenerateKeys;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Main {

    public static void main(String[] args) throws Exception {
        Blockchain blockchain;
        File blockchainFile = new File(Blockchain.getFileName());
        if (blockchainFile.exists()) {
            blockchain = (Blockchain) SerializationUtils.deserialize(blockchainFile.getName());
            if (blockchain.isValidated()) {
                blockchain.printBlockchain();
                List<Miner> minerList = createMiners(blockchain);
                createNRead(blockchain, minerList);
            } else {
                System.out.println("Blockchain not valid");
            }

        } else {
            blockchain = new Blockchain();
            List<Miner> minerList = createMiners(blockchain);
            createNRead(blockchain, minerList);
        }
    }


    private static List<Miner> createMiners(Blockchain blockchain) {
        return IntStream.range(1, 10)
                .mapToObj(j -> new Miner(j, blockchain))
                .collect(Collectors.toList());
    }


    private static void createNRead(Blockchain blockchain, List<Miner> miners) throws Exception {
        GenerateKeys gk = new GenerateKeys(1024);
        for (int i = 0; i < 3; i++) {
            Block block = blockchain.createNewBlock();

            String data = Blockchain.messages.get(i);
            if (!new File("KeyPair/privateKey").exists() ||
                    !new File("KeyPair/privateKey").exists()) {
                gk.createKeys();
                gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
                gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
            }
            Message msg = new Message(blockchain.getNewMessageID(), data, "KeyPair/privateKey");
            blockchain.addData(msg);

            ExecutorService executor = Executors.newFixedThreadPool(miners.size());
            System.out.println("Generating new block...");
            miners.forEach(miner -> {
                miner.setBlock(block);
                executor.submit(miner);
            });

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        System.out.println("Done!");
    }
}
