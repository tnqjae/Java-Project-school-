import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

interface Variable {// 변수

	TextField ID = new TextField(6);
	TextField PW = new TextField(6);
/********************탭 패털 변수*********************************/
	String[] header = { "상품명", "수량", "현금가", "카드" };
	String[][] data = {};

	DefaultTableModel model = new DefaultTableModel(data, header) {//테이블 데이터 수정 못하게 함
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	JTable product = new JTable(model);
	JScrollPane sc = new JScrollPane(product);
	Product_table productT = new Product_table();
}

public class Main extends JFrame implements KeyListener, Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton login = new JButton("login");
	Container panel = this.getContentPane();
	Label namelabel = new Label("ID :");
	Label passwordLabel = new Label("PW :");
	JLabel background = new JLabel("");

	public JTabbedPane PaneTab;// 탭

	public String id1 = "asdf";
	public String pw1 = "asdf";

	Main() {
		/*********************로그인************************/
		PW.setEchoChar('*');

		namelabel.setBounds(110, 270, 50, 40);
		passwordLabel.setBounds(110, 318, 50, 40);
		ID.setBounds(170, 270, 210, 40);
		PW.setBounds(170, 318, 210, 40);
		login.setBounds(390, 265, 100, 100);

		ID.setFont(new Font("굴림", Font.PLAIN, 40));
		PW.setFont(new Font("굴림", Font.PLAIN, 40));
		login.setFont(new Font("굴림", Font.BOLD, 20));

		background.setIcon(new ImageIcon("src\\image\\big.png"));
		background.setBounds(0, 0, 600, 553);

		this.setLayout(null);
		/******************* 로그인 엔터 키 이벤트 **********************/

		KeyListener key_li = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) // 컨트롤 누르면 pw로 커서 이동
					PW.requestFocus();
				else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String test_id = ID.getText();
					String test_pw = PW.getText();
					if (test_id.equals(id1) && test_pw.equals(pw1)) {
						@SuppressWarnings("unused")
						SubMainpane pane = new SubMainpane();
						dispose();
					}

					else if (ID.getText() != id1 && PW.getText() != pw1) {
						JOptionPane.showMessageDialog(null, "ID 또는 PW 가 틀렸습니다.", "에러", JOptionPane.ERROR_MESSAGE);
						ID.setText("");
						PW.setText("");
						ID.requestFocus();
					}
				}
			}

			/**************** 사용하지 않음 ****************/
			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		};
		/*********************************/
		ID.addKeyListener(key_li);
		PW.addKeyListener(key_li);
		login.addActionListener(this);

		this.add(namelabel);
		this.add(ID);
		this.add(passwordLabel);
		this.add(PW);
		this.add(login);
		this.add(background);

		this.setTitle("조선컴팔이 로그인");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		this.setVisible(true);
	}

	/********************** 메인 *********************/

	public static void main(String[] args) throws IOException {
		new Main();

		// 아래는 ASUS RTX 2080Ti를 찾는 예
		Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")// 다나와 제품 리스트 에이젝스를 이용
				.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
				.data("query", "ASUS RTX 2080Ti").post();
		// 포스트 방식을 파싱하기 때문에 헤더를 만들어야 함 필요한 헤더는 Origin Referer 쿼리에는 찾을 물품의 이름을 입력
		Element proc = doc.select("p.price_sect").select("a").first();
		// html에서 태그가 <p>이고 클래스가 .price_sect 내부의 태그가 <a>인것중 첫번째를 가져옴
		String procloc = proc.attr("href").replaceAll(" ", "+"); // 태그 속성이 href 인것의 내용물을 파싱해서 공백은 +로 바꿈
		// procloc에는 결과적으로 쿼리에 넣은 물품 이름을 검색해서 나온 물품 사이트의 주소가 들어감
		Document doc1 = Jsoup.connect(procloc).get(); // get 방식으로 procloc 사이트를 파싱
		Elements lowprice = doc1.select("span.lwst_prc").select("em");
		// 태그가 <span>이고 클래스가 lwst_prc 그리고 내부 태그가 em인것을 파싱
		String lowpricevalue = lowprice.select("em").html();
		// 태그가 <em>인것의 내용물을 파싱 예 : <em>내용물</em>
		Scanner scan = new Scanner(lowpricevalue);
		int i = 0;
		String[] lowrealval = new String[] { null, null, null, null, null };
		while (scan.hasNext()) {
			lowrealval[i] = scan.nextLine();
			i++;
		} // 파싱한 값들을 배열에 넣는 과정
		scan.close();
		if (lowrealval[0] == null)
			lowrealval[0] = "(없음)";
		else if (lowrealval[1] == null)
			lowrealval[1] = "(없음)";
		// 만약 파싱한 값이 없으면 없음으로 표시
	}

	/****************** 사용하지 않음 ******************/
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/********************** 로그인 버튼으로 작동하는 이벤트 *********************************/
	public void actionPerformed(ActionEvent e) {
		String test_id = ID.getText();
		String test_pw = PW.getText();
		if (test_id.equals(id1) && test_pw.equals(pw1)) {
			@SuppressWarnings("unused")
			SubMainpane pane = new SubMainpane();
			dispose();
		}

		else if (ID.getText() != id1 && PW.getText() != pw1) {
			JOptionPane.showMessageDialog(null, "ID 또는 PW 가 틀렸습니다.", "에러", JOptionPane.ERROR_MESSAGE);
			ID.setText("");
			PW.setText("");
			ID.requestFocus();
		}
	}
}

