package com.omniversity.verification_service.service;


public class DefaultAccountVerificationContext extends AbstractEmailContext {
    private String token;

    // Initialize the common part of the account verification email
    // Might need to modify the 'setTemplateLocation' so that different templates can be used for varying occasions.
    @Override
    public <T> void init(String name) {
        put("name", name);
        setTemplateLocation("mailing/email-verification");
        setSubject("Complete your registration");
        setFrom("omniversity@co.gmail.com");
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

}
