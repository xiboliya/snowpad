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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDefaultTableModel;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.dialog.KeyCheckDialog;
import com.xiboliya.snowpad.dialog.ShortcutSetDialog;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.setting.SettingAdapter;
import com.xiboliya.snowpad.util.Util;

/**
 * "快捷键管理"对话框
 * 
 * @author 冰原
 * 
 */
public class ShortcutManageDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final String[] SHORTCUT_MANAGE_TABLE_TITLE_TEXTS = new String[] { "功能", "快捷键" }; // 表格标题
  private SettingAdapter settingAdapter = null; // 用于解析和保存软件配置文件的工具类
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTable tabMain = null; // 显示数据的表格组件
  private JScrollPane spnMain = null;
  private BaseButton btnEdit = new BaseButton("编辑(E)");
  private BaseButton btnRemove = new BaseButton("清除(D)");
  private BaseButton btnReset = new BaseButton("恢复默认(R)");
  private BaseButton btnKeyCheck = new BaseButton("按键检测(K)");
  private BaseButton btnResetAll = new BaseButton("全部恢复默认(A)");
  private BaseButton btnCancel = new BaseButton("关闭");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Vector<Vector<String>> cells = new Vector<Vector<String>>();
  private Vector<String> cellsTitle = new Vector<String>();
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private ShortcutSetDialog shortcutSetDialog = null;
  private KeyCheckDialog keyCheckDialog = null;

  /**
   * 构造方法
   * 
   * @param owner 用于显示该对话框的父组件
   * @param modal 是否为模式对话框
   * @param txaSource 针对操作的文本域
   * @param settingAdapter 用于解析和保存软件配置文件的工具类
   */
  public ShortcutManageDialog(JFrame owner, boolean modal, BaseTextArea txaSource, SettingAdapter settingAdapter) {
    super(owner, modal, txaSource);
    this.settingAdapter = settingAdapter;
    this.init();
    this.setMnemonic();
    this.addTable();
    this.refresh();
    this.addListeners();
    this.setSize(520, 275);
    this.setMinimumSize(new Dimension(550, 275)); // 设置本窗口的最小尺寸
    this.setResizable(true);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("快捷键管理");
    this.pnlMain.add(this.pnlLeft, BorderLayout.CENTER);
    this.pnlMain.add(this.pnlRight, BorderLayout.EAST);
    this.btnEdit.setBounds(10, 20, 120, Util.BUTTON_HEIGHT);
    this.btnRemove.setBounds(10, 55, 120, Util.BUTTON_HEIGHT);
    this.btnReset.setBounds(10, 90, 120, Util.BUTTON_HEIGHT);
    this.btnKeyCheck.setBounds(10, 125, 120, Util.BUTTON_HEIGHT);
    this.btnResetAll.setBounds(10, 160, 120, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 205, 120, Util.BUTTON_HEIGHT);
    this.pnlRight.setPreferredSize(new Dimension(140, 275)); // 设置面板的最适尺寸
    this.pnlRight.add(this.btnEdit);
    this.pnlRight.add(this.btnRemove);
    this.pnlRight.add(this.btnReset);
    this.pnlRight.add(this.btnKeyCheck);
    this.pnlRight.add(this.btnResetAll);
    this.pnlRight.add(this.btnCancel);
  }

  /**
   * 为各组件设置助记符
   */
  private void setMnemonic() {
    this.btnEdit.setMnemonic('E');
    this.btnRemove.setMnemonic('D');
    this.btnReset.setMnemonic('R');
    this.btnKeyCheck.setMnemonic('K');
    this.btnResetAll.setMnemonic('A');
  }

  /**
   * 在面板上添加表格视图
   */
  private void addTable() {
    for (String title : SHORTCUT_MANAGE_TABLE_TITLE_TEXTS) {
      this.cellsTitle.add(title);
    }
    this.baseDefaultTableModel = new BaseDefaultTableModel(this.cells, this.cellsTitle);
    this.tabMain = new JTable(this.baseDefaultTableModel);
    this.tabMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.tabMain.getTableHeader().setReorderingAllowed(false); // 不可整列移动
    this.spnMain = new JScrollPane(this.tabMain);
    this.pnlLeft.add(this.spnMain, BorderLayout.CENTER);
  }

  /**
   * 获取数据项
   */
  private void getCells() {
    this.cells.clear();
    Vector<String> cellsLine = null;
    int length = Util.SHORTCUT_NAMES.length;
    for (int i = 0; i < length; i++) {
      cellsLine = new Vector<String>();
      String name = Util.SHORTCUT_NAMES[i];
      cellsLine.add(name);
      cellsLine.add(Util.transferShortcut(Util.setting.shortcutMap.get(name)));
      this.cells.add(cellsLine);
    }
  }

  /**
   * 刷新表格中的数据
   */
  public void refresh() {
    this.getCells();
    this.tabMain.updateUI();
    this.tabMain.setRowSelectionInterval(0, 0); // 自动选中当前激活的文件行
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.btnEdit.addActionListener(this);
    this.btnRemove.addActionListener(this);
    this.btnReset.addActionListener(this);
    this.btnKeyCheck.addActionListener(this);
    this.btnResetAll.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.btnEdit.addKeyListener(this.buttonKeyAdapter);
    this.btnRemove.addKeyListener(this.buttonKeyAdapter);
    this.btnReset.addKeyListener(this.buttonKeyAdapter);
    this.btnKeyCheck.addKeyListener(this.buttonKeyAdapter);
    this.btnResetAll.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.tabMain.addKeyListener(this.keyAdapter);
    this.tabMain.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) { // 双击
          onEnter();
        }
      }
    });
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnEdit.equals(source)) {
      this.onEnter();
    } else if (this.btnRemove.equals(source)) {
      this.removeShortcut();
    } else if (this.btnReset.equals(source)) {
      this.resetShortcut();
    } else if (this.btnKeyCheck.equals(source)) {
      this.keyCheck();
    } else if (this.btnResetAll.equals(source)) {
      this.resetShortcuts();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * "清除"的操作方法
   */
  private void removeShortcut() {
    int index = this.tabMain.getSelectedRow();
    Util.setting.shortcutMap.put(this.tabMain.getValueAt(index, 0).toString(), "");
    this.tabMain.setValueAt("", index, 1);
    ((SnowPadFrame) this.getOwner()).shortcutManageToSetMenuAccelerator(index);
  }

  /**
   * "恢复默认"的操作方法
   */
  private void resetShortcut() {
    int index = this.tabMain.getSelectedRow();
    String shortcutValue = Util.SHORTCUT_VALUES[index];
    Util.setting.shortcutMap.put(this.tabMain.getValueAt(index, 0).toString(), shortcutValue);
    this.tabMain.setValueAt(Util.transferShortcut(shortcutValue), index, 1);
    ((SnowPadFrame) this.getOwner()).shortcutManageToSetMenuAccelerator(index);
  }

  /**
   * "按键检测"的操作方法
   */
  private void keyCheck() {
    if (this.keyCheckDialog == null) {
      this.keyCheckDialog = new KeyCheckDialog(this, true);
    } else {
      this.keyCheckDialog.setVisible(true);
    }
  }

  /**
   * "全部恢复默认"的操作方法
   */
  private void resetShortcuts() {
    int result = JOptionPane.showConfirmDialog(this, "此操作将恢复所有的快捷键设置！\n是否继续？", Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    this.settingAdapter.initShortcuts();
    this.refresh();
    ((SnowPadFrame) this.getOwner()).setMenuAccelerator();
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    int index = this.tabMain.getSelectedRow();
    String keyName = this.tabMain.getValueAt(index, 0).toString();
    if (this.shortcutSetDialog == null) {
      this.shortcutSetDialog = new ShortcutSetDialog(this, true, keyName);
    } else {
      this.shortcutSetDialog.setKeyName(keyName);
      this.shortcutSetDialog.setVisible(true);
    }
    if (!this.shortcutSetDialog.getOk()) {
      return;
    }
    this.tabMain.setValueAt(Util.transferShortcut(Util.setting.shortcutMap.get(keyName)), index, 1);
    ((SnowPadFrame) this.getOwner()).shortcutManageToSetMenuAccelerator(index);
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

}
