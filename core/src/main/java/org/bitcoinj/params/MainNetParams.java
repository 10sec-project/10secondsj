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
    private static final long GENESIS_TIME = 1718000085;
    private static final long GENESIS_NONCE = 16274777;
    private static final Sha256Hash GENESIS_HASH = Sha256Hash.wrap("0000018606e1c1cf5fc028f7ea3a73ad98c69e91a1508d07ff3aeb75a6320eb1");

    public MainNetParams() {
        super();
        id = ID_MAINNET;

        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(Block.STANDARD_MAX_DIFFICULTY_TARGET);

        port = 9471;
        packetMagic = 0xfbc4b5ddL;
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
        checkpoints.put(1080, Sha256Hash.wrap("000000d3421e193f87a83638201466f99a7f80f60b21d8a52b770d7a40515a24"));
        checkpoints.put(10800, Sha256Hash.wrap("0000004ff82421870a3b127af2f82d04f2fb9677cb375a42016bd46087d9700b"));
        checkpoints.put(20520, Sha256Hash.wrap("0000002b3f7f6ed9c5b3866421ccbd6c75c3ce49b0e859fabcdff0838bbb1ab7"));
        checkpoints.put(30240, Sha256Hash.wrap("0000001e4cd5e2ad82dee4bbfc85278e1601bc18d57095a65ab6e97a362c0fe8"));
        checkpoints.put(41040, Sha256Hash.wrap("000000478a9902e1605426547744e91a254b79d1241f62ad6ec6fa1a376f82af"));
        checkpoints.put(50760, Sha256Hash.wrap("0000002aafffa98b4639972d36233e1467848f1f61f3436d7920d3dd7b94ad49"));
        checkpoints.put(60480, Sha256Hash.wrap("0000000dbdb8edb2e962c534024d7c8691002d0b63a50a3efed4ead06d2aa595"));
        checkpoints.put(70200, Sha256Hash.wrap("000000437d40c29bbbfdf4a739db402e6015af29780dfa05815ea6580cd50f06"));
        checkpoints.put(81000, Sha256Hash.wrap("00000014a5eda3377dcafa9fa2bc494afa1be5a7293e2e3e016ea25ced9629e4"));
        checkpoints.put(90720, Sha256Hash.wrap("00000012481985af3c8b0ff972efc06fa265bd432a4d1978b1607c849c0f3857"));
        checkpoints.put(100440, Sha256Hash.wrap("000000377f643c135c4c2f4c8430d139eefd55066c6b0d3ba43d6492f2850936"));
        checkpoints.put(110160, Sha256Hash.wrap("00000029ba5a1e1cb637506bb468a52c20d32150a508fbba5039fe5c627f15d5"));
        checkpoints.put(143640, Sha256Hash.wrap("000000b94e32511f2c20f5807f46e2e847f1f275391884d0155ef9ae7d063c72"));

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
