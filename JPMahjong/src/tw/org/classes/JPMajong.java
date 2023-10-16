package tw.org.classes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPMajong extends JPanel {
	private int[] JPMajong, rivercount, pccount;
	private int rand, temp, dice1, dice2, dicesum, position, whoplay, mfc, nowplaycard;
	private int[][] Mountain, card;
	private LinkedList<LinkedList<JLabel>> majongimage, riverimage;
	private LinkedList<LinkedList<Integer>> mountainList, cardList, riverList;// 存放刪除用##
	private LinkedList<LinkedList<LinkedList<Integer>>> pongchowlist;
	private LinkedList<LinkedList<LinkedList<JLabel>>> pongchowimage;
	private BufferedImage image;
	private LinkedList<LinkedList<Image>> cardimagelist;
	private boolean[] ponck, chowck, chowpossible;

//	public JLabel testimage;
	public JPMajong() {
		JPMajong = new int[136];
		mountainList = new LinkedList<>();
		cardList = new LinkedList<>();
		majongimage = new LinkedList<>();
		riverimage = new LinkedList<>();
		riverList = new LinkedList<>();
		cardimagelist = new LinkedList<>();
		pongchowlist = new LinkedList<>();
		pongchowimage = new LinkedList<>();
		rivercount = new int[4];
		pccount = new int[4];
		for (int i = 0; i < JPMajong.length; i++) {
			JPMajong[i] = i;
		} // 新麻將
		setimage(0.05);
		Shuffle();
		PileUp();
		dice();
		deal();
		setriverlist();
		setponchowlist();
		setcardimage(cardList, majongimage, true);
		System.out.printf("dice:%d,position:%d\n", dicesum, position);
		System.out.println(cardList.get(0).size());
		System.out.println(cardList.get(1).size());
		System.out.println(cardList.get(2).size());
		System.out.println(cardList.get(3).size());
		whoplay = 0;// 判斷誰能打牌
	}

	private void setimage(double size) {
		String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "紅5" };
		String[] type = { "萬", "筒", "條", };
		String[] type1 = { "東", "南", "西", "北", "白", "發", "中" };
		try {
			for (int i = 0; i < type.length; i++) {
				LinkedList<Image> imagelist = new LinkedList<>();
				for (int j = 0; j < number.length; j++) {
//					image = ImageIO.read(new File(String.format("Majong/image/%s%s.png", number[j], type[i])));
					image = ImageIO.read(getClass().getResource(String.format("/%s%s.png", number[j], type[i])));
					Image imagetemp = image.getScaledInstance((int) (image.getWidth() * size),
							(int) (image.getHeight() * size), Image.SCALE_SMOOTH);// 縮放圖片
					imagelist.add(imagetemp);
				}
				cardimagelist.add(imagelist);
			} // 0~9m、0~9p、0~9s加入Linkedlist
			for (int j = 0; j == 0; j++) {
				LinkedList<Image> imagelist = new LinkedList<>();
				for (int i = 0; i < type1.length; i++) {
//					image = ImageIO.read(new File(String.format("Majong/image/%s.png", type1[i])));
					image = ImageIO.read(getClass().getResource(String.format("/%s.png",type1[i])));
					Image imagetemp = image.getScaledInstance((int) (image.getWidth() * size),
							(int) (image.getHeight() * size), Image.SCALE_SMOOTH);// 縮放圖片
					imagelist.add(imagetemp);
				}
				cardimagelist.add(imagelist);
				LinkedList<Image> space = new LinkedList<>();
//				image = ImageIO.read(new File(String.format("Majong/image/%s.png", "測試")));
				image = ImageIO.read(getClass().getResource(String.format("/%s.png","測試")));
				Image imagetemp = image.getScaledInstance((int) (image.getWidth() * 0.005),
						(int) (image.getHeight() * 0.005), Image.SCALE_SMOOTH);// 縮放圖片
				space.add(imagetemp);
				cardimagelist.add(space);// 空白牌
			} // 做區域變數用的迴圈

		} catch (Exception e) {
			System.out.println(e + " in setimage");
		}
//		testimage = new JLabel(new ImageIcon(cardimagelist.get(3).get(0)));
	}

	public void Shuffle() {
		for (int i = JPMajong.length - 1; i > 0; i--) {
			rand = (int) (Math.random() * (i + 1));
			temp = JPMajong[rand];
			JPMajong[rand] = JPMajong[i];
			JPMajong[i] = temp;
		}
	}// 洗牌

	public void PileUp() {
		Mountain = new int[4][34];
		for (int i = 0; i < JPMajong.length; i++) {
			Mountain[i % 4][i / 4] = JPMajong[i];
		} // 堆四家牌山
		for (int i = 0; i < Mountain.length; i++) {
			LinkedList<Integer> mtlist = new LinkedList<>();
			for (int j = 0; j < Mountain[i].length; j++) {
				mtlist.add((Mountain[i][j]));
			}
			mountainList.add(mtlist);
		} // 放進LinkedList內
	}

	public void dice() {
		dice1 = (int) (Math.random() * 6 + 1);
		dice2 = (int) (Math.random() * 6 + 1);
		dicesum = dice1 + dice2;
		position = (4 - (dicesum % 4)) % 4;// 抓牌方位 餘0=mountain[0] 餘1mountain[3] 餘2mountain[2] 餘3mountain[1]
		// (4-餘數)%4
	}// 擲骰子、取位置

	public void deal() {
		card = new int[4][14];// 定義四家麻將手牌
		for (int i = 0; i < 4; i++) {
			card[i][13] = 136;
		} // 空白牌
		for (int i = 0; i < 53; i++) {
			if (i < 48) {
				card[(4 - ((i / 4) % 4)) % 4][i % 4
						+ (i / 16) * 4] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
//				mountainList.get((position - ((dicesum * 2 + i) / 34) + 4) % 4)
//						.remove(((((34 - dicesum * 2) - 1 - i) % 34) + 34) % 34);// 發牌到玩家手上後移除牌山List內的牌(弄錯)
				mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
						.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
			} else {
				switch (i) {
				case 48:
					card[0][12] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
					mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
							.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
					break;
				case 49:
					card[0][13] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
					mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
							.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
					break;
				case 50:
					card[1][12] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
					mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
							.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
					break;
				case 51:
					card[2][12] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
					mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
							.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
					break;
				case 52:
					card[3][12] = Mountain[(position + ((dicesum * 2 + i) / 34)) % 4][(dicesum * 2 + i) % 34];
					mountainList.get((position + ((dicesum * 2 + i) / 34)) % 4)
							.remove(((dicesum * 2 + i) <= 34 ? dicesum * 2 : 0));// 發牌到玩家手上後移除牌山List內的牌
					mfc = (position + ((dicesum * 2 + i) / 34)) % 4;
					break;
				}

			}
		}
		for (int i = 0; i < card.length; i++) {
			LinkedList<Integer> crlist = new LinkedList<>();
			for (int j = 0; j < card[i].length; j++) {
				crlist.add((card[i][j]));
			}
			cardList.add(crlist);
		} // 把牌放進cardList

//		for (int i = 0; i < card.length; i++) {
//			cardList.add(card[i]);
//		}
//		System.out.println(cardList.getFirst());
	}// 發牌

	public void Mountainname() {
		String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] type = { "萬", "筒", "條", };
		String[] type1 = { "東", "南", "西", "北", "白", "發", "中" };
		for (int[] c1 : Mountain) {
//			LinkedList<String> mtlist = new LinkedList<>();
			for (int c2 : c1) {
				if (c2 < 108) {
					if (c2 == 19 || c2 == 55 || c2 == 91) {
//						mtlist.add("紅" + number[(c2 / 4) - (9 * (c2 / 36))] + type[c2 / 36]);
						System.out.printf("紅%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);
					} else {
//						mtlist.add(number[(c2 / 4) - (9 * (c2 / 36))] + type[c2 / 36]);
						System.out.printf("%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);
					}
				} else if (c2 == 136) {
					System.out.print("");
				} else {
//					mtlist.add(type1[(c2 - 108) / 4]);
					System.out.printf("%s ", type1[(c2 - 108) / 4]);
				}

			}
//			mountainList.add(mtlist);
			System.out.println();
		} // 定義牌山麻將牌名稱後印牌 不能排序
		System.out.println("---------------------");
		for (int i = 0; i < mountainList.size(); i++) {
			for (int j = 0; j < mountainList.get(i).size(); j++) {
				System.out.print(mountainList.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}

//	public void cardname() {
//		String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
//		String[] type = { "萬", "筒", "條", };
//		String[] type1 = { "東", "南", "西", "北", "白", "發", "中" };
//		int i = 0, j = 0;
//		try {
//			for (int[] c1 : card) {
//				j = 0;
//				Arrays.sort(c1);// 花色排序
//				for (int c2 : c1) {
//					if (c2 < 108) {
//						if (c2 == 19 || c2 == 55 || c2 == 91) {
//							image = ImageIO.read(new File(String.format("Majong/image/%s%s%s.png","紅",
//									number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36])));
//							Image imagetemp = image.getScaledInstance((int) (image.getWidth() * 0.1),
//									(int) (image.getHeight() * 0.1), Image.SCALE_SMOOTH);//縮放圖片0.1倍
//							majongimage[i][j] = new JLabel(new ImageIcon(imagetemp));
//							System.out.printf("紅%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);// 紅寶牌
//						} else {
//							image = ImageIO.read(new File(String.format("Majong/image/%s%s.png",
//									number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36])));
//							Image imagetemp = image.getScaledInstance((int) (image.getWidth() * 0.1),
//									(int) (image.getHeight() * 0.1), Image.SCALE_SMOOTH);
//							majongimage[i][j] = new JLabel(new ImageIcon(imagetemp));
//							System.out.printf("%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);
//						} // 除字牌及紅寶牌以外的牌
//					} else if (c2 == 136) {
//						image = ImageIO.read(new File(String.format("Majong/image/%s.png",
//								"測試")));
//						Image imagetemp = image.getScaledInstance((int) (image.getWidth() * 0.1),
//								(int) (image.getHeight() * 0.1), Image.SCALE_SMOOTH);
//						majongimage[i][j] = new JLabel(new ImageIcon(imagetemp));
//						System.out.print("測試");// 空
//					} else {
//						image = ImageIO.read(new File(String.format("Majong/image/%s.png",
//								type1[(c2 - 108) / 4])));
//						Image imagetemp = image.getScaledInstance((int) (image.getWidth() * 0.1),
//								(int) (image.getHeight() * 0.1), Image.SCALE_SMOOTH);
//						majongimage[i][j] = new JLabel(new ImageIcon(imagetemp));
//						System.out.printf("%s ", type1[(c2 - 108) / 4]);// 字牌
//					}
//					j++;
//				}
//				System.out.println();
//				i++;
//			} // 定義手牌麻將牌名稱後印牌且照花色排序
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//	}

//	public JLabel getcard(int position) {
//		return jlabel[position];
//	}// 取得牌

	public int getplay() {
		return whoplay;
	}

	public LinkedList<JLabel> getcardimage(int position) {
		return majongimage.get(position);
	}// 取得手牌圖片
	
	public LinkedList<LinkedList<Integer>> getcardlist() {
		return cardList;
	}

	public LinkedList<JLabel> getriverimage(int position) {
		return riverimage.get(position);
	}// 取得牌河圖片

	public void sortcard() {
		for (int i = 0; i < cardList.size(); i++) {
			cardList.get(i).sort(null);
//			System.out.println(cardList.get(i));
		}
	}

	public void setcardimage(LinkedList<LinkedList<Integer>> list, LinkedList<LinkedList<JLabel>> jlabel,
			boolean sort) {
		String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] type = { "萬", "筒", "條", };
		String[] type1 = { "東", "南", "西", "北", "白", "發", "中" };
		if (sort) {
			sortcard();
		}
		try {
			for (LinkedList<Integer> c1 : list) {
				LinkedList<JLabel> majongimagetemp = new LinkedList<>();
				for (int c2 : c1) {
					if (c2 < 108) {
						if (c2 == 19 || c2 == 55 || c2 == 91) {
							majongimagetemp.add(new JLabel(new ImageIcon(cardimagelist.get(c2 / 36).get(9))));
							System.out.printf("紅%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);// 紅寶牌
						} else {
							majongimagetemp.add(new JLabel(
									new ImageIcon(cardimagelist.get(c2 / 36).get((c2 / 4) - (9 * (c2 / 36))))));
							System.out.printf("%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);
						} // 除字牌及紅寶牌以外的牌
					} else if (c2 == 136) {
						majongimagetemp.add(new JLabel(new ImageIcon(cardimagelist.get(4).get(0))));// 空白牌
						System.out.print("測試 ");// 空
					} else {
						majongimagetemp.add(new JLabel(new ImageIcon(cardimagelist.get(3).get((c2 - 108) / 4))));
						System.out.printf("%s ", type1[(c2 - 108) / 4]);// 字牌
					}
				}
				jlabel.add(majongimagetemp);
				System.out.println();
			} // 定義手牌麻將牌名稱後印牌且照花色排序
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void reicon(LinkedList<LinkedList<Integer>> list, LinkedList<LinkedList<JLabel>> jlabel, boolean sort) {
		String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] type = { "萬", "筒", "條", };
		String[] type1 = { "東", "南", "西", "北", "白", "發", "中" };
		if (sort) {
			sortcard();
		}

		int i = 0;
		int j = 0;
		try {
			for (LinkedList<Integer> c1 : list) {
				j = 0;
//				System.out.println("TEST");
				for (int c2 : c1) {
					if (c2 < 108) {
						if (c2 == 19 || c2 == 55 || c2 == 91) {
							ImageIcon icon = new ImageIcon(cardimagelist.get(c2 / 36).get(9));
							jlabel.get(i).get(j).setIcon(icon);
//							System.out.printf("紅%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);// 紅寶牌
						} else {
							ImageIcon icon = new ImageIcon(cardimagelist.get(c2 / 36).get((c2 / 4) - (9 * (c2 / 36))));
							jlabel.get(i).get(j).setIcon(icon);
//							System.out.printf("%s%s ", number[(c2 / 4) - (9 * (c2 / 36))], type[c2 / 36]);
						} // 除字牌及紅寶牌以外的牌
					} else if (c2 == 136) {
						ImageIcon icon = new ImageIcon(cardimagelist.get(4).get(0));// 空白牌
						jlabel.get(i).get(j).setIcon(icon);
//						System.out.print("測試 ");// 空
					} else {
						ImageIcon icon = new ImageIcon(cardimagelist.get(3).get((c2 - 108) / 4));
						jlabel.get(i).get(j).setIcon(icon);
//						System.out.printf("%s ", type1[(c2 - 108) / 4]);// 字牌
					}
					j++;
				}
				i++;
//				System.out.println();
			} // 定義手牌麻將牌名稱後印牌且照花色排序
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void gameover() {
		int i = 0;
		for (LinkedList<Integer> m : mountainList) {
			i = i + m.size();
		}
		if (i <= 14) {
			System.out.println("流局!");
			whoplay = 4;
		}
	}// 判斷流局

	public void setriverlist() {
		for (int i = 0; i < 4; i++) {
			LinkedList<Integer> river = new LinkedList<>();
			for (int j = 0; j < 30; j++) {
				river.add(136);
			}
			riverList.add(river);
		} // 初始化4家牌河的List
		setcardimage(riverList, riverimage, false);
	}

	public void cardriver(int i, int j) {
		riverList.get(i).set(rivercount[i], cardList.get(i).get(j));
		rivercount[i]++;// 用於更新牌河
		reicon(riverList, riverimage, false);
//		System.out.println(riverimage.get(i));
	}

	public void setponchowlist() {
		for (int i = 0; i < 4; i++) {
			LinkedList<LinkedList<Integer>> ponchow1 = new LinkedList<>();
			for (int j = 0; j < 4; j++) {
				LinkedList<Integer> ponchow = new LinkedList<>();
				for (int k = 0; k < 4; k++) {
					ponchow.add(136);
				} // 0~4張牌
				ponchow1.add(ponchow);
			} // 0~4個吃碰
			pongchowlist.add(ponchow1);
		} // 0~4玩家位置
//		System.out.println(pongchowlist);
//		System.out.println(pongchowlist.get(0));
//		System.out.println(pongchowlist.get(0).get(0));
//		System.out.println(pongchowlist.get(0).get(0).get(0));
		for (int i = 0; i < 4; i++) {
			LinkedList<LinkedList<JLabel>> pongchowtemp = new LinkedList<>();
			setcardimage(pongchowlist.get(i), pongchowtemp, false);
			pongchowimage.add(pongchowtemp);
		}
	}// 擺放吃碰用的List

	public void pontest(int playeri) {
		int count = 0;
		for (int i = 0; i < cardList.size(); i++) {
			for (int j = 0; j < cardList.get(i).size(); j++) {
				System.out.print(cardList.get(i).get(j) + ",");
			}
			System.out.println();
		}
		System.out.println("----------------------------↑cardlist↑-------------------");
		int[] temp = new int[2];
		for (int i = 0; i < cardList.get(playeri).size(); i++) {
			System.out.println(cardList.get(playeri).get(i));
			System.out.println("----------------");
			System.out.print(cardList.get(playeri).get(i) / 4 + " ");
			System.out.println(nowplaycard / 4);
			if (cardList.get(playeri).get(i) / 4 == nowplaycard / 4) {
				pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
				temp[count] = i;
				count++;
				System.out.println("123");
			}
			if (count == 2) {
				break;
			}
		} // 8:00 從這開始改
		cardList.get(playeri).remove(temp[0]);
		cardList.get(playeri).remove(temp[1] - 1);
		cardList.get(playeri).add(136);
		cardList.get(playeri).add(136);
		pongchowlist.get(playeri).get(pccount[playeri]).set(count, nowplaycard);
		pccount[playeri]++;// 記數該玩家有幾個碰吃
		riverList.get(whoplay).set(rivercount[whoplay] - 1, 136);
		rivercount[whoplay]--;
		reicon(cardList, majongimage, true);
		reicon(riverList, riverimage, false);
		reicon(pongchowlist.get(playeri), pongchowimage.get(playeri), false);
		whoplay = playeri;
	}

	public void chowtest(int playeri, int chowj) {
		int count = 0;
		int max, min;
		min = ((nowplaycard / 4) / 9) * 9;// 判斷該花色最小值
		max = min + 8;//// 判斷該花色最大值
		if (max == 35) {
			max = 33;
		} // 處理字牌最大值
		for (int i = 0; i < cardList.size(); i++) {
			for (int j = 0; j < cardList.get(i).size(); j++) {
				System.out.print(cardList.get(i).get(j) + ",");
			}
			System.out.println();
		}
		System.out.println("----------------------------↑cardlist↑-------------------");
		int[] temp = new int[2];
		for (int i = 0; i < cardList.get(playeri).size(); i++) {
			if (chowj == 0) {
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) - 2 && count == 0) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) - 1 && count == 1) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
			} else if (chowj == 1) {
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) - 1 && count == 0) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) + 1 && count == 1) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
			} else if (chowj == 2) {
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) + 1 && count == 0) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
				if (cardList.get(playeri).get(i) / 4 == (nowplaycard / 4) + 2 && count == 1) {
					pongchowlist.get(playeri).get(pccount[playeri]).set(count, cardList.get(playeri).get(i));
					temp[count] = i;
					count++;
				}
			}

		} // 8:00 從這開始改
		cardList.get(playeri).remove(temp[0]);
		cardList.get(playeri).remove(temp[1] - 1);
		cardList.get(playeri).add(136);
		cardList.get(playeri).add(136);
		pongchowlist.get(playeri).get(pccount[playeri]).set(count, nowplaycard);
		pccount[playeri]++;// 記數該玩家有幾個碰吃
		riverList.get(whoplay).set(rivercount[whoplay] - 1, 136);
		rivercount[whoplay]--;
		reicon(cardList, majongimage, true);
		reicon(riverList, riverimage, false);
		reicon(pongchowlist.get(playeri), pongchowimage.get(playeri), false);
		whoplay = playeri;
	}

	public LinkedList<LinkedList<JLabel>> getponchowimage(int position) {
		return pongchowimage.get(position);
	}

	private void canpon(int card, LinkedList<LinkedList<Integer>> cardlist, int whoplay) {
		int[][] numberAndType = new int[4][34];// [0~8]=1~9萬，[9~17]=1~9筒，[18~26]=1~9條，[27~33]=東南西北白發中
		ponck = new boolean[4];
		for (int i = 0; i < cardlist.size(); i++) {
			if (i != whoplay) {
				for (int j = 0; j < cardList.get(i).size(); j++) {
					if (cardList.get(i).get(j) != 136) {
						numberAndType[i][(cardList.get(i).get(j) / 4)]++;
					} // 不判斷空白牌
				} // 把cardList整理成numberAndType的新形式供後續判斷
				if (++numberAndType[i][card / 4] >= 3) {
					ponck[i] = true;
				} // >=3代表可以碰
//				System.out.println(numberAndType[i]);
				System.out.println("玩家" + i + "可不可以碰:" + ponck[i]);
				System.out.println("------------");
			}
		} // ponck為1代表該玩家可碰，0則代表不能碰
	}// ok

	public boolean[] getponck() {
		return ponck;
	}

	private void canchow(int card, LinkedList<LinkedList<Integer>> cardlist, int whoplay) {
		int[][] numberAndType = new int[4][34];// [0~8]=1~9萬，[9~17]=1~9筒，[18~26]=1~9條，[27~33]=東南西北白發中
		chowpossible = new boolean[3];
		chowck = new boolean[4];
		int max, min;
		min = ((card / 4) / 9) * 9;// 判斷該花色最小值
		max = min + 8;//// 判斷該花色最大值
		if (max == 35) {
			max = 33;
		} // 處理字牌最大值

		if (card < 108) {
			// 字牌不用判斷能不能吃
			for (int i = 0; i < cardlist.size(); i++) {
				if (i == (whoplay + 3) % 4) {
					for (int j = 0; j < cardList.get(i).size(); j++) {
						if (cardList.get(i).get(j) != 136) {
							numberAndType[i][(cardList.get(i).get(j) / 4)]++;
						} // 不判斷空白牌
					} // 把cardList整理成numberAndType的新形式供後續判斷
					try {
						if ((numberAndType[i][(card / 4) - 1] > 0 && ((card / 4) - 1) > min)
								&& (numberAndType[i][(card / 4) - 2] > 0 && ((card / 4) - 2) >= min)) {
							chowck[i] = true;// 往前數兩張可不可以吃
							chowpossible[0] = true;
							System.out.println("1");
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println(e + "陣列1");
					}
					try {
						if ((numberAndType[i][(card / 4) - 1] > 0 && ((card / 4) - 1) >= min)
								&& (numberAndType[i][(card / 4) + 1] > 0 && ((card / 4) - 2) <= max)) {
							chowck[i] = true;// 往前後各數一張可不可以吃
							chowpossible[1] = true;
							System.out.println("2");
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println(e + "陣列2");
					}
					try {
						if ((numberAndType[i][(card / 4) + 1] > 0 && ((card / 4) + 1) < max)
								&& (numberAndType[i][(card / 4) + 2] > 0 && ((card / 4) + 2) <= max)) {
							chowck[i] = true;// 往後數兩張可不可以吃
							chowpossible[2] = true;
							System.out.println("3");
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println(e + "陣列3");
					}
//					System.out.println(numberAndType[i]);
					System.out.println("玩家" + i + "可不可以吃:" + chowck[i]);
					System.out.println("------------");
				}
			}
		} // chowck為1代表該玩家可碰，0則代表不能碰
	}// ok

	public boolean[] getchowpossible() {
		return chowpossible;
	}

	public boolean[] getchowck() {
		return chowck;
	}

	public void playing(int i, int j) {
		nowplaycard = cardList.get(i).get(j);
		cardriver(i, j);// 打出的牌增加至牌河
		cardList.get(i).remove(j);// 刪除點擊的牌
		cardList.get(i).add(136);// 增加一個空白牌
		canpon(nowplaycard, cardList, whoplay);
		canchow(nowplaycard, cardList, whoplay);
//		chose(canpon(cardtemp,cardList,whoplay),canchow(cardtemp,cardList,whoplay),cangun(cardtemp,cardList,whoplay),canhu(cardtemp,cardList,whoplay),whoplay);//要增加判斷吃碰胡的方法

	}

	public void nextcard(int i, int j) {
		whoplay = (whoplay + 3) % 4;// 判斷輪到誰出牌
		cardList.get(whoplay).removeLast();// 移除空白牌
		reicon(cardList, majongimage, true);// 重新印ICON
		try {
			cardList.get(whoplay).add(mountainList.get(mfc).getFirst());
			mountainList.get(mfc).removeFirst();
		} catch (NoSuchElementException e) {
			System.out.println(e + " 換下一排牌山");
			mfc++;
			mfc = mfc % 4;
			cardList.get(whoplay).add(mountainList.get(mfc).getFirst());
			mountainList.get(mfc).removeFirst();
		}
		reicon(cardList, majongimage, false);// 重新印ICON
		gameover();// 判斷流局
	}

	// 牌山的資料結構要在發牌後remove，之後打牌也要隨時remove done
	// 圖片的lintener要用jlabel設為image實現
}
