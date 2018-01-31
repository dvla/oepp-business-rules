package uk.gov.dvla.rules.preconditions.data;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class Closure {
    private Date date;
    private String reason;
    private Boolean debtCollectionIndicator;
    private String courtIndicator;

    private Closure(String reason) {
        this.date = new Date();
        this.reason = reason;
    }

    public static Closure withReason(String reason) {
        return new Closure(reason);
    }

    public Date getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public Boolean getDebtCollectionIndicator() {
        return debtCollectionIndicator;
    }

    public Closure withDebtCollectionIndicator(boolean debtCollectionIndicator) {
        this.debtCollectionIndicator = debtCollectionIndicator;
        return this;
    }

    public String getCourtIndicator(){
        return  courtIndicator;
    }

    public Closure withCourtIndicator(String courtIndicator) {
        this.courtIndicator = courtIndicator;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("reason", reason)
                .add("debtCollectionIndicator", debtCollectionIndicator)
                .add("courtIndicator", courtIndicator)
                .toString();
    }
}
