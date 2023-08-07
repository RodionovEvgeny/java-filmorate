package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer firstUserId, Integer secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        firstUser.getFriends().add(secondUser.getId());
        secondUser.getFriends().add(firstUser.getId());
    }

    public void deleteFriend(Integer firstUserId, Integer secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        firstUser.getFriends().remove(secondUser.getId());
        secondUser.getFriends().remove(firstUser.getId());
    }

    public Set<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);

        return firstUser.getFriends().stream()
                .filter(id -> secondUser.getFriends().contains(id))
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toSet());
    }

    public Set<User> getUsersFriends(Integer id) {
        return userStorage.getUserById(id).getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
