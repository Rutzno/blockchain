package blockchain;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Blockchain implements Serializable {

    private static final long serialVersionUID = 12345L;

    private transient static final String fileName = "blockchain.data";
    public transient static final List<Message> streamMessages = new ArrayList<>();
    public transient static final List<Message> savedMessages = new ArrayList<>();
//    public transient static final List<Message> datas = new ArrayList<>();
    private final List<Block> blocks;
    private int difficulty;

    public Blockchain(List<Block> blocks) {
        this.blocks = blocks;

    }

    public Blockchain() {
        this.blocks = new ArrayList<>();
        this.difficulty = 0;
    }

    public static String getFileName() {
        return fileName;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Block createNewBlock() {
        int id = blocks.size() + 1;
        return blocks.size() == 0 ?
                new Block(1, "0") :
                new Block(id, getLastBlock().getHash());
    }


    public void addBlock(Block block) {
        if (isBlockValidated(getLastBlock(), block)) {
            blocks.add(block);
            savedMessages.clear();
            savedMessages.addAll(streamMessages);
            regulateNumberN();
            try {
                SerializationUtils.serialize(this, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Block not valid");
        }
    }

    public void regulateNumberN() {
        System.out.println(getLastBlock());
        if (getLastBlock().getGeneratingTime() < 15) {
            difficulty++;
            System.out.println("N was increased to " + difficulty);
        } else if (getLastBlock().getGeneratingTime() >= 15 && getLastBlock().getGeneratingTime() <= 60) {
            System.out.println("N stays the same ");
        } else {
            difficulty--;
            System.out.println("N was decreased by 1");
        }
        System.out.println();
    }

    public Block getFirstBlock() {
        return blocks.get(0);
    }

    public Block getLastBlock() {
        return blocks.size() != 0 ? blocks.get(blocks.size() - 1) : null;
    }

    public void addData(Message data) {
        if (streamMessages.equals(savedMessages)) {
            streamMessages.clear();
        }
        streamMessages.add(data);
    }

    public long getNewMessageID() {
        if (getLastBlock() == null) return 1;
        List<Message> messages = getLastBlock().getData();
        return messages.get(messages.size() - 1).getId() + 1;
    }

    public boolean isBlockValidated(Block previousBlock, Block block) {
        String zeros = "0".repeat(difficulty);

        return previousBlock == null || previousBlock.getHash().equals(block.getPreviousHash()) &&
                block.getHash().startsWith(zeros);
    }

    public boolean isValidated() {
        boolean isValid = true;
        Block previousBlock = getFirstBlock();
        for (int i = 1; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (!previousBlock.getHash().equals(block.getPreviousHash())) {
                isValid = false;
                break;
            }
            previousBlock = block;
        }
        return isValid;
    }


    public void printBlockchain() {
        blocks.forEach(block -> System.out.printf("%s%n%n", block));
    }

    public static List<String> messages = List.of("Mack: Hey, I'm first!",
            "Hawa: It's not fair!",
            "Hawa: You always will be first because it is your blockchain!",
            "Hawa: Anyway, thank you for this amazing chat.",
            "Mack: You're welcome :)",
            "Karim: Hey Mack, nice chat");
}
