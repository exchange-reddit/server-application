package com.omniversity.server.log.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity
@DiscriminatorValue("PW_CHANGE")
public class PwChangeLog extends AbstractLog{
    private String prevPw;

    public PwChangeLog(String updateUser, String updateIP, String prevPw) {
        super(updateUser, updateIP);
        this.prevPw = prevPw;
    }
}
