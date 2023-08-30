package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class DbUserStorageTest {
    private final User user1 = new User("1email@email.com",
            "login1", LocalDate.now().minusYears(20), 0, "Name1");
    private final User user2 = new User("2email@email.com",
            "login2", LocalDate.now().minusYears(20), 0, "Name2");
    private final User user3 = new User("3email@email.com",
            "login3", LocalDate.now().minusYears(20), 0, "Name3");

    @Autowired
    private UserStorage userStorage;

    @AfterEach
    void tearDown() {
        userStorage.deleteAllUsers();
    }

    @Test
    void addUser() {
        User user = userStorage.addUser(user1);

        assertThat(user).hasFieldOrPropertyWithValue("name", user1.getName());
        assertThat(user).hasFieldOrPropertyWithValue("login", user1.getLogin());
        assertThat(user).hasFieldOrPropertyWithValue("birthday", user1.getBirthday());
        assertThat(user).hasFieldOrPropertyWithValue("email", user1.getEmail());
    }

    @Test
    void updateUser() {
        int id = userStorage.addUser(user1).getId();
        user2.setId(id);
        User user = userStorage.updateUser(user2);

        assertThat(user).hasFieldOrPropertyWithValue("id", id);
        assertThat(user).hasFieldOrPropertyWithValue("name", user2.getName());
        assertThat(user).hasFieldOrPropertyWithValue("login", user2.getLogin());
        assertThat(user).hasFieldOrPropertyWithValue("birthday", user2.getBirthday());
        assertThat(user).hasFieldOrPropertyWithValue("email", user2.getEmail());
        assertThat(userStorage.getAllUsers()).hasSize(1);
    }

    @Test
    void getAllUsers() {
        userStorage.addUser(user1);
        assertThat(userStorage.getAllUsers()).hasSize(1);
        userStorage.addUser(user2);
        Set<User> users = userStorage.getAllUsers();
        assertThat(users).hasSize(2);
    }

    @Test
    void getUserById() {
        int id = userStorage.addUser(user1).getId();
        User user = userStorage.getUserById(id);
        assertThat(user).hasFieldOrPropertyWithValue("id", id);
        assertThat(user).hasFieldOrPropertyWithValue("name", user1.getName());
        assertThat(user).hasFieldOrPropertyWithValue("login", user1.getLogin());
        assertThat(user).hasFieldOrPropertyWithValue("birthday", user1.getBirthday());
        assertThat(user).hasFieldOrPropertyWithValue("email", user1.getEmail());
    }

    @Test
    void addFriends() {
        int firstUserId = userStorage.addUser(user1).getId();
        int secondUserId = userStorage.addUser(user2).getId();

        userStorage.addFriends(firstUserId, secondUserId);
        List<User> friends = userStorage.getUsersFriends(firstUserId);

        assertThat(friends).hasSize(1);
    }

    @Test
    void getUsersFriends() {
        int firstUserId = userStorage.addUser(user1).getId();
        int secondUserId = userStorage.addUser(user2).getId();
        int thirdUserId = userStorage.addUser(user3).getId();

        userStorage.addFriends(firstUserId, secondUserId);
        userStorage.addFriends(firstUserId, thirdUserId);
        List<User> friends = userStorage.getUsersFriends(firstUserId);

        assertThat(friends).hasSize(2);
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("id", secondUserId);
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("name", user2.getName());
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("login", user2.getLogin());
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("birthday", user2.getBirthday());
        assertThat(friends.get(0)).hasFieldOrPropertyWithValue("email", user2.getEmail());
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("id", thirdUserId);
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("name", user3.getName());
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("login", user3.getLogin());
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("birthday", user3.getBirthday());
        assertThat(friends.get(1)).hasFieldOrPropertyWithValue("email", user3.getEmail());
    }

    @Test
    void getMutualFriends() {
        int firstUserId = userStorage.addUser(user1).getId();
        int secondUserId = userStorage.addUser(user2).getId();
        int thirdUserId = userStorage.addUser(user3).getId();

        userStorage.addFriends(firstUserId, secondUserId);
        userStorage.addFriends(thirdUserId, secondUserId);
        List<User> mutualFriends = userStorage.getMutualFriends(firstUserId, thirdUserId);

        assertThat(mutualFriends).hasSize(1);
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("id", secondUserId);
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("name", user2.getName());
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("login", user2.getLogin());
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("birthday", user2.getBirthday());
        assertThat(mutualFriends.get(0)).hasFieldOrPropertyWithValue("email", user2.getEmail());
    }

    @Test
    void deleteFriend() {
        int firstUserId = userStorage.addUser(user1).getId();
        int secondUserId = userStorage.addUser(user2).getId();

        userStorage.addFriends(firstUserId, secondUserId);
        List<User> friends = userStorage.getUsersFriends(firstUserId);
        assertThat(friends).hasSize(1);

        userStorage.deleteFriend(firstUserId, secondUserId);
        List<User> friendsAfterDeletion = userStorage.getUsersFriends(firstUserId);
        assertThat(friendsAfterDeletion).hasSize(0);
    }
}