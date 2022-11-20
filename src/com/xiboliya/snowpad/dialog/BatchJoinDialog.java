/**
 * Copyright (C) 2016 冰原
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * 批处理"拼接行"对话框
 * 
 * @author 冰原
 * 
 */
public class BatchJoinDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblLines = new JLabel("拼接行数：");
  private BaseTextField txtLines = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public BatchJoinDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.addListeners();
    this.setSize(220, 110);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.txtLines.setText("");
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("拼接行");
    this.pnlMain.setLayout(null);
    this.lblLines.setBounds(20, 10, 60, Util.VIEW_HEIGHT);
    this.txtLines.setBounds(80, 10, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblLines);
    this.pnlMain.add(this.txtLines);
    this.btnOk.setBounds(20, 50, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(115, 50, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtLines.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 拼接行
   */
  private void joinText() {
    int lines = 0;
    try {
      lines = Integer.parseInt(this.txtLines.getText().trim());
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请输入数字！");
      this.txtLines.requestFocus();
      this.txtLines.selectAll();
      return;
    }
    if (lines <= 0) {
      TipsWindow.show(this, "数值必须大于0！");
      this.txtLines.requestFocus();
      this.txtLines.selectAll();
    } else {
      this.toJoinText(lines);
      this.dispose();
    }
  }

  /**
   * 以指定的行数拼接行
   * 
   * @param lines
   *          拼接的行数
   */
  private void toJoinText(int lines) {
    CurrentLines currentLines = new CurrentLines(this.txaSource);
    String strContent = currentLines.getStrContent();
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum() + 1;
    String[] arrText = strContent.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    StringBuilder stbText = new StringBuilder();
    for (int n = 0; n < arrText.length; n++) {
      if (((n + 1) % lines) > 0) {
        stbText.append(arrText[n]);
      } else {
        stbText.append(arrText[n] + "\n");
      }
    }
    if (!stbText.toString().endsWith("\n")) {
      if (endLineNum != this.txaSource.getLineCount()) {
        stbText.append("\n");
      }
    } else {
      if (endLineNum == this.txaSource.getLineCount()) {
        stbText.deleteCharAt(stbText.length() - 1);
      }
    }
    this.txaSource.replaceRange(stbText.toString(), startIndex, endIndex);
    endIndex = startIndex + stbText.length() - 1; // 拼接行后，当前选区内末行的行尾偏移量
    if (this.txaSource.getText().length() == endIndex + 1) {
      endIndex++;
    }
    this.txaSource.select(startIndex, endIndex);
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.joinText();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
