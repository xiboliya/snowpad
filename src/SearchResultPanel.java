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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.util.LinkedList;

/**
 * �Զ��������������
 * 
 * @author ��ԭ
 * 
 */
public class SearchResultPanel extends JPanel implements CaretListener {
  private static final long serialVersionUID = 1L;
  private SearchResult searchResult;
  private String filePath = "";
  private JLabel lblTitleText = new JLabel("���ҽ��");
  private JButton btnTitleClose = new JButton();
  private BaseTextArea txaMain = new BaseTextArea();
  private JScrollPane srpMain = new JScrollPane(this.txaMain);
  private JPanel pnlTitle = new JPanel();
  private BorderLayout layoutTitle = new BorderLayout();
  private BorderLayout layout = new BorderLayout();
  private SnowPadFrame owner;
  private Color color = new Color(0, 0, 0, 0);

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
  }

  /**
   * ˢ�²��ҽ��
   */
  public void refreshResult() {
    if (this.searchResult != null) {
      BaseTextArea txaMain = this.searchResult.getTxaMain();
      String strSearch = this.searchResult.getStrSearch();
      LinkedList<SearchBean> listIndex = this.searchResult.getListIndex();
      int count = -1;
      if (!listIndex.isEmpty()) {
        count = listIndex.size();
      }
      String strSource = "";
      if (txaMain != null) {
        filePath = txaMain.getFileName();
        if (Util.isTextEmpty(filePath)) {
          filePath = txaMain.getTitle();
        }
        strSource = txaMain.getText();
      }
      String strMain = "���� " + "\"" + strSearch + "\" [" + count + " ��]\n" + filePath + "\n";
      try {
        for (SearchBean searchBean : listIndex) {
          int lineNum = txaMain.getLineOfOffset(searchBean.getStart());
          String strLine = strSource.substring(txaMain.getLineStartOffset(lineNum), txaMain.getLineEndOffset(lineNum));
          if (!strLine.endsWith("\n")) {
            strLine += "\n";
          }
          strMain += "Line " + (lineNum + 1) + ":" + strLine;
        }
        this.txaMain.setText(strMain);
      } catch (BadLocationException x) {
         x.printStackTrace();
      }
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
    int lineNum = currentLine.getLineNum();
    if (strLine.startsWith("Line ")) {
      SearchBean searchBean = this.searchResult.getListIndex().get(lineNum - 2);
      int start = searchBean.getStart();
      int end = searchBean.getEnd();
      BaseTextArea textArea = this.searchResult.getTxaMain();
      textArea.select(start, end);
      textArea.requestFocus();
      this.owner.searchResultToSwitchFile(filePath);
    }
  }

  public void setSearchResult(SearchResult searchResult) {
    this.searchResult = searchResult;
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
   * ���ı����еĹ��仯ʱ�����������¼�
   */
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // �ػ浱ǰ�ı����Խ�����ض�����»��Ƶ�ǰ�б������ҵ�����
  }

}
