package fi.haagahelia.bookstore.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.bookstore.domain.Book;
import fi.haagahelia.bookstore.domain.BookRepository;
import fi.haagahelia.bookstore.domain.Category;
import fi.haagahelia.bookstore.domain.CategoryRepository;
import fi.haagahelia.bookstore.domain.UserRepository;

import java.util.Optional;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "USER")
    public void testGetBookById() throws Exception {
        Book book = new Book("Test Book", "Test Author", 2020, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nimi").value("Test Book"))
                .andExpect(jsonPath("$.kirjailija").value("Test Author"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateBook() throws Exception {
        Category category = new Category("Drama");
        category.setId(1L);
        Book book = new Book("New Book", "New Author", 2024, category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nimi").value("New Book"))
                .andExpect(jsonPath("$.kirjailija").value("New Author"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBook() throws Exception {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        mockMvc.perform(delete("/api/books/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}