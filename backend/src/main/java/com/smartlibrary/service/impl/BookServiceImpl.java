package com.smartlibrary.service.impl;

import com.smartlibrary.aop.LogExecutionTime;
import com.smartlibrary.dto.book.BookPatchRequest;
import com.smartlibrary.dto.book.BookRequest;
import com.smartlibrary.dto.book.BookResponse;
import com.smartlibrary.dto.common.PageResponse;
import com.smartlibrary.entity.Book;
import com.smartlibrary.entity.Category;
import com.smartlibrary.exception.DuplicateResourceException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.mapper.BookMapper;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.CategoryRepository;
import com.smartlibrary.repository.ReviewRepository;
import com.smartlibrary.repository.projection.BookSummary;
import com.smartlibrary.repository.spec.BookSpecifications;
import com.smartlibrary.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Book business logic.
 *
 * <p>Demonstrates {@code @Transactional} (write) vs read-only transactions,
 * dynamic filtering with {@link Specification}, pagination/sorting and an AOP
 * timed method ({@link LogExecutionTime}).</p>
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final BookMapper bookMapper;
    private final CloudinaryService cloudinaryService;

    public BookServiceImpl(BookRepository bookRepository,
                           CategoryRepository categoryRepository,
                           ReviewRepository reviewRepository,
                           BookMapper bookMapper ,CloudinaryService cloudinaryService) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
        this.bookMapper = bookMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public BookResponse create(BookRequest request) {
        return null;
    }

    @Transactional
    @Override
    public BookResponse create(BookRequest request, MultipartFile image) {

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Book book = bookMapper.toEntity(request, category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.upload(image);
            book.setImageUrl(imageUrl);
        }

        bookRepository.save(book);

        return bookMapper.toResponse(book, null);
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request, MultipartFile image) {

        Book book = findBook(id);

        if (!book.getIsbn().equals(request.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException(
                    "Book with ISBN already exists: " + request.getIsbn());
        }

        int borrowed = book.getQuantity() - book.getAvailableQuantity();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setQuantity(request.getQuantity());
        book.setAvailableQuantity(Math.max(0, request.getQuantity() - borrowed));
        book.setPrice(request.getPrice());
        book.setPublicationYear(request.getPublicationYear());
        book.setCategory(resolveCategory(request.getCategoryId()));

        if (image != null && !image.isEmpty()) {

            cloudinaryService.delete(book.getImageUrl());

            String imageUrl = cloudinaryService.upload(image);

            book.setImageUrl(imageUrl);
        }

        bookRepository.save(book);

        return toResponse(book);
    }

    @Override
    @Transactional
    public BookResponse patch(Long id,
                              BookPatchRequest request,
                              MultipartFile image) {
        Book book = findBook(id);
        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getDescription() != null) book.setDescription(request.getDescription());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublicationYear() != null) book.setPublicationYear(request.getPublicationYear());
        if (image != null && !image.isEmpty()) {
            cloudinaryService.delete(book.getImageUrl());

            String imageUrl = cloudinaryService.upload(image);

            book.setImageUrl(imageUrl);
        }
        if (request.getCategoryId() != null) book.setCategory(resolveCategory(request.getCategoryId()));
        if (request.getQuantity() != null) {
            int borrowed = book.getQuantity() - book.getAvailableQuantity();
            book.setQuantity(request.getQuantity());
            book.setAvailableQuantity(Math.max(0, request.getQuantity() - borrowed));
        }
        return toResponse(book);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = findBook(id);
        cloudinaryService.delete(book.getImageUrl());

        bookRepository.delete(book);
        log.info("Deleted book id={}", id);
    }

    @Override
    public BookResponse getById(Long id) {
        return toResponse(findBook(id));
    }

    @Override
    @LogExecutionTime
    public PageResponse<BookResponse> list(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy == null || sortBy.isBlank() ? "id" : sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> result = bookRepository.findAll(pageable);
        return PageResponse.from(result, this::toResponse);
    }

    @Override
    public PageResponse<BookResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Book> result = bookRepository.search(keyword == null ? "" : keyword, pageable);
        return PageResponse.from(result, this::toResponse);
    }

    @Override
    @LogExecutionTime
    public PageResponse<BookResponse> filter(String title, String author, Long categoryId,
                                             BigDecimal minPrice, BigDecimal maxPrice,
                                             Boolean available, int page, int size) {
        Specification<Book> spec = Specification
                .where(BookSpecifications.titleContains(title))
                .and(BookSpecifications.authorContains(author))
                .and(BookSpecifications.hasCategory(categoryId))
                .and(BookSpecifications.priceBetween(minPrice, maxPrice))
                .and(BookSpecifications.onlyAvailable(available));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Book> result = bookRepository.findAll(spec, pageable);
        return PageResponse.from(result, this::toResponse);
    }

    @Override
    public List<BookSummary> summaries() {
        return bookRepository.findAllSummaries();
    }

    @Override
    public List<BookResponse> topBorrowed(int limit) {
        return bookRepository.findTopBorrowed(limit).stream().map(this::toResponse).toList();
    }

    // ---- helpers ----

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    private BookResponse toResponse(Book book) {
        Double avg = reviewRepository.averageRating(book.getId());
        return bookMapper.toResponse(book, avg);
    }
}
