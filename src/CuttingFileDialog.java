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

package com.xiboliya.snowpad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "切割文件"对话框
 * 
 * @author 冰原
 * 
 */
public class CuttingFileDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblPath = new JLabel("输入文件路径：");
  private BaseTextField txtPath = new BaseTextField();
  private JButton btnSelectFile = new JButton("选择文件(S)");
  private JLabel lblCutSize = new JLabel("切割文件大小：");
  private BaseTextField txtCutSize = new BaseTextField(true, "\\d{0,4}"); // 限制用户只能输入数字，并且不能超过4位
  private JComboBox<String> cmbCutUnit = new JComboBox<String>(Util.STORAGE_UNIT);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public CuttingFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(420, 200);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("切割文件");
    this.pnlMain.setLayout(null);
    this.lblPath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.txtPath.setBounds(10, 35, 270, 30);
    this.pnlMain.add(this.lblPath);
    this.pnlMain.add(this.txtPath);
    this.btnSelectFile.setBounds(290, 37, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnSelectFile);
    this.lblCutSize.setBounds(10, 87, 100, Util.VIEW_HEIGHT);
    this.txtCutSize.setBounds(110, 85, 50, 30);
    this.cmbCutUnit.setBounds(170, 87, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblCutSize);
    this.pnlMain.add(this.txtCutSize);
    this.pnlMain.add(this.cmbCutUnit);
    this.btnOk.setBounds(90, 130, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 130, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
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
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtPath.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
    this.txtCutSize.addKeyListener(this.keyAdapter);
    this.cmbCutUnit.addKeyListener(this.keyAdapter);
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
    } else if (this.btnSelectFile.equals(e.getSource())) {
      this.selectFile();
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

  private void cuttingFile() {
    String strPath = this.txtPath.getText();
    if (Util.isTextEmpty(strPath)) {
      JOptionPane.showMessageDialog(this, "文件路径不能为空，请输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = new File(strPath);
    if (!file.exists()) {
      JOptionPane.showMessageDialog(this, "文件不存在，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    } else if (file.isDirectory()) {
      JOptionPane.showMessageDialog(this, "不支持目录操作，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    long length = file.length();
    if (length == 0) {
      JOptionPane.showMessageDialog(this, "当前文件为空文件，无法切割，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    String strCutSize = this.txtCutSize.getText();
    if (Util.isTextEmpty(strCutSize)) {
      JOptionPane.showMessageDialog(this, "切割文件大小不能为空，请输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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
    } else {
      fileParent.mkdirs(); // 如果目录不存在，则创建之
    }
    RandomAccessFile randomAccessFile = null;
    byte byteArr[] = new byte[cutSize];
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte buffer[] = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        count++;
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "切割文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      onCancel();
    } catch (Exception x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "切割文件失败！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } finally {
      try {
        randomAccessFile.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  private void toCuttingFile(File file, byte buffer[], int len) {
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
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.cuttingFile();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
