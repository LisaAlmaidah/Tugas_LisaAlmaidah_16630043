/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas_lisaalmaidah_16630043.Home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tugas_lisaalmaidah_16630043.Pengaturan.Koneksi;

/**
 *
 * @author Administrator
 */
public class HomeController {
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel tabMode;
    
    public String kode_otomatis(){
        String kode = "";
        int kodeLama;
        try {
            pst = conn.prepareStatement("select kode_film from tb_film order by kode_film desc limit 1");
            rs = pst.executeQuery();
            if(!rs.next()){
                kode = "FM-0001";
            }else{
                kodeLama = Integer.parseInt(rs.getString(1).substring(4))+1;
                if (kodeLama<10){
                    kode = "FM-000"+kodeLama;
                }else if(kodeLama>=10 && kodeLama < 100){
                    kode = "FM-00"+kodeLama;
                }else if(kodeLama>=100 && kodeLama < 1000){
                    kode = "FM-0"+kodeLama;
                }else{
                    kode = "FM-"+kodeLama;
                }
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Terjadi kesalahan pada kode otomatis. Details:\n"+ex.toString());
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kode;
    }
    
    public void refresh(HomeView hv){
        hv.textKode.setEnabled(false);
        hv.buttonSimpan.setEnabled(true);
        hv.buttonHapus.setEnabled(false);
        hv.buttonUbah.setEnabled(false);
        hv.textKode.setText(kode_otomatis());
        hv.textJudul.setText("");
        hv.textSutradara.setText("");
        hv.textTahunPerilisan.setText("");
        hv.textSinopsis.setText("");
    }
    private String validasi(HomeView fb){
        /*Membuat fungsi validasi. Digunakan untuk mencek inputan apakah data sudah terisi.
        Jika data sudah terisi maka keterangan menjadi Sukses
        */
        String ket;        
        if (fb.textKode.getText().equals("")){
            ket = "Kode film belum diisi!";
        }
        else if (fb.textJudul.getText().equals("")){
            ket = "Judul film belum diisi!";
        }
        else if(fb.textSutradara.getText().equals("")){
            ket = "Nama sutradara belum diisi!";
        }
        else if(fb.textTahunPerilisan.getText().equals("")){
            ket = "Tahun perilisan film belum diisi!";
        }
        else if(fb.textSinopsis.getText().equals("")){
            ket = "Sinopsis film belum diisi!";
        }
        else{
            ket = "Sukses";
        }
        return ket;
    }
    
    
    public void simpan_film(HomeView fb){
        //Ini adalah method simpan film. Bertujuan untuk menyimpan data film yang diinput ke tabel film
        if (validasi(fb).equals("Sukses")){
                 try {
                int isSimpan;
                pst = conn.prepareStatement("insert into tb_film values(?,?,?,?,?)");                
                //pst = conn.prepareStatement("insert into tbl_film values('"+fb.textKode.getText()+"', '"+fb.textJudul.getText()+"','"+fb.comboKategori.getSelectedItem()+"','"+fb.textSutradara.getText()+"','"+fb.textTahunPerilisan.getText()+"')" );                                
                pst.setString(1, fb.textKode.getText()); //maksud 1 disini adalah kolom pertama dari tabel film
                pst.setString(2, fb.textJudul.getText());
                pst.setString(3, fb.textSutradara.getText());
                pst.setString(4, fb.textTahunPerilisan.getText());
                pst.setString(5, fb.textSinopsis.getText());
                isSimpan = pst.executeUpdate();
                if (isSimpan == 1){
                    JOptionPane.showMessageDialog(null, "Data film berhasil disimpan!", "Sukses Simpan Data", JOptionPane.INFORMATION_MESSAGE);
                    refresh(fb);
                    tampil_data(fb);
                }else{
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada simpan data","Gagal Simpan Data",JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses simpan data.\nDetail: "+ex.toString());
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, validasi(fb),"Kesalahan Input Data!",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapus_film(HomeView fb){
        //ini adalah method hapus film. Digunakan untuk menghapus data film yang ada pada tabel film
        int hapus = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data film dengan id film:"+ fb.textKode.getText() + "?",
                "Hapus Data?",JOptionPane.YES_NO_OPTION);
        if (hapus == JOptionPane.YES_OPTION){
            try {
                int isHapus;
                pst = conn.prepareStatement("delete from tb_film where kode_film=?");                
                pst.setString(1, fb.textKode.getText());
                isHapus = pst.executeUpdate();
                if (isHapus == 1){
                    JOptionPane.showMessageDialog(null, "Data film berhasil dihapus!","Hapus Data Sukses",JOptionPane.INFORMATION_MESSAGE);
                    refresh(fb);
                    tampil_data(fb);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada hapus data film\nDetail: "+ex.toString());
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void ubah_film(HomeView fb){
        //ini adalah method ubah film. Digunakan untuk mengubah data film yang ada pada tabel film
        int ubah = JOptionPane.showConfirmDialog(null, "Yakin ingin mengubah data film dengan id film:"+ fb.textKode.getText() + "?",
                "Ubah Data?",JOptionPane.YES_NO_OPTION);
        if (ubah == JOptionPane.YES_OPTION){
            if(validasi(fb).equals("Sukses")){
                try {
                    int isUbah;
                   // pst = conn.prepareStatement("update tbl_film set nama_film=?, kategori=?, username=?, password=? where kode_film=?");                    
                    pst = conn.prepareStatement("update tb_film set judul_film=?, sutradara=?, tahun_perilisan=?, sinopsis=? where kode_film=?");                                     
                    pst.setString(1, fb.textJudul.getText());
                    pst.setString(2, fb.textSutradara.getText());
                    pst.setString(3, fb.textTahunPerilisan.getText());
                    pst.setString(4, fb.textSinopsis.getText());
                    pst.setString(5, fb.textKode.getText());
                    isUbah = pst.executeUpdate();
                    if (isUbah == 1){
                        JOptionPane.showMessageDialog(null, "Data film berhasil diperbaharui!","Sukses Ubah Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        refresh(fb);
                        tampil_data(fb);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses ubah data film\nDetail: "+ex.toString(),
                            "Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }else{
                JOptionPane.showMessageDialog(null, validasi(fb),"Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
            }
        }       
    }
    
    public void tampil_data(HomeView fb){
        //ini adalah method tampil data. Digunakan untuk menampilkan data film yang ada pada tabel film
        Object[] Baris ={"Kode Film","Judul Film","Sutradara","Tahun Perilisan","Sinopsis"};
        tabMode = new DefaultTableModel(null, Baris);
        fb.tabelFilm.setModel(tabMode);        
        try {
           pst = conn.prepareStatement("select * from tb_film");
           rs = pst.executeQuery();
            while(rs.next()){
                String kode = rs.getString("kode_film"); //rs.getString(1);
                String judul = rs.getString("judul_film");
                String sutradara = rs.getString("sutradara");
                String tahun = rs.getString("tahun_perilisan");
                String sinopsis = rs.getString("sinopsis");
                String[] data={kode, judul, sutradara, tahun, sinopsis};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada tampil data film.\nDetails: "+e.toString());
        }
    }
    
    public void cari_film(HomeView fb){
        //ini adalah method cari film. Digunakan untuk menampilkan data film yang ada pada tabel film sesuai yang dicari
        Object[] Baris ={"Kode Film","Judul Film","Sutradara","Tahun Perilisan","Sinopsis"};
        tabMode = new DefaultTableModel(null, Baris);
        fb.tabelFilm.setModel(tabMode);        
        try {
            pst = conn.prepareStatement("select * from tb_film where judul_film like '%"+fb.textCari.getText()+"%'");
            rs = pst.executeQuery();
            while(rs.next()){
                String kode = rs.getString("kode_film"); //rs.getString(1);
                String judul = rs.getString("judul_film");
                String sutradara = rs.getString("sutradara");
                String tahun = rs.getString("tahun_perilisan");
                String sinopsis = rs.getString("sinopsis");
                String[] data={kode, judul, sutradara, tahun, sinopsis};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada tampil data film.\nDetails: "+e.toString());
        }
    }
    
    public void pilih_film(HomeView fb){
        //ini adalah method pilih film. Digunakan untuk menampilkan data film yang dipilih pada tblBarang ke inputan
        int row = fb.tabelFilm.getSelectedRow();
        fb.textKode.setText(fb.tabelFilm.getValueAt(row, 0).toString());
        fb.textJudul.setText(fb.tabelFilm.getValueAt(row, 1).toString());
        fb.textSutradara.setText(fb.tabelFilm.getValueAt(row, 2).toString());
        fb.textTahunPerilisan.setText(fb.tabelFilm.getValueAt(row, 3).toString());                
        fb.textSinopsis.setText(fb.tabelFilm.getValueAt(row, 4).toString());                
        fb.textKode.setEnabled(false);
        fb.buttonSimpan.setEnabled(false);
        fb.buttonUbah.setEnabled(true);
        fb.buttonHapus.setEnabled(true);
    }
    
    public void keluar(HomeView fb){
//        int selectedOption = JOptionPane.showConfirmDialog(null, "Apakah anda ingin keluar dari form?", "Tutup Form", JOptionPane.YES_NO_OPTION);
//        if (selectedOption == JOptionPane.YES_OPTION) {
//            this.dispose();
//        }
    }
}
