package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "pk.bill",
                joinColumns = @JoinColumn(name = "bill_id")),
        @AssociationOverride(name = "pk.user",
                joinColumns = @JoinColumn(name = "user_id"))})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pk")
public class Bill_User {

    private Bill_User_Id pk;
    private Site         site;

    @EmbeddedId
    public Bill_User_Id getPk() {
        return pk;
    }

    public void setPk(Bill_User_Id pk) {
        this.pk = pk;
    }

    @Transient
    public Bill getBill() {
        return pk.getBill();
    }

    public void setBill(Bill bill) {
        this.pk.setBill(bill);
    }

    @Transient
    public User getUser() {
        return pk.getUser();
    }

    public void setUser(User user) {
        this.pk.setUser(user);
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Bill_User that = (Bill_User) o;
        return getPk() != null ? getPk().equals(that.getPk()) : that.getPk() == null;
    }

}