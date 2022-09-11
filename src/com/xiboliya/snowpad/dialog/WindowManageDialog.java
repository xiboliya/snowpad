/**
 * Copyright (C) 2014 冰原
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
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseDefaultTableModel;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.common.FileExt;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.util.Util;

/**
 * "窗口管理"对话框
 * 
 * @author 冰原
 * 
 */
public class WindowManageDialog extends BaseDialog implements ActionListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private static final String[] WINDOW_MANAGE_TABLE_TITLE_TEXTS = new String[] { "文件名", "路径", "类型" }; // 表格标题
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTable tabMain = null; // 显示数据的表格组件
  private JScrollPane spnMain = null;
  private JButton btnOk = new JButton("激活");
  private JButton btnSave = new JButton("保存");
  private JButton btnClose = new JButton("关闭");
  private JButton btnSort = new JButton("排序");
  private JButton btnMoveUp = new JButton("上移");
  private JButton btnMoveDown = new JButton("下移");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Vector<Vector<String>> cells = new Vector<Vector<String>>();
  private Vector<String> cellsTitle = new Vector<String>();
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private TableRowSorter<TableModel> tableRowSorter = null;
  private JTabbedPane tpnMain = null; // 显示文本域的选项卡组件

  /**
   * 构造方法
   * 
   * @param owner
   *          用于显示该对话框的父组件
   * @param modal
   *          是否为模式对话框
   * @param txaSource
   *          针对操作的文本域
   * @param tpnMain
   *          显示文本域的选项卡组件
   */
  public WindowManageDialog(JFrame owner, boolean modal, JTextArea txaSource, JTabbedPane tpnMain) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.tpnMain = tpnMain;
    this.init();
    this.addTable();
    this.refresh();
    this.addListeners();
    this.setSize(520, 310);
    this.setMinimumSize(new Dimension(520, 310)); // 设置本窗口的最小尺寸
    this.setResizable(true);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("窗口管理");
    this.pnlMain.add(this.pnlLeft, BorderLayout.CENTER);
    this.pnlMain.add(this.pnlRight, BorderLayout.EAST);
    this.btnOk.setBounds(10, 20, 90, Util.BUTTON_HEIGHT);
    this.btnSave.setBounds(10, 55, 90, Util.BUTTON_HEIGHT);
    this.btnClose.setBounds(10, 90, 90, Util.BUTTON_HEIGHT);
    this.btnSort.setBounds(10, 125, 90, Util.BUTTON_HEIGHT);
    this.btnMoveUp.setBounds(10, 160, 90, Util.BUTTON_HEIGHT);
    this.btnMoveDown.setBounds(10, 195, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 240, 90, Util.BUTTON_HEIGHT);
    this.pnlRight.setPreferredSize(new Dimension(110, 275)); // 设置面板的最适尺寸
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnSave);
    this.pnlRight.add(this.btnClose);
    this.pnlRight.add(this.btnSort);
    this.pnlRight.add(this.btnMoveUp);
    this.pnlRight.add(this.btnMoveDown);
    this.pnlRight.add(this.btnCancel);
  }

  /**
   * 在面板上添加表格视图
   */
  private void addTable() {
    for (String title : WINDOW_MANAGE_TABLE_TITLE_TEXTS) {
      this.cellsTitle.add(title);
    }
    this.baseDefaultTableModel = new BaseDefaultTableModel();
    this.tabMain = new JTable(this.baseDefaultTableModel);
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
    for (int i = 0; i < this.tpnMain.getTabCount(); i++) {
      JViewport viewport = ((JScrollPane) this.tpnMain.getComponentAt(i)).getViewport();
      BaseTextArea textArea = (BaseTextArea) viewport.getView();
      String path = textArea.getFile() == null ? "" : textArea.getFile().getParent();
      cellsLine = new Vector<String>();
      cellsLine.add(textArea.getPrefix() + textArea.getTitle());
      cellsLine.add(path);
      cellsLine.add(textArea.getFileExt().toString().substring(1));
      this.cells.add(cellsLine);
    }
    this.baseDefaultTableModel.setDataVector(this.cells, this.cellsTitle);
  }

  /**
   * 刷新表格中的数据
   */
  public void refresh() {
    this.tableRowSorter = new TableRowSorter<TableModel>(this.baseDefaultTableModel);
    this.tabMain.setRowSorter(this.tableRowSorter); // 设置排序过滤器
    this.getCells();
    this.tabMain.updateUI();
    int index = this.tpnMain.getSelectedIndex();
    this.tabMain.setRowSelectionInterval(index, index); // 自动选中当前激活的文件行
    this.setBtnEnabled();
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnSave.addActionListener(this);
    this.btnClose.addActionListener(this);
    this.btnSort.addActionListener(this);
    this.btnMoveUp.addActionListener(this);
    this.btnMoveDown.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnSave.addKeyListener(this.buttonKeyAdapter);
    this.btnClose.addKeyListener(this.buttonKeyAdapter);
    this.btnSort.addKeyListener(this.buttonKeyAdapter);
    this.btnMoveUp.addKeyListener(this.buttonKeyAdapter);
    this.btnMoveDown.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.tabMain.addKeyListener(this.keyAdapter);
    this.tabMain.getSelectionModel().addListSelectionListener(this);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnSave.equals(e.getSource())) {
      this.saveFiles();
    } else if (this.btnClose.equals(e.getSource())) {
      this.closeFiles();
    } else if (this.btnSort.equals(e.getSource())) {
      this.sortFiles();
    } else if (this.btnMoveUp.equals(e.getSource())) {
      this.moveFiles(true);
    } else if (this.btnMoveDown.equals(e.getSource())) {
      this.moveFiles(false);
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 表格视图中待操作的行所表示的文件路径
   * 
   * @param index
   *          表格视图中待操作的行索引
   * @return 当前行所表示的文件路径
   */
  private String getFilePath(int index) {
    return this.tabMain.getValueAt(index, 1).toString() + this.tabMain.getValueAt(index, 0).toString();
  }

  /**
   * "保存"的操作方法
   */
  private void saveFiles() {
    int[] indexs = this.tabMain.getSelectedRows();
    LinkedList<String> paths = new LinkedList<String>();
    for (int index : indexs) {
      paths.add(this.getFilePath(index));
    }
    ((SnowPadFrame) this.getOwner()).windowManageToSaveFile(paths);
    this.refresh();
  }

  /**
   * "关闭"的操作方法
   */
  private void closeFiles() {
    int[] indexs = this.tabMain.getSelectedRows();
    LinkedList<String> paths = new LinkedList<String>();
    for (int index : indexs) {
      paths.add(this.getFilePath(index));
    }
    ((SnowPadFrame) this.getOwner()).windowManageToCloseFile(paths);
    this.refresh();
  }

  /**
   * "排序"的操作方法
   */
  private void sortFiles() {
    this.tpnMain.removeAll();
    int rowCount = this.tabMain.getRowCount();
    int index = this.tabMain.getSelectedRow();
    LinkedList<String> paths = new LinkedList<String>();
    for (int i = 0; i < rowCount; i++) {
      paths.add(this.getFilePath(i));
    }
    ((SnowPadFrame) this.getOwner()).windowManageToSortFile(paths, index);
  }

  /**
   * "上移/下移"的操作方法
   * 
   * @param isUp
   *          移动方向，true表示上移，false表示下移。
   */
  private void moveFiles(boolean isUp) {
    int rowCount = this.tabMain.getRowCount();
    int index = this.tabMain.getSelectedRow();
    int newIndex = index + 1;
    if (isUp) {
      newIndex = index - 1;
    }
    int columnCount = this.tabMain.getColumnCount();
    for (int i = 0; i < columnCount; i++) {
      String value = this.tabMain.getValueAt(index, i).toString();
      String newValue = this.tabMain.getValueAt(newIndex, i).toString();
      this.tabMain.setValueAt(value, newIndex, i);
      this.tabMain.setValueAt(newValue, index, i);
    }
    this.tabMain.setRowSelectionInterval(newIndex, newIndex);
  }

  /**
   * 根据当前的选择，设置部分按钮是否可用
   */
  private void setBtnEnabled() {
    int rowCount = this.tabMain.getRowCount();
    if (rowCount <= 1) {
      this.btnMoveUp.setEnabled(false);
      this.btnMoveDown.setEnabled(false);
      return;
    }
    int[] indexs = this.tabMain.getSelectedRows();
    if (indexs == null || indexs.length == 0) {
      return;
    }
    int firstIndex = indexs[0];
    int lastIndex = indexs[indexs.length - 1];
    if (firstIndex >= 0 && firstIndex == lastIndex) {
      if (firstIndex <= 0) {
        this.btnMoveUp.setEnabled(false);
        this.btnMoveDown.setEnabled(true);
      } else if (firstIndex >= rowCount - 1) {
        this.btnMoveUp.setEnabled(true);
        this.btnMoveDown.setEnabled(false);
      } else {
        this.btnMoveUp.setEnabled(true);
        this.btnMoveDown.setEnabled(true);
      }
    } else {
      this.btnMoveUp.setEnabled(false);
      this.btnMoveDown.setEnabled(false);
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    int index = this.tabMain.getSelectedRow();
    if (index >= 0) {
      ((SnowPadFrame) this.getOwner()).windowManageToSwitchFile(this.getFilePath(index));
    }
    this.dispose();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * 组件选择值发生更改时调用
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    this.setBtnEnabled();
  }
}
