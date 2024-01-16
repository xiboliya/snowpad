/**
 * Copyright (C) 2022 冰原
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.FileExt;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;
import com.xiboliya.snowpad.window.TipsWindow.Background;

/**
 * "转换文件换行符"对话框
 * 
 * @author 冰原
 * 
 */
public class ChangeLineSeparatorDialog extends BaseDialog implements ActionListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private static final String[] FILE_LINE_SEPARATORS = new String[] { LineSeparator.WINDOWS.getName(),
      LineSeparator.UNIX.getName(), LineSeparator.MACINTOSH.getName() }; // 选择换行符的数组
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblSourcePath = new JLabel("源文件路径：");
  private BaseButton btnAddSource = new BaseButton("+");
  private BaseButton btnReduceSource = new BaseButton("-");
  private JList<String> listPath = new JList<String>();
  private JScrollPane srpPath = new JScrollPane(this.listPath);
  private JLabel lblTargetLineSeparator = new JLabel("转为文件换行符：");
  private JComboBox<String> cmbLineSeparator = new JComboBox<String>(FILE_LINE_SEPARATORS);
  private JCheckBox chkRetainSource = new JCheckBox("转换后保留源文件(R)", true);
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
  private LineSeparator lineSeparator = LineSeparator.DEFAULT;

  public ChangeLineSeparatorDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.setSize(340, 310);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("转换文件换行符");
    this.pnlMain.setLayout(null);
    this.lblSourcePath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpPath.setBounds(10, 35, 270, 135);
    this.btnAddSource.setBounds(290, 35, 30, 30);
    this.btnAddSource.setToolTipText("添加文件");
    this.btnReduceSource.setBounds(290, 70, 30, 30);
    this.btnReduceSource.setToolTipText("移除文件");
    this.pnlMain.add(this.lblSourcePath);
    this.pnlMain.add(this.btnAddSource);
    this.pnlMain.add(this.btnReduceSource);
    this.pnlMain.add(this.srpPath);
    this.lblTargetLineSeparator.setBounds(10, 180, 130, Util.VIEW_HEIGHT);
    this.cmbLineSeparator.setBounds(10, 205, 180, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTargetLineSeparator);
    this.pnlMain.add(this.cmbLineSeparator);
    this.chkRetainSource.setBounds(10, 240, 150, Util.VIEW_HEIGHT);
    this.pnlMain.add(this.chkRetainSource);
    this.btnOk.setBounds(230, 190, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(230, 230, 90, Util.BUTTON_HEIGHT);
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
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.chkRetainSource.setMnemonic('R');
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnAddSource.addActionListener(this);
    this.btnAddSource.addKeyListener(this.buttonKeyAdapter);
    this.btnReduceSource.addActionListener(this);
    this.btnReduceSource.addKeyListener(this.buttonKeyAdapter);
    this.listPath.addKeyListener(this.keyAdapter);
    this.listPath.addListSelectionListener(this);
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
   * 设置各按钮是否可用
   */
  private void setSourceButtonEnabled() {
    if (this.listPath.isSelectionEmpty()) {
      this.btnReduceSource.setEnabled(false);
    } else {
      this.btnReduceSource.setEnabled(true);
    }
  }

  /**
   * 转换文件换行符的处理方法
   */
  private void changeLineSeparatorAll() {
    int size = this.defaultListModel.getSize();
    if (size <= 0) {
      TipsWindow.show(this, "源文件路径不能为空，请添加！");
      return;
    }
    this.setLineSeparator();
    LinkedList<String> failFileNames = new LinkedList<String>();
    for (int i = 0; i < size; i++) {
      String strFile = this.defaultListModel.get(i);
      if (!Util.isTextEmpty(strFile)) {
        File fileSource = new File(strFile);
        if (fileSource.isFile()) {
          try {
            this.changeLineSeparator(fileSource);
          } catch (Exception x) {
            // x.printStackTrace();
            failFileNames.add(strFile);
          }
        }
      }
    }
    if (failFileNames.isEmpty()) {
      TipsWindow.show(this, "转换文件换行符成功！", Background.GREEN);
    } else {
      StringBuilder stbFileNames = new StringBuilder();
      for (String fileName : failFileNames) {
        stbFileNames.append(fileName + "\n");
      }
      JOptionPane.showMessageDialog(this, "如下文件转换换行符失败：\n" + stbFileNames, Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
    }
  }

  private void changeLineSeparator(File file) {
    CharEncoding charEncoding = Util.checkFileEncoding(file);
    String strCharset = charEncoding.toString();
    LineSeparator lineSeparator = LineSeparator.DEFAULT;
    InputStreamReader inputStreamReader = null;
    String strResult = "";
    try {
      inputStreamReader = new InputStreamReader(new FileInputStream(file), strCharset);
      char[] chrBuf = new char[Util.BUFFER_LENGTH];
      int len = 0;
      switch (charEncoding) {
      case UTF8:
      case ULE:
      case UBE:
        inputStreamReader.read(); // 去掉文件开头的BOM
        break;
      }
      StringBuilder stbTemp = new StringBuilder();
      while ((len = inputStreamReader.read(chrBuf)) != -1) {
        stbTemp.append(chrBuf, 0, len);
      }
      strResult = stbTemp.toString();
      if (strResult.indexOf(LineSeparator.WINDOWS.toString()) >= 0) {
        strResult = strResult.replaceAll(LineSeparator.WINDOWS.toString(),
            LineSeparator.UNIX.toString());
        lineSeparator = LineSeparator.WINDOWS;
      } else if (strResult.indexOf(LineSeparator.MACINTOSH.toString()) >= 0) {
        strResult = strResult.replaceAll(LineSeparator.MACINTOSH.toString(),
            LineSeparator.UNIX.toString());
        lineSeparator = LineSeparator.MACINTOSH;
      } else if (strResult.indexOf(LineSeparator.UNIX.toString()) >= 0) {
        lineSeparator = LineSeparator.UNIX;
      } else { // 当文件内容不足1行时，则设置为系统默认的换行符
        lineSeparator = LineSeparator.DEFAULT;
      }
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
    if (!Util.isTextEmpty(strResult)) {
      this.toChangeLineSeparator(file, strResult, charEncoding);
    }
  }

  private void toChangeLineSeparator(File file, String strText, CharEncoding charEncoding) {
    FileOutputStream fileOutputStream = null;
    try {
      if (this.chkRetainSource.isSelected()) {
        String fileName = this.getFileNameWithLineSeparator(file.getName());
        file = new File(file.getParent() + Util.FILE_SEPARATOR + fileName);
      }
      fileOutputStream = new FileOutputStream(file);
      strText = strText.replaceAll(LineSeparator.UNIX.toString(), this.lineSeparator.toString());
      byte[] byteStr;
      int[] charBOM = new int[] { -1, -1, -1 }; // 根据当前的字符编码，存放BOM的数组
      switch (charEncoding) {
      case UTF8:
        charBOM[0] = 0xef;
        charBOM[1] = 0xbb;
        charBOM[2] = 0xbf;
        break;
      case ULE:
        charBOM[0] = 0xff;
        charBOM[1] = 0xfe;
        break;
      case UBE:
        charBOM[0] = 0xfe;
        charBOM[1] = 0xff;
        break;
      }
      byteStr = strText.getBytes(charEncoding.toString());
      for (int i = 0; i < charBOM.length; i++) {
        if (charBOM[i] == -1) {
          break;
        }
        fileOutputStream.write(charBOM[i]);
      }
      fileOutputStream.write(byteStr);
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 根据转换的文件换行符获取新的文件名
   * 
   * @param fileName 文件名
   * @return 转换后的新文件名
   */
  private String getFileNameWithLineSeparator(String fileName) {
    if (Util.isTextEmpty(fileName) || this.lineSeparator == null) {
      return "";
    }
    FileExt[] arrFileExt = FileExt.values(); // 获取包含枚举所有成员的数组
    String result = fileName;
    FileExt fileExt = FileExt.DEFAULT;
    for (FileExt tempFileExt : arrFileExt) {
      if (!FileExt.DEFAULT.equals(tempFileExt) && fileName.toLowerCase().endsWith(tempFileExt.toString().toLowerCase())) {
        result = fileName.substring(0, fileName.length() - tempFileExt.toString().length());
        fileExt = tempFileExt;
        break;
      }
    }
    result += "_" + this.lineSeparator.getName();
    result += fileExt;
    return result;
  }

  private void setLineSeparator() {
    int index = this.cmbLineSeparator.getSelectedIndex();
    switch (index) {
    case 0:
      this.lineSeparator = LineSeparator.WINDOWS;
      break;
    case 1:
      this.lineSeparator = LineSeparator.UNIX;
      break;
    case 2:
      this.lineSeparator = LineSeparator.MACINTOSH;
      break;
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
    this.changeLineSeparatorAll();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
