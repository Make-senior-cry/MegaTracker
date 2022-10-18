package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.RefreshTokensRepository;
import ru.mirea.megatracker.repositories.UsersRepository;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.exceptions.UnconfirmedPasswordException;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements ru.mirea.megatracker.interfaces.AuthService {
    private final UsersRepository usersRepository;
    private final RefreshTokensRepository refreshTokensRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UsersRepository usersRepository, RefreshTokensRepository refreshTokensRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.refreshTokensRepository = refreshTokensRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public void register(User user) {
        hashUserPasswordAndSave(user);
    }

    @Override
    public boolean checkForEmailExistence(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void checkRefreshToken(String email) {
        Optional<User> maybeUser = usersRepository.findByEmail(email);
        if (maybeUser.isEmpty()) return;
        User user = maybeUser.get();

        if (refreshTokensRepository.existsByUser(user)) {
            refreshTokensRepository.deleteByUser(user);
        }
    }

    @Transactional
    @Override
    public void logOut(String refreshToken) {
        refreshTokensRepository.deleteByToken(refreshToken);
    }

    @Transactional
    @Override
    public void updatePassword(String oldPassword, String newPassword, String newPasswordRepeat, String token) {
        String userEmail = jwtUtil.getUsernameFromJwtToken(token);
        Optional<User> maybeUser = usersRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty()) return;
        User user = maybeUser.get();

        String passwordToChange = user.getPassword();

        // User cannot update the password
        if (newPassword.equals(newPasswordRepeat) && passwordEncoder.matches(oldPassword, passwordToChange)) {
            throw new UnconfirmedPasswordException();
        }
        user.setPassword(newPassword);
        hashUserPasswordAndSave(user);
    }

    private void hashUserPasswordAndSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }
}
