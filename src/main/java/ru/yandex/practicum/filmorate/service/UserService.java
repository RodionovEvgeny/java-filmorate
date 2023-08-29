package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.addFriends(firstUserId, secondUserId);
    }

    public void deleteFriend(Integer firstUserId, Integer secondUserId) {
        userStorage.deleteFriend(firstUserId, secondUserId);
    }

    public Set<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {
        return new HashSet<>(userStorage.getMutualFriends(firstUserId, secondUserId));
    }

    public List<User> getUsersFriends(Integer id) {
        return userStorage.getUsersFriends(id);
    }
}
