-- =========================================================================
-- HỆ THỐNG QUẢN LÝ THỰC TẬP
-- =========================================================================

-- Mật khẩu mẫu đã được mã hóa BCrypt tương ứng:
-- Admin: admin123
-- Mentor: mentor123@
-- Student: student123@

-- ==========================================
-- 1. TẠO TÀI KHOẢN USERS (5 MENTOR + 10 STUDENT)
-- ==========================================
ALTER SEQUENCE IF EXISTS users_user_id_seq RESTART WITH 1;

INSERT INTO users (user_name, password_hash, full_name, email, role, is_active, created_at)
VALUES
    -- 1 admin phụ
    ('admin01', '$2a$12$WDw99CBgOYEI3dIj/3Ft7ufJPMrtF6ihJVMliVmKclvHvTeuYBx1q', 'Nguyễn Văn Admin', 'admin1@gmail.com', 'ADMIN', true, '2025-08-01 08:00:00'),

    -- 5 Mentors
    ('mentor01', '$2a$12$UlbxKBD/hf2GKcCidroFa.XQwX4v0TQo7qXYkbUd8fiYvCa.1khva', 'Nguyễn Văn Thịnh', 'mentor1@gmail.com', 'MENTOR', true, '2025-08-01 08:05:00'),
    ('mentor02', '$2a$12$UlbxKBD/hf2GKcCidroFa.XQwX4v0TQo7qXYkbUd8fiYvCa.1khva', 'Đàm Minh Tuấn', 'mentor2@gmail.com', 'MENTOR', true, '2025-08-01 08:06:00'),
    ('mentor03', '$2a$12$UlbxKBD/hf2GKcCidroFa.XQwX4v0TQo7qXYkbUd8fiYvCa.1khva', 'Diệp Phàm', 'mentor3@gmail.com', 'MENTOR', true, '2025-08-01 08:07:00'),
    ('mentor04', '$2a$12$UlbxKBD/hf2GKcCidroFa.XQwX4v0TQo7qXYkbUd8fiYvCa.1khva', 'Cố Hạo Đình', 'mentor4@gmail.com', 'MENTOR', true, '2025-08-01 08:08:00'),
    ('mentor05', '$2a$12$UlbxKBD/hf2GKcCidroFa.XQwX4v0TQo7qXYkbUd8fiYvCa.1khva', 'Tiêu Mặc', 'mentor5@gmail.com', 'MENTOR', true, '2025-08-01 08:09:00'),

    -- 10 Students
    ('student01', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Lý Trung Bình', 'student1@gmail.com', 'STUDENT', true, '2025-12-01 09:00:00'),
    ('student02', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Phó Phượng Thành', 'student2@gmail.com', 'STUDENT', true, '2025-12-01 09:01:00'),
    ('student03', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Mặc Cảnh Thâm', 'student3@gmail.com', 'STUDENT', true, '2025-12-01 09:02:00'),
    ('student04', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Lục Lăng Thiên', 'student4@gmail.com', 'STUDENT', true, '2025-12-01 09:03:00'),
    ('student05', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Thẩm Tuấn Duệ', 'student5@gmail.com', 'STUDENT', true, '2025-12-01 09:04:00'),
    ('student06', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Hàn Đông Thần', 'student6@gmail.com', 'STUDENT', true, '2025-12-01 09:05:00'),
    ('student07', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Tạ Đinh Phong', 'student7@gmail.com', 'STUDENT', true, '2025-12-01 09:06:00'),
    ('student08', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Kỷ Lục Thần', 'student8@gmail.com', 'STUDENT', true, '2025-12-01 09:07:00'),
    ('student09', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Uông Tử Phong', 'student9@gmail.com', 'STUDENT', true, '2025-12-01 09:08:00'),
    ('student10', '$2a$12$ArrbhbAAqtcKo.KQtz7LUuj9fS1qPHPsTb4fjnn2OqDfAhLWuyk5m', 'Trần Bắc Huyền', 'student10@gmail.com', 'STUDENT', true, '2025-12-01 09:09:00');


-- ==========================================
-- 2. TẠO CHI TIẾT MENTORS (Sử dụng cột user_id theo cơ chế @MapsId)
-- ==========================================
INSERT INTO mentors (user_id, department, academic_rank, created_at)
VALUES
    ((SELECT user_id FROM users WHERE user_name = 'mentor01'), 'Công nghệ phần mềm', 'Thạc sĩ', '2025-08-01 08:05:00'),
    ((SELECT user_id FROM users WHERE user_name = 'mentor02'), 'Hệ thống thông tin', 'Tiến sĩ', '2025-08-01 08:06:00'),
    ((SELECT user_id FROM users WHERE user_name = 'mentor03'), 'An toàn thông tin', 'Thạc sĩ', '2025-08-01 08:07:00'),
    ((SELECT user_id FROM users WHERE user_name = 'mentor04'), 'Khoa học máy tính', 'Giảng viên', '2025-08-01 08:08:00'),
    ((SELECT user_id FROM users WHERE user_name = 'mentor05'), 'Mạng máy tính', 'Phó Giáo sư', '2025-08-01 08:09:00');


-- ==========================================
-- 3. TẠO CHI TIẾT STUDENTS
-- ==========================================
INSERT INTO students (user_id, student_code, major, "class", date_of_birth, address, created_at)
VALUES
    ((SELECT user_id FROM users WHERE user_name = 'student01'), 'B22DCCN001', 'Multimedia', 'D22PT-01', '2004-03-15', 'Quận 1, TP. HCM', '2025-12-01 09:00:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student02'), 'B22DCCN002', 'Multimedia', 'D22PT-01', '2004-05-20', 'Quận 3, TP. HCM', '2025-12-01 09:01:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student03'), 'B22DCCN003', 'Multimedia', 'D22PT-02', '2004-08-11', 'Thủ Đức, TP. HCM', '2025-12-01 09:02:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student04'), 'B22DCCN004', 'Multimedia', 'D22PT-02', '2004-12-02', 'Bình Thạnh, TP. HCM', '2025-12-01 09:03:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student05'), 'B22DCCN005', 'Multimedia', 'D22PT-03', '2004-01-25', 'Quận 7, TP. HCM', '2025-12-01 09:04:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student06'), 'B22DCCN006', 'Multimedia', 'D22PT-03', '2004-07-09', 'Tân Bình, TP. HCM', '2025-12-01 09:05:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student07'), 'B22DCCN007', 'Multimedia', 'D22PT-04', '2004-10-30', 'Gò Vấp, TP. HCM', '2025-12-01 09:06:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student08'), 'B22DCCN008', 'Multimedia', 'D22PT-04', '2004-02-14', 'Quận 10, TP. HCM', '2025-12-01 09:07:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student09'), 'B22DCCN009', 'Multimedia', 'D22PT-05', '2004-09-18', 'Phú Nhuận, TP. HCM', '2025-12-01 09:08:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student10'), 'B22DCCN010', 'Multimedia', 'D22PT-05', '2004-11-05', 'Quận 5, TP. HCM', '2025-12-01 09:09:00');


