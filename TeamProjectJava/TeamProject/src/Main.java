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

interface Variable {// ����

	TextField ID = new TextField(6);
	TextField PW = new TextField(6);
/********************�� ���� ����*********************************/
	String[] header = { "��ǰ��", "����", "���ݰ�", "ī��" };
	String[][] data = {};

	DefaultTableModel model = new DefaultTableModel(data, header) {//���̺� ������ ���� ���ϰ� ��
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

	public JTabbedPane PaneTab;// ��

	public String id1 = "asdf";
	public String pw1 = "asdf";

	Main() {
		/*********************�α���************************/
		PW.setEchoChar('*');

		namelabel.setBounds(110, 270, 50, 40);
		passwordLabel.setBounds(110, 318, 50, 40);
		ID.setBounds(170, 270, 210, 40);
		PW.setBounds(170, 318, 210, 40);
		login.setBounds(390, 265, 100, 100);

		ID.setFont(new Font("����", Font.PLAIN, 40));
		PW.setFont(new Font("����", Font.PLAIN, 40));
		login.setFont(new Font("����", Font.BOLD, 20));

		background.setIcon(new ImageIcon("src\\image\\big.png"));
		background.setBounds(0, 0, 600, 553);

		this.setLayout(null);
		/******************* �α��� ���� Ű �̺�Ʈ **********************/

		KeyListener key_li = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) // ��Ʈ�� ������ pw�� Ŀ�� �̵�
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
						JOptionPane.showMessageDialog(null, "ID �Ǵ� PW �� Ʋ�Ƚ��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
						ID.setText("");
						PW.setText("");
						ID.requestFocus();
					}
				}
			}

			/**************** ������� ���� ****************/
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

		this.setTitle("���������� �α���");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		this.setVisible(true);
	}

	/********************** ���� *********************/

	public static void main(String[] args) throws IOException {
		new Main();

		// �Ʒ��� ASUS RTX 2080Ti�� ã�� ��
		Document doc = Jsoup.connect("http://search.danawa.com/ajax/getProductList.ajax.php")// �ٳ��� ��ǰ ����Ʈ ���������� �̿�
				.header("Origin", "http://search.danawa.com").header("Referer", "http://search.danawa.com/")
				.data("query", "ASUS RTX 2080Ti").post();
		// ����Ʈ ����� �Ľ��ϱ� ������ ����� ������ �� �ʿ��� ����� Origin Referer �������� ã�� ��ǰ�� �̸��� �Է�
		Element proc = doc.select("p.price_sect").select("a").first();
		// html���� �±װ� <p>�̰� Ŭ������ .price_sect ������ �±װ� <a>�ΰ��� ù��°�� ������
		String procloc = proc.attr("href").replaceAll(" ", "+"); // �±� �Ӽ��� href �ΰ��� ���빰�� �Ľ��ؼ� ������ +�� �ٲ�
		// procloc���� ��������� ������ ���� ��ǰ �̸��� �˻��ؼ� ���� ��ǰ ����Ʈ�� �ּҰ� ��
		Document doc1 = Jsoup.connect(procloc).get(); // get ������� procloc ����Ʈ�� �Ľ�
		Elements lowprice = doc1.select("span.lwst_prc").select("em");
		// �±װ� <span>�̰� Ŭ������ lwst_prc �׸��� ���� �±װ� em�ΰ��� �Ľ�
		String lowpricevalue = lowprice.select("em").html();
		// �±װ� <em>�ΰ��� ���빰�� �Ľ� �� : <em>���빰</em>
		Scanner scan = new Scanner(lowpricevalue);
		int i = 0;
		String[] lowrealval = new String[] { null, null, null, null, null };
		while (scan.hasNext()) {
			lowrealval[i] = scan.nextLine();
			i++;
		} // �Ľ��� ������ �迭�� �ִ� ����
		scan.close();
		if (lowrealval[0] == null)
			lowrealval[0] = "(����)";
		else if (lowrealval[1] == null)
			lowrealval[1] = "(����)";
		// ���� �Ľ��� ���� ������ �������� ǥ��
	}

	/****************** ������� ���� ******************/
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/********************** �α��� ��ư���� �۵��ϴ� �̺�Ʈ *********************************/
	public void actionPerformed(ActionEvent e) {
		String test_id = ID.getText();
		String test_pw = PW.getText();
		if (test_id.equals(id1) && test_pw.equals(pw1)) {
			@SuppressWarnings("unused")
			SubMainpane pane = new SubMainpane();
			dispose();
		}

		else if (ID.getText() != id1 && PW.getText() != pw1) {
			JOptionPane.showMessageDialog(null, "ID �Ǵ� PW �� Ʋ�Ƚ��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			ID.setText("");
			PW.setText("");
			ID.requestFocus();
		}
	}
}

