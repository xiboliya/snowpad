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

package com.xiboliya.snowpad.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CurrentLine;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "转到"对话框
 * 
 * @author 冰原
 * 
 */
public class GotoDialog extends BaseDialog implements ActionListener, ChangeListener {
  private static final long serialVersionUID = 1L;
  private JTabbedPane tpnMain = new JTabbedPane();
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlBottom = new JPanel();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private boolean percentChanged = false; // 用于标识是否移动过百分比的滑块
  private BaseButton btnGoto = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  // 行号
  private JPanel pnlLine = new JPanel();
  private JLabel lblCurLine = new JLabel();
  private JLabel lblEndLine = new JLabel();
  private JLabel lblGotoLine = new JLabel("转到行号：");
  private BaseTextField txtGotoLine = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  // 偏移量
  private JPanel pnlOffset = new JPanel();
  private JLabel lblCurOffset = new JLabel();
  private JLabel lblEndOffset = new JLabel();
  private JLabel lblGotoOffset = new JLabel("转到偏移量：");
  private BaseTextField txtGotoOffset = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  // 百分比
  private JSlider sldPercent = new JSlider();
  private JPanel pnlPercent = new JPanel();
  private JLabel lblGotoPercent = new JLabel("转到百分比：");
  private BaseTextField txtGotoPercent = new BaseTextField();

