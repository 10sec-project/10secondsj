/*
 * Copyright 2013 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import java.net.URI;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.net.discovery.HttpDiscovery;


import static com.google.common.base.Preconditions.checkState;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends AbstractBitcoinNetParams {
    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;
    private static final long GENESIS_TIME = 1720842645;
    private static final long GENESIS_NONCE = 10190520;
    private static final Sha256Hash GENESIS_HASH = Sha256Hash.wrap("00000181816d9769a0279716ec893f4aa6d5a60c49efd7c85c7480d729846abe");

    public MainNetParams() {
        super();
        id = ID_MAINNET;

        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(Block.STANDARD_MAX_DIFFICULTY_TARGET);

        port = 9471;
        packetMagic = 0xfdc6b7dfL;
        dumpedPrivateKeyHeader = 65;
        addressHeader = 65;
        p2shHeader = 5;
        segwitAddressHrp = "tsec";
        spendableCoinbaseDepth = 180;
        bip32HeaderP2PKHpub = 0x0488b21e; // The 4 byte header that serializes in base58 to "xpub".
        bip32HeaderP2PKHpriv = 0x0488ade4; // The 4 byte header that serializes in base58 to "xprv"
        bip32HeaderP2WPKHpub = 0x04b24746; // The 4 byte header that serializes in base58 to "zpub".
        bip32HeaderP2WPKHpriv = 0x04b2430c; // The 4 byte header that serializes in base58 to "zprv"

        majorityEnforceBlockUpgrade = MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = MAINNET_MAJORITY_WINDOW;

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
        checkpoints.put(0, Sha256Hash.wrap("00000181816d9769a0279716ec893f4aa6d5a60c49efd7c85c7480d729846abe"));

        dnsSeeds = new String[] {
                "dns1.10seconds.info",
                "dns2.10seconds-seed.info",
                "dns3.10seconds-node.info",
        };
        httpSeeds = new HttpDiscovery.Details[] {
        };

        // These are in big-endian format, which is what the SeedPeers code expects.
        // Updated Apr. 11th 2019
        addrSeeds = new int[] {
                // dns1.10seconds.info
                0x3b1172d2,
                // dns2.10seconds-seed.info
                0xc5b07eaf,
                // dns3.10seconds-node.info
                0x28ddcd77,
        };
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public Block getGenesisBlock() {
        synchronized (GENESIS_HASH) {
            if (genesisBlock == null) {
                genesisBlock = Block.createGenesis(this);
                genesisBlock.setDifficultyTarget(Block.STANDARD_MAX_DIFFICULTY_TARGET);
                genesisBlock.setTime(GENESIS_TIME);
                genesisBlock.setNonce(GENESIS_NONCE);
                checkState(genesisBlock.getHash().equals(GENESIS_HASH), "Invalid genesis hash");
            }
        }
        return genesisBlock;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
