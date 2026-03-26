package com.example.librarymgmt.config;

import com.example.librarymgmt.model.Book;
import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.Member;
import com.example.librarymgmt.model.Role;
import com.example.librarymgmt.repository.BookRepository;
import com.example.librarymgmt.repository.BorrowRepository;
import com.example.librarymgmt.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(MemberRepository memberRepository,
                               BookRepository bookRepository,
                               BorrowRepository borrowRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            Member admin = new Member("admin", passwordEncoder.encode("admin123"), "admin@library.com", Role.ADMIN);
            Member librarian = new Member("librarian", passwordEncoder.encode("lib123"), "librarian@library.com", Role.LIBRARIAN);
            Member alice = new Member("alice", passwordEncoder.encode("alice123"), "alice@example.com", Role.MEMBER);
            Member bob = new Member("bob", passwordEncoder.encode("bob123"), "bob@example.com", Role.MEMBER);

            memberRepository.save(admin);
            memberRepository.save(librarian);
            memberRepository.save(alice);
            memberRepository.save(bob);

            Book book1 = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
            Book book2 = new Book("Effective Java", "Joshua Bloch", "978-0134685991");

            bookRepository.save(book1);
            bookRepository.save(book2);

            borrowRepository.save(new Borrow(alice, book1, LocalDate.now().minusDays(7)));
            borrowRepository.save(new Borrow(bob, book2, LocalDate.now().minusDays(3)));
        };
    }
}
