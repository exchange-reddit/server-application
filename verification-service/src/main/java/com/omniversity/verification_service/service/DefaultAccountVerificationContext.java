package com.omniversity.verification_service.service;

// This is just for the account verification email.
// For password reset email, please check the DefaultPasswordResetContext.java (To be implemented)
public class DefaultAccountVerificationContext extends AbstractEmailContext {
    private String code;

    // Initialize the common part of the account verification email
    // Might need to modify the 'setTemplateLocation' so that different templates can be used for varying occasions.
    @Override
    public <T> void init(String name) {
        put("name", name);
        // Defines the location of the template
        setTemplateLocation("../../resources/templates/mailing/email_verification_en.html");
        // Defines the title (subject) of the email
        setSubject("Complete your registration");
        // Defines the sender of the email
        setFrom("omniversity.co@gmail.com");
    }

    public void setToken(String code) {
        this.code = code;
        put("token", code);
    }

}
