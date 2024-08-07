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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "切割文件"对话框
 * 
 * @author 冰原
 * 
 */
public class CuttingFileDialog extends BaseDialog implements ActionListener, DropTargetListener {
  private static final long serialVersionUID = 1L;
  private static final String[] STORAGE_UNIT = new String[] {"B(字节)","KB(千字节)","MB(兆字节)"}; // 文件存储单位
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblPath = new JLabel("输入文件路径：");
  private BaseTextField txtPath = new BaseTextField();
  private BaseButton btnSelectFile = new BaseButton("选择文件(S)");
  private JRadioButton radCutSize = new JRadioButton("按文件大小切割：", true);
  private BaseTextField txtCutSize = new BaseTextField(true, "\\d{0,4}"); // 限制用户只能输入数字，并且不能超过4位
  private JComboBox<String> cmbCutUnit = new JComboBox<String>(STORAGE_UNIT);
  private JRadioButton radCutCount = new JRadioButton("按文件个数切割：", false);
  private BaseTextField txtCutCount = new BaseTextField(true, "\\d{0,4}"); // 限制用户只能输入数字，并且不能超过4位
  private JLabel lblCutCount = new JLabel("个");
  private ButtonGroup bgpCut = new ButtonGroup();
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public CuttingFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(420, 240);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("切割文件");
    this.pnlMain.setLayout(null);
    this.lblPath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.txtPath.setBounds(10, 35, 270, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblPath);
    this.pnlMain.add(this.txtPath);
    this.btnSelectFile.setBounds(290, 35, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnSelectFile);
    this.radCutSize.setBounds(10, 80, 130, Util.VIEW_HEIGHT);
    this.txtCutSize.setBounds(140, 80, 50, Util.INPUT_HEIGHT);
    this.cmbCutUnit.setBounds(200, 80, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.radCutSize);
    this.pnlMain.add(this.txtCutSize);
    this.pnlMain.add(this.cmbCutUnit);
    this.radCutCount.setBounds(10, 120, 130, Util.VIEW_HEIGHT);
    this.txtCutCount.setBounds(140, 120, 50, Util.INPUT_HEIGHT);
    this.lblCutCount.setBounds(200, 120, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.radCutCount);
    this.pnlMain.add(this.txtCutCount);
    this.pnlMain.add(this.lblCutCount);
    this.btnOk.setBounds(90, 165, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 165, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpCut.add(this.radCutSize);
    this.bgpCut.add(this.radCutCount);
    new DropTarget(this.txtPath, this); // 创建拖放目标，即设置某个组件接收drop操作
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
    this.txtPath.selectAll();
  }

