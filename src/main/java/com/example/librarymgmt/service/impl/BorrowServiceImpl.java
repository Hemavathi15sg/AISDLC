package com.example.librarymgmt.service.impl;

import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.service.BorrowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final List<Borrow> borrows;

    public BorrowServiceImpl() {
        this.borrows = List.of();
    }

    public BorrowServiceImpl(List<Borrow> borrows) {
        this.borrows = borrows;
    }

    @Override
    public List<Borrow> getAllBorrows() {
        return borrows;
    }

    @Override
    public List<Borrow> getBorrowsByStatus(BorrowStatus status) {
        return borrows.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }
}
