package com.polarbookshop.catalogservice.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private BookService bookService;

    @BeforeEach
    void setUp() {
        this.bookService = new BookService(bookRepository);
    }

    @Test
    void viewBookList() {
        Book book = Instancio.create(Book.class);
        when(bookRepository.findAll()).thenReturn(List.of(book));
        var resp = bookService.viewBookList();
        assertThat(resp).contains(book);
    }

    @Test
    void viewBookDetails() {
        Book book = Instancio.create(Book.class);
        when(bookRepository.findByIsbn(book.isbn())).thenReturn(Optional.of(book));
        var resp = bookService.viewBookDetails(book.isbn());
        assertThat(resp.isbn()).isEqualTo(book.isbn());
    }

    @Test
    void addBookToCatalog() {
        Book book = Instancio.create(Book.class);
        when(bookRepository.save(book)).thenReturn(book);
        var resp = bookService.addBookToCatalog(book);
        verify(bookRepository).save(eq(book));
        assertThat(resp).isEqualTo(book);
    }

    @Test
    void removeBookFromCatalog() {
        Book book = Instancio.create(Book.class);
        bookService.removeBookFromCatalog(book.isbn());
        verify(bookRepository).deleteByIsbn(eq(book.isbn()));
    }

    @Test
    void editBookDetails() {
        Book book = Instancio.create(Book.class);
        Book bkEdited = new Book(book.isbn(), book.title(), book.author(), book.price());
        var resp = bookService.editBookDetails(book.isbn(), bkEdited);
        verify(bookRepository).save(eq(bkEdited));
    }
}
