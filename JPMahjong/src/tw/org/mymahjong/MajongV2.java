package tw.org.mymahjong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tw.org.classes.JPMajong;

public class MajongV2 extends JFrame {
	public JPMajong jpmajong;
	private JButton test;
	private LinkedList<LinkedList<JButton>> btn, btnchow;
	private JPanel[] card, pc, river, box, btnlayout, btnchowlayout;
//	private JPanel[][][] ponchow;
	private GridBagConstraints gbc;
	public boolean[] btnwait = new boolean[4];
	public boolean pcplayer;
	private MajongV2 mjv2 = this;
	public int mousei, mousej;
	private ImageIcon backgroundtest ;
	
	public MajongV2() {
		setLayout(new BorderLayout());
		jpmajong = new JPMajong();
		test = new JButton("TEST");
		JPanel top = new JPanel(new FlowLayout());
		top.add(test);
		JPanel center = new JPanel(new BorderLayout());



		// 牌河
		river = new JPanel[4];
		river[0] = new JPanel(new GridBagLayout());
		river[0].setPreferredSize(new Dimension(1280, 280));
		river[1] = new JPanel(new GridBagLayout());
		river[1].setPreferredSize(new Dimension(600, 600));
		river[2] = new JPanel(new GridBagLayout());
		river[2].setPreferredSize(new Dimension(1280, 280));
		river[3] = new JPanel(new GridBagLayout());
		river[3].setPreferredSize(new Dimension(600, 600));
		gbc = new GridBagConstraints();// 用於控制GridBagLayout
		gbc.insets = new Insets(5, 0, 0, 0);// 設定各元素間距
		// 牌河

		// 按鈕
		JPanel btncenter = new JPanel(new BorderLayout());
		btncenter.setOpaque(false);
		btn = new LinkedList<LinkedList<JButton>>();
		btnlayout = new JPanel[4];
		for (int i = 0; i < btnlayout.length; i++) {
			LinkedList<JButton> btntemp = new LinkedList<>();
			final int itemp = i;
			if (i == 0 || i == 2) {
				btnlayout[i] = new JPanel(new FlowLayout());
				btntemp.add(new JButton("吃"));
				btntemp.add(new JButton("碰"));
				btntemp.add(new JButton("槓"));
				btntemp.add(new JButton("胡"));
				btntemp.add(new JButton("取消"));
				btn.add(btntemp);
			} else if (i == 1 || i == 3) {
				btnlayout[i] = new JPanel(new GridLayout(5, 1));
				btntemp.add(new JButton("吃"));
				btntemp.add(new JButton("碰"));
				btntemp.add(new JButton("槓"));
				btntemp.add(new JButton("胡"));
				btntemp.add(new JButton("取消"));
				btn.add(btntemp);
			}
		}

		for (int i = 0; i < btn.size(); i++) {
			for (int j = 0; j < btn.get(i).size(); j++) {
				final int itemp = i;
				final int jtemp = j;
				btnlayout[i].add(btn.get(i).get(j));
				btn.get(i).get(j).setVisible(false);
				btn.get(i).get(j).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						chosebtn(itemp, jtemp);
						System.out.println(itemp + " " + jtemp);
					}
				});
			}
		}
		btncenter.add(btnlayout[0], BorderLayout.SOUTH);
		btncenter.add(btnlayout[1], BorderLayout.WEST);
		btncenter.add(btnlayout[2], BorderLayout.NORTH);
		btncenter.add(btnlayout[3], BorderLayout.EAST);
		// 按鈕

		// 3種吃按鈕
		JPanel btnchowcenter = new JPanel(new BorderLayout());
		btnchowcenter.setOpaque(false);
		btnchow = new LinkedList<LinkedList<JButton>>();
		btnchowlayout = new JPanel[4];

		for (int i = 0; i < btnchowlayout.length; i++) {
			LinkedList<JButton> btnchowtemp = new LinkedList<>();
			final int itemp = i;
			if (i == 0 || i == 2) {
				btnchowlayout[i] = new JPanel(new FlowLayout());
				btnchowtemp.add(new JButton("1"));
				btnchowtemp.add(new JButton("2"));
				btnchowtemp.add(new JButton("3"));
				btnchow.add(btnchowtemp);
			} else if (i == 1 || i == 3) {
				btnchowlayout[i] = new JPanel(new GridLayout(3, 1));
				btnchowtemp.add(new JButton("1"));
				btnchowtemp.add(new JButton("2"));
				btnchowtemp.add(new JButton("3"));
				btnchow.add(btnchowtemp);
			}
		}
		for (int i = 0; i < btnchow.size(); i++) {
			for (int j = 0; j < btnchow.get(i).size(); j++) {
				final int itemp = i;
				final int jtemp = j;
				btnchowlayout[i].add(btnchow.get(i).get(j));
				btnchow.get(i).get(j).setVisible(false);
				btnchow.get(i).get(j).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jpmajong.chowtest(itemp, jtemp);
						btnhide(itemp);
						btnwait[itemp] = true;
						System.out.println(itemp + " " + jtemp);
					}
				});
			}
		}

		btnchowcenter.add(btnchowlayout[0], BorderLayout.SOUTH);
		btnchowcenter.add(btnchowlayout[1], BorderLayout.WEST);
		btnchowcenter.add(btnchowlayout[2], BorderLayout.NORTH);
		btnchowcenter.add(btnchowlayout[3], BorderLayout.EAST);