/***************** 여기서부터는 탭에 들어갈 패널 *******************/
class CPU extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[10];
	
	public CPU() {
		setLayout(null);

		int j = 0;

		//@SuppressWarnings("unused")

		p[0] = new JButton("AMD 2700X");
		p[1] = new JButton("AMD 2600X");
		p[2] = new JButton("AMD 2990WX");
		p[3] = new JButton("AMD 2600");
		p[4] = new JButton("Intel i9-9900K");
		p[5] = new JButton("Intel i7-9700K");
		p[6] = new JButton("Intel i7-8700K");
		p[7] = new JButton("Intel i5-8500");
		p[8] = new JButton("Intel i5-8400");
		p[9] = new JButton("Intel i3-8100");

		for (int i = 0; i < 10; i++) {
			if (i >= 4 && i <= 7) {
				p[i].setBounds((30 + (135 * j)), 200, 120, 100);
				j++;
				add(p[i]);
			} else if (i >= 8 && i <= 10) {
				if (j == 4)
					j = 0;
				p[i].setBounds((30 + (135 * j)), 350 , 120, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((30 + (135 * i)), 50, 120, 100);
				add(p[i]);
			}
		}

		for (int i = 0; i < 10; ++i)
			p[i].addActionListener(this);
	}

	/******** 클릭 이벤트 **********/
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {// 위에 다이얼로그에서 YES를 눌렀을때 ans 0을 반환함 PLAIN은 1
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6066419").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6066419"));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6385810").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6385810"));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6066396").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6066396"));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6515580").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6515580"));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6515603").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6515603"));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5529981").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5529981"));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[7])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6020667").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6020667"));
					productT.Rowadd(p[7].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[8])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5530356").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5530356"));
					productT.Rowadd(p[8].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		if (e.getSource().equals(p[9])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5530456").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5530456"));
					productT.Rowadd(p[9].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

	}
}

