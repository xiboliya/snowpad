/**
 * Copyright (C) 2013 冰原
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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * 批处理"插入"对话框
 * 
 * @author 冰原
 * 
 */
public class BatchInsertDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblInsert = new JLabel("插入：");
  private BaseTextField txtInsert = new BaseTextField();
  private JRadioButton radLineStart = new JRadioButton("行首(S)", false);
  private JRadioButton radLineEnd = new JRadioButton("行尾(E)", true);
  private JPanel pnlLineStartEnd = new JPanel(new GridLayout(2, 1));
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private ButtonGroup bgpLineStartEnd = new ButtonGroup();

  public BatchInsertDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.addListeners();
    this.setSize(220, 150);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.txtInsert.setText("");
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("插入文本");
    this.pnlMain.setLayout(null);
    this.lblInsert.setBounds(20, 10, 50, Util.VIEW_HEIGHT);
    this.txtInsert.setBounds(70, 10, 130, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblInsert);
    this.pnlMain.add(this.txtInsert);
    this.pnlLineStartEnd.setBounds(10, 40, 95, 70);
    this.pnlLineStartEnd.setBorder(new TitledBorder("插入位置"));
    this.pnlLineStartEnd.add(this.radLineStart);
    this.pnlLineStartEnd.add(this.radLineEnd);
    this.pnlMain.add(this.pnlLineStartEnd);
    this.radLineStart.setMnemonic('S');
    this.radLineEnd.setMnemonic('E');
    this.bgpLineStartEnd.add(radLineStart);
    this.bgpLineStartEnd.add(radLineEnd);
    this.radLineStart.setSelected(true);
    this.btnOk.setBounds(115, 50, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(115, 85, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.radLineStart.addKeyListener(this.keyAdapter);
    this.radLineEnd.addKeyListener(this.keyAdapter);
    this.txtInsert.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 插入字符
   */
  private void insertText() {
    String strInsert = this.txtInsert.getText();
    if (Util.isTextEmpty(strInsert)) {
      JOptionPane.showMessageDialog(this, "请输入插入的文本！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtInsert.requestFocus();
    } else {
      this.toInsertText(strInsert);
      this.dispose();
    }
  }

  /**
   * 在行首/行尾插入字符串
   * 
   * @param strInsert
   *          在行首/行尾插入的字符串
   */
  private void toInsertText(String strInsert) {
    CurrentLines currentLines = new CurrentLines(this.txaSource);
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum();
    String strContent = currentLines.getStrContent();
    boolean isEndLine = false; // 用于标识选区内结尾行是否为文本域末行，如果是末行则为true
    if (this.txaSource.getLineCount() == endLineNum + 1) {
      isEndLine = true;
    }
    if (strContent.endsWith("\n")) {
      strContent = strContent.substring(0, strContent.length() - 1);
    }
    String[] arrText = strContent.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    boolean isLineStart = this.radLineStart.isSelected();
    StringBuilder stbText = new StringBuilder();
    for (int n = 0; n < arrText.length; n++) {
      if (isLineStart) {
        arrText[n] = strInsert + arrText[n];
      } else {
        arrText[n] = arrText[n] + strInsert;
      }
      stbText.append(arrText[n] + "\n");
    }
    if (isEndLine) {
      stbText.deleteCharAt(stbText.length() - 1);
    }
    this.txaSource.replaceRange(stbText.toString(), startIndex, endIndex);
    endIndex = startIndex + stbText.length() - 1; // 插入文本后，当前选区内末行的行尾偏移量
    if (this.txaSource.getText().length() == endIndex + 1) {
      endIndex++;
    }
    this.txaSource.select(startIndex, endIndex);
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.insertText();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
