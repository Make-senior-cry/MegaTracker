package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.RefreshTokensRepository;
import ru.mirea.megatracker.repositories.UsersRepository;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.util.UnconfirmedPasswordException;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UsersRepository usersRepository;
    private final RefreshTokensRepository refreshTokensRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UsersRepository usersRepository, RefreshTokensRepository refreshTokensRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.refreshTokensRepository = refreshTokensRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public boolean checkForEmailExistence(String email) {
        return usersRepository.existsByEmail(email);
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

    @Transactional
    public void updatePassword(String oldPassword, String newPassword, String newPasswordRepeat, String token) {
        Optional<User> user = usersRepository.findByEmail(jwtUtil.getUsernameFromJwtToken(token));

        if (user.isPresent()) {
            String passwordToChange = user.get().getPassword();
            if (newPassword.equals(newPasswordRepeat) && passwordEncoder.matches(oldPassword, passwordToChange)) {
                user.get().setPassword(passwordEncoder.encode(newPassword));
                usersRepository.save(user.get());
            }
            else {
                throw new UnconfirmedPasswordException("Password does not match");
            }
        }
    }
}
