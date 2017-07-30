package application;
	
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * Plays videos in video tutoring setting. Communicates with laptop running the server program.
 * @author Joan Ressing
 */
public class Client extends Application {
	static MediaView view;
	static String[] clips;
	static BufferedInputStream in;
	static PrintWriter out;
	
	@Override
	public void start(Stage stage) throws IOException, URISyntaxException, InterruptedException {
		// Create full screen scene
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		stage.setFullScreen(true);
		Scene scene = new Scene(new Group(), dim.getWidth(), dim.getHeight());
		stage.setScene(scene);
		((Group)scene.getRoot()).getChildren().add(setBackground());
		stage.show();
		
		// Set up connection
		Socket s = new Socket("192.168.1.148", 12576);			//<-- adjust IP, don't close s unless you know what you're doing.
		in = new BufferedInputStream(s.getInputStream());
		out = new PrintWriter(s.getOutputStream(), true);
		
		// Create the view and add it to the Scene
		clips = getClipList();
		view = new MediaView();
		((Group)scene.getRoot()).getChildren().add(view);
		
		// Play video's
		playNextVideo(0);
	}
	
	/**
	 * Plays all videos in the array. Very hardcoded, adjust where needed.
	 * @param index: current video
	 */
	private void playNextVideo(int index) {		
	    write(index, clips[index]);
	    
	    Media media = new Media(uri(clips[index]));
	    MediaPlayer player = new MediaPlayer(media);
	    
	    //On end of video play the next video.
	    player.setOnEndOfMedia(() -> {
	        if (index < clips.length) {
	        	if(index > 12 && index < 43)
					try {
						in.read();
					} catch (IOException e) { e.printStackTrace(); }
	            
	        	if(index < 43)
	        		playNextVideo(index+1);
	        }
	    });
	    
	    player.setAutoPlay(true);
	    view.setMediaPlayer(player);
	}
	
	/**
	 * Sends command to server program.
	 * @param index:	current video
	 * @param gesture:	last shown gesture
	 */
	private void write(int index, String gesture) {
		if(index > 0 && index < 11)		{ out.write(gesture.substring(0, gesture.length()-2));	out.flush(); }
		if(index == 12)					{ out.write("testphasestart");							out.flush(); }
		if(index > 12 && index < 43)	{ out.write(gesture.substring(0, gesture.length()-2));	out.flush(); }
		if(index == 43)					{ out.write("testphaseover");							out.flush(); }
	}

	private ImageView setBackground() {
		Image image = new Image("background.png");
        ImageView iv = new ImageView();
        iv.setImage(image);
        return iv;
	}

	/**
	 * Creates list of videos, very hardcoded, adjust where needed.
	 * @return list of videos
	 */
	private String[] getClipList() throws IOException {
		String[] clipList = new String[44];
		clipList[0] = "learn";
		int index = 1;
		
		String[] gestures = getGestures();
		
		for(int i=0; i<1; i++) {
			customShuffle(gestures, "");
			for(String s: gestures) {
				clipList[index] = s + "_l";
				index++;
			}
		}
		clipList[index] = "rest";	index++;
		clipList[index] = "test1";	index++;
		
		for(int i=0; i<3; i++) {
			customShuffle(gestures, gestures[9]);
			for(String s: gestures) {
				clipList[index] = s + "_t";
				index++;
			}
		}
		clipList[index] = "test2";
		return clipList;
	}
	
	/**
	 * Pseudorandom shuffle function. Does not allow for back-to-back gestures.
	 * Also prevents similar gestures to be shown after another. Adjust where needed.
	 * @param names:	names of gestures
	 * @param last:		last gesture of previous series
	 */
	private void customShuffle(String[] names, String last) {
		Collections.shuffle(Arrays.asList(names));
		
		while(names[0].equals(last) || badOrder(names)) 
			Collections.shuffle(Arrays.asList(names));
	}

	/**
	 * Checks whether the order is satisfies all constraints. Adjust if needed.
	 * @param names:	names of gestures
	 * @return true if constraints are not satisfied. false if ordering is ok.
	 */
	private boolean badOrder(String[] names) {
		for(int i=0; i < 10; i++) {
			if(names[i].equals("always") || names[i].equals("monday")) {
				if(i == 0)
					return names[1].equals("may") || names[1].equals("september");
				if(i == 9)
					return names[8].equals("may") || names[8].equals("september");
				return names[i-1].equals("may") || names[i-1].equals("september") || names[i+1].equals("may") || names[i+1].equals("september");
			}
		}
		return false;
	}

	/**
	 * Converts filename to URI.
	 * @param name of video
	 * @return URI
	 */
	private static String uri(String name) {
		return Paths.get("clips/" + name + ".mp4").toUri().toString();
	}
	
	private static String[] getGestures() throws IOException {
		int group = in.read();
		
		if(group == 1)
			return new String[]{"always","autumn","evening","fast","februari","july","life","may","october","saturday"};
		else
			return new String[]{"april","begin","december","earlier","march","monday","september","slow","summer","year"};
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
