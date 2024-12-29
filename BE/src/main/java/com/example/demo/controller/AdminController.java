
package com.example.demo.controller;

import com.example.demo.dto.AppointmentDto;
import com.example.demo.dto.DentistDto;
import com.example.demo.dto.PatientDto;
import com.example.demo.model.Appointment;
import com.example.demo.model.Dentist;
import com.example.demo.repository.DentistRepository;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DentistService;
import com.example.demo.service.PatientService;
import com.example.demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.example.demo.config.GenToken.generateToken;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class AdminController {

    private final DentistService dentistService;

    private final S3Service S3Service;

    private final PatientService patientService;

    private final AppointmentService appointmentService;
    private final DentistRepository dentistRepository;

    @PostMapping("/add-doctor")
    @Operation(summary = "Luu 1 dentist vao phong kham")
    public ResponseEntity<?> save(@RequestPart("image") MultipartFile imageFile,
                                  @RequestPart("name") String name,
                                  @RequestPart("email") String email,
                                  @RequestPart("password") String password,
                                  @RequestPart("fees") String fees,
                                  @RequestPart("speciality") String speciality,
                                  @RequestPart("about") String about) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                response.put("success", false);
                response.put("message", "Image file is required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            String imageUrl = S3Service.uploadFile(imageFile);
            DentistDto dentistDto = DentistDto.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .fees(fees)
                    .speciality(speciality)
                    .imgUrl(imageUrl)
                    .status(1)
                    .isWorking(1)
                    .position("Dentist")
                    .about(about)
                    .build();
            dentistService.save(dentistDto);
            response.put("success", true);
            response.put("message", "Dentist added successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred while processing the request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = request.get("email");
            String password = request.get("password");
            // Kiểm tra role của người dùng
            if (!dentistService.checkRole(email)) {
                response.put("success", false);
                response.put("message", "Không đủ quyền");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);  // Trả về 403 Forbidden
            }
            if (dentistService.login(email, password) == true) {
                // Tạo token sử dụng hàm tự viết
                String token = generateToken(email, password);

                response.put("success", true);
                response.put("token", token);
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


    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAdminDashboard(@RequestHeader(value = "aToken", required = false) String aToken) {
        try {
            // Kiểm tra token nếu cần thiết (tùy vào yêu cầu bảo mật của bạn)
            if (aToken == null || !validateToken(aToken)) {
                Map<String, Object> unauthorizedResponse = new HashMap<>();
                unauthorizedResponse.put("success", false);
                unauthorizedResponse.put("message", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
            }

            // Lấy danh sách các đối tượng từ database
            List<Dentist> dentists = dentistService.findAll();
            List<PatientDto> patients = patientService.findAll();
            List<Appointment> appointments = appointmentService.findAll();
            List<AppointmentDto> appointmentDtos = appointmentService.findAppointmentsWithDentists();

            // Đảo ngược danh sách các cuộc hẹn để lấy các cuộc hẹn mới nhất
            Collections.reverse(appointments);

            // Tạo đối tượng để trả về
            Map<String, Object> dashData = new HashMap<>();
            dashData.put("dentists", dentists.size());
            dashData.put("appointments", appointments.size());
            dashData.put("patients", patients.size());
            dashData.put("latestAppointments", appointmentDtos);
            // Tạo response JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("dashData", dashData);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Phương thức mẫu để kiểm tra token (cần được cài đặt cụ thể hơn)
    private boolean validateToken(String token) {
        // Thêm logic để xác thực token tại đây
        return true; // Trả về true nếu hợp lệ, false nếu không
    }

    @GetMapping("/appointments")
    public ResponseEntity<Map<String, Object>> getAppointments(@RequestHeader(value = "aToken", required = false) String aToken) {
        try {
            // Kiểm tra token nếu cần thiết (tùy vào yêu cầu bảo mật của bạn)
            if (aToken == null || !validateToken(aToken)) {
                Map<String, Object> unauthorizedResponse = new HashMap<>();
                unauthorizedResponse.put("success", false);
                unauthorizedResponse.put("message", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
            }

            // Lấy danh sách các cuộc hẹn từ database
            List<AppointmentDto> appointments = appointmentService.findAppointmentsWithDentists();
            for (AppointmentDto appointment : appointments) {
                DentistDto dentist = appointmentService.findDentistByAppointmentId(appointment.getAppointmentId());
                appointment.setDentist(dentist);

                String patientName = patientService.findNameById(appointment.getPatientId());
                appointment.setPatientName(patientName);
            }


            // Tạo response JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("appointments", appointments);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@RequestBody Map<String, Integer> request,
                                                                 @RequestHeader(value = "aToken", required = false) String aToken)
    {
        try {
            // Kiểm tra token nếu cần thiết (tùy vào yêu cầu bảo mật của bạn)
            if (aToken == null || !validateToken(aToken)) {
                Map<String, Object> unauthorizedResponse = new HashMap<>();
                unauthorizedResponse.put("success", false);
                unauthorizedResponse.put("message", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
            }

            int appointmentId = Integer.parseInt(request.get("appointmentId").toString());
            AppointmentDto appointment = appointmentService.findAppointmentById(appointmentId);
            if (appointment == null) {
                Map<String, Object> notFoundResponse = new HashMap<>();
                notFoundResponse.put("success", false);
                notFoundResponse.put("message", "Appointment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            appointment.setCancelled(true);
            appointmentService.save(appointment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appointment cancelled successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/all-doctors")
    public ResponseEntity<Map<String, Object>> getAllDentists(@RequestHeader(value = "aToken", required = false) String aToken) {
        try {
            // Kiểm tra token nếu cần thiết (tùy vào yêu cầu bảo mật của bạn)
            if (aToken == null || !validateToken(aToken)) {
                Map<String, Object> unauthorizedResponse = new HashMap<>();
                unauthorizedResponse.put("success", false);
                unauthorizedResponse.put("message", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
            }

            List<Dentist> dentists = dentistService.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("dentists", dentists);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/change-availability")
    public ResponseEntity<Map<String, Object>> changeAvailability(@RequestBody Map<String, Object> request,
            @RequestHeader(value = "aToken", required = false) String aToken) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate token
            if (aToken == null || !validateToken(aToken)) {
                response.put("success", false);
                response.put("message", "Unauthorized access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check if 'dentistId' is provided
            if (!request.containsKey("dentistId") || request.get("dentistId") == null) {
                response.put("success", false);
                response.put("message", "'dentistId' is missing or null in the request body");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Parse 'dentistId'
            int dentistId;
            try {
                dentistId = Integer.parseInt(request.get("dentistId").toString());
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "'dentistId' must be a valid integer");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Fetch dentist by ID
            DentistDto dentist = dentistService.findDentistById(dentistId);
            if (dentist == null) {
                response.put("success", false);
                response.put("message", "Dentist not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Toggle availability
            dentist.setStatus(dentist.getStatus() == 1 ? 0 : 1);
            dentistService.update(dentist);

            response.put("success", true);
            response.put("message", "Availability changed successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