class Mainboard extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[7];
	
	Mainboard() {
		setLayout(null);

		int j = 0;

		p[0] = new JButton("ASUS ROG MAXIMUS XI HERO Z470");// 6511874
		p[1] = new JButton("ASUS ROG STRIX Z370-F\nGAMING");// 5539405
		p[2] = new JButton("ASUS PRIME B360M-A"); // 6023368
		p[3] = new JButton("ASUS PRIME H310M-K");// 6024610
		p[4] = new JButton("ASUS ROG ZENITH EXTREME X399");// 5862931
		p[5] = new JButton("ASUS PRIME B450M-A"); // 6352352
		p[6] = new JButton("ASUS PRIME A320M-K");// 5122402

		for (int i = 0; i < 7; i++) {
			if (i >= 3 && i <= 5) {
				p[i].setBounds((40 + (190 * j)), 200, 170, 100);
				j++;
				add(p[i]);
			} else if (i >= 6 && i <= 7) {
				if (j == 3)
					j = 0;
				p[i].setBounds((40 + (190 * j)), 350, 170, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((40 + (190 * i)), 50, 170, 100);
				add(p[i]);
			}
		}
		
		for (int i = 0; i < 7; ++i)
			p[i].addActionListener(this);
	}
	/******** 클릭 이벤트 **********/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6511874").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6511874"));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5539405").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5539405"));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6023368").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6023368"));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6024610").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6024610"));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5862931").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5862931"));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=6352352").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=6352352"));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://prod.danawa.com/info/?pcode=5122402").get();
				Elements lowprice = doc.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI("http://prod.danawa.com/info/?pcode=5122402"));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}

	}
}

class Ram extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[3];
	
	public Ram() {
		setLayout(null);
		p[0] = new JButton("삼성전자 DDR4 8G PC4-21300"); // 5937666
		p[1] = new JButton("삼성전자 DDR4 16G PC4-21300");// 5941995
		p[2] = new JButton("삼성전자 DDR4 4G PC4-19200"); // 5040061
		for (int i = 0; i < 3; i++) {
			p[i].setBounds((60 + (120 * i)), 100, 100, 100);
			add(p[i]);
		}
		for (int i = 0; i < 3; ++i)
			p[i].addActionListener(this);
	}

	/******** 클릭 이벤트 **********/
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[1].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[2].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

	}
}

class GraphicCard extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[10];

	GraphicCard() {
		setLayout(null);

		int j = 0;

		p[0] = new JButton("ASUS RTX 2080Ti");
		p[1] = new JButton("ASUS RTX 2080");
		p[2] = new JButton("ASUS RTX 2070");
		p[3] = new JButton("ASUS GTX 1080Ti");
		p[4] = new JButton("ASUS GTX 1080");
		p[5] = new JButton("ASUS GTX 1070Ti");
		p[6] = new JButton("ASUS GTX 1070");
		p[7] = new JButton("GTX 1060 6GB");
		p[8] = new JButton("GTX 1060 3GB");
		p[9] = new JButton("GTX 1050Ti");

		for (int i = 0; i < 10; i++) {
			if (i >= 4 && i <= 7) {
				p[i].setBounds((60 + (120 * j)), 300, 100, 100);
				j++;
				add(p[i]);
			} else if (i >= 8 && i <= 10) {
				if (j == 4)
					j = 0;
				p[i].setBounds((60 + (120 * j)), 500, 100, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((60 + (120 * i)), 100, 100, 100);
				add(p[i]);
			}
		}
		
		for (int i = 0; i < 10; ++i)
			p[i].addActionListener(this);
	}
	/******** 클릭 이벤트 **********/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[1].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[2].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[3].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[4].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[5].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[6].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[7])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[7].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[7].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[8])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[8].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[8].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[9])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[9].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[9].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

	}
}

