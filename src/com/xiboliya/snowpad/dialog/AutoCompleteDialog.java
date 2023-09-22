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

package com.xiboliya.snowpad.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.setting.Setting;
import com.xiboliya.snowpad.util.Util;

/**
 * "自动完成"对话框
 * 
 * @author 冰原
 * 
 */
public class AutoCompleteDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private Setting setting = null; // 软件参数配置类
  private JCheckBox chkEnable = new JCheckBox("开启自动完成(E)", false);
  private JTextArea txaView = new JTextArea();
  private BaseButton btnClose = new BaseButton("关闭");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private boolean isAutoComplete = false; // 自动完成

  /**
   * 构造方法
   * 
   * @param owner 父窗口
   * @param modal 是否为模式窗口
   * @param setting 软件参数配置类
   */
  public AutoCompleteDialog(JFrame owner, boolean modal, Setting setting) {
    super(owner, modal);
    this.setting = setting;
    this.setTitle("自动完成");
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(160, 190);
    this.setVisible(true);
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
   * 初始化界面
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.chkEnable.setBounds(15, 10, 120, Util.VIEW_HEIGHT);
    this.chkEnable.setMnemonic('E');
    this.txaView.setBounds(20, 45, 110, 70);
    this.txaView.setBorder(new EtchedBorder());
    this.txaView.setOpaque(true);
    this.txaView.setEditable(false);
    this.txaView.setFocusable(false);
    this.btnClose.setBounds(30, 125, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.chkEnable);
    this.pnlMain.add(this.txaView);
    this.pnlMain.add(this.btnClose);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.isAutoComplete = this.setting.autoComplete;
    this.chkEnable.setSelected(this.isAutoComplete);
    String strView = "";
    for (int i = 0; i < Util.AUTO_COMPLETE_BRACKETS_LEFT.length(); i++) {
      strView += " " + Util.AUTO_COMPLETE_BRACKETS_LEFT.charAt(i) + " " + Util.AUTO_COMPLETE_BRACKETS_RIGHT.charAt(i);
      if ((i % 2) == 0) {
        strView += "    ";
      } else {
        strView += "\n";
      }
    }
    this.txaView.setText(strView);
    this.setAutoComplete(this.isAutoComplete);
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.btnClose.addActionListener(this);
    this.chkEnable.addActionListener(this);
    // 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭对话框
    this.chkEnable.addKeyListener(this.keyAdapter);
    this.btnClose.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 获取是否自动完成
   */
  public boolean getAutoComplete() {
    return this.isAutoComplete;
  }

  /**
   * 设置是否自动完成
   */
  public void setAutoComplete(boolean isAutoComplete) {
    this.isAutoComplete = isAutoComplete;
    this.txaView.setEnabled(this.isAutoComplete);
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.onCancel();
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnClose.equals(source)) {
      this.onCancel();
    } else if (this.chkEnable.equals(source)) {
      this.setAutoComplete(this.chkEnable.isSelected());
    }
  }

}
