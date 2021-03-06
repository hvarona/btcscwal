package de.bitsharesmunich.cryptocoincore.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Henry
 */
public class GeneralTransaction {

    private long id = -1;
    private String txid;
    private Coin type;
    private long block;
    private long fee;
    private int confirm;
    private Date date;
    private int blockHeight;
    private String memo = null;

    private List<GTxIO> txInputs = new ArrayList();
    private List<GTxIO> txOutputs = new ArrayList();

    public GeneralTransaction() {
    }

    public GeneralTransaction(long id, String txid, Coin type, long block, long fee, int confirm, Date date, int blockHeight, String memo) {
        this.id = id;
        this.txid = txid;
        this.type = type;
        this.block = block;
        this.fee = fee;
        this.confirm = confirm;
        this.date = date;
        this.blockHeight = blockHeight;
        this.memo = memo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTxid() {        return txid;    }

    public void setTxid(String txid) {        this.txid = txid;    }

    public Coin getType() {
        return type;
    }

    public void setType(Coin type) {
        this.type = type;
    }

    public long getBlock() {
        return block;
    }

    public void setBlock(long block) {
        this.block = block;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<GTxIO> getTxInputs() {
        return txInputs;
    }

    public void setTxInputs(List<GTxIO> txInputs) {
        this.txInputs = txInputs;
    }

    public List<GTxIO> getTxOutputs() {
        return txOutputs;
    }

    public void setTxOutputs(List<GTxIO> txOutputs) {
        this.txOutputs = txOutputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralTransaction that = (GeneralTransaction) o;

        if (txid != null ? !txid.equals(that.txid) : that.txid != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = txid != null ? txid.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }

    public double getAccountBalanceChange(){
        double balance = 0;
        boolean theresAccountInput = false;

        for (GTxIO txInputs : this.getTxInputs()){
            if (txInputs.isOut() && (txInputs.getAddress() != null)){
                balance += -txInputs.getAmount();
                theresAccountInput = true;
            }
        }

        for (GTxIO txOutput : this.getTxOutputs()){
            if (!txOutput.isOut() && (txOutput.getAddress() != null)){
                balance += txOutput.getAmount();
            }
        }

        if (theresAccountInput){
            balance += -this.getFee();
        }

        return balance;
    }

}
