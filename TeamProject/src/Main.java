import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Main extends JFrame implements ActionListener{
	Container panel = this.getContentPane();
	TextField id = new TextField();
	JButton login = new JButton("login");
	
	Main(){
		
 		login.setBounds(400,300, 80, 80);
 		this.setLayout(null);
		this.add(login);
		
		
		ActionListener event = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == login) {
				}
			}
		};
		
		btn1.addActionListener(event);
		
		this.setTitle("Á¶¼±ÄÄÆÈÀÌ");
		this.setSize(600, 600);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new Main();
	}
}