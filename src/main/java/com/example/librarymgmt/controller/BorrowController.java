package com.example.librarymgmt.controller;

import com.example.librarymgmt.dto.ErrorResponse;
import com.example.librarymgmt.exception.ForbiddenException;
import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.security.UserDetailsImpl;
import com.example.librarymgmt.service.BorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBorrows(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID bookId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        UUID currentMemberId = currentUser.getId();
        boolean isPrivileged = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_LIBRARIAN"));

        List<Borrow> borrows;

        if (memberId != null) {
            // Authorization: only the member themselves or a privileged user may query by memberId
            if (!memberId.equals(currentMemberId) && !isPrivileged) {
                logger.warn("Unauthorized borrow query attempt by member {} for member {}",
                        currentMemberId, memberId);
                throw new ForbiddenException("Access denied: Cannot view other members' borrow records");
            }
            borrows = borrowService.getBorrowsByMemberId(memberId);
        } else if (bookId != null) {
            // Viewing borrows by book is restricted to privileged roles
            if (!isPrivileged) {
                logger.warn("Unauthorized borrow-by-book query attempt by member {}", currentMemberId);
                throw new ForbiddenException("Access denied: Only librarians and admins can query borrows by book");
            }
            borrows = borrowService.getBorrowsByBookId(bookId);
        } else if (status != null) {
            // Validate status enum value and return 400 for invalid values (do NOT fall back to all borrows)
            if (status.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "Status parameter must not be empty. Allowed values: " + Arrays.toString(BorrowStatus.values())));
            }
            BorrowStatus borrowStatus;
            try {
                borrowStatus = BorrowStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid borrow status filter '{}' requested by member {}", status, currentMemberId);
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                                "Invalid status value. Allowed values: " + Arrays.toString(BorrowStatus.values())));
            }
            // Privileged users can filter all borrows by status; members see only their own
            if (isPrivileged) {
                borrows = borrowService.getBorrowsByStatus(borrowStatus);
            } else {
                borrows = borrowService.getBorrowsByMemberId(currentMemberId).stream()
                        .filter(b -> b.getStatus() == borrowStatus)
                        .toList();
            }
        } else {
            // No filter: privileged users see all borrows, members see only their own
            if (isPrivileged) {
                borrows = borrowService.getAllBorrows();
            } else {
                borrows = borrowService.getBorrowsByMemberId(currentMemberId);
            }
        }

        return ResponseEntity.ok(borrows);
    }
}
