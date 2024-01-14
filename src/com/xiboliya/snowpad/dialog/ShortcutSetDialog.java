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

package com.xiboliya.snowpad.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.util.Util;

/**
 * "快捷键编辑"对话框
 * 
 * @author 冰原
 * 
 */
public class ShortcutSetDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final String KEY_UNDEFINED = "[未定义]"; // 未定义按键
  private static final int[] ALL_KEY_CODES = new int[] {
    KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7,
    KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_F,
    KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N,
    KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_U, KeyEvent.VK_V,
    KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z, KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4,
    KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12,
    KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7,
    KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_NUM_LOCK, KeyEvent.VK_ADD, KeyEvent.VK_SUBTRACT, KeyEvent.VK_MULTIPLY, KeyEvent.VK_DIVIDE, KeyEvent.VK_DECIMAL,
    KeyEvent.VK_ESCAPE, KeyEvent.VK_TAB, KeyEvent.VK_SPACE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_SLASH, KeyEvent.VK_BACK_SLASH, KeyEvent.VK_OPEN_BRACKET,
    KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_COMMA, KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PERIOD, KeyEvent.VK_QUOTE, KeyEvent.VK_SEMICOLON, KeyEvent.VK_INSERT,
    KeyEvent.VK_DELETE, KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER,
    KeyEvent.VK_EQUALS, KeyEvent.VK_MINUS, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_WINDOWS, KeyEvent.VK_CONTEXT_MENU, KeyEvent.VK_SCROLL_LOCK, KeyEvent.VK_PAUSE }; // 所有可以用作快捷键的按键常量
  private static final int[] SINGLE_KEY_CODES = new int[] { KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5, KeyEvent.VK_F6,
    KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12 }; // 可以单独用作快捷键的按键常量
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblName = new JLabel("功能名称：");
  private JLabel lblNameView = new JLabel();
  private JCheckBox chkCtrl = new JCheckBox(Util.CTRL);
  private JCheckBox chkAlt = new JCheckBox(Util.ALT);
  private JCheckBox chkShift = new JCheckBox(Util.SHIFT);
  private JCheckBox chkCommand = new JCheckBox(Util.COMMAND);
  private JComboBox<String> cmbShortcuts = null;
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private boolean isOk = false; // 用于标识是否点击了确定按钮
  private String keyName = ""; // 功能名称
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public ShortcutSetDialog(JDialog owner, boolean modal, String keyName) {
    super(owner, modal);
    this.keyName = keyName;
    this.init();
    this.addListeners();
    this.setSize(380, 150);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   * 
   * @param isVisible 设置本窗口是否可见，如果可见则为true
   */
  @Override
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
    String[] strKeys = new String[ALL_KEY_CODES.length + 1];
    strKeys[0] = KEY_UNDEFINED; // 添加未定义的按键
    for (int i = 0; i < ALL_KEY_CODES.length; i++) {
      strKeys[i + 1] = KeyEvent.getKeyText(ALL_KEY_CODES[i]);
    }
    this.cmbShortcuts = new JComboBox<String>(strKeys);
  }

  /**
   * 刷新功能名称和快捷键的显示
   */
  private void refreshKeyView() {
    if (!Util.isTextEmpty(this.keyName)) {
      this.lblNameView.setText(this.keyName);
      String keyValue = Util.setting.shortcutMap.get(this.keyName);
      boolean hasCtrl = false;
      boolean hasAlt = false;
      boolean hasShift = false;
      boolean hasCommand = false;
      String value = KEY_UNDEFINED;
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
    StringBuilder stbShortcut = new StringBuilder();
    boolean hasCtrl = this.chkCtrl.isSelected();
    boolean hasAlt = this.chkAlt.isSelected();
    boolean hasShift = this.chkShift.isSelected();
    boolean hasCommand = this.chkCommand.isSelected();
    if (index == 0 && !hasCtrl && !hasAlt && !hasShift && !hasCommand) {
      return stbShortcut.toString();
    }
    if (hasCtrl) {
      stbShortcut.append(Util.CTRL).append("+");
    }
    if (hasAlt) {
      stbShortcut.append(Util.ALT).append("+");
    }
    if (hasShift) {
      stbShortcut.append(Util.SHIFT).append("+");
    }
    if (hasCommand) {
      stbShortcut.append(Util.COMMAND).append("+");
    }
    if (index > 0) {
      stbShortcut.append(String.valueOf(ALL_KEY_CODES[index - 1]));
    }
    return stbShortcut.toString();
  }

  /**
   * 检测下拉列表中选择的按键是否支持单独设置为快捷键
   * 
   * @param index 下拉列表中当前选择项的索引
   * @return 是否支持单独设置为快捷键，如支持单独设置返回true，不支持返回false
   */
  private boolean isSingleKey(int index) {
    boolean label = false;
    int keyCode = ALL_KEY_CODES[index - 1];
    for (int tempCode : SINGLE_KEY_CODES) {
      if (tempCode == keyCode) {
        label = true;
      }
    }
    return label;
  }

  /**
   * 检测当前设置的快捷键是否与其他功能的快捷键重复
   * 
   * @param shortcut 当前设置的快捷键
   * @return 是否与其他快捷键重复，如重复返回true，不重复返回false
   */
  private boolean isRepeatedShortcut(String shortcut) {
    if (Util.isTextEmpty(shortcut)) {
      return false;
    }
    boolean label = false;
    for (String name : Util.SHORTCUT_NAMES) {
      if (shortcut.equalsIgnoreCase(Util.setting.shortcutMap.get(name))) {
        if (!name.equalsIgnoreCase(keyName)) { // 排除当前设置的功能
          label = true;
          break;
        }
      }
    }
    return label;
  }

  /**
   * 保存功能快捷键
   * 
   * @param keyName 功能名称
   * @param shortcut 当前设置的快捷键
   */
  private void saveShortcut(String keyName, String shortcut) {
    Util.setting.shortcutMap.put(keyName, shortcut);
    this.dispose();
    this.isOk = true;
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
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnCancel.equals(source)) {
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
  @Override
  public void onEnter() {
    int index = this.cmbShortcuts.getSelectedIndex();
    String shortcutCode = this.getShortcutCode(index);
    if (Util.isTextEmpty(shortcutCode)) {
      int result = JOptionPane.showConfirmDialog(this, "当前没有选择任何按键，继续操作会清除此功能快捷键，是否继续？",
          Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        this.saveShortcut(keyName, shortcutCode);
      }
      return;
    }
    if (index <= 0) {
      JOptionPane.showMessageDialog(this, "请在右侧的下拉列表中选择一个按键！",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else if (!this.chkCtrl.isSelected() && !this.chkAlt.isSelected() && !this.chkShift.isSelected() && !this.chkCommand.isSelected()
      && !this.isSingleKey(index)) {
      JOptionPane.showMessageDialog(this, "请在左侧选择一个或多个控制按键！",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else if (this.isRepeatedShortcut(shortcutCode)) {
      JOptionPane.showMessageDialog(this, "当前快捷键已被占用，请重新设置！",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else {
      this.saveShortcut(keyName, shortcutCode);
    }
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
    this.isOk = false;
  }
}
