package io.github.miroslavpokorny.blog.authentication;

import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TokenAuthentication implements Authentication {
    private final UserManager userManager;

    private final static Map<String, TokenSession> tokens = new HashMap<>();

    @Autowired
    public TokenAuthentication(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String createAuthentication(String email, String password) {
        User user = userManager.getUserByEmail(email);
        if (user != null && user.isActivated() && user.isEnabled() && BCrypt.checkpw(password, user.getPassword())) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUserImpl(userManager, user);
            user.setLastSignInDate(new Date());
            userManager.updateUser(user);
            TokenSession token = new TokenSession();
            token.setUser(authenticatedUser);
            token.setTokenId(TokenSession.generateNewToken());
            tokens.put(token.getTokenId(), token);
            return token.getTokenId();
        }
        return null;
    }

    @Override
    public boolean isAuthenticate(String tokenId) {
        TokenSession token = tokens.get(tokenId);
        if (token != null) {
            if (token.isValid()) {
                token.updateValidity();
                return true;
            }
            tokens.remove(tokenId);
        }
        return false;
    }

    @Override
    public void destroyAuthentication(String tokenId) {
        tokens.remove(tokenId);
    }

    @Override
    public void createUser(String nickname, String email, String password, String surname, String name) {
        userManager.createUser(email, BCrypt.hashpw(password, BCrypt.gensalt()), nickname, name, surname);
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser(String tokenId) {
        TokenSession token = tokens.get(tokenId);
        if (token != null) {
            if (token.isValid()) {
                token.updateValidity();
                return token.getUser();
            }
        }
        return null;
    }

    @Override
    public boolean checkPassword(String plainPassword, String hashPassword) {
        return BCrypt.checkpw(plainPassword, hashPassword);
    }

    @Override
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public String generatePassword(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(ThreadLocalRandom.current().nextInt(0, chars.length())));
        }
        return password.toString();
    }
}
