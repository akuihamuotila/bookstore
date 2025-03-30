package fi.haagahelia.bookstore.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void createNewCategory() {
        Category category = new Category("Science Fiction");
        repository.save(category);
        assertThat(category.getId()).isNotNull();
    }

    @Test
    void deleteCategory() {
        Category category = new Category("Temporary");
        repository.save(category);
        repository.delete(category);
        assertThat(repository.findById(category.getId())).isEmpty();
    }

    @Test
    void findCategoryById() {
        Category category = new Category("Mystery");
        repository.save(category);
        assertThat(repository.findById(category.getId())).isPresent();
        assertThat(repository.findById(category.getId()).get().getName()).isEqualTo("Mystery");
    }
}