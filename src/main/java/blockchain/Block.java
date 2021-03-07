package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Block implements Serializable {

    private static final long serialVersionUID = 12L;
    private static final Random random = new Random();

    private int miner;
    private final int id;
    private final long timestamp;
    private final String previousHash;
    private long magicNumber;
    private String hash;
    private List<Message> data;
    private long generatingTime;

    public Block(int id, String prevHash) {
        this.id = id;
        this.timestamp = new Date().getTime();
        this.previousHash = prevHash;
        data = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public long getGeneratingTime() {
        return generatingTime;
    }

    public void setGeneratingTime(long generatingTime) {
        this.generatingTime = generatingTime;
    }

    public int getMiner() {
        return miner;
    }

    public void setMiner(int miner) {
        this.miner = miner;
    }

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }

    /**
     * Generate Hash that start with difficulty zeros (difficulty)
     * @param difficulty
     * @return boolean
     */
    public boolean mine(int difficulty) {
        String input;
        String zeros = "0".repeat(difficulty);
        String resultHash;
        int rndNumber;
        do {
            rndNumber = random.nextInt(100000000);
            input = String.format("%d%d%s%s%d", id, timestamp, previousHash, data, rndNumber);
            resultHash = StringUtil.applySha256(input);
            synchronized (this) {
                if (hash != null) return false;
            }
        } while (!resultHash.startsWith(zeros));
        hash = resultHash;
        magicNumber = rndNumber;
        return true;
    }

    @Override
    public String toString() {
        String sbData = data.isEmpty() ?
                "no messages" :
                data.stream()
                        .map(msg -> "\n" + msg.getData())
                        .collect(Collectors.joining());

        return "Block:\n" +
                "Created by miner # " + miner +"\n" +
                "Id: " + id +"\n" +
                "Timestamp: " + timestamp +"\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + previousHash + "\n" +
                "Hash of the block:\n" + hash + "\n" +
                "Block data: " + sbData + "\n" +
                "Block was generating for " + generatingTime + " seconds";
    }
}
