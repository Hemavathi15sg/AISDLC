package com.example.librarymgmt.service;

import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.repository.BorrowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;

    public BorrowService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public List<Borrow> getBorrowsByMemberId(UUID memberId) {
        return borrowRepository.findByMemberId(memberId);
    }

    public List<Borrow> getBorrowsByBookId(UUID bookId) {
        return borrowRepository.findByBookId(bookId);
    }

    public List<Borrow> getBorrowsByStatus(BorrowStatus status) {
        return borrowRepository.findByStatus(status);
    }

    public Borrow saveBorrow(Borrow borrow) {
        return borrowRepository.save(borrow);
    }
}