-- ==========================================
-- 4. TẠO GIAI ĐOẠN THỰC TẬP (INTERNSHIP PHASES)
-- ==========================================
ALTER SEQUENCE IF EXISTS internship_phases_phase_id_seq RESTART WITH 1;

INSERT INTO internship_phases (phase_name, start_date, end_date, description, created_at)
VALUES
    ('Học kỳ Doanh nghiệp - Đợt 1 2026', '2026-01-05', '2026-03-15', 'Giai đoạn thực tập cơ sở, làm quen quy trình và xây dựng core nghiệp vụ.', '2025-12-15 10:00:00'),
    ('Thực tập Tốt nghiệp - Đợt 2 2026', '2026-03-20', '2026-05-30', 'Giai đoạn thực tập chuyên sâu, hoàn thiện sản phẩm và báo cáo khóa luận.', '2025-12-15 10:05:00'),
    ('Thực tập Dự án Startup mở rộng', '2026-06-01', '2026-08-15', 'Giai đoạn thử nghiệm dự án thực tế kết hợp lab nghiên cứu ứng dụng AI.', '2025-12-15 10:10:00');


-- ==========================================
-- 5. TẠO CÁC ĐỢT ĐÁNH GIÁ (ASSESSMENT ROUNDS)
-- ==========================================
ALTER SEQUENCE IF EXISTS assessment_rounds_round_id_seq RESTART WITH 1;

