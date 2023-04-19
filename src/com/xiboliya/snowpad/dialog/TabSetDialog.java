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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "Tab字符设置"对话框
 * 
 * @author 冰原
 * 
 */
public class TabSetDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblTabSize = new JLabel("Tab键所占字符数：");
  private BaseTextField txtTabSize = new BaseTextField(true, "\\d{0,2}"); // 限制用户只能输入数字，并且不能超过2位
  private JCheckBox chkReplaceBySpace = new JCheckBox("以空格代替Tab键(R)", false);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private int tabSize = Util.DEFAULT_TABSIZE; // Tab键所占字符数
  private boolean isReplaceBySpace = false; // 以空格代替Tab键

  public TabSetDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(240, 130);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("Tab字符设置");
    this.pnlMain.setLayout(null);
    this.lblTabSize.setBounds(20, 10, 120, Util.VIEW_HEIGHT);
    this.txtTabSize.setBounds(140, 10, 50, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTabSize);
    this.pnlMain.add(this.txtTabSize);
    this.chkReplaceBySpace.setBounds(36, 40, 150, Util.VIEW_HEIGHT);
    this.chkReplaceBySpace.setMnemonic('R');
    this.pnlMain.add(this.chkReplaceBySpace);
    this.btnOk.setBounds(20, 65, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(130, 65, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.initView();
    }
    super.setVisible(visible);
  }

  /**
   * 初始化文本框
   */
  private void initView() {
    this.tabSize = this.txaSource.getTabSize();
    this.txtTabSize.setText(String.valueOf(this.tabSize));
    this.txtTabSize.selectAll();
    this.isReplaceBySpace = ((BaseTextArea) this.txaSource).getTabReplaceBySpace();
    this.chkReplaceBySpace.setSelected(this.isReplaceBySpace);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtTabSize.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.chkReplaceBySpace.addActionListener(this);
    this.chkReplaceBySpace.addKeyListener(this.keyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    } else if (this.chkReplaceBySpace.equals(source)) {
      this.setReplaceBySpace(this.chkReplaceBySpace.isSelected());
    }
  }

  /**
   * 设置Tab所占字符数
   */
  private void setTabSize() {
    int tabSize = Util.DEFAULT_TABSIZE;
    try {
      tabSize = Integer.parseInt(this.txtTabSize.getText().trim());
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请输入数字！");
      this.txtTabSize.setText("");
      return;
    }
    if (tabSize < Util.MIN_TABSIZE) {
      TipsWindow.show(this, "必须大于等于" + Util.MIN_TABSIZE + "！");
      this.txtTabSize.setText(String.valueOf(Util.MIN_TABSIZE));
    } else if (tabSize > Util.MAX_TABSIZE) {
      TipsWindow.show(this, "必须小于等于" + Util.MAX_TABSIZE + "！");
      this.txtTabSize.setText(String.valueOf(Util.MAX_TABSIZE));
    } else {
      this.tabSize = tabSize;
      this.dispose();
    }
  }

  /**
   * 获取Tab键所占字符数
   */
  public int getTabSize() {
    return this.tabSize;
  }

  /**
   * 获取是否以空格代替Tab键
   */
  public boolean getReplaceBySpace() {
    return this.isReplaceBySpace;
  }

  /**
   * 设置是否以空格代替Tab键
   */
  public void setReplaceBySpace(boolean isReplaceBySpace) {
    this.isReplaceBySpace = isReplaceBySpace;
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.setTabSize();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
