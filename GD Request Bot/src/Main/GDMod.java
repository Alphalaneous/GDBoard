package Main;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GDMod {
	public static void run(String... args){
		String PID = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("tasklist", "/fi", "\"IMAGENAME eq GeometryDash.exe\"", "/fo", "CSV").redirectErrorStream(true);
			Process process = pb.start();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				while (true) {
					String line = in.readLine();
					if (line == null) {
						break;
					}
					if(line.contains("GeometryDash.exe")){
						PID = line.split(",")[1].replaceAll("\"","");
					}
					System.out.println(line);
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		System.out.println(PID);
		try {
			String[] cmd = new String[]{System.getenv("APPDATA") + "\\GDBoard\\bin\\gdmod.exe", PID};
			String[] fillCmd = ArrayUtils.addAll(cmd, args);
			ProcessBuilder pb = new ProcessBuilder(fillCmd).redirectErrorStream(true);
			pb.start();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
