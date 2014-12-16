package nxt;

import nxt.crypto.HashFunction;
import nxt.util.Convert;
import nxt.util.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestMintCalculations {

    @Test
    public void targetCalculation() {
        byte[] target = CurrencyMint.getTarget((byte) 4, (byte) 32, 1, 0, 100000);
        Logger.logDebugMessage("initial target: " + Arrays.toString(target));
        Assert.assertEquals(32, target.length);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16}, target);

        target = CurrencyMint.getTarget((byte) 4, (byte) 32, 1, 50000, 100000);
        Logger.logDebugMessage("midway target: " + Arrays.toString(target));
        Assert.assertEquals(32, target.length);
        Assert.assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0}, target);

        target = CurrencyMint.getTarget((byte) 4, (byte) 32, 1, 100000, 100000);
        Logger.logDebugMessage("final target: " + Arrays.toString(target));
        Assert.assertEquals(32, target.length);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, target);

        target = CurrencyMint.getTarget((byte) 4, (byte) 32, 100, 100000, 100000);
        Logger.logDebugMessage("final target for 100 units: " + Arrays.toString(target));
        Assert.assertEquals(32, target.length);
        Assert.assertArrayEquals(new byte[]{92, -113, -62, -11, 40, 92, -113, -62, -11, 40, 92, -113, -62, -11, 40, 92, -113, -62, -11, 40, 92, -113, -62, -11, 40, 92, -113, 2, 0, 0, 0, 0}, target);

        target = CurrencyMint.getTarget((byte) 1, (byte) 5, 1, 0, 100000);
        Logger.logDebugMessage("very low target: " + Arrays.toString(target));
        Assert.assertEquals(32, target.length);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, target);
    }

    @Test
    public void hashing() {
        long nonce;
        for (nonce=0; nonce < Long.MAX_VALUE; nonce++) {
            if (CurrencyMint.meetsTarget(CurrencyMint.getHash(HashFunction.Keccak25.getId(), nonce, 123, 1, 1, 987),
                    CurrencyMint.getTarget((byte) 8, (byte) 16, 1, 0, 100000))) {
                break;
            }
        }
        Assert.assertEquals(149, nonce);

        for (nonce=0; nonce < Long.MAX_VALUE; nonce++) {
            if (CurrencyMint.meetsTarget(CurrencyMint.getHash(HashFunction.Keccak25.getId(), nonce, 123, 1, 1, 987),
                    CurrencyMint.getTarget((byte) 8, (byte) 16, 1, 100000, 100000))) {
                break;
            }
        }
        Assert.assertEquals(120597, nonce);

        for (nonce=0; nonce < Long.MAX_VALUE; nonce++) {
            if (CurrencyMint.meetsTarget(CurrencyMint.getHash(HashFunction.Keccak25.getId(), nonce, 123, 100, 1, 987),
                    CurrencyMint.getTarget((byte) 8, (byte) 16, 100, 0, 100000))) {
                break;
            }
        }
        Assert.assertEquals(5123, nonce);
    }

    @Test
    public void sha256() {
        byte[] hash = HashFunction.SHA256.hash(new byte[]{0x61,0x62,0x63});
        Assert.assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", Convert.toHexString(hash));
        hash = HashFunction.SHA256.hash(new byte[]{});
        Assert.assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", Convert.toHexString(hash));
    }

    @Test
    public void sha3() {
        byte[] hash = HashFunction.SHA3.hash(new byte[]{(byte)0x41, (byte)0xFB});
        Assert.assertEquals("A8EACEDA4D47B3281A795AD9E1EA2122B407BAF9AABCB9E18B5717B7873537D2".toLowerCase(), Convert.toHexString(hash));
        hash = HashFunction.SHA3.hash(new byte[]{});
        Assert.assertEquals("c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470", Convert.toHexString(hash));
    }

    @Test
    public void scrypt() {
        byte[] hash = HashFunction.SCRYPT.hash(new byte[]{(byte)0x41, (byte)0xFB});
        Assert.assertEquals("da3f4f010d772567a8896465d11df28693b244c91b8ba4bea5a30f6be572b667".toLowerCase(), Convert.toHexString(hash));
        hash = HashFunction.SCRYPT.hash(new byte[]{});
        Assert.assertEquals("0cf2967ca5c120e80b37f8f75c971842e05da107278c1058e6ffbc68911c11f1", Convert.toHexString(hash));
    }
}