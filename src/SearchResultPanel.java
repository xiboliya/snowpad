/**
 * Copyright (C) 2018 冰原
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
 * 自定义的搜索结果面板
 * 
 * @author 冰原
 * 
 */
public class SearchResultPanel extends JPanel implements CaretListener {
  private static final long serialVersionUID = 1L;
  private SearchResult searchResult;
  private String filePath = "";
  private JLabel lblTitleText = new JLabel("查找结果");
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
   * 初始化
   */
  private void init() {
    this.setLayout(this.layout);
    this.pnlTitle.setLayout(this.layoutTitle);
    this.pnlTitle.setBorder(new EmptyBorder(0, 5, 0, 0));
    this.btnTitleClose.setMargin(new Insets(0, 0, 0, 0));
    this.btnTitleClose.setIcon(Util.CLOSE_ICON);
    this.pnlTitle.add(this.lblTitleText, BorderLayout.WEST);
    this.pnlTitle.add(this.btnTitleClose, BorderLayout.EAST);
    this.srpMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条
    this.srpMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); // 始终显示水平滚动条
    this.add(this.pnlTitle, BorderLayout.NORTH);
    this.add(this.srpMain, BorderLayout.CENTER);
    this.txaMain.setSelectionColor(this.color);
    this.txaMain.setEditable(false);
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.txaMain.addCaretListener(this);
    this.txaMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) { // 双击
          gotoResult();
        }
      }
    });
    this.txaMain.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // 回车键
          gotoResult();
        }
      }
    });
  }

  /**
   * 刷新查找结果
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
      String strMain = "查找 " + "\"" + strSearch + "\" [" + count + " 处]\n" + filePath + "\n";
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
   * 跳转到查找结果的当前行
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
   * 获取关闭按钮
   * 
   * @return 关闭按钮
   */
  public JButton getCloseButton() {
    return this.btnTitleClose;
  }

  /**
   * 当文本域中的光标变化时，将触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // 重绘当前文本域，以解决在特定情况下绘制当前行背景错乱的问题
  }

}
