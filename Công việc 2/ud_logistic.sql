-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3308
-- Thời gian đã tạo: Th8 10, 2021 lúc 12:43 PM
-- Phiên bản máy phục vụ: 5.7.28
-- Phiên bản PHP: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `ud_logistic`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
CREATE TABLE IF NOT EXISTS `khachhang` (
  `maKhachHang` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenKhachHang` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `diaChi` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soDienThoai` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`maKhachHang`, `tenKhachHang`, `diaChi`, `soDienThoai`) VALUES
('610d6af62570f9bf34dd2157', 'Hollie Cote', '964 Berkeley Place, Fairacres, Massachusetts, 578', '+84 (880) 486-389w'),
('610d6af62e33f6b39ec520d6', 'Irma Rushs', '117 Lake Place, Trona, Alabama, 8462', '+84 (910) 519-3653'),
('610d6af68358b596e8a319d0', 'Lynnette Henson', '914 Prescott Place, Walland, Vermont, 6811', '+84 (822) 594-2427'),
('610d6af69b2d7b8ef14dd236', 'Landry Frazier', '327 Vanderbilt Avenue, Mappsville, West Virginia, 6735', '+84 (809) 511-2404'),
('610d6af6a303bb7931e48d34', 'Dickson Hernandez', '320 Hegeman Avenue, Marenisco, American Samoa, 64603', '+84 (867) 587-2638'),
('610d6af6ac1d8966707690da', 'Anderson Rivers', '772 Jackson Place, Grayhawk, Michigan, 3995', '+84 (980) 583-2426'),
('610d6af6af209740fefaf055', 'Stella Carter', '177 Kane Street, Masthope, Marshall Islands, 4279', '+84 (885) 510-2951'),
('610d6d341481516216a986c0', 'Bridget Burchs', '640 Kent Avenue, Brecon, Texas, 1824', '+84 (971) 512-3288'),
('610d6d344a83cc09f27831aa', 'Odonnell Buckner', '110 Regent Place, Williston, Maryland, 4169', '+84 (853) 548-2222'),
('610d6d347ab51e34cf9ac3a5', 'Albert Aguirre', '563 Monument Walk, Hampstead, North Carolina, 3860', '+84 (887) 565-2104'),
('610d6d34afc4237bc6011882', 'Sarah Carroll', '640 Grove Street, Brownlee, Utah, 552', '+84 (830) 511-3001'),
('610d6d34b9e0be2dc9ad917b', 'Oneal Aguilar', '925 Fairview Place, Snowville, Oregon, 7652', '+84 (985) 532-2789'),
('610d6d34e70952f47a9ef5c0', 'Alyssa Cruz', '636 Amherst Street, Cornucopia, New Jersey, 9969', '+84 (963) 524-2427'),
('610d6d34f5a4adb9d0763dc8', 'Inez Carey', '710 Boulevard Court, Needmore, New York, 3314', '+84 (831) 561-3615'),
('610d6d93027fb98123546e59', 'Sophia Everett', '989 Louise Terrace, Sims, Maine, 6263', '+84 (950) 565-2183'),
('610d6d93041748905c5334e8', 'Marta Tucker', '139 Oak Street, Mulino, Arizona, 1337', '+84 (947) 438-3693'),
('610d6d932c401aacb7c0e719', 'Marla Jennings', '968 Jackson Street, Williamson, Indiana, 1923', '+84 (897) 577-2867'),
('610d6d935b5363f5f2e643bf', 'Penelope Sullivan', '541 Dorchester Road, Bethany, Washington, 7936', '+84 (938) 544-3801'),
('610d6d935df4365cb559352f', 'Walls Herman', '701 Bushwick Place, Dana, Oklahoma, 3298', '+84 (973) 434-3358');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
CREATE TABLE IF NOT EXISTS `nhanvien` (
  `maNhanVien` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `hoTen` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `diaChi` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soDienThoai` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `boPhan` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `chucVu` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`maNhanVien`, `hoTen`, `diaChi`, `soDienThoai`, `boPhan`, `chucVu`) VALUES
('1507b204-48ef-45e6-a6df-28db8580136a', 'Nguyễn Ngọc Gia Huy', 'Q2,HCM', '665421869', 'Kinh doanh', 'Nhân viên'),
('0eb99749-0960-4dea-bdf9-96dce6f2914e', 'Do Dang Hoa', 'HCM', '0797842160', 'Kinh doanh', 'Nhân viên'),
('c053df07-a57b-4e76-892a-b788c39a04b0', 'Lê HữuTrung', 'TP Thủ Đức, HCM', '54322790', 'Kinh doanh', 'Nhân viên'),
('d026f444-166c-4b30-bea3-28af6782b154', 'Lê Thành Đạt', 'Gò Vấp TP HCM', '34678763', 'Kinh doanh', 'Nhân viên');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phongban`
--

DROP TABLE IF EXISTS `phongban`;
CREATE TABLE IF NOT EXISTS `phongban` (
  `maPhong` text CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `tenPhong` text CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `phongban`
--

INSERT INTO `phongban` (`maPhong`, `tenPhong`) VALUES
('7e50aea9-473f-492d-9397-e22457d145c6', 'Nhân sự'),
('7f4a321b-23d6-477a-99af-836df927c545', 'Kế toán'),
('acc891d4-9981-403b-bac8-beb14b32422f', 'Kinh doanh'),
('ad2a060e-1bda-4292-a129-bc44f4df4563', 'Quản lý'),
('f2a89906-1ee6-4f2e-b7e6-2ad5efe79eb9', 'Truyền thông');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thongtinvanchuyen`
--

DROP TABLE IF EXISTS `thongtinvanchuyen`;
CREATE TABLE IF NOT EXISTS `thongtinvanchuyen` (
  `maThongTinVanChuyen` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soXe` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soToKhai` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `ngayDiGiaoHang` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `noiLayCong` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `donGia` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `phiCauDuong` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `neoXe` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tamUng` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soBill` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `soConTainer` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `noiDongHang` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `nhienLieu` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `phiNangCong` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `luongTheoChuyen` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `khachHang` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `nhanVien` text COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thongtinvanchuyen`
--

INSERT INTO `thongtinvanchuyen` (`maThongTinVanChuyen`, `soXe`, `soToKhai`, `ngayDiGiaoHang`, `noiLayCong`, `donGia`, `phiCauDuong`, `neoXe`, `tamUng`, `soBill`, `soConTainer`, `noiDongHang`, `nhienLieu`, `phiNangCong`, `luongTheoChuyen`, `khachHang`, `nhanVien`) VALUES
('0b92bb89-c1f4-44bc-959b-d2477189d716', '3', '1', '18/10/2021', 'Cty', '2000000', '500000', '100000', '2000000', '1', '1', 'CTY TN Hoàn Kiếms', '20000', '50000', '500000', '610d6af69b2d7b8ef14dd236', '1507b204-48ef-45e6-a6df-28db8580136a'),
('b5da01bc-e71e-4722-b692-a74c0678e0d8', '2', '1', '10-2-2021', 'Quận 1', '2000000', '50000', '15000', '300000', '1', '3', 'Quận 1', '20000', '20000', '500000', '610d6af62570f9bf34dd2157', 'd026f444-166c-4b30-bea3-28af6782b154'),
('1313bb70-2e01-4931-ab54-9c24808f3c60', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '610d6af62570f9bf34dd2157', '1507b204-48ef-45e6-a6df-28db8580136a');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