//		btnchowcenter.add(top, BorderLayout.CENTER);
		// 3種吃按鈕

		box = new JPanel[4];
		for (int i = 0; i < 4; i++) {
			if (i == 0 || i == 2) {
				box[i] = new JPanel();
				box[i].setLayout(new BoxLayout(box[i], BoxLayout.X_AXIS));// BoxLayout，裡面放兩個flowlayout，分別是吃碰的跟手牌的
			} else if (i == 1 || i == 3) {
				box[i] = new JPanel();
				box[i].setLayout(new BoxLayout(box[i], BoxLayout.Y_AXIS));// BoxLayout，裡面放兩個Gridlayout，分別是吃碰的跟手牌的
			}
		}

		card = new JPanel[4];
		for (int i = 0; i < 4; i++) {
			if (i == 0 || i == 2) {
				card[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
				card[i].setPreferredSize(new Dimension(800, 60));
			} else if (i == 1 || i == 3) {
				card[i] = new JPanel(new GridLayout(14, 1));
			}
		}

		pc = new JPanel[4];
		for (int i = 0; i < 4; i++) {
			if (i == 0 || i == 2) {
				pc[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
//				pc[i].setPreferredSize(new Dimension(100,60));
			} else if (i == 1 || i == 3) {
				pc[i] = new JPanel(new GridLayout(4, 5));
				pc[i].setPreferredSize(new Dimension(170, 500));
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < jpmajong.getcardimage(i).size(); j++) {
				final int itemp = i;
				final int jtemp = j;
				mousei = i;
				mousej = i;
				// 內部類別的變數需要做final來引用外部變數才能正常使用
				card[i].add(jpmajong.getcardimage(i).get(j));
				jpmajong.getcardimage(i).get(j).addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (jpmajong.getplay() == itemp) {
							if (jpmajong.getcardlist().get(itemp).get(jtemp) != 136) {
								jpmajong.playing(itemp, jtemp);
								btnVisible();
								waitchose waitchose = new waitchose(mjv2);
								Thread t1 = new Thread(waitchose);
								t1.start();
								System.out.println(itemp + "," + jtemp);
							}
						} else if (jpmajong.getplay() == 4) {
							System.out.println("流局!");
						} else {
							System.out.println("還沒輪到你出牌");
						} // 之後寫進類別

					}
				});// 將手牌以Jlabel物件加入至Layout中並創建MouseListener供點擊
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < jpmajong.getponchowimage(i).size(); j++) {
				for (int k = 0; k < jpmajong.getponchowimage(i).get(j).size(); k++) {
					pc[i].add(jpmajong.getponchowimage(i).get(j).get(k));
				}
				if (i == 0 || i == 2) {
					pc[i].add(new JLabel("-"));
				}
			}
		} // 吃碰牌的Jlabel
		for (int i = 0; i < 4; i++) {
			box[i].add(pc[i]);
			box[i].add(card[i]);
		}

		add(box[3], BorderLayout.EAST);
		add(box[0], BorderLayout.SOUTH);
		add(box[1], BorderLayout.WEST);
		add(box[2], BorderLayout.NORTH);// 印出四家手牌

		center.add(btncenter, BorderLayout.CENTER);
		btncenter.add(btnchowcenter, BorderLayout.CENTER);
		center.add(river[3], BorderLayout.EAST);
		center.add(river[0], BorderLayout.SOUTH);
		center.add(river[1], BorderLayout.WEST);
		center.add(river[2], BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);