INSERT INTO assessment_rounds (phase_id, round_name, start_date, end_date, description, is_active, created_at)
VALUES
    ((SELECT phase_id FROM internship_phases WHERE phase_name = 'Học kỳ Doanh nghiệp - Đợt 1 2026'), 'Đánh giá giữa kỳ - Phase 1', '2026-02-05', '2026-02-10', 'Đánh giá tiến độ sau 1 tháng thực tập đầu tiên.', true, '2025-12-15 10:00:00'),
    ((SELECT phase_id FROM internship_phases WHERE phase_name = 'Học kỳ Doanh nghiệp - Đợt 1 2026'), 'Đánh giá cuối kỳ - Phase 1', '2026-03-10', '2026-03-15', 'Tổng kết điểm và nghiệm thu báo cáo Phase 1.', true, '2025-12-15 10:01:00'),
    ((SELECT phase_id FROM internship_phases WHERE phase_name = 'Thực tập Tốt nghiệp - Đợt 2 2026'), 'Đánh giá giữa kỳ - Phase 2', '2026-04-20', '2026-04-25', 'Kiểm tra tiến độ hoàn thiện hệ thống API và DB.', true, '2025-12-15 10:05:00'),
    ((SELECT phase_id FROM internship_phases WHERE phase_name = 'Thực tập Tốt nghiệp - Đợt 2 2026'), 'Đánh giá cuối kỳ - Phase 2', '2026-05-25', '2026-05-30', 'Đánh giá tổng quan năng lực và thái độ làm việc trước hội đồng.', true, '2025-12-15 10:06:00');


-- ==========================================
-- 6. TIÊU CHÍ ĐÁNH GIÁ (EVALUATION CRITERIA)
-- ==========================================
ALTER SEQUENCE IF EXISTS evaluation_criteria_criterion_id_seq RESTART WITH 1;

INSERT INTO evaluation_criteria (criterion_name, description, max_score, created_at)
VALUES
    ('Kỹ năng chuyên môn (Hard Skills)', 'Khả năng viết code sạch, tối ưu thuật toán và vận dụng công nghệ vào dự án.', 10.00, '2025-12-10 08:00:00'),
    ('Tư duy logic & Giải quyết vấn đề', 'Khả năng phân tích yêu cầu, bóc tách vấn đề và đề xuất giải pháp kỹ thuật.', 10.00, '2025-12-10 08:01:00'),
    ('Kỹ năng làm việc nhóm (Teamwork)', 'Sự phối hợp với mentor và các thành viên khác, sử dụng công cụ Git/Jira.', 10.00, '2025-12-10 08:02:00'),
    ('Thái độ & Kỷ luật', 'Tính chuyên cần, đúng giờ, tuân thủ deadline và quy định của tổ chức.', 10.00, '2025-12-10 08:03:00'),
    ('Chất lượng tài liệu & Báo cáo', 'Đánh giá cách viết doc API, comment code, và viết báo cáo tiến độ định kỳ.', 10.00, '2025-12-10 08:04:00');


-- ==========================================
-- 7. TRỌNG SỐ TIÊU CHÍ TRONG TỪNG ĐỢT (ROUND CRITERIA)
-- ==========================================
ALTER SEQUENCE IF EXISTS round_criteria_round_criterion_id_seq RESTART WITH 1;

