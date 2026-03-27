package com.example.librarymgmt.controller;

import com.example.librarymgmt.exception.InvalidOperationException;
import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.service.BorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private static final Logger log = LoggerFactory.getLogger(BorrowController.class);

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<List<Borrow>> getBorrows(
            @RequestParam(required = false) String status) {

        List<Borrow> borrows;

        if (status == null) {
            borrows = borrowService.getAllBorrows();
        } else {
            try {
                BorrowStatus borrowStatus = BorrowStatus.valueOf(status.toUpperCase());
                borrows = borrowService.getBorrowsByStatus(borrowStatus);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid borrow status filter value received: {}", status);
                throw new InvalidOperationException("Invalid status value: " + status +
                        ". Allowed values: BORROWED, RETURNED, OVERDUE");
            }
        }

        return ResponseEntity.ok(borrows);
    }
}
