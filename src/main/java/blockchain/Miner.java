package blockchain;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 03/19/2020
 */

public class Miner implements Runnable {

    private final int id;
    private final Blockchain blockchain;
    private Block block;

    public Miner(int id, Blockchain blockchain) {
        this.id = id;
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        int difficulty = blockchain.getDifficulty();
        boolean mined;

        long startTime = System.currentTimeMillis();
        block.setData(Blockchain.savedMessages);
        mined = block.mine(difficulty);

        long endTime = System.currentTimeMillis();
        long timeInSec = (endTime - startTime) / 1000;

        if (mined) {
            block.setMiner(id);
            block.setGeneratingTime(timeInSec);
            blockchain.addBlock(block);
        }
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
