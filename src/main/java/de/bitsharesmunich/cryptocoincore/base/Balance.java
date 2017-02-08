package de.bitsharesmunich.cryptocoincore.base;

import java.util.Date;

/**
 *
 * @author Henry
 */
public class Balance {

    protected Coin type;
    protected Date date;
    protected double ammount;

    public Coin getType() {
        return type;
    }

    public void setType(Coin type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

}
