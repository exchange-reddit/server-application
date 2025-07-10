package com.omniversity.server.log.entity;

import jakarta.persistence.Column;

public class AccountUpdateLog extends AbstractLog{
    @Column(name = "auditContent")
    private String auditPurpose;

    public AccountUpdateLog(String updateUser, String updateIP, String auditPurpose) {
        super(updateUser, updateIP);
        this.auditPurpose = auditPurpose;
    }
}