  public GotoDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("转到");
    this.init();
    this.updateView();
    this.addListeners();
    this.setSize(240, 195);
    this.setVisible(true);
  }

  private void init() {
    this.pnlMain.setLayout(null);
    // 行号
    this.pnlLine.setLayout(null);
    this.lblCurLine.setBounds(15, 10, 220, Util.VIEW_HEIGHT);
    this.lblEndLine.setBounds(15, 35, 220, Util.VIEW_HEIGHT);
    this.pnlLine.add(this.lblCurLine);
    this.pnlLine.add(this.lblEndLine);
    this.lblGotoLine.setBounds(15, 60, 80, Util.VIEW_HEIGHT);
    this.txtGotoLine.setBounds(95, 58, 100, Util.INPUT_HEIGHT);
    this.pnlLine.add(this.lblGotoLine);
    this.pnlLine.add(this.txtGotoLine);
    // 偏移量
    this.pnlOffset.setLayout(null);
    this.lblCurOffset.setBounds(15, 10, 220, Util.VIEW_HEIGHT);
    this.lblEndOffset.setBounds(15, 35, 220, Util.VIEW_HEIGHT);
    this.pnlOffset.add(this.lblCurOffset);
    this.pnlOffset.add(this.lblEndOffset);
    this.lblGotoOffset.setBounds(15, 60, 80, Util.VIEW_HEIGHT);
    this.txtGotoOffset.setBounds(95, 58, 100, Util.INPUT_HEIGHT);
    this.pnlOffset.add(this.lblGotoOffset);
    this.pnlOffset.add(this.txtGotoOffset);
    // 百分比
    this.pnlPercent.setLayout(null);
    this.sldPercent.setBounds(20, 10, 200, 35);
    this.lblGotoPercent.setBounds(15, 60, 80, Util.VIEW_HEIGHT);
    this.txtGotoPercent.setBounds(95, 58, 100, Util.INPUT_HEIGHT);
    this.txtGotoPercent.setEditable(false);
    this.txtGotoPercent.setFocusable(false);
    this.pnlPercent.add(this.sldPercent);
    this.pnlPercent.add(this.lblGotoPercent);
    this.pnlPercent.add(this.txtGotoPercent);
    // 按钮
    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 130, 240, 65);
    this.btnGoto.setBounds(25, 5, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(128, 5, 85, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnGoto);
    this.pnlBottom.add(this.btnCancel);

    this.tpnMain.setBounds(0, 0, 240, 130);
    this.tpnMain.add(this.pnlLine, "行号");
    this.tpnMain.add(this.pnlOffset, "偏移量");
    this.tpnMain.add(this.pnlPercent, "百分比");
    this.pnlMain.add(this.tpnMain);
    this.pnlMain.add(this.pnlBottom);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
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
   * 设置选项卡的当前视图
   * 
   * @param index 视图的索引号
   */
  private void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  private int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 更新当前和结尾状态的显示，包括：行号、偏移量或百分比
   */
  private void updateView() {
    CurrentLine currentLine = new CurrentLine(this.txaSource);
    int total = this.txaSource.getText().length();
    int lineNum = currentLine.getLineNum() + 1;
    this.lblCurLine.setText("当前行号：" + lineNum);
    this.lblEndLine.setText("结尾行号：" + this.txaSource.getLineCount());
    int currentIndex = currentLine.getCurrentIndex();
    this.lblCurOffset.setText("当前偏移量：" + currentIndex);
    this.lblEndOffset.setText("结尾偏移量：" + total);
    this.sldPercent.setValue(currentIndex * 100 / total);
    this.percentChanged = false;
    this.txtGotoPercent.setText(this.sldPercent.getValue() + "%");
  }

  private void addListeners() {
    this.btnGoto.addActionListener(this);
    this.btnGoto.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.txtGotoLine.addKeyListener(this.keyAdapter);
    this.txtGotoOffset.addKeyListener(this.keyAdapter);
    this.sldPercent.addChangeListener(this);
    this.sldPercent.addKeyListener(this.keyAdapter);
  }

  /**
   * "取消"按钮的处理方法
   */
  private void cancelGoto() {
    this.dispose();
    this.txtGotoLine.setText("");
    this.txtGotoOffset.setText("");
  }

  /**
   * 转到指定行号
   */
  private void gotoLine() {
    int total = this.txaSource.getLineCount(); // 文本域总行数
    String str = this.txtGotoLine.getText().trim();
    if (Util.isTextEmpty(str)) {
      TipsWindow.show(this, "行号不能为空！");
      this.txtGotoLine.requestFocus();
      return;
    }
    int target = 1; // 指定的行号
    try {
      target = Integer.parseInt(str);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请输入数值！");
      this.txtGotoLine.requestFocus();
      this.txtGotoLine.selectAll();
      return;
    }
    if (target <= 0) {
      TipsWindow.show(this, "行号必须大于0！");
    } else if (target > total) {
      TipsWindow.show(this, "行号超出范围！");
    } else {
      try {
        // 获取指定行起始处的偏移量，指定行号的取值范围：x>=0 && x<文本域总行数
        int offset = this.txaSource.getLineStartOffset(target - 1);
        this.txaSource.setCaretPosition(offset);
      } catch (BadLocationException x) {
        // x.printStackTrace();
      }
      this.cancelGoto();
    }
    this.txtGotoLine.requestFocus();
    this.txtGotoLine.selectAll();
  }

  /**
   * 转到指定偏移量
   */
  private void gotoOffset() {
    int total = this.txaSource.getText().length(); // 文本域总偏移量
    String str = this.txtGotoOffset.getText().trim();
    if (Util.isTextEmpty(str)) {
      TipsWindow.show(this, "偏移量不能为空！");
      this.txtGotoOffset.requestFocus();
      return;
    }
    int target = 0; // 指定的偏移量
    try {
      target = Integer.parseInt(str);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请输入数值！");
      this.txtGotoOffset.requestFocus();
      this.txtGotoOffset.selectAll();
      return;
    }
    if (target < 0) {
      TipsWindow.show(this, "偏移量必须大于等于0！");
    } else if (target > total) {
      TipsWindow.show(this, "偏移量超出范围！");
    } else {
      this.txaSource.setCaretPosition(target);
      this.cancelGoto();
    }
    this.txtGotoOffset.requestFocus();
    this.txtGotoOffset.selectAll();
  }

  /**
   * 转到指定百分比
   */
  private void gotoPercent() {
    if (percentChanged) {
      int total = this.txaSource.getText().length(); // 文本域总偏移量
      int target = this.sldPercent.getValue(); // 指定的百分比
      int offset = total * target / 100;
      this.txaSource.setCaretPosition(offset);
    }
    this.cancelGoto();
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnCancel.equals(source)) {
      this.onCancel();
    } else if (this.btnGoto.equals(source)) {
      this.onEnter();
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (this.sldPercent.equals(e.getSource())) {
      this.percentChanged = true;
      this.txtGotoPercent.setText(this.sldPercent.getValue() + "%");
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    if (this.getTabbedIndex() == 0) {
      this.gotoLine();
    } else if (this.getTabbedIndex() == 1) {
      this.gotoOffset();
    } else {
      this.gotoPercent();
    }
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.cancelGoto();
  }
}
