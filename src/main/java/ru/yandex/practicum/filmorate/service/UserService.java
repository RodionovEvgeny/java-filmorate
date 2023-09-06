package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

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

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(checkUsersName(user));
    }

    public User updateUser(User user) {
        return userStorage.updateUser(checkUsersName(user));
    }

    public Set<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    private User checkUsersName(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        return user;
    }
}
