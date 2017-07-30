
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends Frame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private Dimension screenSize;
	private JLabel waitLabel;
	private JButton[] buttons;
	
	public void setLabel(String s) throws InterruptedException {
		// Set new text
		waitLabel.setText(s);
		
		// (Re)center the label
		int width = waitLabel.getFontMetrics(waitLabel.getFont()).stringWidth(s);
		waitLabel.setBounds((screenSize.width-width)/2, screenSize.height/2-40, 600, 60);
	}
	
	public Window() throws InterruptedException {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);
		this.setVisible(true);
		this.setBackground(new Color(200, 200, 250));
		this.setFont(new Font("Arial", Font.BOLD, 20));
	
		panel = new JPanel();
		panel.setLayout(null);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
		waitLabel = new JLabel();
		
		waitLabel.setFont(new Font("Arial", Font.ROMAN_BASELINE, 25));
		panel.add(waitLabel);
		setLabel("Try to remember the gestures.");
		panel.validate();
		this.add(panel);
	}
	
	public void setUpGUI(BufferedWriter data, PrintStream out, int gestures) {
		waitLabel.setVisible(false);
		buttons = createButtons((double)screenSize.width/100, (double)screenSize.height/100, gestures);
		updateButtons(buttons, data, out);
		panel.revalidate();
	}
	
	public void thankYou() throws InterruptedException {
		for (JButton b: buttons)
			b.setVisible(false);
		setLabel("Thank you for participating.");
		waitLabel.setVisible(true);
	}
	
	/**
	 * Add buttonlistener to buttons.
	 */
	private void updateButtons(JButton[] buttons, BufferedWriter data, PrintStream out) {
		ButtonListener l = new ButtonListener(buttons, data, out);
		for (JButton b : buttons) {
			b.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
			b.addActionListener(l);
			b.setBackground(new Color(250, 250, 250));	
		}
		
		buttons[10].setBackground(new Color(250, 220, 200));
		buttons[11].setBackground(new Color(220, 255, 220));
	}
	
	/**
	 * Create the correct set of buttons.
	 * @param width
	 * @param height
	 * @param gesture set
	 * @return buttons
	 */
	private JButton[] createButtons(double w, double h, int gestures) {
		return gestures == 1 ? createButtonsA(w,h) : createButtonsB(w,h);
	}
	
	/**
	 * Hardcoded, if changing gestures, adjust this!
	 * @param width
	 * @param height
	 * @return buttons
	 */
	private JButton[] createButtonsA(double w, double h) {
		double bWidth = 16*w, bHeight = 20*h;
		
		JButton life 		= new JButton("LIFE");		panel.add(life);		life.setSize((int)bWidth, (int)bHeight);		life.setLocation((int)(6*w), (int)(13*h));
		JButton always 		= new JButton("ALWAYS");	panel.add(always);		always.setSize((int)bWidth, (int)bHeight);		always.setLocation((int)(24*w), (int)(13*h));	
		JButton evening 	= new JButton("EVENING");	panel.add(evening);		evening.setSize((int)bWidth, (int)bHeight);		evening.setLocation((int)(42*w), (int)(13*h));	
		JButton july		= new JButton("JULY");		panel.add(july);		july.setSize((int)bWidth, (int)bHeight);		july.setLocation((int)(60*w), (int)(13*h));	
		JButton februari 	= new JButton("FEBRUARI");	panel.add(februari);	februari.setSize((int)bWidth, (int)bHeight);	februari.setLocation((int)(78*w), (int)(13*h));	
		JButton autumn 		= new JButton("AUTUMN");	panel.add(autumn);		autumn.setSize((int)bWidth, (int)bHeight);		autumn.setLocation((int)(6*w), (int)(37*h));
		JButton october		= new JButton("OCTOBER");	panel.add(october);		october.setSize((int)bWidth, (int)bHeight);		october.setLocation((int)(24*w), (int)(37*h));
		JButton may 		= new JButton("MAY");		panel.add(may);			may.setSize((int)bWidth, (int)bHeight);			may.setLocation((int)(42*w), (int)(37*h));
		JButton fast 		= new JButton("FAST");		panel.add(fast);		fast.setSize((int)bWidth, (int)bHeight);		fast.setLocation((int)(60*w), (int)(37*h));
		JButton saturday	= new JButton("SATURDAY");	panel.add(saturday);	saturday.setSize((int)bWidth, (int)bHeight);	saturday.setLocation((int)(78*w), (int)(37*h));

		JButton idk = new JButton("PASS");				panel.add(idk);			idk.setSize((int)bWidth, (int)bHeight);			idk.setLocation((int)(60*w), (int)(67*h));
		JButton submit = new JButton("CONFIRM");		panel.add(submit);		submit.setSize((int)bWidth, (int)bHeight);		submit.setLocation((int)(78*w), (int)(67*h));
		
		return new JButton[]{life, always, evening, july, februari, autumn, october, may, fast, saturday, idk, submit};
	}
	
	/**
	 * Hardcoded, if changing gestures, adjust this!
	 * @param width
	 * @param height
	 * @return buttons
	 */
	private JButton[] createButtonsB(double w, double h) {
		double bWidth = 16*w, bHeight = 20*h;
		
		JButton april 		= new JButton("APRIL");		panel.add(april);		april.setSize((int)bWidth,(int)bHeight);		april.setLocation((int)(6*w), (int)(13*h));
		JButton december 	= new JButton("DECEMBER");	panel.add(december);	december.setSize((int)bWidth, (int)bHeight);	december.setLocation((int)(24*w), (int)(13*h));	
		JButton year 		= new JButton("YEAR");		panel.add(year);		year.setSize((int)bWidth, (int)bHeight);		year.setLocation((int)(42*w), (int)(13*h));	
		JButton begin		= new JButton("BEGIN");		panel.add(begin);		begin.setSize((int)bWidth, (int)bHeight);		begin.setLocation((int)(60*w), (int)(13*h));	
		JButton slow 		= new JButton("SLOW");		panel.add(slow);		slow.setSize((int)bWidth, (int)bHeight);		slow.setLocation((int)(78*w), (int)(13*h));	
		JButton march 		= new JButton("MARCH");		panel.add(march);		march.setSize((int)bWidth, (int)bHeight);		march.setLocation((int)(6*w), (int)(37*h));
		JButton monday	 	= new JButton("MONDAY");	panel.add(monday);		monday.setSize((int)bWidth, (int)bHeight);		monday.setLocation((int)(24*w), (int)(37*h));
		JButton september 	= new JButton("SEPTEMBER");	panel.add(september);	september.setSize((int)bWidth, (int)bHeight);	september.setLocation((int)(42*w), (int)(37*h));
		JButton earlier 	= new JButton("EARLIER");	panel.add(earlier);		earlier.setSize((int)bWidth, (int)bHeight);		earlier.setLocation((int)(60*w), (int)(37*h));
		JButton summer		= new JButton("SUMMER");	panel.add(summer);		summer.setSize((int)bWidth, (int)bHeight);		summer.setLocation((int)(78*w), (int)(37*h));

		JButton idk = new JButton("PASS");				panel.add(idk);			idk.setSize((int)bWidth, (int)bHeight);			idk.setLocation((int)(60*w), (int)(67*h));
		JButton submit = new JButton("CONFIRM");		panel.add(submit);		submit.setSize((int)bWidth, (int)bHeight);		submit.setLocation((int)(78*w), (int)(67*h));
		
		return new JButton[]{april, december, year, begin, slow, march, monday, september, earlier, summer, idk, submit};
	}
}
