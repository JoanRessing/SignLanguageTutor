import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;

public class ButtonListener implements ActionListener {
	private JButton[] buttons;
	private BufferedWriter data;
	private PrintStream out;
	private String selected = "";
	
	public ButtonListener(JButton[] buttons, BufferedWriter data, PrintStream out) {
		this.buttons = buttons;
		this.data = data;
		this.out = out;
		this.selected = "";
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		resetButtons(buttons);
		switch(arg0.getActionCommand()) {
			case "CONFIRM":				if(!selected.equals("")) {
											try {
												out.write('1');
												data.write(selected.toLowerCase() + "\n");
												data.flush();
											} catch (IOException e) { e.printStackTrace(); }
											selected = "";
										}
										break;
			case "PASS":				((JButton)arg0.getSource()).setBackground(new Color(229,91,91));
										buttons[11].setBackground(Color.green);
										selected = "WEET IK NIET";
										break;
			default:					((JButton)arg0.getSource()).setBackground(Color.lightGray);
										buttons[11].setBackground(Color.green);
										selected = ((JButton)arg0.getSource()).getText();
										break;
		}
	}
	
	private void resetButtons(JButton[] buttons) {
		for(int i=0; i<10; i++)
			buttons[i].setBackground(new Color(250, 250, 250));
		buttons[10].setBackground(new Color(250, 220, 200));
		buttons[11].setBackground(new Color(220, 255, 220));
	}
}
