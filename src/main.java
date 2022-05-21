import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class main extends JPanel implements KeyListener, ActionListener, Runnable {
	private static final long serialVersionUID = 1L;

	private static final int WINDOWS_WIDTH = 700;
	private static final int WINDOWS_HEIGHT = 500;
	private static final int CONTROL_LAYOUT_HEIGHT = 1;
	private static final int BALL_SIZE = 15;

	private static final Dimension WINDOW_DIMINSIONS = new Dimension(WINDOWS_WIDTH, WINDOWS_HEIGHT);
	private static final Dimension CANVAS_DIMINSIONS = new Dimension(WINDOWS_WIDTH,
			WINDOW_DIMINSIONS.height - CONTROL_LAYOUT_HEIGHT);
	private static final Rectangle CONTROL_LAYOUT_DIMINSIONS = new Rectangle(0, CANVAS_DIMINSIONS.height, WINDOWS_WIDTH,
			CONTROL_LAYOUT_HEIGHT);
	private static boolean right = false;
	private static boolean left = false;
	private static final int BRICK_BREADTH = 300;
	private static final int BRICK_HEIGHT = 10;
	private static final int BAT_HEIGHT = 5;
	private static final int BAT_WIDTH = 40;

	private static boolean PAUSE = false;
	private static boolean RUNNING = false;

	private static int movex = -1;
	private static int movey = -1;
	private static final int BAT_SPEED = 3;
   
	public static ArrayList<main> BallList = new ArrayList<main>();
	
	private boolean ballFallDown = false;
	private boolean bricksOver = false;
	private static int count = 0;
	private static int fal = 0;
	private Rectangle Ball;
	private Rectangle Balls;
	private Rectangle Bat;
	//private Rectangle[] Brick;
	private Rectangle Brick;
	private Rectangle Brick2;
	private Rectangle gameScreen;
	private String status;
	private JButton button;
	JButton btn = new JButton("SAVE GAME");
	JButton btn2 = new JButton("Load GAME");
	JPanel btnPanel = new JPanel();
	JLabel Label = new JLabel("Score: " + count +"/" + fal);
	public static String file = "C:\\\\Users\\\\Jonathan\\\\eclipse-workspace\\\\bricks\\\\src\\\\bricks\\\\progress.txt";
	private static enum STATUS {
		START, PAUSE, RESUME, STOP
	}

	main() {
		startGame();
		
		
		initializeVariables();
		JFrame frame = new JFrame("Tennis Game Project");
		btnPanel.add(btn);
		btnPanel.add(btn2);
		btnPanel.add(Label);
		
		//btn.setBounds(10, 10, 150, 30);
		frame.add(btnPanel, BorderLayout.SOUTH);
		//add(Label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(WINDOW_DIMINSIONS);
		frame.getContentPane().add(this);
		frame.pack();

		
		frame.setVisible(true);
		btn2.addActionListener(this);
		btn.addActionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		frame.invalidate();
	}

	public static void main(String[] args) {
		new main();

	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(gameScreen.x, gameScreen.y, gameScreen.width, gameScreen.height);
		//ball
		g.setColor(Color.blue);
		g.fillOval(Ball.x, Ball.y, Ball.width, Ball.height);
		
		//bat
		g.setColor(Color.green);
		g.fill3DRect(Bat.x, Bat.y, Bat.width, Bat.height, true);
		// this will paint below the peddle
		g.setColor(Color.GRAY);
		g.fillRect(CONTROL_LAYOUT_DIMINSIONS.x, CONTROL_LAYOUT_DIMINSIONS.y, CONTROL_LAYOUT_DIMINSIONS.width,
				CONTROL_LAYOUT_DIMINSIONS.height);
		// this will draw border line
		g.setColor(Color.RED);
		g.drawRect(gameScreen.x, gameScreen.y, gameScreen.width - 1, gameScreen.height);

		
		if (Brick != null) {
			g.fill3DRect(Brick.x, Brick.y, Brick.width, Brick.height, true);
			
		}
		
		if (Brick2 != null) {
			g.fill3DRect(Brick2.x, Brick2.y, Brick2.width, Brick2.height, true);
			
		}
		//repaint()

	}

	// Game Loop
	public void run() {
		while (RUNNING) {
			if (PAUSE) {
				sleep();
				continue;
			}
				if (Brick != null) {
					if (Brick.intersects(Ball) || Brick2.intersects(Ball)) {
						movey = -movey;
					} // end of 2nd if..
				// end of 1st if..
			} // end of for loop..
				
				

//			
			repaint();
			Ball.x += movex;
			Ball.y += movey;

			if (left == true && Bat.x >= gameScreen.x) {
				Bat.x -= BAT_SPEED;
				right = false;
			}
			if (right == true && Bat.x <= gameScreen.width - Bat.width) {
				Bat.x += BAT_SPEED;
				left = false;
			}
			// /===== Ball reverses when strikes the bat
			if (Ball.intersects(Bat)) {
				movey = -movey;
				count++;
				System.out.println("you catch the ball: " + count);
				Label.setText("Score: " +count + "/" + fal);
				System.out.println(Label.getText());
			}
			// ball reverses when touches left and right boundary
			if (Ball.x <= gameScreen.x || gameScreen.width - Ball.width <= Ball.x) {
				movex = -movex;
			} // if ends here
			if (Ball.y <= gameScreen.y) {
				movey = -movey;
			} // if ends here.....
			if (Ball.y+10 >= gameScreen.height) {// when ball falls below bat game is over...
				movey = -movey;
				ballFallDown = true;
				fal++;
				
				System.out.println("you fall: " + fal);
				Label.setText("Score: " + count + "/" + fal);
				System.out.println(Label.getText());
				status = "YOU LOST THE GAME";
				repaint();
				//button.setText(STATUS.START.toString());
				//break;
			}

			sleep();
		} // while loop ends here

	}

	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (Exception ex) {
		} // try catch ends here
	}

	// HANDLING KEY EVENTS
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = true;
			// System.out.print("left");
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = true;
			// System.out.print("right");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = false;
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		//this.startGame();
		String file = "C:\\\\Users\\\\Jonathan\\\\eclipse-workspace\\\\bricks\\\\src\\\\bricks\\\\progress.txt";
		//int content = count;
		
		String s=Integer.toString(count);
		String s1=Integer.toString(fal);
		
		String xP=Integer.toString(Ball.x);
		String yP=Integer.toString(Ball.y);
		
		
		BufferedWriter bw = null;
		if(e.getSource() == btn)
		{
			
			 try{
			      boolean isFile = false;
			     
			       
			     FileWriter outFile = new FileWriter(file);
                 PrintWriter out = new PrintWriter(outFile,true);
                 out.println(xP);
                 out.println(yP);
                 out.println(s);
                 out.println(s1);
                 out.close();
               }
               catch(IOException e1)
               {
               	
               }
			
			System.out.println("X " + Ball.x + " Y " + Ball.y);
			
		}
		
		
		if(e.getSource() == btn2)
		{
			try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

        		xP = br.readLine();
        		yP = br.readLine();
        		s = br.readLine();
        		s1 = br.readLine();
        		br.close();
            }
        	 catch(IOException e1)
            {
            	
            }
			Label.setText("Score: " + s + "/" + s1);
			Ball.x = Integer.parseInt(xP);
			Ball.y = Integer.parseInt(yP);
			
			System.out.println(s + s1 + xP + yP);
		}
		
