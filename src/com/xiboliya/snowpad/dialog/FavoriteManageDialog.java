/**
 * Copyright (C) 2026 冰原
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
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseDefaultTableModel;
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.frame.SnowPadFrame;
import com.xiboliya.snowpad.util.Util;

/**
 * "管理收藏夹"对话框
 * 
 * @author 冰原
 * 
 */
public class FavoriteManageDialog extends BaseDialog implements ActionListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private static final String[] FAVORITE_MANAGE_TABLE_TITLE_TEXTS = new String[] { "文件路径" }; // 表格标题
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTable tabMain = null; // 显示数据的表格组件
  private JScrollPane spnMain = null;
  private BaseButton btnAdd = new BaseButton("添加");
  private BaseButton btnOk = new BaseButton("打开");
  private BaseButton btnDelete = new BaseButton("删除");
  private BaseButton btnSort = new BaseButton("排序");
  private BaseButton btnMoveUp = new BaseButton("上移");
  private BaseButton btnMoveDown = new BaseButton("下移");
  private BaseButton btnCancel = new BaseButton("取消");
  private Vector<Vector<String>> cells = new Vector<Vector<String>>();
  private Vector<String> cellsTitle = new Vector<String>();
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private TableRowSorter<TableModel> tableRowSorter = null;
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器

  /**
   * 构造方法
   * 
   * @param owner 用于显示该对话框的父组件
   * @param modal 是否为模式对话框
   */
  public FavoriteManageDialog(JFrame owner, boolean modal) {
    super(owner, modal);
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
    this.setTitle("管理收藏夹");
    this.pnlMain.add(this.pnlLeft, BorderLayout.CENTER);
    this.pnlMain.add(this.pnlRight, BorderLayout.EAST);
    this.btnAdd.setBounds(10, 20, 90, Util.BUTTON_HEIGHT);
    this.btnOk.setBounds(10, 55, 90, Util.BUTTON_HEIGHT);
    this.btnDelete.setBounds(10, 90, 90, Util.BUTTON_HEIGHT);
    this.btnSort.setBounds(10, 125, 90, Util.BUTTON_HEIGHT);
    this.btnMoveUp.setBounds(10, 160, 90, Util.BUTTON_HEIGHT);
    this.btnMoveDown.setBounds(10, 195, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 240, 90, Util.BUTTON_HEIGHT);
    this.pnlRight.setPreferredSize(new Dimension(110, 275)); // 设置面板的最适尺寸
    this.pnlRight.add(this.btnAdd);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnDelete);
    this.pnlRight.add(this.btnSort);
    this.pnlRight.add(this.btnMoveUp);
    this.pnlRight.add(this.btnMoveDown);
    this.pnlRight.add(this.btnCancel);
  }

  /**
   * 在面板上添加表格视图
   */
  private void addTable() {
    for (String title : FAVORITE_MANAGE_TABLE_TITLE_TEXTS) {
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
    for (String favorite : Util.setting.favoriteList) {
      cellsLine = new Vector<String>();
      cellsLine.add(favorite);
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
    if (!this.cells.isEmpty()) {
      this.tabMain.setRowSelectionInterval(0, 0); // 自动选中第一条数据
    }
    this.tabMain.updateUI();
    this.setBtnEnabled();
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.btnAdd.addActionListener(this);
    this.btnOk.addActionListener(this);
    this.btnDelete.addActionListener(this);
    this.btnSort.addActionListener(this);
    this.btnMoveUp.addActionListener(this);
    this.btnMoveDown.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.btnAdd.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnDelete.addKeyListener(this.buttonKeyAdapter);
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
    Object source = e.getSource();
    if (this.btnAdd.equals(source)) {
      this.addFiles();
    } else if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnDelete.equals(source)) {
      this.deleteFiles();
    } else if (this.btnSort.equals(source)) {
      this.sortFiles();
    } else if (this.btnMoveUp.equals(source)) {
      this.moveFiles(true);
    } else if (this.btnMoveDown.equals(source)) {
      this.moveFiles(false);
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * "添加"的操作方法
   */
  private void addFiles() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
      this.openFileChooser.setFileFilter(this.openFileChooser.getAcceptAllFileFilter()); // 设置为默认过滤器
      this.openFileChooser.setMultiSelectionEnabled(true);
    }
    this.openFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File[] files = this.openFileChooser.getSelectedFiles();
    if (files == null || files.length <= 0) {
      return;
    }
    LinkedList<String> paths = new LinkedList<String>();
    for (File file : files) {
      paths.add(file.getAbsolutePath());
    }
    ((SnowPadFrame) this.getOwner()).favoriteManageToAddFile(paths);
    this.refresh();
  }

  /**
   * "删除"的操作方法
   */
  private void deleteFiles() {
    int[] indexs = this.tabMain.getSelectedRows();
    LinkedList<String> paths = new LinkedList<String>();
    for (int index : indexs) {
      paths.add(this.tabMain.getValueAt(index, 0).toString());
    }
    ((SnowPadFrame) this.getOwner()).favoriteManageToDeleteFile(paths);
    this.refresh();
  }

  /**
   * "排序"的操作方法
   */
  private void sortFiles() {
    int rowCount = this.tabMain.getRowCount();
    LinkedList<String> paths = new LinkedList<String>();
    for (int i = 0; i < rowCount; i++) {
      paths.add(this.tabMain.getValueAt(i, 0).toString());
    }
    ((SnowPadFrame) this.getOwner()).favoriteManageToSortFile(paths);
  }

  /**
   * "上移/下移"的操作方法
   * 
   * @param isUp 移动方向，true表示上移，false表示下移。
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
    int[] indexs = this.tabMain.getSelectedRows();
    if (rowCount <= 0 || indexs == null || indexs.length == 0) {
      this.btnOk.setEnabled(false);
      this.btnDelete.setEnabled(false);
      this.btnSort.setEnabled(false);
      this.btnMoveUp.setEnabled(false);
      this.btnMoveDown.setEnabled(false);
    } else {
      this.btnOk.setEnabled(true);
      this.btnDelete.setEnabled(true);
      if (rowCount <= 1) {
        this.btnSort.setEnabled(false);
        this.btnMoveUp.setEnabled(false);
        this.btnMoveDown.setEnabled(false);
        return;
      } else {
        this.btnSort.setEnabled(true);
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
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    int index = this.tabMain.getSelectedRow();
    if (index >= 0) {
      String filePath = this.tabMain.getValueAt(index, 0).toString();
      ((SnowPadFrame) this.getOwner()).openFile(filePath);
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
