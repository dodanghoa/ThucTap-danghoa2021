package com.example.ud_giaohang.model;

import java.util.List;

public class DonHang {
    public User getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(User nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public User getNguoiLayHang() {
        return nguoiLayHang;
    }

    public void setNguoiLayHang(User nguoiLayHang) {
        this.nguoiLayHang = nguoiLayHang;
    }

    public User getNguoiGiaoHang() {
        return nguoiGiaoHang;
    }

    public void setNguoiGiaoHang(User nguoiGiaoHang) {
        this.nguoiGiaoHang = nguoiGiaoHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getNguoiTra() {
        return nguoiTra;
    }

    public void setNguoiTra(String nguoiTra) {
        this.nguoiTra = nguoiTra;
    }

    public String getThoiGianGiao() {
        return thoiGianGiao;
    }

    public void setThoiGianGiao(String thoiGianGiao) {
        this.thoiGianGiao = thoiGianGiao;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(String trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getTrongLuong() {
        return trongLuong;
    }

    public void setTrongLuong(String trongLuong) {
        this.trongLuong = trongLuong;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getTienHang() {
        return tienHang;
    }

    public void setTienHang(String tienHang) {
        this.tienHang = tienHang;
    }

    public String getTienThuHo() {
        return tienThuHo;
    }

    public void setTienThuHo(String tienThuHo) {
        this.tienThuHo = tienThuHo;
    }

    public String getCuocPhi() {
        return cuocPhi;
    }

    public void setCuocPhi(String cuocPhi) {
        this.cuocPhi = cuocPhi;
    }

    public String getTongCuoc() {
        return tongCuoc;
    }

    public void setTongCuoc(String tongCuoc) {
        this.tongCuoc = tongCuoc;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getNgayGiaoHang() {
        return ngayGiaoHang;
    }

    public void setNgayGiaoHang(String ngayGiaoHang) {
        this.ngayGiaoHang = ngayGiaoHang;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public DonHang(User nguoiGui, User nguoiLayHang, User nguoiGiaoHang, String maDonHang, String hoTen, String diaChi, String tenHang, String nguoiTra, String thoiGianGiao, String ghiChu, String trangThaiDonHang, String soDT, String trongLuong, String soLuong, String tienHang, String tienThuHo, String cuocPhi, String tongCuoc, String ngayDat, String ngayGiaoHang, List<String> image) {
        this.nguoiGui = nguoiGui;
        this.nguoiLayHang = nguoiLayHang;
        this.nguoiGiaoHang = nguoiGiaoHang;
        this.maDonHang = maDonHang;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.tenHang = tenHang;
        this.nguoiTra = nguoiTra;
        this.thoiGianGiao = thoiGianGiao;
        this.ghiChu = ghiChu;
        this.trangThaiDonHang = trangThaiDonHang;
        this.soDT = soDT;
        this.trongLuong = trongLuong;
        this.soLuong = soLuong;
        this.tienHang = tienHang;
        this.tienThuHo = tienThuHo;
        this.cuocPhi = cuocPhi;
        this.tongCuoc = tongCuoc;
        this.ngayDat = ngayDat;
        this.ngayGiaoHang = ngayGiaoHang;
        this.image = image;
    }

    public DonHang() {
    }

    User nguoiGui, nguoiLayHang, nguoiGiaoHang;
    String maDonHang, hoTen, diaChi, tenHang, nguoiTra, thoiGianGiao, ghiChu, trangThaiDonHang, soDT, trongLuong, soLuong, tienHang, tienThuHo, cuocPhi, tongCuoc, ngayDat,ngayGiaoHang;
    List<String> image;
}