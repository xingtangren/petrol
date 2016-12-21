package petrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static void main(String[] args) {
//		File file = createFile("assets/data.txt");
//		List<PetrolBean> list = readLine2List(file);
//		PetrolBean tempBean = new PetrolBean("2016-08-12", 150,4999,0.56);
//		list.add(tempBean);
//		list.add(tempBean);
//		writeList2Txt(file, list);
	}

	public static File createFile(String filePath) {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("创建txt文件出错！");
			e.printStackTrace();
		}
		return file;
	}

	public static List<PetrolBean> readLine2List(File file) {
		FileReader fr;
		List<PetrolBean> petroList = new ArrayList<PetrolBean>();
		try {
			fr = new FileReader(file.toString());
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] arrs = null;
			PetrolBean tempBean;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if(!"".equals(line)){
					arrs = line.split(",");
					tempBean = new PetrolBean(arrs[0], Integer.parseInt(arrs[1]),Integer.parseInt(arrs[2]),Double.parseDouble(arrs[3]));
					petroList.add(tempBean);
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			System.out.println("读取txt文件出错！");
			e.printStackTrace();
		}
		return petroList;
	}

	public static List<PetrolBean> writeList2Txt(File file, List<PetrolBean> petrolList) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.write("");
			BufferedWriter bw = new BufferedWriter(fw);
			String line = "";
			for (PetrolBean bean : petrolList) {
				line = bean.getDate()+","+bean.getMoney()+","+bean.getKilometre()+","+bean.getConsumption();
				bw.write(line + "\n");
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return petrolList;
	}

}
