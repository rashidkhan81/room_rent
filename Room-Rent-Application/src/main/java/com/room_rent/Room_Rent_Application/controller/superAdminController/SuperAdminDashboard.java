package com.room_rent.Room_Rent_Application.controller.superAdminController;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.service.superAdminDashboard.SuperAdminDashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class SuperAdminDashboard extends BaseController {
    private final CustomMessageSource customMessageSource;
    private final SuperAdminDashboardServiceImpl superAdminDashboardService;



    @GetMapping("/total-room")
    public ResponseEntity<?> getRoomCO() {
         long totalRoomCount = superAdminDashboardService.getTotalRoom();
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(DASHBOARD)),
                        totalRoomCount
                )
        );
    }

}
