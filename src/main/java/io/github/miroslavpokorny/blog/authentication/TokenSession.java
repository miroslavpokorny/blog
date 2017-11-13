package io.github.miroslavpokorny.blog.authentication;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.util.Base64Utils;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Date;

public class TokenSession {
    static final int TOKEN_BYTES = 40;
    static final long ONE_MINUTE_IN_MILLI_SECONDS = 60000;
    public static final int TOKEN_AUTHENTICATION_VALIDITY_IN_MINUTES = 30;

    private AuthenticatedUser user;
    private String tokenId;
    private Date validity;

    public TokenSession() {
        updateValidity(TOKEN_AUTHENTICATION_VALIDITY_IN_MINUTES);
    }

    public static String generateNewToken() {
        return Base64Utils.encodeToUrlSafeString(KeyGenerators.secureRandom(TOKEN_BYTES).generateKey());
    }

    public AuthenticatedUser getUser() {
        return user;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public void updateValidity() {
        this.updateValidity(TOKEN_AUTHENTICATION_VALIDITY_IN_MINUTES);
    }

    public void updateValidity(int minutes) {
        Calendar date = Calendar.getInstance();
        long time = date.getTimeInMillis();
        this.validity = new Date(time + minutes * ONE_MINUTE_IN_MILLI_SECONDS);
    }

    public boolean isValid() {
        Calendar date = Calendar.getInstance();
        long time = date.getTimeInMillis();
        return validity.getTime() - date.getTimeInMillis() > 0;
    }
}
