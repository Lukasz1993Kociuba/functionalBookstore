package com.example.functionalBookstore.infrastructure;

import com.example.functionalBookstore.domain.inventory.core.BookService;
import com.example.functionalBookstore.domain.inventory.core.model.Book;
import com.example.functionalBookstore.domain.inventory.core.ports.incoming.AddNewBook;
import com.example.functionalBookstore.domain.inventory.core.ports.incoming.GetBookById;
import com.example.functionalBookstore.domain.inventory.core.ports.incoming.GetBooksPaginatedAndSorted;
import com.example.functionalBookstore.domain.inventory.core.ports.outgoing.BookDatabase;
import com.example.functionalBookstore.domain.inventory.infrastructure.BookDatabaseAdapter;
import com.example.functionalBookstore.domain.inventory.infrastructure.BookRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryDomainConfig {

    @Bean
    public BookDatabase bookDatabase(BookRepository bookRepository) {
        return new BookDatabaseAdapter(bookRepository);
    }

    @Bean
    @Qualifier("AddNewBook")
    public AddNewBook addNewBook(BookDatabase bookDatabase) {
        return new BookService(bookDatabase);
    }

    @Bean
    @Qualifier("GetBookById")
    public GetBookById getBookById(BookDatabase bookDatabase) {
        return new BookService(bookDatabase);
    }

    @Bean
    @Qualifier("GetBooksPaginatedAndSorted")
    public GetBooksPaginatedAndSorted getBooksPaginatedAndSorted(BookDatabase bookDatabase) {
        return new BookService(bookDatabase);
    }
}
