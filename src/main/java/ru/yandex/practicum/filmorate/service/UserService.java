package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(String firstUserId, String secondUserId) { // TODO добавить валидацию айдишников
        User firstUser = userStorage.getUserById(Integer.valueOf(firstUserId));
        User secondUser = userStorage.getUserById(Integer.valueOf(secondUserId));
        firstUser.getFriends().add(secondUser.getId());
        secondUser.getFriends().add(firstUser.getId());
    }

    public void deleteFriend(int firstUserId, int secondUserId) {

    }

    public void deleteFriend(User firstUser, User secondUser) {
        firstUser.getFriends().remove(secondUser.getId());
        secondUser.getFriends().remove(firstUser.getId());
    }

    public Set<Integer> getMutualFriends(int firstUserId, int secondUserId) {
        return null;
    }

    public Set<Integer> getMutualFriends(User firstUser, User secondUser) {
        return firstUser.getFriends().stream()
                .filter(id -> secondUser.getFriends().contains(id))
                .collect(Collectors.toSet());
    }
}
