/**
 * Copyright (C) 2022 冰原
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

package com.xiboliya.snowpad.dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.common.CurrentLine;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "预览书签"对话框
 * 
 * @author 冰原
 * 
 */
public class BookmarkPreviewDialog extends BaseDialog implements ActionListener, CaretListener {
  private static final long serialVersionUID = 1L;
  private static final String prefixLine = "Line ";
  private JLabel lblMain = new JLabel("书签行：");
  private BaseTextArea txaMain = new BaseTextArea();
  private JScrollPane srpMain = new JScrollPane(this.txaMain);
  private JButton btnOk = new JButton("跳转");
  private JButton btnCancel = new JButton("取消");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Color color = new Color(0, 0, 0, 0);
  private LinkedList<Integer> bookmarks = null;

  public BookmarkPreviewDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("预览书签");
    this.init();
    this.updateView();
    this.addListeners();
    this.setSize(320, 300);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.updateView();
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.lblMain.setBounds(10, 10, 140, Util.VIEW_HEIGHT);
    this.srpMain.setBounds(10, 30, 300, 200);
    this.pnlMain.add(this.lblMain);
    this.pnlMain.add(this.srpMain);
    this.btnOk.setBounds(45, 240, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(185, 240, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.txaMain.setSelectionColor(this.color);
    this.txaMain.setSelectedTextColor(this.txaMain.getForeground());
    this.txaMain.setEditable(false);
  }

  /**
   * 更新显示
   */
  private void updateView() {
    StringBuilder stbLines = new StringBuilder();
    this.bookmarks = ((BaseTextArea)this.txaSource).getBookmarks();
    String text = this.txaSource.getText();
    int lineCount = this.txaSource.getLineCount();
    try {
      for (int bookmark : this.bookmarks) {
        if (bookmark < lineCount) {
          int start = this.txaSource.getLineStartOffset(bookmark);
          int end = this.txaSource.getLineEndOffset(bookmark);
          stbLines.append("Line " + (bookmark + 1) + ":" + text.substring(start, end));
          if (bookmark == lineCount - 1) {
            stbLines.append("\n");
          }
        } else {
          stbLines.append("[Line " + (bookmark + 1) + "]:" + "\n");
        }
      }
    } catch (BadLocationException x) {
      // x.printStackTrace();
    }
    if (stbLines.length() > 0) {
      stbLines.deleteCharAt(stbLines.length() - 1);
    }
    this.txaMain.setText(stbLines.toString());
    this.txaMain.setCaretPosition(0);
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.txaMain.addCaretListener(this);
    this.txaMain.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) { // 双击
          gotoResult();
        }
      }
    });
    this.btnCancel.addActionListener(this);
    this.btnOk.addActionListener(this);
    // 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭当前对话框
    this.txaMain.addKeyListener(this.keyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 跳转到当前书签的所在行
   */
  private void gotoResult() {
    if (this.bookmarks.isEmpty()) {
      TipsWindow.show(this, "当前文件没有书签，无法跳转！");
      return;
    }
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    String strLine = currentLine.getStrLine();
    int lineNum = currentLine.getLineNum();
    int bookmark = this.bookmarks.get(lineNum);
    if (strLine.startsWith(prefixLine)) {
      try {
        int offset = this.txaSource.getLineStartOffset(bookmark);
        this.txaSource.setCaretPosition(offset);
      } catch (BadLocationException x) {
        // x.printStackTrace();
      }
      this.dispose();
    } else {
      JOptionPane.showMessageDialog(this, "此书签的行号：" + (bookmark + 1) + "不存在，无法跳转！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }

  /**
   * "取消"按钮的处理方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * "确定"按钮的处理方法
   */
  @Override
  public void onEnter() {
    this.gotoResult();
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * 当文本域中的光标变化时，将触发此事件
   */
  @Override
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // 重绘当前文本域，以解决在特定情况下绘制当前行背景错乱的问题
  }
}
