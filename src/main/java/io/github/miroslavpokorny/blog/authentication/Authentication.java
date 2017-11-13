package io.github.miroslavpokorny.blog.authentication;

import javax.servlet.http.Cookie;

public interface Authentication {
    /**
     * @param tokenId
     * @return true if user is authenticated in current browser session
     */
    boolean isAuthenticate(String tokenId);

    /**
     * Crete new authentication for user
     * @param email email of user (Unique identifier)
     * @param password user password (in plain text format)
     * @return tokenId if user exist and password is correct or NULL if user does not exist or password is invalid
     */
    String createAuthentication(String email, String password);

    /**
     * @param tokenId
     * Destroy authentication on server side, user may be un authenticated after executing this method
     */
    void destroyAuthentication(String tokenId);

    /**
     * Try to create new user in data storage
     * @param nickname nickname of user (must be unique in data store -> may throw exception)
     * @param email email of user (must be unique in data store -> may throw exception)
     * @param password plain text (un hashed) password
     * @param surname surname of user (optional, can be null)
     * @param name name of user (optional, can be null)
     */
    void createUser(String nickname, String email, String password, String surname, String name);

    /**
     * Return authenticated user object
     * @param tokenId
     * @return AuthenticatedUser if user is authenticated or null if user is not authenticated
     */
    AuthenticatedUser getAuthenticatedUser(String tokenId);

    /**
     * Check if plain text password is correct for has
     * @param plainPassword plain text password
     * @param hashPassword hashed password
     * @return true if password are correct, otherwise false
     */
    boolean checkPassword(String plainPassword, String hashPassword);

    /**
     * Create hash for plain text password
     * @param plainPassword plain text password
     * @return return hashed password
     */
    String hashPassword(String plainPassword);

    /**
     * Generate new password
     * @param length length of password
     * @return return new random password
     */
    String generatePassword(int length);
}
