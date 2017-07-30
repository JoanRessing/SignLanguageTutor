import java.io.IOException;

/**
 * Read csv data and convert to confusion matrix. Part of bachelor thesis project.
 * If you want to use the code you will have to adjust the code since many things are hardcoded.
 * @author Joan Ressing
 */
public class ConfusionComputer {

	//Create two sets of gestures.
	public static String[] setA = {"always","autumn","evening","fast","februari","july","life","may","october","saturday"};
	public static String[] setB = {"april","begin","december","earlier","march","monday","september","slow","summer","year"};
	
	public static void main(String[] args) throws IOException {
		boolean set = false;									//true for setA, false for setB
		
		String[][] data = readData(set);						//EDIT FILE NAMES WITHIN FUNCTION
		int[][] confusion = new int[10][10];
		
		for(int i=0; i < data.length; i++) {
			int x = getGestureIndex(data[i][0], set);
			int y = getGestureIndex(data[i][1], set);

			if(x >= 0 && y >= 0)
				confusion[x][y] += 1;
		}
		
		printConfusion(confusion, set);	
	}

	private static void printConfusion(int[][] confusion, boolean set) {
		System.out.print("\t");
		printSet(set);
		
		String[] ss = set ? setA : setB;
		
		for(int i=0; i < confusion.length; i++) {
			if(ss[i].length() >=8)
				System.out.print(ss[i]);
			else
				System.out.print(ss[i] + "\t");
			
			for(int j=0; j < confusion[0].length; j++) {
				System.out.print(confusion[i][j] + "\t");
			}
			System.out.println();
		}
	}

	private static void printSet(boolean set) {
		String[] ss = set ? setA : setB;
		for(String s: ss)
			if(s.length() >= 8)
				System.out.print(s);
			else
				System.out.print(s + "\t");
		System.out.println();
	}

	/**
	 * Returns the index of the given string in the gesture array.
	 * @param name: name of gesture
	 * @param set:	true for setA, false for setB
	 * @return index of name
	 */
	private static int getGestureIndex(String name, boolean set) {
		int index = 0;
		
		String[] signs = set ? setA : setB;
		
		if(name.equals("weet ik niet"))
			return -1;
		
		for(String s: signs)
			if(s.equals(name))
				break;
			else
				index++;
			
		return index;
	}

	private static String[][] readData(boolean set) throws IOException {
		String [][] data = new String[480][2];
		for(int i=0; i<480; i++) {data[i][0]=""; data[i][1]="";}
			
		FileConverter fc = new FileConverter();
		
		if(set) {
			add(data, fc.readLines("pilotdata_p1_r1_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p2_r2_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p3_r1_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p4_r2_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p5_r1_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p6_r2_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p7_r2_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p8_r1_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p9_r1_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p10_r1_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p11_r2_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p12_r2_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p13_r1_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p14_r2_g1_c1.csv"));
			add(data, fc.readLines("pilotdata_p15_r1_g2_c2.csv"));
			add(data, fc.readLines("pilotdata_p16_r2_g2_c2.csv"));
		} else {
			add(data, fc.readLines("pilotdata_p1_r1_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p2_r2_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p3_r1_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p4_r2_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p5_r1_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p6_r2_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p7_r2_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p8_r1_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p9_r1_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p10_r1_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p11_r2_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p12_r2_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p13_r1_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p14_r2_g1_c2.csv"));
			add(data, fc.readLines("pilotdata_p15_r1_g2_c1.csv"));
			add(data, fc.readLines("pilotdata_p16_r2_g2_c1.csv"));
		}

		return data;
	}

	private static void add(String[][] data, String[] lines) {
		int i=0;
		while(!data[i][0].equals(""))		//find free index
			i+=30;
		
		for(int j=2; j<32; j++) {
			data[i] = lines[j].split(",");
			i++;
		}
	}
	
}