INSERT INTO round_criteria (round_id, criterion_id, weight)
VALUES
    (1, 1, 0.20), (1, 2, 0.30), (1, 3, 0.20), (1, 4, 0.30),
    (2, 1, 0.40), (2, 2, 0.20), (2, 3, 0.10), (2, 4, 0.10), (2, 5, 0.20),
    (3, 1, 0.30), (3, 2, 0.30), (3, 3, 0.20), (3, 4, 0.20),
    (4, 1, 0.50), (4, 2, 0.20), (4, 3, 0.10), (4, 4, 0.10), (4, 5, 0.10);


-- ==========================================
-- 8. PHÂN CÔNG THỰC TẬP (INTERNSHIP ASSIGNMENTS)
-- ==========================================
ALTER SEQUENCE IF EXISTS internship_assignments_assignment_id_seq RESTART WITH 1;

INSERT INTO internship_assignments (student_id, mentor_id, phase_id, status, created_at)
VALUES
    ((SELECT user_id FROM users WHERE user_name = 'student01'), (SELECT user_id FROM users WHERE user_name = 'mentor01'), 1, 'IN_PROGRESS', '2026-01-04 08:00:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student02'), (SELECT user_id FROM users WHERE user_name = 'mentor01'), 1, 'IN_PROGRESS', '2026-01-04 08:01:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student03'), (SELECT user_id FROM users WHERE user_name = 'mentor02'), 1, 'IN_PROGRESS', '2026-01-04 08:02:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student04'), (SELECT user_id FROM users WHERE user_name = 'mentor02'), 1, 'IN_PROGRESS', '2026-01-04 08:03:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student05'), (SELECT user_id FROM users WHERE user_name = 'mentor03'), 1, 'IN_PROGRESS', '2026-01-04 08:04:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student06'), (SELECT user_id FROM users WHERE user_name = 'mentor03'), 1, 'IN_PROGRESS', '2026-01-04 08:05:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student07'), (SELECT user_id FROM users WHERE user_name = 'mentor04'), 1, 'IN_PROGRESS', '2026-01-04 08:06:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student08'), (SELECT user_id FROM users WHERE user_name = 'mentor04'), 1, 'IN_PROGRESS', '2026-01-04 08:07:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student09'), (SELECT user_id FROM users WHERE user_name = 'mentor05'), 1, 'IN_PROGRESS', '2026-01-04 08:08:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student10'), (SELECT user_id FROM users WHERE user_name = 'mentor05'), 1, 'IN_PROGRESS', '2026-01-04 08:09:00'),

    -- Phân công tiếp sang Phase 2
    ((SELECT user_id FROM users WHERE user_name = 'student01'), (SELECT user_id FROM users WHERE user_name = 'mentor01'), 2, 'PENDING', '2026-03-18 08:00:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student03'), (SELECT user_id FROM users WHERE user_name = 'mentor02'), 2, 'PENDING', '2026-03-18 08:01:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student05'), (SELECT user_id FROM users WHERE user_name = 'mentor03'), 2, 'PENDING', '2026-03-18 08:02:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student07'), (SELECT user_id FROM users WHERE user_name = 'mentor04'), 2, 'PENDING', '2026-03-18 08:03:00'),
    ((SELECT user_id FROM users WHERE user_name = 'student09'), (SELECT user_id FROM users WHERE user_name = 'mentor05'), 2, 'PENDING', '2026-03-18 08:04:00');


-- ==========================================
-- 9. KẾT QUẢ ĐÁNH GIÁ CHI TIẾT (ASSESSMENT RESULTS)
-- ==========================================
ALTER SEQUENCE IF EXISTS assessment_results_result_id_seq RESTART WITH 1;

