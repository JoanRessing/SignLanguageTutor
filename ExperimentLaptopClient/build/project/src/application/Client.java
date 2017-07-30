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
		Socket s = new Socket("192.168.1.122", 11268);
		in = new BufferedInputStream(s.getInputStream());
		out = new PrintWriter(s.getOutputStream(), true);
		
		// Create the view and add it to the Scene
		clips = getClipList();
		view = new MediaView();
		((Group)scene.getRoot()).getChildren().add(view);
		
		// Play video's
//		playNextVideo(0);
		
		clips[32]="learn";
		clips[31]="learn";
		playVideoTest(0);
	}
	
	private void playVideoTest(int index) {	
	    write(index, clips[index]);
	    
	    Media media = new Media(uri(clips[index]));
	    MediaPlayer player = new MediaPlayer(media);
	    
	    player.setOnEndOfMedia(() -> {
	        if (index < clips.length)
	        	if(index > 32 && index < 83)
					try {
						in.read();
					} catch (IOException e) { e.printStackTrace(); }

		    if(index==2)
		    	playVideoTest(30);
		    else if(index==33)
		    	playVideoTest(81);
		    else playVideoTest(index+1);
	    });
	    
	    player.setAutoPlay(true);
	    view.setMediaPlayer(player);
	}
	
	private void playNextVideo(int index) {		
	    write(index, clips[index]);
	    
	    Media media = new Media(uri(clips[index]));
	    MediaPlayer player = new MediaPlayer(media);
	    
	    player.setOnEndOfMedia(() -> {
	        if (index < clips.length)
	        	if(index > 32 && index < 83)
					try {
						in.read();
					} catch (IOException e) { e.printStackTrace(); }
	            playNextVideo(index+1);
	    });
	    
	    player.setAutoPlay(true);
	    view.setMediaPlayer(player);
	}
	
	private void write(int index, String gesture) {
		if(index == 32)					{ out.write("testphasestart");	out.flush(); }
		if(index > 32 && index < 83)	{ out.write(gesture);			out.flush(); }
		if(index == 83)					{ out.write("testphaseover");	out.flush(); }
	}

	private ImageView setBackground() {
		Image image = new Image("background.png");
        ImageView iv = new ImageView();
        iv.setImage(image);
        return iv;
	}

	private String[] getClipList() throws IOException {
		String[] clipList = new String[84];
		clipList[0] = "learn";
		int index = 1;
		
		String[] gestures = getGestures();
		
		for(int i=0; i<3; i++) {
			Collections.shuffle(Arrays.asList(gestures));
			for(String s: gestures) {
				clipList[index] = s + "_l";
				index++;
			}
		}
		
		clipList[index] = "rest";	index++;
		clipList[index] = "test1";	index++;
		
		for(int i=0; i<5; i++) {
			Collections.shuffle(Arrays.asList(gestures));
			for(String s: gestures) {
				clipList[index] = s + "_t";
				index++;
			}
		}
		
		clipList[index] = "test2";
		return clipList;
	}

	private static String uri(String name) {
		return Paths.get("clips/" + name + ".mp4").toUri().toString();
	}
	
	private static String[] getGestures() throws IOException {
		String group = String.valueOf(in.read());
		
		if(group.equals("1"))
			return new String[]{"always","evening","begin","februari","autumn","life","monday","may","fast","saturday"};
		else
			return new String[]{"april","december","year","july","slow","march","october","september","earlier","summer"};
	}

	public static void main(String[] args) {
		launch(args);
	}
}
