package hcmus.edu.vn.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TimKiemCDDVDUI extends JDialog {
	
	JTextField txtTimKiem;
	JButton btnTimKiem;
	
	DefaultTableModel dtm;
	JTable tblCD;
	
	Connection conn=null;
	PreparedStatement preStatement=null;
	ResultSet result=null;
	
	public TimKiemCDDVDUI(String tieude)
	{
		this.setTitle(tieude);
		addControl();
		addEvent();
		ketNoiCoSoDuLieu();
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
		btnTimKiem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyTimKiem();
			}
		});
	}

	protected void xuLyTimKiem() {
		// TODO Auto-generated method stub
		try
		{
			
			String sql="select * from CDDVDCollection where Ma LIKE ? OR TieuDe LIKE ? OR NamXuatBan LIKE ? OR LoaiDia LIKE ?";
			preStatement =conn.prepareStatement(sql);
			String keyword ="%"+txtTimKiem.getText()+"%";
			preStatement.setString(1, keyword);
			preStatement.setString(2, keyword);
			preStatement.setString(3, keyword);
			preStatement.setString(4, keyword);
			result = preStatement.executeQuery();
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
		JTable tblCD = new JTable(dtm);
		JScrollPane scTBL=new JScrollPane(tblCD,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnMain.add(scTBL,BorderLayout.CENTER);
		
		JPanel pnTim= new JPanel();
		pnTim.setLayout(new FlowLayout());
		JLabel lblTimKiem= new JLabel("Nhap du lieu");
		txtTimKiem=new JTextField(15);
		btnTimKiem=new JButton("Tim kiem");
		pnTim.add(lblTimKiem);
		pnTim.add(txtTimKiem);
		pnTim.add(btnTimKiem);
		pnMain.add(pnTim,BorderLayout.NORTH);
	}
	public void showWindow()
	{
		this.setSize(400, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setVisible(true);
	}
}
