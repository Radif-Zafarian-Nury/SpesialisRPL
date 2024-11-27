package com.example.spesialisRPL.User;

import java.util.Optional;

public interface UserRepository {
    void saveUser(UserData userData);
    Optional<UserData> findByNik(String nik);
}