INSERT INTO assessment_results (assignment_id, round_id, criterion_id, score, comments, evaluated_by, evaluation_date, created_at)
VALUES
    -- Mentor 1 chấm điểm cho Student 1 (Assignment 1) ở Round 1 (Giữa kỳ Phase 1)
    (1, 1, 1, 8.50, 'Code chạy tốt, cấu trúc Spring Boot sạch.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 14:00:00', '2026-02-07 14:00:00'),
    (1, 1, 2, 9.00, 'Tư duy bóc tách bài toán logic rất nhanh.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 14:05:00', '2026-02-07 14:05:00'),
    (1, 1, 3, 8.00, 'Phối hợp nhóm ăn ý, tích cực git commit.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 14:10:00', '2026-02-07 14:10:00'),
    (1, 1, 4, 9.50, 'Đi làm đúng giờ, tuân thủ tuyệt đối quy định.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 14:15:00', '2026-02-07 14:15:00'),

    -- Mentor 1 chấm điểm cho Student 2 (Assignment 2) ở Round 1
    (2, 1, 1, 7.00, 'Cần cải thiện thêm kỹ năng tối ưu truy vấn SQL.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 15:00:00', '2026-02-07 15:00:00'),
    (2, 1, 2, 7.50, 'Khả năng giải quyết vấn đề ở mức khá.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 15:05:00', '2026-02-07 15:05:00'),
    (2, 1, 3, 8.50, 'Hỗ trợ các thành viên khác rất nhiệt tình.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 15:10:00', '2026-02-07 15:10:00'),
    (2, 1, 4, 8.00, 'Thái độ tốt, tích cực tham gia meeting.', (SELECT user_id FROM users WHERE user_name = 'mentor01'), '2026-02-07 15:15:00', '2026-02-07 15:15:00'),

    -- Mentor 2 chấm điểm cho Student 3 (Assignment 3) ở Round 1
    (3, 1, 1, 9.00, 'Kỹ năng Spring Boot và Security xuất sắc vượt kỳ vọng.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 09:00:00', '2026-02-08 09:00:00'),
    (3, 1, 2, 8.50, 'Giải quyết triệt để các bug nghiêm trọng.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 09:05:00', '2026-02-08 09:05:00'),
    (3, 1, 3, 7.00, 'Cần tương tác nhiều hơn trên Jira.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 09:10:00', '2026-02-08 09:10:00'),
    (3, 1, 4, 9.00, 'Hoàn thành task đúng deadline được giao.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 09:15:00', '2026-02-08 09:15:00'),

    -- Mentor 2 chấm điểm cho Student 4 (Assignment 4) ở Round 1
    (4, 1, 1, 6.50, 'Code còn tương đối sơ sài, cần tối ưu thêm tầng DTO.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 10:00:00', '2026-02-08 10:00:00'),
    (4, 1, 2, 7.00, 'Phân tích thiết kế hệ thống còn chậm.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 10:05:00', '2026-02-08 10:05:00'),
    (4, 1, 3, 8.00, 'Giao tiếp hòa đồng với đội ngũ.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 10:10:00', '2026-02-08 10:10:00'),
    (4, 1, 4, 8.50, 'Chăm chỉ, chịu khó lắng nghe góp ý.', (SELECT user_id FROM users WHERE user_name = 'mentor02'), '2026-02-08 10:15:00', '2026-02-08 10:15:00'),

    -- Mentor 3 chấm điểm cho Student 5 (Assignment 5) ở Round 1
    (5, 1, 1, 8.00, 'Nắm chắc kiến thức Security và JWT.', (SELECT user_id FROM users WHERE user_name = 'mentor03'), '2026-02-09 09:00:00', '2026-02-09 09:00:00'),
    (5, 1, 2, 8.00, 'Logic xử lý phân quyền tương đối chặt chẽ.', (SELECT user_id FROM users WHERE user_name = 'mentor03'), '2026-02-09 09:05:00', '2026-02-09 09:05:00'),
    (5, 1, 3, 7.50, 'Làm việc nhóm khá tốt.', (SELECT user_id FROM users WHERE user_name = 'mentor03'), '2026-02-09 09:10:00', '2026-02-09 09:10:00'),
    (5, 1, 4, 9.00, 'Báo cáo tiến độ đầy đủ không sót buổi nào.', (SELECT user_id FROM users WHERE user_name = 'mentor03'), '2026-02-09 09:15:00', '2026-02-09 09:15:00');