class Powersupply extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[10];

	Powersupply() {
		setLayout(null);

		int j = 0;

		p[0] = new JButton("AX1500i 80PLUS TITANIUM");
		p[1] = new JButton("AX1200i PLATINUM");
		p[2] = new JButton("EVGA SUPERNOVA 1000G+ 80PLUS");
		p[3] = new JButton("시소닉 SSR-850FX Full Modular");
		p[4] = new JButton("EVGA 750 GQ 80PLUS GOLD");
		p[5] = new JButton("마이크로닉스 Classic II 700W");
		p[6] = new JButton("마이크로닉스 Classic II 600W");
		p[7] = new JButton("마이크로닉스 Classic II 500W ");

		for (int i = 0; i < 8; i++) {
			if (i >= 3 && i <= 5) {
				p[i].setBounds((30 + (190 * j)), 300, 170, 100);
				j++;
				add(p[i]);
			} else if (i >= 6 && i <= 7) {
				if (j == 3)
					j = 0;
				p[i].setBounds((30 + (190 * j)), 500, 170, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((30 + (190 * i)), 100, 170, 100);
				add(p[i]);
			}
		}
		
		for (int i = 0; i < 8; ++i)
			p[i].addActionListener(this);
	}
	/******** 클릭 이벤트 **********/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[1].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[2].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[3].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[4].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[5].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[6].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[7])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[7].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[7].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

	}
}

class HddSsd extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[10];

	HddSsd() {
		setLayout(null);

		int j = 0;

		p[0] = new JButton("WD 1TB BLUE WD10EZEX");
		p[1] = new JButton("Seagate 4TB BarraCuda ST4000DM004");
		p[2] = new JButton("삼성전자 860 EVO (250GB)");
		p[3] = new JButton("삼성전자 860 EVO (500GB)");
		p[4] = new JButton("삼성전자 970 EVO M.2 2280 (250GB)");
		p[5] = new JButton("삼성전자 860 EVO (2TB)");
		p[6] = new JButton("삼성전자 970 PRO M.2 2280 (512GB)");
		p[7] = new JButton("삼성전자 970 PRO M.2 2280 (1TB)");
		p[8] = new JButton("삼성전자 860 EVO (4TB)");
		p[9] = new JButton("KingSpec P3 Series (128GB)");

		for (int i = 0; i < 10; i++) {
			if (i >= 4 && i <= 7) {
				p[i].setBounds((60 + (120 * j)), 300, 100, 100);
				j++;
				add(p[i]);
			} else if (i >= 8 && i <= 10) {
				if (j == 4)
					j = 0;
				p[i].setBounds((60 + (120 * j)), 500, 100, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((60 + (120 * i)), 100, 100, 100);
				add(p[i]);
			}
		}
		/******** 클릭 이벤트 **********/
		for (int i = 0; i < 10; ++i)
			p[i].addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[1].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[2].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[3].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[4].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[5].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[6].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[7])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[7].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[7].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[8])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[8].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[8].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[9])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[9].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[9].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

	}
}

class Cooler extends JPanel implements Variable, ActionListener {
	private static final long serialVersionUID = 1L;
	JButton[] p = new JButton[10];