/***************** ���⼭���ʹ� �ǿ� �� �г� *******************/
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

	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {// ���� ���̾�α׿��� YES�� �������� ans 0�� ��ȯ�� PLAIN�� 1
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		p[0] = new JButton("�Ｚ���� DDR4 8G PC4-21300"); // 5937666
		p[1] = new JButton("�Ｚ���� DDR4 16G PC4-21300");// 5941995
		p[2] = new JButton("�Ｚ���� DDR4 4G PC4-19200"); // 5040061
		for (int i = 0; i < 3; i++) {
			p[i].setBounds((60 + (120 * i)), 100, 100, 100);
			add(p[i]);
		}
		for (int i = 0; i < 3; ++i)
			p[i].addActionListener(this);
	}

	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		p[3] = new JButton("�üҴ� SSR-850FX Full Modular");
		p[4] = new JButton("EVGA 750 GQ 80PLUS GOLD");
		p[5] = new JButton("����ũ�δн� Classic II 700W");
		p[6] = new JButton("����ũ�δн� Classic II 600W");
		p[7] = new JButton("����ũ�δн� Classic II 500W ");

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
	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		p[2] = new JButton("�Ｚ���� 860 EVO (250GB)");
		p[3] = new JButton("�Ｚ���� 860 EVO (500GB)");
		p[4] = new JButton("�Ｚ���� 970 EVO M.2 2280 (250GB)");
		p[5] = new JButton("�Ｚ���� 860 EVO (2TB)");
		p[6] = new JButton("�Ｚ���� 970 PRO M.2 2280 (512GB)");
		p[7] = new JButton("�Ｚ���� 970 PRO M.2 2280 (1TB)");
		p[8] = new JButton("�Ｚ���� 860 EVO (4TB)");
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
		/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		p[5] = new JButton("��� TRINITY WHITE LED");
		p[6] = new JButton("�𷯸����� V8 GTS");
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
	/******** Ŭ�� �̺�Ʈ **********/
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[0].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[1].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[2].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[3].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[4].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[5].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[6].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[7].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[8].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
					lowrealval[0] = "(����)";
				else if (lowrealval[1] == null)
					lowrealval[1] = "(����)";
				int ans = JOptionPane.showConfirmDialog(null,
						p[9].getText() + " �� ���� ��������\n" + lowrealval[0] + " �� ������\n" + lowrealval[1]
								+ " �� ���ݰ�\n���� ����Ʈ�� �̵��Ͻðڽ��ϱ�?",
						"Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		// ���̺� ����°�
		// http://blog.naver.com/PostView.nhn?blogId=tkddlf4209&logNo=220599772823 ������

	}

	public void Rowadd(String name, String money, String card) {//���̺� ������ �߰� �޼ҵ�
		String num = "1";
		DefaultTableModel m = (DefaultTableModel) product.getModel();
		String[] str = { name, num , money, card};
		m.addRow(str);
	}

}

class SubMainpane implements Variable,ActionListener {
	JPanel p_L = new JPanel();// ���� �г�
	JPanel p_R = new JPanel();// ������ �г�
	public Frame fs = new Frame("���������� �޴�");

	public CPU Pcpu;
	public Mainboard PmainB;
	public Ram Pram;
	public GraphicCard Pgraphic;
	public Powersupply Ppower;
	public HddSsd phsd;
	public Cooler pcooler;

	JButton Add = new JButton("+");
	JButton Del = new JButton("-");
	JButton Rem = new JButton("����");
	JButton Pay = new JButton("����");
	JButton tot = new JButton("�հ�");
	
