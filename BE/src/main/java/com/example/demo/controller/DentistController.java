package com.example.demo.controller;


import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.dto.TreatmentDto;
import com.example.demo.model.Dentist;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DentistService;
import com.example.demo.service.PatientService;
import com.example.demo.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.config.GenToken.generateToken;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/doctor")
public class DentistController {
    private final DentistService dentistService;

    private final PatientService patientService;

    private final TreatmentService treatmentService;

    private final AppointmentService appointmentService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginDentist(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");
            if (dentistService.login(email, password) == true) {
                // Tạo token sử dụng hàm tự viết
                String token = generateToken(email, password);

                Map<String, Object> data = new HashMap<>();
                response.put("success", true);
                response.put("token", token);
                response.put("dentistId", dentistService.findDentistByEmail(email).getDentistId());

            } else {
                response.put("success", false);
                response.put("message", "Invalid credentials");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getDoctorProfile(@RequestHeader(value = "dToken", required = false) String dToken,
                                                                @RequestParam(value = "dentistId", required = false) Integer dentistId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate the token


            // Fetch profile using service
            DentistDto profileData = dentistService.findDentistById(dentistId);

            if (profileData != null) {
                response.put("success", true);
                response.put("profileData", profileData);
            } else {
                response.put("success", false);
                response.put("message", "Profile not found");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<Map<String, Object>> updateDoctorProfile(
            @RequestHeader(value = "dToken", required = false) String dToken,
            @RequestBody Map<String, Object> updateData) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra tính hợp lệ của token
            if (dToken == null || dToken.isEmpty()) {
                response.put("success", false);
                response.put("message", "Token is missing or invalid");
                return ResponseEntity.badRequest().body(response);
            }

            // Lấy dữ liệu từ request
            int dentistId;
            try {
                dentistId = Integer.parseInt(updateData.get("dentistId").toString());
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "'dentistId' must be a valid integer");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            int isWorking = Integer.parseInt(updateData.get("available").toString());
            String about = (String) updateData.get("about");
            String fees = (String) updateData.get("fees");

            // Tìm và cập nhật thông tin bác sĩ
            DentistDto dentist = dentistService.findDentistById(dentistId);
            if (dentist != null) {
                dentist.setIsWorking(isWorking);
                dentist.setAbout(about);
                dentist.setFees(fees);

                dentistService.update(dentist);

                response.put("success", true);
                response.put("message", "Profile updated successfully.");
            } else {
                response.put("success", false);
                response.put("message", "Dentist not found.");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }



    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@RequestHeader(value = "dToken", required = false) String dToken,
                                                            @RequestParam(value = "dentistId", required = false) Integer dentistId) {
        try {
            if (validateToken(dToken)) {
                Map<String, Object> response = new HashMap<>();
            }
            List<AppointmentDto> appointments = appointmentService.findAppointmentByDentistId(dentistId);
            List<PatientDto> patients = patientService.findPatientByDentistId(dentistId);
            List<TreatmentDto> treatments = treatmentService.findTreatmentByDentistId(dentistId);

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> dashData = new HashMap<>();

            dashData.put("appointments", appointments.size());
            dashData.put("patients", patients.size());
            dashData.put("latestAppointments", appointments);

            // Lấy danh sách fees
            List<Double> feesList = treatments.stream()
                    .map(TreatmentDto::getFees) // Giữ nguyên kiểu Double
                    .collect(Collectors.toList());

            // Gán fees vào từng appointment
            for (int i = 0; i < appointments.size() && i < feesList.size(); i++) {
                AppointmentDto appointment = appointments.get(i);
                appointment.setFees(feesList.get(i)); // Gán fees trực tiếp dưới dạng Double
            }

            double totalFees = 0;
            // Tính tổng fees, chỉ tính khi IsPaid = 1 và không bị hủy
            for (int i = 0; i < appointments.size(); i++) {
                AppointmentDto appointment = appointments.get(i);
                if (appointment.getIsPaid() == true) {
                    totalFees += appointment.getFees();
                }
            }

            response.put("totalFees", totalFees);
            response.put("success", true);
            response.put("dashData", dashData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/cancel-appointment")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@RequestHeader(value = "dToken", required = false) String dToken,
                                                                 @RequestBody Map<String, Object> request) {
        try {
            if (!validateToken(dToken)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            int appointmentId = (int) request.get("appointmentId");
            AppointmentDto appointment = appointmentService.findAppointmentById(appointmentId);

            appointment.setCancelled(true);
            appointmentService.save(appointment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appointment cancelled successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/complete-appointment")
    public ResponseEntity<Map<String, Object>> completeAppointment(@RequestHeader(value = "dToken", required = false) String dToken,
                                                                   @RequestBody Map<String, Object> request) {
        try {
            if (!validateToken(dToken)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            int appointmentId = (int) request.get("appointmentId");
            AppointmentDto appointment = appointmentService.findAppointmentById(appointmentId);

            appointment.setIsCompleted(true);
            appointmentService.save(appointment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appointment completed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/appointments")
    public ResponseEntity<Map<String, Object>> getAppointments(@RequestHeader(value = "dToken", required = false) String dToken,
                                                               @RequestParam(value = "dentistId", required = false) Integer dentistId) {
        try {
            if (!validateToken(dToken)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
            List<AppointmentDto> appointments = appointmentService.findAppointmentByDentistId(dentistId);
            List<TreatmentDto> treatments = treatmentService.findTreatmentByDentistId(dentistId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            // Lấy danh sách fees
            List<String> feesList = treatments.stream()
                    .map(treatment -> String.valueOf(treatment.getFees())) // Chuyển từ Double sang String
                    .collect(Collectors.toList());

            // Gán fees vào từng appointment
            for (int i = 0; i < appointments.size() && i < feesList.size(); i++) {
                AppointmentDto appointment = appointments.get(i);
                appointment.setFees(Double.parseDouble(feesList.get(i))); // Gán fees, chuyển từ String về Double nếu cần
            }

            // Đưa danh sách appointments vào response
            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Dung cho ben User
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllDentists() {
        try {

            List<Dentist> dentists = dentistService.findAll();
            dentists.remove(0);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("dentists", dentists);
            response.put("length", dentists.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private boolean validateToken(String token) {
        // Thêm logic để xác thực token tại đây
        return true; // Trả về true nếu hợp lệ, false nếu không
    }
}
