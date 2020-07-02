package Main;

import java.io.*;
import java.util.Properties;

public class Variables {

	private static Properties variables = new Properties();
	private static FileInputStream in;
	private static BufferedWriter writer;
	private static File file = new File(Defaults.saveDirectory + "\\GDBoard\\vars.board");

	static {
		while (true) {
			try {
				writer = new BufferedWriter(new FileWriter(file, true));
				in = new FileInputStream(file);
				break;
			} catch (IOException e) {
				file.mkdir();
				file.getParentFile().mkdir();
			}
		}
	}

	public static void setVar(String name, Object object) {
		try {
			in = new FileInputStream(file);
			variables.load(in);
			if (variables.containsKey(name)) {
				BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "\\GDBoard\\vars.board"));
				StringBuilder inputBuffer = new StringBuilder();
				String line;
				while ((line = file.readLine()) != null) {
					inputBuffer.append(line);
					inputBuffer.append('\n');
				}
				file.close();
				FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\vars.board");
				fileOut.write(inputBuffer.toString().replace(name + "=" + variables.get(name).toString(), name + "=" + object).getBytes());
				fileOut.close();
			} else {
				writer = new BufferedWriter(new FileWriter(file, true));
				writer.newLine();
				writer.write((name + "=" + object));
				writer.close();
			}
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static String getVar(String name){

		try {
			in = new FileInputStream(file);
			variables.load(in);
			in.close();
			if (variables.containsKey(name)) {
				return variables.get(name).toString();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/*public static void clearVars() throws IOException{
		FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\vars.board");
		fileOut.write("".getBytes());
		fileOut.close();
	}*/
}
