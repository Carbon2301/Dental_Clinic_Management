# Dental_Clinic_Management

## Giới Thiệu

Dự án **Quản lý phòng khám nha khoa** là một ứng dụng quản lý các hoạt động của phòng khám, được xây dựng dựa trên các nguyên lý của lập trình hướng đối tượng (OOP). Hệ thống hỗ trợ quản lý thông tin bệnh nhân, bác sĩ, lịch hẹn, điều trị, thuốc, và hóa đơn, giúp nâng cao hiệu quả quản lý và tiết kiệm thời gian.

## Các Tính Năng Chính

- **Quản lý bệnh nhân**: Thêm, sửa, tìm kiếm thông tin bệnh nhân.
- **Quản lý bác sĩ**: Thêm, sửa, phân công vai trò, tìm kiếm bác sĩ.
- **Quản lý lịch hẹn**: Đặt lịch, theo dõi trạng thái, hủy lịch hẹn, hoàn thành lịch hẹn.
- **Quản lý điều trị**: Thêm, tra cứu thông tin điều trị.
- **Quản lý thuốc và hóa đơn**: Lập hóa đơn, tính tổng tiền.

## Công Nghệ Sử Dụng
 
- **Backend**: Spring Boot
- **Database**: SQL SERVER
- **Frontend**: React.js
- **Khác**: Docker

## Yêu Cầu Hệ Thống

- **Java**: Phiên bản 21 hoặc cao hơn
- **Node.js**: https://nodejs.org/en/download

## Hướng Dẫn Cài Đặt và Chạy Dự Án

### Clone Repository
```bash
git clone https://github.com/KhaiLe190904/Dental_Clinic_Management
- Để load database hãy chạy lệnh sau: docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Dentalclinic@20241" -p 1444:1433 --name sql_2019 --hostname sql_2019 -d mcr.microsoft.com/mssql/server:2019-latest
- Frontend: npm install -> npm run dev (chạy 2 terminal: cd frontend và cd admin)
- Backend: Run application trong IntellIJ IDEA hoặc bất kì IDE nào hỗ trợ SpringBoot




