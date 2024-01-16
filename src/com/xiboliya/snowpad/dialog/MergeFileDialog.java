/**
 * Copyright (C) 2021 冰原
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
import java.io.File;
import java.io.RandomAccessFile;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.chooser.SaveFileChooser;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;
import com.xiboliya.snowpad.window.TipsWindow.Background;

/**
 * "拼接文件"对话框
 * 
 * @author 冰原
 * 
 */
public class MergeFileDialog extends BaseDialog implements ActionListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private static final int BIG_BUFFER_LENGTH = 1024 * 1024; // 大缓冲区的大小
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private SaveFileChooser saveFileChooser = null; // "保存"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblSourcePath = new JLabel("源文件路径：");
  private BaseButton btnAddSource = new BaseButton("+");
  private BaseButton btnReduceSource = new BaseButton("-");
  private BaseButton btnUpSource = new BaseButton("↑");
  private BaseButton btnDownSource = new BaseButton("↓");
  private JList<String> listPath = new JList<String>();
  private JScrollPane srpPath = new JScrollPane(this.listPath);
  private JLabel lblTargetPath = new JLabel("保存文件路径：");
  private BaseTextField txtTargetPath = new BaseTextField();
  private BaseButton btnTargetPath = new BaseButton("...");
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private DefaultListModel<String> defaultListModel = new DefaultListModel<String>();

  public MergeFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(340, 310);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("拼接文件");
    this.pnlMain.setLayout(null);
    this.lblSourcePath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpPath.setBounds(10, 35, 270, 135);
    this.btnAddSource.setBounds(290, 35, 30, 30);
    this.btnAddSource.setToolTipText("添加文件");
    this.btnReduceSource.setBounds(290, 70, 30, 30);
    this.btnReduceSource.setToolTipText("移除文件");
    this.btnUpSource.setBounds(290, 105, 30, 30);
    this.btnUpSource.setToolTipText("上移文件");
    this.btnDownSource.setBounds(290, 140, 30, 30);
    this.btnDownSource.setToolTipText("下移文件");
    this.pnlMain.add(this.lblSourcePath);
    this.pnlMain.add(this.btnAddSource);
    this.pnlMain.add(this.btnReduceSource);
    this.pnlMain.add(this.btnUpSource);
    this.pnlMain.add(this.btnDownSource);
    this.pnlMain.add(this.srpPath);
    this.lblTargetPath.setBounds(10, 180, 110, Util.VIEW_HEIGHT);
    this.txtTargetPath.setBounds(10, 207, 270, Util.INPUT_HEIGHT);
    this.btnTargetPath.setBounds(290, 203, 30, 30);
    this.btnTargetPath.setToolTipText("保存文件");
    this.pnlMain.add(this.lblTargetPath);
    this.pnlMain.add(this.txtTargetPath);
    this.pnlMain.add(this.btnTargetPath);
    this.btnOk.setBounds(50, 245, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(200, 245, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.listPath.setModel(this.defaultListModel);
    this.listPath.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
   * 初始化各控件的状态
   */
  private void initView() {
    this.setSourceButtonEnabled();
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnAddSource.addActionListener(this);
    this.btnAddSource.addKeyListener(this.buttonKeyAdapter);
    this.btnReduceSource.addActionListener(this);
    this.btnReduceSource.addKeyListener(this.buttonKeyAdapter);
    this.btnUpSource.addActionListener(this);
    this.btnUpSource.addKeyListener(this.buttonKeyAdapter);
    this.btnDownSource.addActionListener(this);
    this.btnDownSource.addKeyListener(this.buttonKeyAdapter);
    this.listPath.addKeyListener(this.keyAdapter);
    this.listPath.addListSelectionListener(this);
    this.txtTargetPath.addKeyListener(this.keyAdapter);
    this.btnTargetPath.addActionListener(this);
    this.btnTargetPath.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "添加文件"的处理方法
   */
  private void addSourceFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
      this.openFileChooser.setFileFilter(this.openFileChooser.getAcceptAllFileFilter()); // 设置为默认过滤器
    }
    this.openFileChooser.setSelectedFile(null);
    this.openFileChooser.setMultiSelectionEnabled(true);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File[] files = this.openFileChooser.getSelectedFiles();
    if (files == null || files.length <= 0) {
      return;
    }
    for (File file : files) {
      if (file != null && file.exists() && !this.defaultListModel.contains(file.getAbsolutePath())) {
        this.defaultListModel.addElement(file.getAbsolutePath());
      }
    }
  }

  /**
   * "移除文件"的处理方法
   */
  private void reduceSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int minIndex = arrIndex[0];
    this.defaultListModel.removeRange(minIndex, arrIndex[arrIndex.length - 1]);
    int size = this.defaultListModel.getSize();
    if (minIndex > size - 1) {
      minIndex = size - 1;
    }
    this.listPath.setSelectedIndex(minIndex);
  }

  /**
   * "上移文件"的处理方法
   */
  private void upSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int minIndex = arrIndex[0] - 1;
    int maxIndex = arrIndex[arrIndex.length - 1];
    if (minIndex < 0) {
      return;
    }
    String element = this.defaultListModel.remove(minIndex);
    this.defaultListModel.insertElementAt(element, maxIndex);
    this.listPath.setSelectionInterval(minIndex, maxIndex - 1);
  }

  /**
   * "下移文件"的处理方法
   */
  private void downSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int maxIndex = arrIndex[arrIndex.length - 1] + 1;
    if (maxIndex >= this.defaultListModel.getSize()) {
      return;
    }
    String element = this.defaultListModel.remove(maxIndex);
    this.defaultListModel.insertElementAt(element, arrIndex[0]);
    this.listPath.setSelectionInterval(arrIndex[0] + 1, maxIndex);
  }

  /**
   * "保存文件"的处理方法
   */
  private void selectFile() {
    if (this.saveFileChooser == null) {
      this.saveFileChooser = new SaveFileChooser();
    }
    this.saveFileChooser.setSelectedFile(null);
    this.saveFileChooser.setMultiSelectionEnabled(false);
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser.showSaveDialog(this)) {
      return;
    }
    File file = this.saveFileChooser.getSelectedFile();
    if (file != null) {
      this.txtTargetPath.setText(file.getAbsolutePath());
    }
  }

  /**
   * 设置各按钮是否可用
   */
  private void setSourceButtonEnabled() {
    if (this.listPath.isSelectionEmpty()) {
      this.btnReduceSource.setEnabled(false);
      this.btnUpSource.setEnabled(false);
      this.btnDownSource.setEnabled(false);
    } else {
      this.btnReduceSource.setEnabled(true);
      int size = this.defaultListModel.getSize();
      int minSelectionIndex = this.listPath.getMinSelectionIndex();
      int maxSelectionIndex = this.listPath.getMaxSelectionIndex();
      if (size == 1 || (minSelectionIndex == 0 && maxSelectionIndex == size - 1)) { // 列表中只有一行数据或选中了所有行
        this.btnUpSource.setEnabled(false);
        this.btnDownSource.setEnabled(false);
      } else {
        if (minSelectionIndex == 0) { // 选中了第一行
          this.btnUpSource.setEnabled(false);
        } else {
          this.btnUpSource.setEnabled(true);
        }
        if (maxSelectionIndex == size - 1) { // 选中了最后一行
          this.btnDownSource.setEnabled(false);
        } else {
          this.btnDownSource.setEnabled(true);
        }
      }      
    }
  }

  /**
   * 拼接文件的处理方法
   */
  private void mergeFile() {
    String strTargetPath = this.txtTargetPath.getText();
    if (Util.isTextEmpty(strTargetPath)) {
      JOptionPane.showMessageDialog(this, "保存文件路径不能为空，请输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = new File(strTargetPath);
    if (file.isDirectory()) {
      JOptionPane.showMessageDialog(this, "保存文件路径不支持目录操作，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int size = this.defaultListModel.getSize();
    if (size <= 0) {
      TipsWindow.show(this, "源文件路径不能为空，请添加！");
      return;
    } else if (size == 1) {
      TipsWindow.show(this, "源文件路径至少为2个，请添加！");
      return;
    }
    Util.checkFile(file);
    if (file.exists()) {
      file.delete();
    }
    boolean result = true;
    for (int i = 0; i < size; i++) {
      String strFile = this.defaultListModel.get(i);
      if (!Util.isTextEmpty(strFile)) {
        File fileSource = new File(strFile);
        if (fileSource.isFile()) {
          RandomAccessFile randomAccessFile = null;
          try {
            randomAccessFile = new RandomAccessFile(fileSource, "r");
            randomAccessFile.seek(0);
            byte[] buffer = new byte[BIG_BUFFER_LENGTH];
            int len = 0;
            while ((len = randomAccessFile.read(buffer)) != -1) {
              toSaveFile(file, buffer, len);
              buffer = new byte[BIG_BUFFER_LENGTH];
            }
          } catch (Exception x) {
            // x.printStackTrace();
            result = false;
            break;
          } finally {
            try {
              randomAccessFile.close();
            } catch (Exception x) {
              // x.printStackTrace();
            }
          }
        }
      }
    }
    if (result) {
      TipsWindow.show(this, "拼接文件完成！", Background.GREEN);
    } else {
      TipsWindow.show(this, "拼接文件失败！");
    }
  }

  private void toSaveFile(File file, byte[] buffer, int len) {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      randomAccessFile.seek(file.length());
      randomAccessFile.write(buffer, 0, len);
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        randomAccessFile.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
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
    } else if (this.btnAddSource.equals(source)) {
      this.addSourceFile();
    } else if (this.btnReduceSource.equals(source)) {
      this.reduceSourceFile();
    } else if (this.btnUpSource.equals(source)) {
      this.upSourceFile();
    } else if (this.btnDownSource.equals(source)) {
      this.downSourceFile();
    } else if (this.btnTargetPath.equals(source)) {
      this.selectFile();
    }
  }

  /**
   * 当列表框改变选择时，触发此事件
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (this.listPath.equals(e.getSource())) {
      this.setSourceButtonEnabled();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.mergeFile();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
