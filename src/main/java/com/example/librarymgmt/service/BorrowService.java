package com.example.librarymgmt.service;

import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;

import java.util.List;

public interface BorrowService {

    List<Borrow> getAllBorrows();

    List<Borrow> getBorrowsByStatus(BorrowStatus status);
}
