import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener.*;
import java.awt.event.ActionListener.*;


interface variable{
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	
}
public class Main extends JFrame implements ActionListener, KeyListener, variable{
	
	JButton login = new JButton("login");
	Container panel = this.getContentPane();
	CPU Pcpu = new CPU();
	mainboard Pmboard = new mainboard();
	
	
	
	private Frame fs = new Frame("조선컴팔이");
	private Frame fs1 = new Frame("Error!");
	private String id1 = "asdf";
	private String pw1 = "asdf";
	
	Main(){
		Label namelabel = new Label("ID :");
		Label passwordLabel = new Label("PW :");
		TextField ID = new TextField(6);
		TextField PW = new TextField(6);
		PW.setEchoChar('*');

		namelabel.setBounds(110, 270, 50, 40);
		passwordLabel.setBounds(110, 318, 50, 40);
		ID.setBounds(170, 270, 210, 40);
		ID.setFont(new Font("굴림",Font.PLAIN,40));
		PW.setBounds(170, 318, 210, 40);
		PW.setFont(new Font("굴림",Font.PLAIN,40));

		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon("src\\image\\big.png"));
		background.setBounds(0,0,600,553);

 		login.setBounds(390,265, 100, 100);
 		login.setFont(new Font("굴림체",Font.BOLD,20));
 		this.setLayout(null);

		ActionListener event = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};

	/*******************로그인 화면 이벤트**********************/
		KeyListener key_li = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_CONTROL)
					PW.requestFocus();
				else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String test_id = ID.getText();
					String test_pw = PW.getText();					
					if(test_id.equals(id1) && test_pw.equals(pw1)) {
						fs.setVisible(true);
						fs.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								fs.setVisible(false);
								fs.dispose();
							}
						});
						
				/***************서브프레임 패널*******************/
						fs.setLayout(new GridLayout(0,2));
						p1.setLayout(null);
						
						p1.add(Pcpu);
						//p2.add();
						p1.setBackground(Color.LIGHT_GRAY);
						p2.setBackground(Color.white);
						
						fs.add(p1);
						fs.add(p2);
						
						
						fs.setVisible(true);
						/*fs.add(Pcpu.fs_panel);
						Pcpu.fs_panel.setLayout(null);				
						Pcpu.fs_panel.setBackground(Color.LIGHT_GRAY);*/
						fs.setSize(1300, 980);
					}

				/***************아이디,비밀번호 다르면 에러창을 띄움***************/
					else if(ID.getText() != id1 && PW.getText() != pw1) {
						Label error = new Label("아이디 또는 비밀번호가 틀렸습니다.");
						Container error_panel = new Container();
						fs1.setVisible(true);
						fs1.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								fs1.setVisible(false);
								fs1.dispose();
							}
							public void keyReleased(KeyEvent e) {
								// TODO Auto-generated method stub
							}
							public void keyTyped(KeyEvent e) {
								// TODO Auto-generated method stub
							}
						});

						fs1.addKeyListener(new KeyAdapter() {
							@Override
							public void keyPressed(KeyEvent a) {
								if(a.getKeyCode() == 27) {
									fs1.setVisible(false);
									fs1.dispose();
									ID.requestFocus();
								}
							}
						});
						
						ID.setText("");
						PW.setText("");
						error.setFont(new Font("굴림",Font.PLAIN,30));
						fs1.add(error);
						fs1.setSize(520, 300);
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		};

		ID.addKeyListener(key_li);
		PW.addKeyListener(key_li);
		login.addActionListener(event);

		this.add(namelabel);
		this.add(ID);
		this.add(passwordLabel);
		this.add(PW);
		this.add(login);
		this.add(background);

		this.setTitle("조선컴팔이");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

class CPU extends JPanel implements variable{
	private String[] columntype = {"제품명", "수량", "가격"};
	private Object[][] data = {{"CPU","1","350000"}}; 
	private JTable product_table;
	//public Container fs_panel = new JPanel();
	
	
	public CPU() {
		JButton[] p = new JButton[10];
		int j = 0;

		p[0] = new JButton("i3"); p[5] = new JButton("i5");
		p[1] = new JButton("i3"); p[6] = new JButton("i7");
		p[2] = new JButton("i3"); p[7] = new JButton("i7");
		p[3] = new JButton("i5"); p[8] = new JButton("i7");
		p[4] = new JButton("i5"); p[9] = new JButton("i9");

		for(int i = 0; i < 10; i++) {
			if(i >= 4 && i <=7) {
				p[i].setBounds((100 + (120*j)), 300, 100, 100);
				j++;
				p1.add(p[i]);
			}
			else if(i >= 8 && i <= 10) {
				if(j == 4)
					j=0;
				p[i].setBounds((100 + (120*j)), 500, 100, 100);
				j++;
				p1.add(p[i]);
			}
			else {
				p[i].setBounds((100 + (120*i)), 100, 100, 100);
				p1.add(p[i]);
			}
		}
		
		/*product_table = new JTable(data,columntype);
		JScrollPane js = new JScrollPane(product_table);
		product_table.setFont(new Font("굴림", Font.PLAIN, 30));
		product_table.setRowHeight(30);
		product_table.setBounds(750, 100, 400, 700);
		add(product_table);*/
	}
}

class mainboard extends JPanel{
	
	
	mainboard(){
		JButton[] p = new JButton[10];
		int j = 0;

		p[0] = new JButton("i3"); p[5] = new JButton("i5");
		p[1] = new JButton("i3"); p[6] = new JButton("i7");
		p[2] = new JButton("i3"); p[7] = new JButton("i7");
		p[3] = new JButton("i5"); p[8] = new JButton("i7");
		p[4] = new JButton("i5"); p[9] = new JButton("i9");

		for(int i = 0; i < 10; i++) {
			if(i >= 4 && i <=7) {
				p[i].setBounds((100 + (120*j)), 300, 100, 100);
				j++;
				add(p[i]);
			}
			else if(i >= 8 && i <= 10) {
				if(j == 4)
					j=0;
				p[i].setBounds((100 + (120*j)), 500, 100, 100);
				j++;
				add(p[i]);
			}
			else {
				p[i].setBounds((100 + (120*i)), 100, 100, 100);
				add(p[i]);
			}
		}
	}
}