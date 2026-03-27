package com.example.librarymgmt.controller;

import com.example.librarymgmt.exception.GlobalExceptionHandler;
import com.example.librarymgmt.exception.InvalidOperationException;
import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BorrowControllerTest {

    @Mock
    private BorrowService borrowService;

    @InjectMocks
    private BorrowController borrowController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(borrowController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getBorrows_withNoStatus_returnsAllBorrows() throws Exception {
        List<Borrow> allBorrows = List.of(
                new Borrow(1L, 10L, 20L, LocalDate.now(), LocalDate.now().plusDays(14), null, BorrowStatus.BORROWED),
                new Borrow(2L, 11L, 21L, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), null, BorrowStatus.OVERDUE)
        );
        when(borrowService.getAllBorrows()).thenReturn(allBorrows);

        mockMvc.perform(get("/api/borrows"))
                .andExpect(status().isOk());

        verify(borrowService, times(1)).getAllBorrows();
        verify(borrowService, never()).getBorrowsByStatus(any());
    }

    @Test
    void getBorrows_withValidStatus_returnsBorrowsByStatus() throws Exception {
        List<Borrow> borrowedRecords = List.of(
                new Borrow(1L, 10L, 20L, LocalDate.now(), LocalDate.now().plusDays(14), null, BorrowStatus.BORROWED)
        );
        when(borrowService.getBorrowsByStatus(BorrowStatus.BORROWED)).thenReturn(borrowedRecords);

        mockMvc.perform(get("/api/borrows").param("status", "BORROWED"))
                .andExpect(status().isOk());

        verify(borrowService, times(1)).getBorrowsByStatus(BorrowStatus.BORROWED);
        verify(borrowService, never()).getAllBorrows();
    }

    @Test
    void getBorrows_withValidStatusLowercase_returnsBorrowsByStatus() throws Exception {
        when(borrowService.getBorrowsByStatus(BorrowStatus.RETURNED)).thenReturn(List.of());

        mockMvc.perform(get("/api/borrows").param("status", "returned"))
                .andExpect(status().isOk());

        verify(borrowService, times(1)).getBorrowsByStatus(BorrowStatus.RETURNED);
        verify(borrowService, never()).getAllBorrows();
    }

    /**
     * Security test: an invalid status must return 400 Bad Request and must NOT
     * fall back to returning all borrow records.
     */
    @Test
    void getBorrows_withInvalidStatus_returnsBadRequest_andDoesNotLeakAllRecords() throws Exception {
        mockMvc.perform(get("/api/borrows").param("status", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Invalid status value: INVALID. Allowed values: BORROWED, RETURNED, OVERDUE"));

        verify(borrowService, never()).getAllBorrows();
        verify(borrowService, never()).getBorrowsByStatus(any());
    }

    @Test
    void getBorrows_withGarbageStatus_returnsBadRequest_andDoesNotLeakAllRecords() throws Exception {
        mockMvc.perform(get("/api/borrows").param("status", "garbage_input"))
                .andExpect(status().isBadRequest());

        verify(borrowService, never()).getAllBorrows();
        verify(borrowService, never()).getBorrowsByStatus(any());
    }
}