	SubMainpane() {
		fs.setVisible(true);
		fs.setLayout(new GridLayout(0, 2));
		fs.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				fs.setVisible(false);
				fs.dispose();
			}
		});
		/*************** ���������� �г� *******************/
		JTabbedPane tabp = new JTabbedPane();//�� �г�

		JLabel price_money = new JLabel("���ݰ�");
		
		
		p_L.setLayout(null);
		p_R.setLayout(null);

		Pcpu = new CPU();
		PmainB = new Mainboard();
		Pram = new Ram();
		Pgraphic = new GraphicCard();
		phsd = new HddSsd();
		Ppower = new Powersupply();
		pcooler = new Cooler();

		tabp.setBounds(0, 0, 645, 700);// �� ������ ����
		/**************** �� �߰� ******************/
		tabp.addTab("CPU", Pcpu);
		tabp.addTab("���κ���", PmainB);
		tabp.addTab("��", Pram);
		tabp.addTab("�׷���ī��", Pgraphic);
		tabp.addTab("HDD/SSD", phsd);
		tabp.addTab("�Ŀ����ö���", Ppower);
		tabp.addTab("����/���� ��", pcooler);
		
		/************* ���̺� ****************/
		sc.setBounds(0, 0, 640, 835);
		product.setRowHeight(20);
		product.setFont(new Font("����", Font.BOLD, 20));
		/**********************��ǰ ���� �߰�/���� ����/�Ѱ� ��ư***********************/
		Add.setBounds(0, 699, 150, 150);
		Add.setFont(new Font("����", Font.PLAIN, 50));
		Del.setBounds(150,699,150,150);
		Del.setFont(new Font("����", Font.PLAIN,50));
		Rem.setBounds(300,699,150,150);
		Rem.setFont(new Font("����", Font.PLAIN,50));
		Pay.setBounds(450,699,200,150);
		Pay.setFont(new Font("����", Font.PLAIN,50));
	/********************�߰� ��ư ������**********************/
		Add.addActionListener(this);
		Del.addActionListener(this);
		Rem.addActionListener(this);
		Pay.addActionListener(this);
	 /************��ǰ ���� �߰�/���� ����/�Ѱ� ��ư �߰�***********************/
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
		//���� ����
		if(e.getSource().equals(Add)) {
			int row = product.getSelectedRow();
			if(row < 0) return;
			DefaultTableModel model = (DefaultTableModel)product.getModel();
			DecimalFormat df = new DecimalFormat("#,##0");//1000�ڸ����� ',' ����
			
			String money = (String)model.getValueAt(row, 2);
			String card = (String)model.getValueAt(row, 3);
			String pcs = (String)model.getValueAt(row, 1);
			
			money = money.replaceAll(",", "");
			card = card.replaceAll(",", "");
			
			int moneytoInt;
			int cardtoInt;
			int pcstoInt;
			
			if(money.equals("(����)")){
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
		//���� ����
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
			
			if(money.equals("(����)")){
				cardtoInt = Integer.parseInt(card);
				pcstoInt = Integer.parseInt(pcs);
				
				cardtoInt = (cardtoInt - (cardtoInt/(pcstoInt)));
				if(pcstoInt ==1) {
					JOptionPane.showMessageDialog(null, "1�� ���� ������ �� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
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
					JOptionPane.showMessageDialog(null, "1�� ���� ������ �� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
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
		//����
		if(e.getSource().equals(Rem)) {
			int row = product.getSelectedRow();
			if(row < 0) return;
			DefaultTableModel model = (DefaultTableModel)product.getModel();
			model.removeRow(row);	
		}
		//����
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
						if(money.equals("(����)")) {
							b =  Integer.parseInt(card);
						}else {
						
							a =  Integer.parseInt(money);
							b =  Integer.parseInt(card);
						}
						sum_a += a;//money�հ�
						sum_b += b;//card�հ�
				}
			String sum_money = df.format(sum_a);
			String sum_card = df.format(sum_b);
			int check = JOptionPane.showConfirmDialog(null,"���ݰ� : "+ sum_money + "\n"+"ī�尡 :" + sum_card +"\n"
					+"�����Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(check == 0) {
				model.setNumRows(0);
				int end = JOptionPane.showConfirmDialog(null,"�����Ǿ����ϴ�.", "�����Ϸ�", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
			
		}
	}
}
