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

package com.xiboliya.snowpad.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
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

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.common.CurrentLine;
import com.xiboliya.snowpad.common.SearchBean;
import com.xiboliya.snowpad.common.SearchResult;
import com.xiboliya.snowpad.common.SearchStyle;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.util.Util;

/**
 * 自定义的搜索结果面板
 * 
 * @author 冰原
 * 
 */
public class SearchResultPanel extends JPanel implements ActionListener, CaretListener {
  private static final long serialVersionUID = 1L;
  private static final String prefixLine = "Line ";
  private static final ImageIcon CLOSE_ICON = new ImageIcon(ClassLoader.getSystemResource("res/close.png")); // 关闭图标
  private LinkedList<SearchResult> searchResults = new LinkedList<SearchResult>();
  private JLabel lblTitleText = new JLabel("查找结果");
  private BaseButton btnTitleClose = new BaseButton();
  private BaseTextArea txaMain = new BaseTextArea();
  private JScrollPane srpMain = new JScrollPane(this.txaMain);
  private JPanel pnlTitle = new JPanel();
  private BorderLayout layoutTitle = new BorderLayout();
  private BorderLayout layout = new BorderLayout();
  private SnowPadFrame owner;
  private Color color = new Color(0, 0, 0, 0);
  private JPopupMenu popMenuMain = new JPopupMenu();
  private JMenuItem itemPopCopyCurrentLine = new JMenuItem("复制当前行(C)", 'C');
  private JMenuItem itemPopRemoveCurrent = new JMenuItem("移除当前结果(R)", 'R');
  private JMenuItem itemPopClear = new JMenuItem("清空结果(L)", 'L');

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
    this.btnTitleClose.setIcon(CLOSE_ICON);
    this.btnTitleClose.setFocusable(false);
    this.btnTitleClose.setToolTipText("关闭");
    this.pnlTitle.add(this.lblTitleText, BorderLayout.WEST);
    this.pnlTitle.add(this.btnTitleClose, BorderLayout.EAST);
    this.srpMain.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条
    this.srpMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); // 始终显示水平滚动条
    this.add(this.pnlTitle, BorderLayout.NORTH);
    this.add(this.srpMain, BorderLayout.CENTER);
    this.txaMain.setSelectionColor(this.color);
    this.txaMain.setEditable(false);
    this.addPopMenu();
  }

  /**
   * 初始化快捷菜单
   */
  private void addPopMenu() {
    this.popMenuMain.add(this.itemPopCopyCurrentLine);
    this.popMenuMain.add(this.itemPopRemoveCurrent);
    this.popMenuMain.add(this.itemPopClear);
    Dimension popSize = this.popMenuMain.getPreferredSize();
    popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuMain.setPopupSize(popSize);
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
        } else if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
          setPopMenuEnabled();
          popMenuMain.show(txaMain, e.getX(), e.getY());
        }
      }
    });
    this.txaMain.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) { // 回车键
          gotoResult();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // ESC键
          close();
        }
      }
    });
    this.itemPopCopyCurrentLine.addActionListener(this);
    this.itemPopRemoveCurrent.addActionListener(this);
    this.itemPopClear.addActionListener(this);
    this.btnTitleClose.addActionListener(this);
  }

  /**
   * 刷新查找结果
   */
  public void refreshResult(SearchResult searchResult) {
    // 倒序排列
    this.searchResults.add(0, searchResult);
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
    StringBuilder stbMain = new StringBuilder("查找 \"" + strSearch + "\" [" + count + " 处]\n" + filePath + "\n");
    try {
      for (SearchBean searchBean : listIndex) {
        int lineNum = textArea.getLineOfOffset(searchBean.getStart());
        StringBuilder stbLine = new StringBuilder(strSource.substring(textArea.getLineStartOffset(lineNum), textArea.getLineEndOffset(lineNum)));
        if (stbLine.charAt(stbLine.length() - 1) != '\n') {
          stbLine.append('\n');
        }
        stbMain.append(prefixLine + (lineNum + 1) + ":" + stbLine);
      }
      this.txaMain.insert(stbMain.toString(), 0);
    } catch (Exception x) {
       // x.printStackTrace();
    }
    this.txaMain.setSelectedTextColor(this.txaMain.getForeground());
    this.txaMain.setCaretPosition(0);
  }

  /**
   * 跳转到查找结果的当前行
   */
  private void gotoResult() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    String strLine = currentLine.getStrLine();
    if (strLine.startsWith(prefixLine)) {
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
      this.owner.searchResultToSwitchFile(textArea.hashCode());
    }
  }

  /**
   * 设置快捷菜单是否可用
   */
  private void setPopMenuEnabled() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    this.itemPopCopyCurrentLine.setEnabled(!Util.isTextEmpty(currentLine.getStrLine()));
    this.itemPopRemoveCurrent.setEnabled(!Util.isTextEmpty(currentLine.getStrLine()));
    this.itemPopClear.setEnabled(!Util.isTextEmpty(this.txaMain.getText()));
  }

  /**
   * 关闭面板
   */
  private void close() {
    this.owner.viewSearchResult(false);
  }

  /**
   * "清空结果"的处理方法
   */
  private void clear() {
    this.txaMain.setText("");
    this.searchResults.clear();
  }

  /**
   * "移除当前结果"的处理方法
   */
  private void removeCurrent() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int lineNum = currentLine.getLineNum();
    int size = this.searchResults.size();
    int oldLineCount = 0;
    int lineCount = 0;
    for (int i = 0; i < size; i++) {
      SearchResult searchResultTemp = this.searchResults.get(i);
      oldLineCount = lineCount;
      lineCount += (searchResultTemp.getListIndex().size() + 2);
      if (lineNum < lineCount) {
        try {
          this.txaMain.replaceRange("", this.txaMain.getLineStartOffset(oldLineCount),
              this.txaMain.getLineEndOffset(lineCount - 1));
        } catch (Exception x) {
          x.printStackTrace();
        }
        this.searchResults.remove(i);
        break;
      }
    }
  }

  /**
   * "复制当前行"的处理方法
   */
  private void copyCurrentLine() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    String strLine = currentLine.getStrLine();
    if (Util.isTextEmpty(strLine)) {
      return;
    }
    if (strLine.startsWith(prefixLine)) {
      int startIndex = currentLine.getStartIndex();
      Util.findText(prefixLine + "\\d+:", this.txaMain, startIndex, true, true, false, false, SearchStyle.PATTERN);
      strLine = strLine.substring(Util.matcher_length);
    }
    ListenerManager.getInstance().postClipboardEvent(strLine);
  }

  /**
   * 当文本域中的光标变化时，将触发此事件
   */
  @Override
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // 重绘当前文本域，以解决在特定情况下绘制当前行背景错乱的问题
  }

  /**
   * 为各菜单项添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.itemPopClear.equals(source)) {
      this.clear();
    } else if (this.itemPopRemoveCurrent.equals(source)) {
      this.removeCurrent();
    } else if (this.itemPopCopyCurrentLine.equals(source)) {
      this.copyCurrentLine();
    } else if (this.btnTitleClose.equals(source)) {
      this.close();
    }
  }
}
