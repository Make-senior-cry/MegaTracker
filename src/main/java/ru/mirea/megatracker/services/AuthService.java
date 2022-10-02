package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.megatracker.models.RefreshToken;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.RefreshTokensRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UsersRepository usersRepository;
    private final RefreshTokensRepository refreshTokensRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsersRepository usersRepository, RefreshTokensRepository refreshTokensRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.refreshTokensRepository = refreshTokensRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public boolean checkForEmailExistence(String email) {
        return usersRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional
    public void checkRefreshToken(String email) {
        Optional<User> verifiableUser = usersRepository.findByEmail(email);
        if (verifiableUser.isEmpty())
            return;

        if (refreshTokensRepository.existsByUser(verifiableUser.get()))
            refreshTokensRepository.deleteByUser(verifiableUser.get());
    }

    @Transactional
    public void logOut(String refreshToken) {
        refreshTokensRepository.deleteByToken(refreshToken);
    }
}
