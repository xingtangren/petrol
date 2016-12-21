package petrol;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eltima.components.ui.DatePicker;

import javax.swing.ListSelectionModel;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DatePicker dp;
	private JTextField moneyTextField;
	private JTextField kilometreTextField;
	private JLabel sumMoneyLabel;
	private JButton saveBtn;
	private JButton cancelBtn;
	private static MainFrame frame;
	
	private List<PetrolBean> petrolList;
	private String FILE_PATH = "/Applications/bora/data.txt";
	private int editIndex = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		setTitle("大宝宝加油记录 [ power by zuobin v1.0 ]");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		initForm();
		initJTable();
	}
	
	/**
	 * 初始化表格
	 */
	private void initJTable(){
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 700, 270);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.setBorder(null);
		petrolList = FileUtils.readLine2List(FileUtils.createFile(FILE_PATH));
		loadTableData(table,petrolList);
		RowRenderer.makeFace(table);//奇偶行变色
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tableRowClicked(); 
			}
		});
	}
	
	private void tableRowClicked(){
		saveBtn.setText("修改");
		int rowIndex = table.getSelectedRow();
		editIndex = rowIndex;
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		dp.setText(model.getValueAt(rowIndex, 0)+"");
		moneyTextField.setText(model.getValueAt(rowIndex, 1)+"");
		kilometreTextField.setText(model.getValueAt(rowIndex, 2)+"");
	}
	/**
	 * 初始化输入表单
	 */
	private void initForm(){
		JLabel lblNewLabel = new JLabel("日期：");
		lblNewLabel.setBounds(30, 310, 60, 30);
		contentPane.add(lblNewLabel);
		
		dp = new DatePicker(this);  
		dp.setBorder(null);
		dp.setPattern("yyyy-MM-dd"); 
		dp.setEditorable(false);
		dp.setBounds(77, 310, 577, 30);
		contentPane.add(dp);
		
		JLabel moneyLabel = new JLabel("金额：");
		moneyLabel.setBounds(30, 350, 60, 30);
		contentPane.add(moneyLabel);
		
		moneyTextField = new JTextField();
		moneyTextField.setColumns(10);
		moneyTextField.setBounds(77, 350, 560, 30);
		contentPane.add(moneyTextField);
		
		JLabel kilometreLabel = new JLabel("公里数：");
		kilometreLabel.setBounds(30, 390, 60, 30);
		contentPane.add(kilometreLabel);
		
		kilometreTextField = new JTextField();
		kilometreTextField.setColumns(10);
		kilometreTextField.setBounds(77, 390, 560, 30);
		contentPane.add(kilometreTextField);
		
		saveBtn = new JButton("添加");
		saveBtn.addActionListener(new SaveButtonActionListener());
		saveBtn.setBounds(200, 430, 120, 35);
		contentPane.add(saveBtn);
		
		cancelBtn = new JButton("关闭");
		cancelBtn.addActionListener(new CancelButtonActionListener());
		cancelBtn.setBounds(380, 430, 120, 35);
		contentPane.add(cancelBtn);
		
		sumMoneyLabel = new JLabel("");
		sumMoneyLabel.setBounds(30, 275, 300, 30);
		contentPane.add(sumMoneyLabel);
	}
	
	/**
	 * 保存按钮点击事件
	 * @author zuobin
	 *
	 */
	private class SaveButtonActionListener implements ActionListener{  
        public void actionPerformed(ActionEvent e) { 
        	   if(!validateForm()){
        		   return;
        	   }
               try{
            	   if(petrolList==null){
	        		   petrolList = FileUtils.readLine2List(FileUtils.createFile(FILE_PATH)); 
	               }
            	   String date = dp.getText();
            	   int money = Integer.parseInt(moneyTextField.getText());
                   int kilometre = Integer.parseInt(kilometreTextField.getText());
                   PetrolBean newBean = new PetrolBean(date, money, kilometre, 0);
                   PetrolBean lastBean;
                   if(editIndex==-1){
                	   lastBean = petrolList.get(petrolList.size()-1);
                	   double consumption = lastBean.getMoney()*1.0/(kilometre-lastBean.getKilometre());
                       DecimalFormat df= new DecimalFormat("######0.00");  
                       consumption = Double.parseDouble(df.format(consumption));
                       newBean.setConsumption(consumption);
                	   petrolList.add(newBean);
                   }else{
                	   int realIndex = petrolList.size()-1-editIndex;
                	   if(realIndex==0){//修改第0行时,consumption为0
                    	   petrolList.set(realIndex , newBean);
                	   }else if(realIndex == petrolList.size()-1){//修改最后1行时，更改最后一行的consumption
                		   lastBean = petrolList.get(petrolList.size()-2);
                    	   double consumption = lastBean.getMoney()*1.0/(kilometre-lastBean.getKilometre());
                           DecimalFormat df= new DecimalFormat("######0.00");  
                           consumption = Double.parseDouble(df.format(consumption));
                           newBean.setConsumption(consumption);
                    	   petrolList.set(realIndex , newBean);
                	   }else{//修改中间的行时，更改当前行的consumption和当前行的下一行的consumption
                		   lastBean = petrolList.get(realIndex-1);
                    	   double consumption = lastBean.getMoney()*1.0/(kilometre-lastBean.getKilometre());
                           DecimalFormat df= new DecimalFormat("######0.00");  
                           consumption = Double.parseDouble(df.format(consumption));
                           newBean.setConsumption(consumption);
                    	   petrolList.set(realIndex , newBean);
                    	   //修改当前行的下一行的consumption
                    	   PetrolBean nextBean = petrolList.get(realIndex+1);
                    	   consumption = newBean.getMoney()*1.0/(nextBean.getKilometre()-newBean.getKilometre());
                           consumption = Double.parseDouble(df.format(consumption));
                           nextBean.setConsumption(consumption);
                           petrolList.set(realIndex+1, nextBean);
                	   }
                   }
                   FileUtils.writeList2Txt(FileUtils.createFile(FILE_PATH), petrolList);
                   loadTableData(table,petrolList);
                   editIndex = -1;
                   dp.setText("");
                   moneyTextField.setText("");
                   kilometreTextField.setText("");
                   saveBtn.setText("添加");
               }catch(NumberFormatException ne){
            	   ne.printStackTrace();
            	   JOptionPane.showMessageDialog(frame, "输入格式不正确！");
               }catch(Exception ex){
            	   ex.printStackTrace();
            	   JOptionPane.showMessageDialog(frame, "保存失败！");
               }
        }     
    }  
	
	/**
	 * 关闭按钮点击事件
	 * @author zuobin
	 *
	 */
	private class CancelButtonActionListener implements ActionListener{  
        public void actionPerformed(ActionEvent e) {  
        	frame.dispose();
        }     
    }  
	
	/**
	 * 加载表数据
	 * @param jtable
	 * @param pList
	 */
	private void loadTableData(JTable jtable,List<PetrolBean> pList){
		String[] columnArr = { "日期", "金额", "公里数", "油耗(元/公里)" };
		DefaultTableModel tableModle = new DefaultTableModel() {
			 public boolean isCellEditable(int row,int column) {
			        return false;
			 }
		};
		tableModle.setColumnIdentifiers(columnArr);
		int rowCount = pList.size();
		tableModle.setRowCount(rowCount);
		PetrolBean bean;
		int rowIndex = 0;
		for (int i=pList.size()-1;i>=0;i--) {
			bean = pList.get(i);
			tableModle.setValueAt(bean.getDate() , rowIndex, 0);
			tableModle.setValueAt(bean.getMoney() , rowIndex, 1);
			tableModle.setValueAt(bean.getKilometre() , rowIndex,2);
			tableModle.setValueAt(bean.getConsumption() , rowIndex, 3);
			rowIndex++;
		}
		jtable.setModel(tableModle);
		RowRenderer.makeFace(table);//奇偶行变色
		updateSumMoneyLabel();
	}
	
	/**
	 * 更新总计金额
	 */
	private void updateSumMoneyLabel(){
		if(petrolList==null){
 		   petrolList = FileUtils.readLine2List(FileUtils.createFile(FILE_PATH)); 
        }
		int sumMoney = 0;
		for(PetrolBean bean : petrolList){
			sumMoney += bean.getMoney();
		}
		double consumption= 0.0;
		int totalKilometre = petrolList.get(petrolList.size()-1).getKilometre();
		consumption = totalKilometre==0 ? consumption : sumMoney*1.0/totalKilometre;
		DecimalFormat df= new DecimalFormat("######0.00");
		sumMoneyLabel.setText("合计金额:"+sumMoney+"元 , 平均每公里:"+df.format(consumption)+"元");
	}
	/**
	 * 表单校验
	 * @return
	 */
	private boolean validateForm(){
		if(isExistEmptyField()){
			JOptionPane.showMessageDialog(frame, "存在为空输入！");
			return false;
		}else if(!isInteger(moneyTextField.getText())){
			JOptionPane.showMessageDialog(frame, "金额请输入整数！");
			return false;
		}else if(!isInteger(kilometreTextField.getText())){
			JOptionPane.showMessageDialog(frame, "公里数请输入整数！");
			return false;
		}
		return true;
	}
	
	private boolean isInteger(String value){
		try{
			Integer.parseInt(value);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private boolean isExistEmptyField(){
		if("".equals(dp.getText())||"".equals(moneyTextField.getText())||"".equals(kilometreTextField.getText())){
			return true;
		}else{
			return false;
		}
	}
}