//		top.setBackground(Color.black);
//		river[0].setBackground(Color.BLUE);
//		river[1].setBackground(Color.GREEN);
//		river[2].setBackground(Color.YELLOW);
//		river[3].setBackground(Color.RED);
//		card[0].setBackground(Color.RED);
//		card[1].setBackground(Color.YELLOW);
//		card[2].setBackground(Color.GREEN);
//		card[3].setBackground(Color.BLUE);
//		pc[0].setBackground(Color.YELLOW);
//		pc[1].setBackground(Color.blue);
//		pc[2].setBackground(Color.RED);
//		pc[3].setBackground(Color.GREEN);
//		//背景
//		backgroundtest = new ImageIcon("/green.png");
//		JLabel bkjlabel = new JLabel(backgroundtest);
//		bkjlabel.setBounds(0, 0, getWidth(),getHeight());
//		add(bkjlabel);
//		//背景

		

		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				jpmajong.Mountainname();
//				jpmajong.pontest();
				repaint();
				System.out.println("------------------");
				System.out.println("test");

			}
		});

		for (int i = 0; i < 4; i++) {
			river(i);
		} // 初始化牌河

		setSize(1920, 1030);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void river(int i) {
		int x, y;
		x = y = 0;
		for (int j = 0; j < jpmajong.getriverimage(i).size(); j++) {
			if (x < 6) {
				gbc.gridx = x;
				gbc.gridy = y;
				river[i].add(jpmajong.getriverimage(i).get(j), gbc);
				x++;
			} else {
				x = 0;
				y++;
				gbc.gridx = x;
				gbc.gridy = y;
				river[i].add(jpmajong.getriverimage(i).get(j), gbc);
				x++;
			}
		} // 設定牌河位置
	}

	public void chosebtn(int playeri, int btnj) {
		if (btnj == 0) {
//			jpmajong.getchowck();
			pcplayer = true;
			btnhide(playeri);
			btnchowVisible(playeri);
			// 吃
		} else if (btnj == 1) {
			jpmajong.pontest(playeri);
			btnwait[playeri] = true;
			pcplayer = true;
			btnhide(playeri);
			// 碰
		} else if (btnj == 2) {
			btnwait[playeri] = true;
			pcplayer = true;
			btnhide(playeri);
			// 槓
		} else if (btnj == 3) {
			btnwait[playeri] = true;
			btnhide(playeri);
			// 胡
		} else if (btnj == 4) {
			btnwait[playeri] = true;
			btnhide(playeri);
			// 取消
		}
		System.out.println(playeri + " " + btnj);
	}

	public void btnVisible() {
		for (int i = 0; i < btn.size(); i++) {
			if (jpmajong.getponck()[i]) {
				btn.get(i).get(1).setVisible(true);
				btn.get(i).get(4).setVisible(true);
			}
			if (jpmajong.getchowck()[i]) {
				btn.get(i).get(0).setVisible(true);
				btn.get(i).get(4).setVisible(true);
			}

			System.out.println("btn");
		}

	}

	public void btnchowVisible(int playeri) {
		for (int j = 0; j < jpmajong.getchowpossible().length; j++) {
			if (jpmajong.getchowpossible()[j]) {
				btnchow.get(playeri).get(j).setVisible(true);
			}
		}
	}

	public void btnhide(int playeri) {
		btn.get(playeri).get(0).setVisible(false);
		btn.get(playeri).get(1).setVisible(false);
		btn.get(playeri).get(2).setVisible(false);
		btn.get(playeri).get(3).setVisible(false);
		btn.get(playeri).get(4).setVisible(false);
		btnchow.get(playeri).get(0).setVisible(false);
		btnchow.get(playeri).get(1).setVisible(false);
		btnchow.get(playeri).get(2).setVisible(false);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MajongV2();

	}
}

class waitchose implements Runnable {
	private MajongV2 MajongV2;

	public waitchose(MajongV2 majongV2) {
		this.MajongV2 = majongV2;
	}

	@Override
	public void run() {
		boolean wait = false;
		MajongV2.btnwait[MajongV2.jpmajong.getplay()] = true;
		for (int i = 0; i < 4; i++) {
			if (!MajongV2.jpmajong.getponck()[i] && !MajongV2.jpmajong.getchowck()[i]) {
				MajongV2.btnwait[i] = true;
			}
		}
		while (wait == false) {
			try {
				System.out.println("TEST");
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println(e + " waitchose()");
			}
			if (MajongV2.btnwait[0] && MajongV2.btnwait[1] && MajongV2.btnwait[2] && MajongV2.btnwait[3]) {
				wait = true;
			}
		}
		MajongV2.btnwait[0] = MajongV2.btnwait[1] = MajongV2.btnwait[2] = MajongV2.btnwait[3] = false;
		if (!MajongV2.pcplayer) {
			MajongV2.jpmajong.nextcard(MajongV2.mousei, MajongV2.mousej);
		} else {
			MajongV2.pcplayer = false;
		}
	}
}
