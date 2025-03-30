package fi.haagahelia.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fi.haagahelia.bookstore.domain.Book;
import fi.haagahelia.bookstore.domain.BookRepository;
import fi.haagahelia.bookstore.domain.Category;
import fi.haagahelia.bookstore.domain.CategoryRepository;

@Controller
@RequestMapping("/books") // HTML-näkymä
public class BookViewController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Näytä kaikki kirjat
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("kirjat", bookRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "kirjat";
    }

    // Lisää uusi kirja
    @PostMapping
    public String addBook(@RequestParam String nimi, @RequestParam String kirjailija, @RequestParam int publicationYear, @RequestParam Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Book book = new Book(nimi, kirjailija, publicationYear, category);
        bookRepository.save(book);
        return "redirect:/books";
    }

    // Muokkaa kirjaa (vain ADMIN)
    @PreAuthorize("hasAuthorize('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookRepository.findById(id).orElse(null));
        model.addAttribute("categories", categoryRepository.findAll());
        return "editBook";
    }

    // Poistaa kirjan (vain ADMIN)
    @PreAuthorize("hasAuthorize('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}