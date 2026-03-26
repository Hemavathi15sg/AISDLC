package com.example.librarymgmt.controller;

import com.example.librarymgmt.model.Borrow;
import com.example.librarymgmt.model.BorrowStatus;
import com.example.librarymgmt.model.Member;
import com.example.librarymgmt.model.Role;
import com.example.librarymgmt.security.UserDetailsImpl;
import com.example.librarymgmt.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for BorrowController authorization checks.
 *
 * Verifies that:
 * - A member can view their own borrow records (200 OK)
 * - A member receives 403 Forbidden when querying another member's records
 * - A LIBRARIAN can view any member's borrow records
 * - An ADMIN can view any member's borrow records
 * - Invalid status values return 400 Bad Request (no silent fallback to all records)
 * - Unauthenticated requests return 401 Unauthorized
 */
@SpringBootTest
@AutoConfigureMockMvc
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowService borrowService;

    private UUID memberAId;
    private UUID memberBId;
    private UserDetailsImpl memberAPrincipal;
    private UserDetailsImpl memberBPrincipal;
    private UserDetailsImpl librarianPrincipal;
    private UserDetailsImpl adminPrincipal;

    @BeforeEach
    void setUp() {
        memberAId = UUID.randomUUID();
        memberBId = UUID.randomUUID();
        UUID librarianId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        memberAPrincipal = buildUserDetails(memberAId, "memberA", Role.MEMBER);
        memberBPrincipal = buildUserDetails(memberBId, "memberB", Role.MEMBER);
        librarianPrincipal = buildUserDetails(librarianId, "librarian", Role.LIBRARIAN);
        adminPrincipal = buildUserDetails(adminId, "admin", Role.ADMIN);
    }

    // -----------------------------------------------------------------------
    // Member querying own data
    // -----------------------------------------------------------------------

    @Test
    void memberCanViewOwnBorrows() throws Exception {
        Borrow borrow = new Borrow();
        when(borrowService.getBorrowsByMemberId(memberAId)).thenReturn(List.of(borrow));

        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberAId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------------------------
    // Member querying another member's data — must be 403
    // -----------------------------------------------------------------------

    @Test
    void memberCannotViewAnotherMembersBorrows() throws Exception {
        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberBId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isForbidden());
    }

    // -----------------------------------------------------------------------
    // Privileged users can query any member's data
    // -----------------------------------------------------------------------

    @Test
    void librarianCanViewAnyMembersBorrows() throws Exception {
        when(borrowService.getBorrowsByMemberId(memberAId)).thenReturn(List.of());

        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberAId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(librarianPrincipal))))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanViewAnyMembersBorrows() throws Exception {
        when(borrowService.getBorrowsByMemberId(memberBId)).thenReturn(List.of());

        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberBId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(adminPrincipal))))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------------------------
    // Invalid status must return 400, not silently fall back to all borrows
    // -----------------------------------------------------------------------

    @Test
    void invalidStatusReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/borrows")
                        .param("status", "INVALID_STATUS")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validStatusReturnsOk() throws Exception {
        when(borrowService.getBorrowsByMemberId(memberAId)).thenReturn(List.of());

        mockMvc.perform(get("/api/borrows")
                        .param("status", BorrowStatus.ACTIVE.name())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------------------------
    // Unauthenticated requests must return 401
    // -----------------------------------------------------------------------

    @Test
    void unauthenticatedRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberAId.toString()))
                .andExpect(status().isUnauthorized());
    }

    // -----------------------------------------------------------------------
    // Member with no filter sees only own borrows
    // -----------------------------------------------------------------------

    @Test
    void memberWithoutFilterSeesOwnBorrowsOnly() throws Exception {
        Borrow borrow = new Borrow();
        when(borrowService.getBorrowsByMemberId(memberAId)).thenReturn(List.of(borrow));

        mockMvc.perform(get("/api/borrows")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------------------------
    // Member cannot query by bookId (privileged only)
    // -----------------------------------------------------------------------

    @Test
    void memberCannotQueryByBookId() throws Exception {
        UUID bookId = UUID.randomUUID();

        mockMvc.perform(get("/api/borrows")
                        .param("bookId", bookId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isForbidden());
    }

    @Test
    void librarianCanQueryByBookId() throws Exception {
        UUID bookId = UUID.randomUUID();
        when(borrowService.getBorrowsByBookId(bookId)).thenReturn(List.of());

        mockMvc.perform(get("/api/borrows")
                        .param("bookId", bookId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(librarianPrincipal))))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------------------------
    // 403 response body contains expected message
    // -----------------------------------------------------------------------

    @Test
    void forbiddenResponseContainsMessage() throws Exception {
        mockMvc.perform(get("/api/borrows")
                        .param("memberId", memberBId.toString())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(
                                authToken(memberAPrincipal))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "Access denied: Cannot view other members' borrow records"));
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private UserDetailsImpl buildUserDetails(UUID id, String username, Role role) {
        Member member = new Member();
        member.setId(id);
        member.setUsername(username);
        member.setPassword("password");
        member.setEmail(username + "@example.com");
        member.setRole(role);
        return new UserDetailsImpl(member);
    }

    private UsernamePasswordAuthenticationToken authToken(UserDetailsImpl principal) {
        return new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
    }
}
