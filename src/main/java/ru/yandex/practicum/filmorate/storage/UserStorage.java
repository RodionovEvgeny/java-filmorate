package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Set<User> getAllUsers();

    void deleteAllUsers();

    User getUserById(Integer id);

    void addFriends(int firstUserId, int secondUserId);

    List<User> getUsersFriends(int userId);

    List<User> getMutualFriends(Integer firstUserId, Integer secondUserId);

    void deleteFriend(Integer firstUserId, Integer secondUserId);
}