//		if (str.equals(STATUS.START.toString())) {
//			button.setText(STATUS.PAUSE.toString());
//			this.startGame();
//		}
		if (str.equals(STATUS.PAUSE.toString())) {
			PAUSE = true;
			button.setText(STATUS.RESUME.toString());
		}
		if (str.equals(STATUS.RESUME.toString())) {
			PAUSE = false;
			button.setText(STATUS.PAUSE.toString());
		}

//		if (str.equals(STATUS.STOP.toString())) {
//			RUNNING = false;
//			button.setText(STATUS.START.toString());
//		}

	}

	public void startGame() {
		requestFocusInWindow(true);
		initializeVariables();
		Thread t = new Thread(this);
		t.start();
	}

	public void initializeVariables() {
		// x = 0, y = 0, width = 350, height = 450.
		gameScreen = new Rectangle(CANVAS_DIMINSIONS);
		RUNNING = true;
		// x = 160, y = 245, width = 40, height = 5
//		Bat = new Rectangle(160, 245, 40, 5);
		Point batCoordinates = getPosition(gameScreen.x, gameScreen.width, gameScreen.height, BAT_WIDTH, BAT_HEIGHT);

		Bat = new Rectangle(batCoordinates.x, batCoordinates.y, BAT_WIDTH + 10, BAT_HEIGHT);
		// initial ball position.
		// x = 160, y = 218, width = 5, height = 5
		Point ballCoordinates = getPosition(Bat.x, Bat.x + Bat.width, Bat.y, BALL_SIZE, BALL_SIZE);
		Ball = new Rectangle(ballCoordinates.x, ballCoordinates.y, BALL_SIZE, BALL_SIZE);

		Brick = new Rectangle();
		createBricks(170, 80);
		Brick2 = new Rectangle();
		// Creating bricks for the game, with size width = 70, height = 50
		createBrick2(170, 200);
		// BRICKS created for the game new ready to use
		movex = -1;
		movey = -1;
		ballFallDown = false;
		bricksOver = false;
		count = 0;
		status = null;

	}

	private Point getPosition(int startPosition, int endPosition, int yAxis, int width, int height) {
		Point point = null;
		if (startPosition < endPosition) {
			int totalWidthMedian = (endPosition + startPosition) / 2;
			int objectWidthMedian = width / 2;
			point = new Point(totalWidthMedian - objectWidthMedian, yAxis - height);
		}
		return point;
	}

	/**
	 * Creating bricks for the game:- creating bricks again because this for loop is
	 * out of while loop in run method
	 */
	public void createBricks(int brickx, int bricky) {
		
			Brick = new Rectangle(brickx, bricky, BRICK_BREADTH, BRICK_HEIGHT);
	}
	
	public void createBrick2(int brickx, int bricky) {
		
		Brick2 = new Rectangle(brickx, bricky, BRICK_BREADTH, BRICK_HEIGHT);
	

	}
}