  /**
   * 根据单选按钮的选择，设置组件是否可用
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radCutSize.isSelected();
    this.txtCutSize.setEnabled(selected);
    this.cmbCutUnit.setEnabled(selected);
    this.txtCutCount.setEnabled(!selected);
    this.lblCutCount.setEnabled(!selected);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtPath.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
    this.radCutSize.addActionListener(this);
    this.radCutSize.addKeyListener(this.keyAdapter);
    this.radCutCount.addActionListener(this);
    this.radCutCount.addKeyListener(this.keyAdapter);
    this.txtCutSize.addKeyListener(this.keyAdapter);
    this.cmbCutUnit.addKeyListener(this.keyAdapter);
    this.txtCutCount.addKeyListener(this.keyAdapter);
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
    } else if (this.btnSelectFile.equals(source)) {
      this.selectFile();
    } else if (this.radCutSize.equals(source)) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radCutCount.equals(source)) {
      this.setComponentEnabledByRadioButton();
    }
  }

  /**
   * "选择文件"的处理方法
   */
  private void selectFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
      this.openFileChooser.setFileFilter(this.openFileChooser.getAcceptAllFileFilter()); // 设置为默认过滤器
    }
    this.openFileChooser.setSelectedFile(null);
    this.openFileChooser.setMultiSelectionEnabled(false);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File file = this.openFileChooser.getSelectedFile();
    if (file != null && file.exists()) {
      this.txtPath.setText(file.getAbsolutePath());
    }
  }

  /**
   * 切割文件
   */
  private void cuttingFile() {
    String strPath = this.txtPath.getText();
    if (Util.isTextEmpty(strPath)) {
      TipsWindow.show(this, "文件路径不能为空，请输入！");
      return;
    }
    File file = new File(strPath);
    if (!file.exists()) {
      TipsWindow.show(this, "文件不存在，请重新输入！");
      return;
    } else if (file.isDirectory()) {
      TipsWindow.show(this, "不支持目录操作，请重新输入！");
      return;
    }
    long length = file.length();
    if (length == 0) {
      TipsWindow.show(this, "文件内容为空，请重新输入！");
      return;
    }
    if (this.radCutSize.isSelected()) {
      this.cuttingFileBySize(file, length);
    } else {
      this.cuttingFileByCount(file, length);
    }
  }

  private void cuttingFileBySize(File file, long length) {
    String strCutSize = this.txtCutSize.getText();
    if (Util.isTextEmpty(strCutSize)) {
      TipsWindow.show(this, "切割文件大小不能为空，请输入！");
      return;
    }
    int cutSize = 0;
    try {
      cutSize = Integer.parseInt(strCutSize);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "切割文件大小格式错误，请输入数字！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    if (cutSize <= 0) {
      JOptionPane.showMessageDialog(this, "切割文件大小必须大于0，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int unitIndex = this.cmbCutUnit.getSelectedIndex();
    switch (unitIndex) {
    case 1:
      cutSize = cutSize * 1024;
      break;
    case 2:
      cutSize = cutSize * 1024 * 1024;
      break;
    }
    if (cutSize >= length) {
      JOptionPane.showMessageDialog(this, "要切割当前文件，切割文件大小必须小于" + length + "字节，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File fileParent = new File(file.getParent() + "/" + file.getName() + "_cutting");
    if (fileParent.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("此操作将覆盖已存在的" + fileParent + "目录！\n是否继续？"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
      Util.deleteAllFiles(fileParent);
    } else {
      fileParent.mkdirs(); // 如果目录不存在，则创建之
    }
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte[] buffer = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        count++;
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "切割文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } catch (Exception x) {
      // x.printStackTrace();
      TipsWindow.show(this, "切割文件失败！");
    } finally {
      try {
        randomAccessFile.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
  }

  private void cuttingFileByCount(File file, long length) {
    String strCutCount = this.txtCutCount.getText();
    if (Util.isTextEmpty(strCutCount)) {
      TipsWindow.show(this, "切割文件个数不能为空，请输入！");
      return;
    }
    int cutCount = 0;
    try {
      cutCount = Integer.parseInt(strCutCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "切割文件个数格式错误，请输入数字！", Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      return;
    }
    if (cutCount <= 1) {
      JOptionPane.showMessageDialog(this, "切割文件个数必须大于1，请重新输入！", Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      return;
    } else if (cutCount > length) {
      JOptionPane.showMessageDialog(this, "要切割当前文件，切割文件个数不能大于" + length + "个，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int cutSize = 0;
    cutSize = (int)Math.ceil(length / (double)cutCount); // 按照小数部分全部进位的方式返回整数，相对于四舍五入，小数部分1~4也进位
    File fileParent = new File(file.getParent() + "/" + file.getName() + "_cutting");
    if (fileParent.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("此操作将覆盖已存在的" + fileParent + "目录！\n是否继续？"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
      Util.deleteAllFiles(fileParent);
    } else {
      fileParent.mkdirs(); // 如果目录不存在，则创建之
    }
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte[] buffer = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        count++;
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "切割文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
    } catch (Exception x) {
      // x.printStackTrace();
      TipsWindow.show(this, "切割文件失败！");
    } finally {
      try {
        randomAccessFile.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
  }

  private void toCuttingFile(File file, byte[] buffer, int len) {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      randomAccessFile.seek(0);
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
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.cuttingFile();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 当用户拖动鼠标，并进入到可放置的区域时，调用此方法。
   */
  @Override
  public void dragEnter(DropTargetDragEvent e) {
    e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE); // 使用“拷贝、移动”方式发起拖动操作
  }

  /**
   * 当用户拖动鼠标，并从可放置的区域移出时，调用此方法。
   */
  @Override
  public void dragExit(DropTargetEvent e) {
  }

  /**
   * 当用户拖动鼠标，并在可放置的区域内移动时，调用此方法。
   */
  @Override
  public void dragOver(DropTargetDragEvent e) {
  }

  /**
   * 当用户修改了当前放置操作后，调用此方法。
   */
  @Override
  public void dropActionChanged(DropTargetDragEvent e) {
  }

  /**
   * 当用户拖动鼠标，并在可放置的区域内放置时，调用此方法。
   */
  @Override
  public synchronized void drop(DropTargetDropEvent e) {
    try {
      Transferable tr = e.getTransferable();
      if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) { // 如果Transferable对象支持拖放，则进行处理
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE); // 使用“拷贝、移动”方式接收放置操作
        List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor); // 从Transferable对象中获取文件列表
        Iterator iterator = fileList.iterator(); // 获取文件列表的迭代器
        if (iterator.hasNext()) {
          File file = (File) iterator.next();
          if (file != null && file.exists()) {
            this.txtPath.setText(file.getAbsolutePath());
          }
        }
        e.getDropTargetContext().dropComplete(true); // 设置放置操作成功结束
      } else {
        e.rejectDrop(); // 如果Transferable对象不支持拖放，则拒绝操作
      }
    } catch (Exception x) {
      // x.printStackTrace();
      e.rejectDrop(); // 如果放置过程中出现异常，则拒绝操作
    }
  }
}
