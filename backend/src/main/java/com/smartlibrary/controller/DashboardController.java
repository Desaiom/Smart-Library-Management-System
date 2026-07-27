package com.smartlibrary.controller;

import com.smartlibrary.dto.common.ApiResponse;
import com.smartlibrary.dto.dashboard.DashboardResponse;
import com.smartlibrary.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Aggregated statistics for the staff dashboard. Access is restricted to
 * ADMIN/LIBRARIAN by {@code SecurityConfig}.
 */
@Tag(name = "Dashboard", description = "Aggregated library statistics")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Get dashboard statistics")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardResponse>> stats() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getStatistics()));
    }
}
