package com.runekid.appello.helper;

/**
 * Created by Runekid on 16/05/2018.
 */

public class TransactionInput {
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TransactionOutput UTXO; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
