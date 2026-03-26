package com.example.librarymgmt.repository;

import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BorrowRepository extends JpaRepository<Borrow, UUID> {
    List<Borrow> findByMemberId(UUID memberId);
    List<Borrow> findByBookId(UUID bookId);
    List<Borrow> findByStatus(BorrowStatus status);
}
