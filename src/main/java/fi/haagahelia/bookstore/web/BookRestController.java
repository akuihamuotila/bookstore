package fi.haagahelia.bookstore.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.bookstore.domain.Book;
import fi.haagahelia.bookstore.domain.BookRepository;
import fi.haagahelia.bookstore.domain.Category;
import fi.haagahelia.bookstore.domain.CategoryRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class BookRestController {

    @Autowired
    private BookRepository repository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    // Hakee kaikki kirjat JSON-muodossa
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    // Hakee yhden kirjan ID:n perusteella JSON-muodossa
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lisää uuden kirjan JSONin kautta
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(book.getCategory().getId());
            category.ifPresent(book::setCategory);
        }
        Book savedBook = repository.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // Päivittää kirjan tiedot
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return repository.findById(id).map(book -> {
            book.setNimi(bookDetails.getNimi());
            book.setKirjailija(bookDetails.getKirjailija());
            book.setPublicationYear(bookDetails.getPublicationYear());

            // Tarkistetaan ja asetetaan oikea kategoria
            if (bookDetails.getCategory() != null && bookDetails.getCategory().getId() != null) {
                Optional<Category> category = categoryRepository.findById(bookDetails.getCategory().getId());
                category.ifPresent(book::setCategory);
            }

            return ResponseEntity.ok(repository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Poistaa kirjan tietokannasta
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}