	Cooler() {
		setLayout(null);

		int j = 0;

		p[0] = new JButton("CORSAIR HYDRO SERIES H115i RGB Platinum");
		p[1] = new JButton("CORSAIR HYDRO SERIES H100i RGB Platinum");
		p[2] = new JButton("CORSAIR HYDRO SERIES H150i PRO RGB ");
		p[3] = new JButton("CORSAIR HYDRO SERIES H80i v2");
		p[4] = new JButton("NOCTUA NH-D15");
		p[5] = new JButton("써모랩 TRINITY WHITE LED");
		p[6] = new JButton("쿨러마스터 V8 GTS");
		p[7] = new JButton("EVGA CLC 280 Liquid");
		p[8] = new JButton("NZXT KRAKEN X72");
		p[9] = new JButton("NZXT KRAKEN X62");

		for (int i = 0; i < 10; i++) {
			if (i >= 4 && i <= 7) {
				p[i].setBounds((60 + (120 * j)), 300, 100, 100);
				j++;
				add(p[i]);
			} else if (i >= 8 && i <= 10) {
				if (j == 4)
					j = 0;
				p[i].setBounds((60 + (120 * j)), 500, 100, 100);
				j++;
				add(p[i]);
			} else {
				p[i].setBounds((60 + (120 * i)), 100, 100, 100);
				add(p[i]);
			}
		}
		
		for (int i = 0; i < 10; ++i)
			p[i].addActionListener(this);
	}
	/******** 클릭 이벤트 **********/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(p[0])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[0].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[0].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[1])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[1].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[1].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[2])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[2].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[2].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[3])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[3].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[3].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[4])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[4].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[4].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

		if (e.getSource().equals(p[5])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[5].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[5].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[6])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[6].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[6].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[7])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[7].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[7].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[8])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[8].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[8].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}
		if (e.getSource().equals(p[9])) {
			try {
				Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")
						.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
						.data("query", p[9].getText()).post();
				Element proc = doc.select("p.price_sect").select("a").first();
				String procloc = proc.attr("href").replaceAll(" ", "+");
				Document doc1 = Jsoup.connect(procloc).get();
				Elements lowprice = doc1.select("span.lwst_prc").select("em");
				String lowpricevalue = lowprice.select("em").html();
				Scanner scan = new Scanner(lowpricevalue);
				int i = 0;
				String[] lowrealval = new String[] { null, null, null, null, null };
				while (scan.hasNext()) {
					lowrealval[i] = scan.nextLine();
					i++;
				}
				scan.close();
				if (lowrealval[0] == null)
					lowrealval[0] = "(없음)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(없음)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " 의 현재 최저가는\n" + lowrealval[0] + " 원 최저가\n" + lowrealval[1]
								+ " 원 현금가\n물건 사이트로 이동하시겠습니까?",
						"확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {
					Desktop.getDesktop().browse(new URI(procloc));
					productT.Rowadd(p[9].getText(), lowrealval[1], lowrealval[0]);
				}
			} catch (IOException ex) {
				ex.printStackTrace();

			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}

		}

	}
}

class Product_table extends JFrame implements Variable {
	private static final long serialVersionUID = 1L;

	public Product_table() {
		add(sc);
		// 테이블 만드는건
		// http://blog.naver.com/PostView.nhn?blogId=tkddlf4209&logNo=220599772823 참고함

	}

	public void Rowadd(String name, String money, String card) {//테이블 데이터 추가 메소드
		String num = "1";
		DefaultTableModel m = (DefaultTableModel) product.getModel();
		String[] str = { name, num , money, card};
		m.addRow(str);
	}

}

class SubMainpane implements Variable,ActionListener {
	JPanel p_L = new JPanel();// 왼쪽 패널
	JPanel p_R = new JPanel();// 오른쪽 패널
	public Frame fs = new Frame("조선컴팔이 메뉴");

	public CPU Pcpu;
	public Mainboard PmainB;
	public Ram Pram;
	public GraphicCard Pgraphic;
	public Powersupply Ppower;
	public HddSsd phsd;
	public Cooler pcooler;

	JButton Add = new JButton("+");
	JButton Del = new JButton("-");
	JButton Rem = new JButton("삭제");
	JButton Pay = new JButton("결제");
	JButton tot = new JButton("합계");
	
