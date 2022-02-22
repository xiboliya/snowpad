/**
 * Copyright (C) 2015 冰原
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * "快捷键编辑"对话框
 * 
 * @author 冰原
 * 
 */
public class ShortcutSetDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private Setting setting = null; // 软件参数配置类
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblName = new JLabel("功能名称：");
  private JLabel lblNameView = new JLabel();
  private JCheckBox chkCtrl = new JCheckBox(Util.CTRL);
  private JCheckBox chkAlt = new JCheckBox(Util.ALT);
  private JCheckBox chkShift = new JCheckBox(Util.SHIFT);
  private JCheckBox chkCommand = new JCheckBox(Util.COMMAND);
  private JComboBox<String> cmbShortcuts = null;
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private boolean isOk = false; // 用于标识是否点击了确定按钮
  private String keyName = ""; // 功能名称
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public ShortcutSetDialog(JDialog owner, boolean modal, Setting setting, String keyName) {
    super(owner, modal);
    this.setting = setting;
    this.keyName = keyName;
    this.init();
    this.addListeners();
    this.setSize(380, 150);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   * 
   * @param isVisible
   *          设置本窗口是否可见，如果可见则为true
   */
  public void setVisible(boolean isVisible) {
    if (isVisible) {
      this.isOk = false;
    }
    super.setVisible(isVisible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("快捷键编辑");
    this.pnlMain.setLayout(null);
    this.initShortcuts();
    this.refreshKeyView();
    this.lblName.setBounds(20, 10, 65, Util.VIEW_HEIGHT);
    this.lblNameView.setBounds(85, 10, 255, Util.VIEW_HEIGHT);
    this.chkCtrl.setBounds(10, 42, 60, Util.VIEW_HEIGHT);
    this.chkAlt.setBounds(75, 42, 60, Util.VIEW_HEIGHT);
    this.chkShift.setBounds(140, 42, 60, Util.VIEW_HEIGHT);
    this.chkCommand.setBounds(205, 42, 60, Util.VIEW_HEIGHT);
    this.cmbShortcuts.setBounds(270, 42, 90, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblName);
    this.pnlMain.add(this.lblNameView);
    this.pnlMain.add(this.chkCtrl);
    this.pnlMain.add(this.chkAlt);
    this.pnlMain.add(this.chkShift);
    this.pnlMain.add(this.chkCommand);
    this.pnlMain.add(this.cmbShortcuts);
    this.btnOk.setBounds(60, 80, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(225, 80, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.lblNameView.setBorder(new EtchedBorder());
  }

  /**
   * 初始化所有按键的显示
   */
  private void initShortcuts() {
    String[] strKeys = new String[Util.ALL_KEY_CODES.length + 1];
    strKeys[0] = Util.KEY_UNDEFINED; // 添加未定义的按键
    for (int i = 0; i < Util.ALL_KEY_CODES.length; i++) {
      strKeys[i + 1] = KeyEvent.getKeyText(Util.ALL_KEY_CODES[i]);
    }
    this.cmbShortcuts = new JComboBox<String>(strKeys);
  }

  /**
   * 刷新功能名称和快捷键的显示
   */
  private void refreshKeyView() {
    if (!Util.isTextEmpty(this.keyName)) {
      this.lblNameView.setText(this.keyName);
      String keyValue = this.setting.shortcutMap.get(this.keyName);
      boolean hasCtrl = false;
      boolean hasAlt = false;
      boolean hasShift = false;
      boolean hasCommand = false;
      String value = Util.KEY_UNDEFINED;
      if (!Util.isTextEmpty(keyValue)) {
        String[] arrKeys = keyValue.split("\\+");
        for (String str : arrKeys) {
          if (Util.CTRL.equalsIgnoreCase(str)) {
            hasCtrl = true;
          } else if (Util.ALT.equalsIgnoreCase(str)) {
            hasAlt = true;
          } else if (Util.SHIFT.equalsIgnoreCase(str)) {
            hasShift = true;
          } else if (Util.COMMAND.equalsIgnoreCase(str)) {
            hasCommand = true;
          } else { // 除控制键之外的按键
            String strKey = Util.transferKeyCode(str);
            if (!Util.isTextEmpty(strKey)) {
              value = strKey;
            }
          }
        }
      }
      this.chkCtrl.setSelected(hasCtrl);
      this.chkAlt.setSelected(hasAlt);
      this.chkShift.setSelected(hasShift);
      this.chkCommand.setSelected(hasCommand);
      this.cmbShortcuts.setSelectedItem(value);
    }
  }

  /**
   * 设置功能名称
   */
  public void setKeyName(String keyName) {
    this.keyName = keyName;
    this.refreshKeyView();
  }

  /**
   * 获取当前界面中设置的快捷键
   * 
   * @return 当前界面中设置的快捷键
   */
  private String getShortcutCode(int index) {
    boolean hasCtrl = false;
    boolean hasAlt = false;
    boolean hasShift = false;
    String value = Util.KEY_UNDEFINED;
    String shortcut = "";
    if (this.chkCommand.isSelected()) {
      shortcut = Util.COMMAND + "+" + shortcut;
    }
    if (this.chkShift.isSelected()) {
      shortcut = Util.SHIFT + "+" + shortcut;
    }
    if (this.chkAlt.isSelected()) {
      shortcut = Util.ALT + "+" + shortcut;
    }
    if (this.chkCtrl.isSelected()) {
      shortcut = Util.CTRL + "+" + shortcut;
    }
    shortcut += String.valueOf(Util.ALL_KEY_CODES[index - 1]);
    return shortcut;
  }

  /**
   * 检测下拉列表中选择的按键是否支持单独设置为快捷键
   * 
   * @param index
   *          下拉列表中当前选择项的索引
   * @return 是否支持单独设置为快捷键，如支持单独设置返回true，不支持返回false
   */
  private boolean isSingleKey(int index) {
    boolean label = false;
    int keyCode = Util.ALL_KEY_CODES[index - 1];
    for (int tempCode : Util.SINGLE_KEY_CODES) {
      if (tempCode == keyCode) {
        label = true;
      }
    }
    return label;
  }

  /**
   * 检测当前设置的快捷键是否与其他功能的快捷键重复
   * 
   * @param shortcut
   *          当前设置的快捷键
   * @return 是否与其他快捷键重复，如重复返回true，不重复返回false
   */
  private boolean isRepeatedShortcut(String shortcut) {
    if (Util.isTextEmpty(shortcut)) {
      return false;
    }
    boolean label = false;
    for (String name : Util.SHORTCUT_NAMES) {
      if (shortcut.equalsIgnoreCase(this.setting.shortcutMap.get(name))) {
        if (!name.equalsIgnoreCase(keyName)) { // 排除当前设置的功能
          label = true;
          break;
        }
      }
    }
    return label;
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.chkCtrl.addKeyListener(this.keyAdapter);
    this.chkAlt.addKeyListener(this.keyAdapter);
    this.chkShift.addKeyListener(this.keyAdapter);
    this.chkCommand.addKeyListener(this.keyAdapter);
    this.cmbShortcuts.addKeyListener(this.keyAdapter);
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
   * 获取是否执行了确定
   * 
   * @return 是否执行了确定
   */
  public boolean getOk() {
    return this.isOk;
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    int index = this.cmbShortcuts.getSelectedIndex();
    if (index <= 0) {
      JOptionPane.showMessageDialog(this, "请在右侧的下拉列表中选择一个按键！",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    } else if (!this.chkCtrl.isSelected() && !this.chkAlt.isSelected() && !this.chkShift.isSelected() && !this.chkCommand.isSelected()
      && !this.isSingleKey(index)) {
      JOptionPane.showMessageDialog(this, "请在左侧选择一个或多个控制按键！",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    } else {
      String shortcutCode = this.getShortcutCode(index);
      if (this.isRepeatedShortcut(shortcutCode)) {
        JOptionPane.showMessageDialog(this, "当前快捷键已被占用，请重新设置！",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
        return;
      }
      this.setting.shortcutMap.put(keyName, shortcutCode);
    }
    this.dispose();
    this.isOk = true;
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
    this.isOk = false;
  }
}
