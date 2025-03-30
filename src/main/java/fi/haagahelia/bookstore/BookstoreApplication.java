package fi.haagahelia.bookstore;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fi.haagahelia.bookstore.domain.Book;
import fi.haagahelia.bookstore.domain.BookRepository;
import fi.haagahelia.bookstore.domain.Category;
import fi.haagahelia.bookstore.domain.CategoryRepository;
import fi.haagahelia.bookstore.domain.User;
import fi.haagahelia.bookstore.domain.UserRepository;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(BookRepository bookRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        return (args) -> {
            // Lisätään kategoriat, jos niitä ei vielä ole
            if (categoryRepository.count() == 0) {
                Category scifi = new Category("Scifi");
                Category fantasy = new Category("Fantasy");
                Category satire = new Category("Satire");

                categoryRepository.save(scifi);
                categoryRepository.save(fantasy);
                categoryRepository.save(satire);

                // Lisätään esimerkkikirjat vain kerran
                bookRepository.save(new Book("Dune", "Frank Herbert", 1965, scifi));
                bookRepository.save(new Book("The Silmarillion", "J.R.R. Tolkien", 1977, fantasy));
                bookRepository.save(new Book("Animal Farm", "George Orwell", 1945, satire));
            }

            // Lisätään käyttäjät vain jos ne eivät ole jo tietokannassa
            Optional<User> existingUser = userRepository.findByKäyttäjätunnus("user");
            Optional<User> existingAdmin = userRepository.findByKäyttäjätunnus("admin");

            if (existingUser.isEmpty()) {
                User user = new User("user", new BCryptPasswordEncoder().encode("user123"), "USER");
                userRepository.save(user);
            }

            if (existingAdmin.isEmpty()) {
                User admin = new User("admin", new BCryptPasswordEncoder().encode("admin123"), "ADMIN");
                userRepository.save(admin);
            }
        };
    }
}