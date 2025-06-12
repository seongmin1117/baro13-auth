package com.baro13.auth.adapter.in;

import com.baro13.auth.adapter.in.dto.response.GrantRoleResponse;
import com.baro13.auth.application.in.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("users/{userId}/roles")
    public ResponseEntity<GrantRoleResponse> grantAdminRole(@PathVariable Long userId) {
        GrantRoleResponse response = GrantRoleResponse.of(authService.grantAdminRole(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