	SubMainpane() {
		fs.setVisible(true);
		fs.setLayout(new GridLayout(0, 2));
		fs.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fs.setVisible(false);
				fs.dispose();
			}
		});
		/*************** 서브프레임 패널 *******************/
		JTabbedPane tabp = new JTabbedPane();//탭 패널

		JLabel price_money = new JLabel("현금가");
		
		
		p_L.setLayout(null);
		p_R.setLayout(null);

		Pcpu = new CPU();
		PmainB = new Mainboard();
		Pram = new Ram();
		Pgraphic = new GraphicCard();
		phsd = new HddSsd();
		Ppower = new Powersupply();
		pcooler = new Cooler();

		tabp.setBounds(0, 0, 645, 700);// 탭 사이즈 조절
		/**************** 탭 추가 ******************/
		tabp.addTab("CPU", Pcpu);
		tabp.addTab("메인보드", PmainB);
		tabp.addTab("램", Pram);
		tabp.addTab("그래픽카드", Pgraphic);
		tabp.addTab("HDD/SSD", phsd);
		tabp.addTab("파워서플라이", Ppower);
		tabp.addTab("공랭/수냉 쿨러", pcooler);
		
		/************* 테이블 ****************/
		sc.setBounds(0, 0, 640, 835);
		product.setRowHeight(20);
		product.setFont(new Font("굴림", Font.BOLD, 20));
		/**********************제품 수량 추가/삭제 결제/총계 버튼***********************/
		Add.setBounds(0, 699, 150, 150);
		Add.setFont(new Font("굴림", Font.PLAIN, 50));
		Del.setBounds(150,699,150,150);
		Del.setFont(new Font("굴림", Font.PLAIN,50));
		Rem.setBounds(300,699,150,150);
		Rem.setFont(new Font("굴림", Font.PLAIN,50));
		Pay.setBounds(450,699,200,150);
		Pay.setFont(new Font("굴림", Font.PLAIN,50));
	/********************추가 버튼 리스너**********************/
		Add.addActionListener(this);
		Del.addActionListener(this);
		Rem.addActionListener(this);
		Pay.addActionListener(this);
	 /************제품 수량 추가/삭제 결제/총계 버튼 추가***********************/
		p_L.add(Add);
		p_L.add(Del);
		p_L.add(Rem);
		p_L.add(Pay);
	/**************************************************************/
		p_L.add(tabp);
		p_R.add(sc);
		p_L.setBackground(Color.LIGHT_GRAY);
		p_R.setBackground(Color.LIGHT_GRAY);

		fs.add(p_L);
		fs.add(p_R);

		fs.setVisible(true);
		fs.setSize(1300, 880);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//수량 증가
		if(e.getSource().equals(Add)) {
			int row = product.getSelectedRow();
			if(row < 0) return;
			DefaultTableModel model = (DefaultTableModel)product.getModel();
			DecimalFormat df = new DecimalFormat("#,##0");//1000자리마다 ',' 붙임
			
			String money = (String)model.getValueAt(row, 2);
			String card = (String)model.getValueAt(row, 3);
			String pcs = (String)model.getValueAt(row, 1);
			
			money = money.replaceAll(",", "");
			card = card.replaceAll(",", "");
			
			int moneytoInt;
			int cardtoInt;
			int pcstoInt;
			
			if(money.equals("(없음)")){
				cardtoInt = Integer.parseInt(card);
				pcstoInt = Integer.parseInt(pcs);
				
				pcstoInt += 1;
				
				if(pcstoInt <= 2)
					cardtoInt = cardtoInt * pcstoInt;
				else 
					cardtoInt = (cardtoInt + (cardtoInt/(pcstoInt-1)));
				
				card = df.format(cardtoInt);

				pcs = Integer.toString(pcstoInt);
				
				model.setValueAt(pcs, row, 1);
				model.setValueAt(card, row, 3);
			}
			else {
				moneytoInt = Integer.parseInt(money);
				cardtoInt = Integer.parseInt(card);
				pcstoInt = Integer.parseInt(pcs);
				
				pcstoInt += 1;
			
				if(pcstoInt <= 2)
					moneytoInt = moneytoInt * pcstoInt;
				else 
				moneytoInt = (moneytoInt + (moneytoInt/(pcstoInt-1)));
				
				if(pcstoInt <= 2)
					cardtoInt = cardtoInt * pcstoInt;
				else 
					cardtoInt = (cardtoInt + (cardtoInt/(pcstoInt-1)));
			
			
				money = df.format(moneytoInt);
				card = df.format(cardtoInt);
			
				pcs = Integer.toString(pcstoInt);
				
				model.setValueAt(pcs, row, 1);
				model.setValueAt(money, row, 2);
				model.setValueAt(card, row, 3);
			}
		}
		//수량 감소
		if(e.getSource().equals(Del)) {
			int row = product.getSelectedRow();
			if(row < 0) return;
			DefaultTableModel model = (DefaultTableModel)product.getModel();
			
			String money = (String)model.getValueAt(row, 2);
			String card = (String)model.getValueAt(row, 3);
			String pcs = (String)model.getValueAt(row, 1);
			
			money = money.replaceAll(",", "");
			card = card.replaceAll(",", "");
			
			
			int moneytoInt;
			int cardtoInt;
			int pcstoInt;
			
			if(money.equals("(없음)")){
				cardtoInt = Integer.parseInt(card);
				pcstoInt = Integer.parseInt(pcs);
				
				cardtoInt = (cardtoInt - (cardtoInt/(pcstoInt)));
				if(pcstoInt ==1) {
					JOptionPane.showMessageDialog(null, "1개 이하 선택할 수 없습니다.", "에러", JOptionPane.ERROR_MESSAGE);
				}
				else {
					pcstoInt -= 1;
		
				
					DecimalFormat df = new DecimalFormat("#,##0");
					card = df.format(cardtoInt);
				
					pcs = Integer.toString(pcstoInt);
				
					model.setValueAt(pcs, row, 1);
					model.setValueAt(card, row, 3);
				}
			}
			else {
			
				moneytoInt = Integer.parseInt(money);
				cardtoInt = Integer.parseInt(card);
				pcstoInt = Integer.parseInt(pcs);
			
				moneytoInt = (moneytoInt - (moneytoInt/(pcstoInt)));
				cardtoInt = (cardtoInt - (cardtoInt/(pcstoInt)));
				if(pcstoInt ==1) {
					JOptionPane.showMessageDialog(null, "1개 이하 선택할 수 없습니다.", "에러", JOptionPane.ERROR_MESSAGE);
				}
				else {
					pcstoInt -= 1;
	
			
					DecimalFormat df = new DecimalFormat("#,##0");
					money = df.format(moneytoInt);
					card = df.format(cardtoInt);
			
					pcs = Integer.toString(pcstoInt);
			
					model.setValueAt(pcs, row, 1);
					model.setValueAt(money, row, 2);
					model.setValueAt(card, row, 3);
				}
			}
			
		}
		//삭제
		if(e.getSource().equals(Rem)) {
			int row = product.getSelectedRow();
			if(row < 0) return;
			DefaultTableModel model = (DefaultTableModel)product.getModel();
			model.removeRow(row);	
		}
		//결제
		if(e.getSource().equals(Pay)) {
			DecimalFormat df = new DecimalFormat("#,##0");
			DefaultTableModel model = (DefaultTableModel)product.getModel();	
			product.clearSelection();
			int Rownum = product.getRowCount();
			
			int a = 0;
			int b = 0;
			int sum_a = 0;
			int sum_b = 0;
			for(int i = 0; i < Rownum; ++i) {
				String money = (String)model.getValueAt(i, 2);
				String card = (String)model.getValueAt(i, 3);
						money = money.replaceAll(",", "");
						card = card.replaceAll(",", "");
						if(money.equals("(없음)")) {
							b =  Integer.parseInt(card);
						}else {
						
							a =  Integer.parseInt(money);
							b =  Integer.parseInt(card);
						}
						sum_a += a;//money합계
						sum_b += b;//card합계
				}
			String sum_money = df.format(sum_a);
			String sum_card = df.format(sum_b);
			int check = JOptionPane.showConfirmDialog(null,"현금가 : "+ sum_money + "\n"+"카드가 :" + sum_card +"\n"
					+"결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(check == 0) {
				model.setNumRows(0);
				int end = JOptionPane.showConfirmDialog(null,"결제되었습니다.", "결제완료", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
			
		}
	}
}
