package com.omniversity.server.log.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACCOUNT_DELETION")
public class AccountDeleteLog extends AbstractLog {

    @Column(name = "auditContent")
    private String auditPurpose;

    public AccountDeleteLog(String updateUser, String updateIP, String auditPurpose) {
        super(updateUser, updateIP);
        this.auditPurpose = auditPurpose;
    }
}
