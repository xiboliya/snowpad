/**
 * Copyright (C) 2023 冰原
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.setting.Setting;
import com.xiboliya.snowpad.util.Util;

/**
 * "首选项"对话框
 * 
 * @author 冰原
 * 
 */
public class PreferencesDialog extends BaseDialog implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  // 编码格式名称的数组
  private static final String[] ENCODING_NAMES = new String[] {
      CharEncoding.GB18030.getName(), CharEncoding.ANSI.getName(), CharEncoding.UTF8.getName(),
      CharEncoding.UTF8_NO_BOM.getName(), CharEncoding.ULE.getName(), CharEncoding.UBE.getName() };
  // 编码格式数据的数组
  private static final String[] ENCODING_VALUES = new String[] {
      CharEncoding.GB18030.getValue(), CharEncoding.ANSI.getValue(), CharEncoding.UTF8.getValue(),
      CharEncoding.UTF8_NO_BOM.getValue(), CharEncoding.ULE.getValue(), CharEncoding.UBE.getValue() };
  // 换行符格式名称的数组
  private static final String[] LINE_SEPARATOR_VALUES = new String[] {
      LineSeparator.DEFAULT.getValue(), LineSeparator.UNIX.getValue(), LineSeparator.MACINTOSH.getValue(), LineSeparator.WINDOWS.getValue() };
  private Setting setting = null; // 软件参数配置类
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private CharEncoding charEncoding = CharEncoding.GB18030; // 字符编码格式
  private LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  // 新建
  private JPanel pnlNew = new JPanel();
  private JLabel lblEncoding = new JLabel("默认字符编码：");
  private JComboBox<String> cmbEncoding = new JComboBox<String>(ENCODING_NAMES);
  private JLabel lblLineSeparator = new JLabel("默认换行符：");
  private JComboBox<String> cmbLineSeparator = new JComboBox<String>(LINE_SEPARATOR_VALUES);
  // 主界面
  private JPanel pnlBottom = new JPanel();
  private JButton btnCancel = new JButton("关闭");

  public PreferencesDialog(JFrame owner, boolean modal, Setting setting) {
    super(owner, modal);
    this.setting = setting;
    this.setTitle("首选项");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.setSize(400, 280);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    // 新建
    this.pnlNew.setLayout(null);
    this.lblEncoding.setBounds(10, 10, 100, Util.VIEW_HEIGHT);
    this.cmbEncoding.setBounds(110, 10, 150, Util.INPUT_HEIGHT);
    this.lblLineSeparator.setBounds(10, 50, 100, Util.VIEW_HEIGHT);
    this.cmbLineSeparator.setBounds(110, 50, 150, Util.INPUT_HEIGHT);
    this.pnlNew.add(this.lblEncoding);
    this.pnlNew.add(this.cmbEncoding);
    this.pnlNew.add(this.lblLineSeparator);
    this.pnlNew.add(this.cmbLineSeparator);
    // 主界面
    this.tpnMain.add(this.pnlNew, "新建");
    this.pnlMain.add(this.tpnMain, BorderLayout.CENTER);
    this.tpnMain.setSelectedIndex(0);
    this.tpnMain.setFocusable(false);
    this.btnCancel.setSize(100, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCancel);
    this.pnlMain.add(this.pnlBottom, BorderLayout.SOUTH);
  }

  /**
   * 初始化各选项的状态
   */
  private void initView() {
    this.charEncoding = this.setting.defaultCharEncoding;
    this.cmbEncoding.setSelectedItem(this.charEncoding.getName());
    this.lineSeparator = this.setting.defaultLineSeparator;
    this.cmbLineSeparator.setSelectedItem(this.lineSeparator.getValue());
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.cmbEncoding.addKeyListener(this.keyAdapter);
    this.cmbEncoding.addItemListener(this);
    this.cmbLineSeparator.addKeyListener(this.keyAdapter);
    this.cmbLineSeparator.addItemListener(this);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 设置字符编码格式
   */
  private void updateEncoding() {
    int index = this.cmbEncoding.getSelectedIndex();
    switch (index) {
      case 0:
        this.charEncoding = CharEncoding.GB18030;
        break;
      case 1:
        this.charEncoding = CharEncoding.ANSI;
        break;
      case 2:
        this.charEncoding = CharEncoding.UTF8;
        break;
      case 3:
        this.charEncoding = CharEncoding.UTF8_NO_BOM;
        break;
      case 4:
        this.charEncoding = CharEncoding.ULE;
        break;
      case 5:
        this.charEncoding = CharEncoding.UBE;
        break;
    }
    this.setting.defaultCharEncoding = this.charEncoding;
  }

  /**
   * 设置换行符格式
   */
  private void updateLineSeparator() {
    int index = this.cmbLineSeparator.getSelectedIndex();
    switch (index) {
      case 0:
        this.lineSeparator = LineSeparator.DEFAULT;
        break;
      case 1:
        this.lineSeparator = LineSeparator.UNIX;
        break;
      case 2:
        this.lineSeparator = LineSeparator.MACINTOSH;
        break;
      case 3:
        this.lineSeparator = LineSeparator.WINDOWS;
        break;
    }
    this.setting.defaultLineSeparator = this.lineSeparator;
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 默认的“取消”操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  @Override
  public void onEnter() {
  }

  /**
   * 当用户已选定或取消选定某项时，触发此事件
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    Object source = e.getSource();
    if (this.cmbEncoding.equals(source)) {
      this.updateEncoding();
    } else if (this.cmbLineSeparator.equals(source)) {
      this.updateLineSeparator();
    }
  }
}
