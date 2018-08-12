package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Bill_User_Id implements Serializable {

    private Bill bill;
    private User user;

    @Override
    public int hashCode() {
        return Objects.hash(bill, user);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Bill_User_Id that = (Bill_User_Id) object;
        return Objects.equals(bill, that.bill) &&
                Objects.equals(user, that.user);
    }

    @ManyToOne
    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
