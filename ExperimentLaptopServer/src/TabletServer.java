import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Server program used by the participant to interact with Pepper/videos. Writes data to file automatically.
 * (You have to adjust the config file inbetween conditions. Set condition 1 to 2.)
 * @author Joan Ressing
 */
public class TabletServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		//Open config file.
		Properties p = new Properties();
		InputStream is = new FileInputStream("dataConfig.properties");
		p.load(is);
		
		//Read config file.
		int participant = Integer.parseInt(p.getProperty("participant"));
		int robot = Integer.parseInt(p.getProperty("robot"));				//robot first or second
		int group = Integer.parseInt(p.getProperty("group"));				//which set used first
		int condition = Integer.parseInt(p.getProperty("condition"));		//first or second condition
		int port = Integer.parseInt(p.getProperty("port"));	
		
		//Create serversocket.
		ServerSocket server = new ServerSocket(port);
		Window window = new Window();
		window.setVisible(true);
		
		try {
			//Set up connection with client. Pepper or laptop running 'ExperimentC'.
			Socket client = server.accept();
			BufferedInputStream in = new BufferedInputStream(client.getInputStream());
			PrintStream out = new PrintStream(client.getOutputStream(), true);
			
			//Create datafile.
			DateFormat df = new SimpleDateFormat("HHmm");
			Date today = Calendar.getInstance().getTime();        
			String date = df.format(today);
			BufferedWriter data = new BufferedWriter(new FileWriter(new File("pilotdata_p" + participant + "_r" + robot + "_g" + group + "_c" + condition + "-" + date + ".csv")));
			data.write("p" + participant + ",r" + robot + ",g" + group + ",c" + condition + "\ngesture,answer\n");
			data.flush();
			
			//Start learn phase.
			out.write(group == condition ? 1 : 2);
			String input = read(in);
			while(!input.equals("testphasestart")) {
				window.setLabel(input);
				System.out.println(input);
				input = read(in);
			}
			
			//Set up GUI for test phase.
			window.setUpGUI(data, out, group == condition ? 1 : 2);
			input = read(in);
			
			//Start test phase.
			while(!input.equals("testphaseover")) {
				data.write(input + ",");
				data.flush();
				input = read(in);
			}
			
			window.thankYou();
			in.close();
			out.close();
	    } finally {
	        server.close();
	    }
		Thread.sleep(3000);
		System.exit(1);
	}
	
	/**
	 * Read a word from inputstream.
	 * @param inputstream
	 * @return word
	 */
	private static String read(BufferedInputStream in) {
		String word = "";
		
		try {
			while(in.available() <= 0) {}
			while(in.available() > 0)
				word += (char)in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return word;
	}
}