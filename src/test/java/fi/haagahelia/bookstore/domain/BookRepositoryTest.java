package fi.haagahelia.bookstore.domain;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createNewBook() {
        Category category = new Category("Fantasy");
        categoryRepository.save(category);

        Book book = new Book("The Hobbit", "J.R.R. Tolkien", 1937, category);
        repository.save(book);
        assertThat(book.getId()).isNotNull();
    }

    @Test
    void deleteBook() {
        Category category = new Category("History");
        categoryRepository.save(category);

        Book book = new Book("Temporary Book", "Author", 2021, category);
        repository.save(book);
        repository.delete(book);

        assertThat(repository.findById(book.getId())).isEmpty();
    }

    @Test
    void findByNimiContainingIgnoreCase() {
        Category category = new Category("Horror");
        categoryRepository.save(category);

        Book book = new Book("It", "Stephen King", 1986, category);
        repository.save(book);

        List<Book> books = repository.findByNimiContainingIgnoreCase("it");
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getKirjailija()).isEqualTo("Stephen King");
    }
}