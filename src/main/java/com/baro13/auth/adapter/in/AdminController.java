package com.baro13.auth.adapter.in;

import com.baro13.auth.adapter.in.dto.response.GrantRoleResponse;
import com.baro13.auth.application.in.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "관리자 권한 부여 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuthService authService;

    @Operation(summary = "관리자 권한 부여", description = "사용자에게 ADMIN 권한을 부여합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "권한 부여 성공"),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("users/{userId}/roles")
    public ResponseEntity<GrantRoleResponse> grantAdminRole(@PathVariable Long userId) {
        GrantRoleResponse response = GrantRoleResponse.of(authService.grantAdminRole(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
