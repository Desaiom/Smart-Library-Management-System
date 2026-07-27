package com.smartlibrary.bootstrap;

import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Category;
import com.smartlibrary.entity.Role;
import com.smartlibrary.entity.User;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.CategoryRepository;
import com.smartlibrary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds demo data on startup for the {@code dev} profile only.
 *
 * <p>Creates default accounts (admin / librarian / user), a few categories and
 * sample books so the app is immediately usable and demoable. Idempotent: it
 * only seeds when the users table is empty.</p>
 *
 * <p>Default credentials:</p>
 * <ul>
 *     <li>admin@library.com / admin123 (ADMIN)</li>
 *     <li>librarian@library.com / librarian123 (LIBRARIAN)</li>
 *     <li>user@library.com / user123 (USER)</li>
 * </ul>
 */
@Slf4j
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      BookRepository bookRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Data already present, skipping seeding.");
            return;
        }
        log.info("Seeding demo data (dev profile)...");

        seedUsers();
        List<Category> categories = seedCategories();
        seedBooks(categories);

        log.info("Seeding complete. Login with admin@library.com / admin123");
    }

    private void seedUsers() {
        userRepository.save(User.builder()
                .name("Library Admin").email("admin@library.com")
                .password(passwordEncoder.encode("admin123"))
                .phone("9000000001").address("HQ").role(Role.ADMIN).build());

        userRepository.save(User.builder()
                .name("Head Librarian").email("librarian@library.com")
                .password(passwordEncoder.encode("librarian123"))
                .phone("9000000002").address("Desk 1").role(Role.LIBRARIAN).build());

        userRepository.save(User.builder()
                .name("Demo Member").email("user@library.com")
                .password(passwordEncoder.encode("user123"))
                .phone("9000000003").address("City").role(Role.USER).build());
    }

    private List<Category> seedCategories() {
        Category tech = categoryRepository.save(Category.builder()
                .name("Technology").description("Programming, software and hardware").build());
        Category fiction = categoryRepository.save(Category.builder()
                .name("Fiction").description("Novels and short stories").build());
        Category science = categoryRepository.save(Category.builder()
                .name("Science").description("Physics, biology, chemistry and more").build());
        return List.of(tech, fiction, science);
    }

    private void seedBooks(List<Category> categories) {
        Category tech = categories.get(0);
        Category fiction = categories.get(1);
        Category science = categories.get(2);

        bookRepository.save(book("Effective Java", "Joshua Bloch", "9780134685991",
                "Best practices for the Java platform", 5, new BigDecimal("45.00"), 2018, tech));
        bookRepository.save(book("Clean Code", "Robert C. Martin", "9780132350884",
                "A handbook of agile software craftsmanship", 4, new BigDecimal("38.50"), 2008, tech));
        bookRepository.save(book("Spring in Action", "Craig Walls", "9781617294945",
                "Comprehensive guide to the Spring framework", 3, new BigDecimal("42.00"), 2018, tech));
        bookRepository.save(book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565",
                "A classic American novel", 6, new BigDecimal("12.99"), 1925, fiction));
        bookRepository.save(book("A Brief History of Time", "Stephen Hawking", "9780553380163",
                "From the Big Bang to black holes", 4, new BigDecimal("18.00"), 1988, science));
    }

    private Book book(String title, String author, String isbn, String description,
                      int qty, BigDecimal price, int year, Category category) {
        return Book.builder()
                .title(title).author(author).isbn(isbn).description(description)
                .quantity(qty).availableQuantity(qty).price(price)
                .publicationYear(year).category(category).build();
    }
}
