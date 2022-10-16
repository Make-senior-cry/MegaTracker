package ru.mirea.megatracker.interfaces;

import ru.mirea.megatracker.models.User;

public interface IAuthService {
    public void register(User user);
    public boolean checkForEmailExistence(String email);
    public void checkRefreshToken(String email);
    public void logOut(String refreshToken);
    public void updatePassword(String oldPassword, String newPassword, String newPasswordRepeat, String token);
}
