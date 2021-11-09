/**
 * Copyright (C) 2018 ��ԭ
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.snowpad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.util.LinkedList;

/**
 * �Զ��������������
 * 
 * @author ��ԭ
 * 
 */
public class SearchResultPanel extends JPanel implements ActionListener, CaretListener {
  private static final long serialVersionUID = 1L;
  private LinkedList<SearchResult> searchResults = new LinkedList<SearchResult>();
  private JLabel lblTitleText = new JLabel("���ҽ��");
  private JButton btnTitleClose = new JButton();
  private BaseTextArea txaMain = new BaseTextArea();
  private JScrollPane srpMain = new JScrollPane(this.txaMain);
  private JPanel pnlTitle = new JPanel();
  private BorderLayout layoutTitle = new BorderLayout();
  private BorderLayout layout = new BorderLayout();
  private SnowPadFrame owner;
  private Color color = new Color(0, 0, 0, 0);
  private JPopupMenu popMenuMain = new JPopupMenu();
  private JMenuItem itemPopClear = new JMenuItem("��ս��(C)", 'C');

  public SearchResultPanel(SnowPadFrame owner) {
    this.owner = owner;
    this.init();
    this.addListeners();
  }

  /**
   * ��ʼ��
   */
  private void init() {
    this.setLayout(this.layout);
    this.pnlTitle.setLayout(this.layoutTitle);
    this.pnlTitle.setBorder(new EmptyBorder(0, 5, 0, 0));
    this.btnTitleClose.setMargin(new Insets(0, 0, 0, 0));
    this.btnTitleClose.setIcon(Util.CLOSE_ICON);
    this.pnlTitle.add(this.lblTitleText, BorderLayout.WEST);
    this.pnlTitle.add(this.btnTitleClose, BorderLayout.EAST);
    this.srpMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // ʼ����ʾ��ֱ������
    this.srpMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); // ʼ����ʾˮƽ������
    this.add(this.pnlTitle, BorderLayout.NORTH);
    this.add(this.srpMain, BorderLayout.CENTER);
    this.txaMain.setSelectionColor(this.color);
    this.txaMain.setEditable(false);
    this.addPopMenu();
  }

  /**
   * ��ʼ����ݲ˵�
   */
  private void addPopMenu() {
    this.popMenuMain.add(this.itemPopClear);
    Dimension popSize = this.popMenuMain.getPreferredSize();
    popSize.width += popSize.width / 5; // Ϊ�����ۣ��ʵ��ӿ�˵�����ʾ
    this.popMenuMain.setPopupSize(popSize);
  }

  /**
   * ��Ӹ�������¼�������
   */
  private void addListeners() {
    this.txaMain.addCaretListener(this);
    this.txaMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) { // ˫��
          gotoResult();
        } else if (e.getButton() == MouseEvent.BUTTON3) { // ����Ҽ�ʱ����ʾ��ݲ˵�
          popMenuMain.show(txaMain, e.getX(), e.getY());
        }
      }
    });
    this.txaMain.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // �س���
          gotoResult();
        }
      }
    });
    this.itemPopClear.addActionListener(this);
  }

  /**
   * ˢ�²��ҽ��
   */
  public void refreshResult(SearchResult searchResult) {
    this.searchResults.add(searchResult);
    BaseTextArea textArea = searchResult.getTextArea();
    String strSearch = searchResult.getStrSearch();
    LinkedList<SearchBean> listIndex = searchResult.getListIndex();
    int count = -1;
    if (!listIndex.isEmpty()) {
      count = listIndex.size();
    }
    String filePath = "";
    String strSource = "";
    if (textArea != null) {
      filePath = textArea.getFileName();
      if (Util.isTextEmpty(filePath)) {
        filePath = textArea.getTitle();
      }
      strSource = textArea.getText();
    }
    String strMain = "���� " + "\"" + strSearch + "\" [" + count + " ��]\n" + filePath + "\n";
    try {
      for (SearchBean searchBean : listIndex) {
        int lineNum = textArea.getLineOfOffset(searchBean.getStart());
        String strLine = strSource.substring(textArea.getLineStartOffset(lineNum), textArea.getLineEndOffset(lineNum));
        if (!strLine.endsWith("\n")) {
          strLine += "\n";
        }
        strMain += "Line " + (lineNum + 1) + ":" + strLine;
      }
      this.txaMain.append(strMain);
    } catch (Exception x) {
       // x.printStackTrace();
    }
    this.txaMain.setSelectedTextColor(this.txaMain.getForeground());
    this.txaMain.setCaretPosition(Util.DEFAULT_CARET_INDEX);
  }

  /**
   * ��ת�����ҽ���ĵ�ǰ��
   */
  private void gotoResult() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    String strLine = currentLine.getStrLine();
    if (strLine.startsWith("Line ")) {
      int lineNum = currentLine.getLineNum();
      int size = this.searchResults.size();
      int lineCount = 0;
      SearchResult searchResult = null;
      for (int i = 0; i < size; i++) {
        SearchResult searchResultTemp = this.searchResults.get(i);
        lineCount += (searchResultTemp.getListIndex().size() + 2);
        if (lineNum < lineCount) {
          searchResult = searchResultTemp;
          lineNum = searchResultTemp.getListIndex().size() - (lineCount - lineNum);
          break;
        }
      }
      SearchBean searchBean = searchResult.getListIndex().get(lineNum);
      int start = searchBean.getStart();
      int end = searchBean.getEnd();
      BaseTextArea textArea = searchResult.getTextArea();
      textArea.select(start, end);
      textArea.requestFocus();
      String filePath = textArea.getFileName();
      if (Util.isTextEmpty(filePath)) {
        filePath = textArea.getTitle();
      }
      this.owner.searchResultToSwitchFile(filePath);
    }
  }

  /**
   * ��ȡ�رհ�ť
   * 
   * @return �رհ�ť
   */
  public JButton getCloseButton() {
    return this.btnTitleClose;
  }

  /**
   * "��ս��"�Ĵ�����
   */
  private void clear() {
    this.txaMain.setText("");
  }

  /**
   * ���ı����еĹ��仯ʱ�����������¼�
   */
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // �ػ浱ǰ�ı����Խ�����ض�����»��Ƶ�ǰ�б������ҵ�����
  }

  /**
   * Ϊ���˵�������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.itemPopClear.equals(e.getSource())) {
      clear();
    }
  }
}
