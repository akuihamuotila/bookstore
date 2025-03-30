package fi.haagahelia.bookstore.domain;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void createNewUser() {
        User user = new User("user1", "$2a$10$hashedpassword", "USER");
        repository.save(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void deleteUser() {
        User user = new User("temporaryuser", "$2a$10$hash", "USER");
        repository.save(user);
        repository.delete(user);
        assertThat(repository.findById(user.getId())).isEmpty();
    }

    @Test
    void findByKäyttäjätunnus() {
        User user = new User("uniqueuser", "$2a$10$hash", "USER");
        repository.save(user);

        Optional<User> foundUser = repository.findByKäyttäjätunnus("uniqueuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getKäyttäjätunnus()).isEqualTo("uniqueuser");
    }
}