package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_site",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "site_id")})
    private Set<Site> sites;

    private boolean enabled = true;
    private String  firstName;
    private String  lastName;
    private String  username;
    private String  password;
    private String  email;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private Integer      documentNumber;

    public void updateFromUser(User user) {
//        if (user.role != null) this.role = user.role;
//        if (user.sites != null) this.sites.addAll(user.sites);
        if (user.firstName != null) this.firstName = user.firstName;
        if (user.lastName != null) this.lastName = user.lastName;
        if (user.username != null) this.username = user.username;
        if (user.documentType != null) this.documentType = user.documentType;
        if (user.documentNumber != null) this.documentNumber = user.documentNumber;
//        if (user.password != null) this.password = user.password;
        if (user.email != null) this.email = user.email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Site> getSites() {
        return sites;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(Integer documentNumber) {
        this.documentNumber = documentNumber;
    }

    public enum DocumentType {CC, CE}


    public enum Role {Owner, Administrator, Crew}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", documentType=" + documentType +
                ", documentNumber=" + documentNumber +
                '}';
    }

    public String toConsole() {
        return "id=" +
               id +
               ", \nenabled=" +
               enabled +
               ", \nfirstName='" +
               firstName +
               '\'' +
               ", \nlastName='" +
               lastName +
               '\'' +
               ", \nusername='" +
               username +
               '\'' +
               ", \nemail='" +
               email +
               '\'' +
               ", \ndocumentType=" +
               documentType +
               ", \ndocumentNumber=" +
               documentNumber;
    }
}
