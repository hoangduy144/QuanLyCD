package hcmus.edu.vn.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CDDVDUI extends JFrame {
	JTextField txtMa, txtTen, txtLoai, txtNamxb;
	JButton btnLuu, btnXoa, btnTim;

	DefaultTableModel dtm;
	JTable tblCD;

	Connection conn=null;
	PreparedStatement preStatement=null;
	ResultSet result=null;

	public CDDVDUI(String tieude)
	{
		super(tieude);
		addControl();
		addEvent();
		ketNoiCoSoDuLieu();
		hienThiToanBoCDDVD();
	}
	public boolean kiemTraMaTonTai(String ma)
	{
		try 
		{
			String sql="select * from CDDVDCollection where Ma=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtMa.getText());
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	public void showDetail(String ma)
	{
		try
		{
			String sql="select * from CDDVDCollection where Ma=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, ma);
			ResultSet rs=preStatement.executeQuery();
			if(rs.next())
			{
				txtMa.setText(rs.getString(1));
				txtTen.setText(rs.getString(2));
				txtLoai.setText(rs.getString(3));
				txtNamxb.setText(rs.getString(4));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();;
		}

	}

	private void hienThiToanBoCDDVD() {
		try
		{
			String sql="select * from CDDVDCollection";
			preStatement=conn.prepareStatement(sql);
			result=preStatement.executeQuery();
			dtm.setRowCount(0);
			while(result.next())
			{
				Vector<Object> vec = new Vector<Object>();
				vec.add(result.getString("Ma"));
				vec.add(result.getString("TieuDe"));
				vec.add(result.getString("LoaiDia"));
				vec.add(result.getInt("NamXuatBan"));
				dtm.addRow(vec);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void ketNoiCoSoDuLieu() {
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl=
					"jdbc:sqlserver://DESKTOP-QA475V7\\SQLEXPRESS:1433;"
							+ "user=sa;password=123456;databaseName=dbQuanLyCDDVD;"
							+ "integratedSecurity=true;encrypt=false";
			conn= DriverManager.getConnection(connectionUrl);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	private void addEvent() {
		// TODO Auto-generated method stub
		btnTim.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TimKiemCDDVDUI ui=new TimKiemCDDVDUI("Tim kiem CD");
				ui.showWindow();
			}
		});
		btnLuu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyLuuCDDVD();
			}
		});
		tblCD.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row=tblCD.getSelectedRow();
				if(row==-1) return;
				String ma=tblCD.getValueAt(row, 0)+"";
				showDetail(ma);
			}
		});
		btnXoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyXoaCDDVD();
			}
		});
	}

	protected void xuLyXoaCDDVD() {
		boolean kt = kiemTraMaTonTai(txtMa.getText());
		if(kt==false)
		{
			JOptionPane.showMessageDialog(null, "Ma ["+txtMa.getText()+"] khong ton tai trong CSDL, vui long kiem tra lai");
		}
		else
		{
			int ret =JOptionPane.showConfirmDialog(null, 
					"Ban co muon xoa ma ["+txtMa.getText()+"] khong?", 
					"Thong bao Xoa",
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)
				return;
			try {
				String sql="delete from CDDVDCollection where Ma=?";
				preStatement=conn.prepareStatement(sql);
				preStatement.setString(1, txtMa.getText());
				int x= preStatement.executeUpdate();
				if(x>0)
				{
					hienThiToanBoCDDVD();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	protected void xuLyLuuCDDVD() {
		if(kiemTraMaTonTai(txtMa.getText()))
		{
			int ret =JOptionPane.showConfirmDialog(null, 
					"Ma ["+txtMa.getText()+"] da ton tai, ban co muon cap nhat khong?", 
					"Thong bao cap nhat",
					JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)
				return;
			try {
				String sql="Update CDDVDCollection set TieuDe=?, LoaiDia=?, NamXuatBan=? where Ma=?";
				preStatement=conn.prepareStatement(sql);
				preStatement.setString(4, txtMa.getText());
				preStatement.setString(1, txtTen.getText());
				preStatement.setString(2, txtLoai.getText());
				preStatement.setString(3, txtNamxb.getText());
				int x= preStatement.executeUpdate();
				if(x>0)
				{
					hienThiToanBoCDDVD();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				String sql="insert into CDDVDCollection values(?,?,?,?)";
				preStatement=conn.prepareStatement(sql);
				preStatement.setString(1, txtMa.getText());
				preStatement.setString(2, txtTen.getText());
				preStatement.setString(3, txtLoai.getText());
				preStatement.setString(4, txtNamxb.getText());
				int x= preStatement.executeUpdate();
				if(x>0)
				{
					hienThiToanBoCDDVD();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addControl() {
		// TODO Auto-generated method stub
		Container con = getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnMain=new JPanel();
		pnMain.setLayout(new BorderLayout());
		con.add(pnMain, BorderLayout.CENTER);

		dtm = new DefaultTableModel();
		dtm.addColumn("Ma");
		dtm.addColumn("Ten");
		dtm.addColumn("Loai");
		dtm.addColumn("Nam xuat ban");
		tblCD = new JTable(dtm);
		JScrollPane scTBL=new JScrollPane(tblCD,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnMain.add(scTBL,BorderLayout.CENTER);

		JPanel pnThongTin = new JPanel();
		pnThongTin.setLayout(new BoxLayout(pnThongTin, BoxLayout.Y_AXIS));

		JPanel pnMa= new JPanel();
		pnMa.setLayout(new FlowLayout());
		JLabel lblMa= new JLabel("Ma");
		txtMa=new JTextField(15);
		pnMa.add(lblMa);
		pnMa.add(txtMa);
		pnThongTin.add(pnMa);

		JPanel pnTen= new JPanel();
		pnTen.setLayout(new FlowLayout());
		JLabel lblTen= new JLabel("Ten");
		txtTen=new JTextField(15);
		pnTen.add(lblTen);
		pnTen.add(txtTen);
		pnThongTin.add(pnTen);

		JPanel pnLoai= new JPanel();
		pnLoai.setLayout(new FlowLayout());
		JLabel lblLoai= new JLabel("Loai");
		txtLoai=new JTextField(15);
		pnLoai.add(lblLoai);
		pnLoai.add(txtLoai);
		pnThongTin.add(pnLoai);

		JPanel pnNamxb= new JPanel();
		pnNamxb.setLayout(new FlowLayout());
		JLabel lblNamxb= new JLabel("Nam xuat ban");
		txtNamxb=new JTextField(15);
		pnNamxb.add(lblNamxb);
		pnNamxb.add(txtNamxb);
		pnThongTin.add(pnNamxb);

		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout());
		btnLuu=new JButton("Luu");
		btnXoa=new JButton("Xoa");
		btnTim=new JButton("Tim kiem");
		pnButton.add(btnLuu);
		pnButton.add(btnXoa);
		pnButton.add(btnTim);
		pnThongTin.add(pnButton);

		pnMain.add(pnThongTin,BorderLayout.NORTH);

		lblMa.setPreferredSize(lblNamxb.getPreferredSize());
		lblTen.setPreferredSize(lblNamxb.getPreferredSize());
		lblLoai.setPreferredSize(lblNamxb.getPreferredSize());
	}
	public void showWindow()
	{
		this.setSize(500, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
