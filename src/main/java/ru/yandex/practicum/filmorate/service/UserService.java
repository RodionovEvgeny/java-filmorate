package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    public void addToFriends(int firstUserId, int secondUserId) {

    }

    public void addToFriends(User firstUser, User secondUser) {
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
