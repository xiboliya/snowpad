/**
 * Copyright (C) 2020 冰原
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextAreaSpecial;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "拆分文件"对话框
 * 
 * @author 冰原
 * 
 */
public class SlicingFileDialog extends BaseDialog implements ActionListener, DropTargetListener {
  private static final long serialVersionUID = 1L;
  private static final String PATTERN_META_CHARACTER = "$()*+.?[^{|"; // 正则表达式元字符
  private JTabbedPane tpnMain = new JTabbedPane();
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  // 单行
  private JPanel pnlSingle = new JPanel();
  private JLabel lblKeywordS = new JLabel("拆分关键字：");
  private BaseTextField txtKeywordS = new BaseTextField();
  // 多行
  private JPanel pnlMulti = new JPanel();
  private JLabel lblKeywordM = new JLabel("拆分关键字：");
  private BaseTextAreaSpecial txaKeywordM = new BaseTextAreaSpecial();
  private JScrollPane srpKeywordM = new JScrollPane(this.txaKeywordM);
  // 文件与按钮
  private JPanel pnlBottom = new JPanel();
  private JRadioButton radCurrentFile = new JRadioButton("拆分当前文件", true);
  private JRadioButton radTargetFile = new JRadioButton("拆分指定文件：", false);
  private BaseTextField txtTargetFile = new BaseTextField();
  private BaseButton btnSelectFile = new BaseButton("...");
  private ButtonGroup bgpFile = new ButtonGroup();
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public SlicingFileDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.initView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(420, 280);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("拆分文件");
    this.pnlMain.setLayout(null);
    // 单行
    this.pnlSingle.setLayout(null);
    this.lblKeywordS.setBounds(30, 12, 90, Util.VIEW_HEIGHT);
    this.txtKeywordS.setBounds(125, 10, 230, Util.INPUT_HEIGHT);
    this.pnlSingle.add(this.lblKeywordS);
    this.pnlSingle.add(this.txtKeywordS);
    // 多行
    this.pnlMulti.setLayout(null);
    this.lblKeywordM.setBounds(30, 12, 90, Util.VIEW_HEIGHT);
    this.srpKeywordM.setBounds(125, 10, 230, 80);
    this.pnlMulti.add(this.lblKeywordM);
    this.pnlMulti.add(this.srpKeywordM);
    // 文件与按钮
    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 130, 420, 150);
    this.radCurrentFile.setBounds(10, 10, 115, Util.VIEW_HEIGHT);
    this.radTargetFile.setBounds(10, 42, 115, Util.VIEW_HEIGHT);
    this.txtTargetFile.setBounds(125, 40, 230, Util.INPUT_HEIGHT);
    this.btnSelectFile.setBounds(370, 40, 25, 25);
    this.pnlBottom.add(this.radCurrentFile);
    this.pnlBottom.add(this.radTargetFile);
    this.pnlBottom.add(this.txtTargetFile);
    this.pnlBottom.add(this.btnSelectFile);
    this.btnOk.setBounds(90, 80, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 80, 85, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnOk);
    this.pnlBottom.add(this.btnCancel);
    this.bgpFile.add(this.radCurrentFile);
    this.bgpFile.add(this.radTargetFile);
    // 面板
    this.tpnMain.setBounds(0, 0, 420, 130);
    this.tpnMain.add(this.pnlSingle, "单行模式");
    this.tpnMain.add(this.pnlMulti, "多行模式");
    this.tpnMain.setFocusable(false);
    this.pnlMain.add(this.tpnMain);
    this.pnlMain.add(this.pnlBottom);
    new DropTarget(this.txtTargetFile, this); // 创建拖放目标，即设置某个组件接收drop操作
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
    String strSel = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(strSel)) {
      if (this.tpnMain.getSelectedIndex() == 0) {
        this.txtKeywordS.setText(strSel);
      } else {
        this.txaKeywordM.setText(strSel);
      }
    }
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.txtKeywordS.selectAll();
    } else {
      this.txaKeywordM.selectAll();
    }
  }

  /**
   * 根据单选按钮的选择，设置组件是否可用
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radCurrentFile.isSelected();
    this.txtTargetFile.setEnabled(!selected);
    this.btnSelectFile.setEnabled(!selected);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtKeywordS.addKeyListener(this.keyAdapter);
    this.txaKeywordM.addKeyListener(this.buttonKeyAdapter);
    this.radCurrentFile.addActionListener(this);
    this.radCurrentFile.addKeyListener(this.keyAdapter);
    this.radTargetFile.addActionListener(this);
    this.radTargetFile.addKeyListener(this.keyAdapter);
    this.txtTargetFile.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
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
    } else if (this.radCurrentFile.equals(source)) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radTargetFile.equals(source)) {
      this.setComponentEnabledByRadioButton();
    } else if (this.btnSelectFile.equals(source)) {
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
      this.txtTargetFile.setText(file.getAbsolutePath());
    }
  }

  /**
   * 按照关键字对文件进行拆分
   *
   * @param isSingle 是否为单行关键字
   */
  private void slicingFile(boolean isSingle) {
    String keyword = this.txtKeywordS.getText();
    if (!isSingle) {
      keyword = this.txaKeywordM.getText();
    }
    if (Util.isTextEmpty(keyword)) {
      TipsWindow.show(this, "拆分关键字不能为空，请输入！");
      return;
    }
    keyword = checkText(keyword);
    if (this.radCurrentFile.isSelected()) {
      this.slicingCurrentFile(keyword);
    } else {
      this.slicingTargetFile(keyword);
    }
  }

  /**
   * 拆分当前文件
   * 
   * @param keyword 关键字
   */
  private void slicingCurrentFile(String keyword) {
    String strText = this.txaSource.getText();
    if (Util.isTextEmpty(strText)) {
      TipsWindow.show(this, "文件内容为空，无法拆分！");
      return;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请检查关键字是否正确！", Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = this.txaSource.getFile();
    if (file != null && file.exists()) {
      File fileSplit = new File(file + "_split");
      if (fileSplit.exists()) {
        int result = JOptionPane.showConfirmDialog(this,
            Util.convertToMsg("此操作将覆盖已存在的" + fileSplit + "目录！\n是否继续？"),
            Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
          return;
        }
        Util.deleteAllFiles(fileSplit);
      } else {
        fileSplit.mkdirs(); // 如果目录不存在，则创建之
      }
      int count = 0;
      for (String text : arrText) {
        if (Util.isTextEmpty(text.trim())) {
          continue;
        }
        try {
          count++;
          File fileText = new File(fileSplit + "/" + count + ".txt");
          toSaveFile(fileText, text, this.txaSource.getCharEncoding(), this.txaSource.getLineSeparator());
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
      JOptionPane.showMessageDialog(this, "拆分文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else {
      TipsWindow.show(this, "拆分文件失败，请先保存文件！");
    }
  }

  /**
   * 拆分指定文件
   * 
   * @param keyword 关键字
   */
  private void slicingTargetFile(String keyword) {
    String strPath = this.txtTargetFile.getText();
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
      JOptionPane.showMessageDialog(this, "当前文件为空文件，无法拆分，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    CharEncoding charEncoding = Util.checkFileEncoding(file);
    String strText = this.toReadFile(file, charEncoding);
    if (Util.isTextEmpty(strText)) {
      TipsWindow.show(this, "拆分文件失败，源文件读取异常！");
      return;
    }
    LineSeparator lineSeparator = LineSeparator.DEFAULT;
    if (strText.indexOf(LineSeparator.WINDOWS.toString()) >= 0) {
      strText = strText.replaceAll(LineSeparator.WINDOWS.toString(),
          LineSeparator.UNIX.toString());
      lineSeparator = LineSeparator.WINDOWS;
    } else if (strText.indexOf(LineSeparator.MACINTOSH.toString()) >= 0) {
      strText = strText.replaceAll(LineSeparator.MACINTOSH.toString(),
          LineSeparator.UNIX.toString());
      lineSeparator = LineSeparator.MACINTOSH;
    } else if (strText.indexOf(LineSeparator.UNIX.toString()) >= 0) {
      lineSeparator = LineSeparator.UNIX;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请检查关键字是否正确！", Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      return;
    }
    File fileSplit = new File(file + "_split");
    if (fileSplit.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("此操作将覆盖已存在的" + fileSplit + "目录！\n是否继续？"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
      Util.deleteAllFiles(fileSplit);
    } else {
      fileSplit.mkdirs(); // 如果目录不存在，则创建之
    }
    int count = 0;
    for (String text : arrText) {
      if (Util.isTextEmpty(text.trim())) {
        continue;
      }
      try {
        count++;
        File fileText = new File(fileSplit + "/" + count + ".txt");
        toSaveFile(fileText, text, charEncoding, lineSeparator);
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
    JOptionPane.showMessageDialog(this, "拆分文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
        JOptionPane.CANCEL_OPTION);
  }

  private String checkText(String strText) {
    strText = strText.replace("\\", "\\\\");
    for (int i = 0; i < PATTERN_META_CHARACTER.length(); i++) {
      char item = PATTERN_META_CHARACTER.charAt(i);
      strText = strText.replace(String.valueOf(item), "\\" + item);
    }
    return strText;
  }

  private String toReadFile(File file, CharEncoding charEncoding) {
    String strCharset = charEncoding.toString();
    InputStreamReader inputStreamReader = null;
    String strText = "";
    try {
      inputStreamReader = new InputStreamReader(new FileInputStream(file), strCharset);
      char[] chrBuf = new char[Util.BUFFER_LENGTH];
      int len = 0;
      StringBuilder stbTemp = new StringBuilder();
      switch (charEncoding) {
      case UTF8:
      case ULE:
      case UBE:
        inputStreamReader.read(); // 去掉文件开头的BOM
        break;
      }
      while ((len = inputStreamReader.read(chrBuf)) != -1) {
        stbTemp.append(chrBuf, 0, len);
      }
      strText = stbTemp.toString();
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
    return strText;
  }

  /**
   * 将文本保存到文件
   * 
   * @param file 保存的文件
   */
  private void toSaveFile(File file, String strText, CharEncoding charEncoding, LineSeparator lineSeparator) throws Exception {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      strText = strText.replaceAll(LineSeparator.UNIX.toString(), lineSeparator.toString());
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
      for (int ch : charBOM) {
        if (ch == -1) {
          break;
        }
        fileOutputStream.write(ch);
      }
      fileOutputStream.write(byteStr);
    } catch (Exception x) {
      // x.printStackTrace();
      throw x; // 将捕获的异常抛出，以便调用处可以进行异常处理
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
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.slicingFile(this.tpnMain.getSelectedIndex() == 0);
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
    if (!this.txtTargetFile.isEnabled()) {
      e.rejectDrop(); // 如果文本框不可用，则拒绝操作
      return;
    }
    try {
      Transferable tr = e.getTransferable();
      if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) { // 如果Transferable对象支持拖放，则进行处理
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE); // 使用“拷贝、移动”方式接收放置操作
        List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor); // 从Transferable对象中获取文件列表
        Iterator iterator = fileList.iterator(); // 获取文件列表的迭代器
        if (iterator.hasNext()) {
          File file = (File) iterator.next();
          if (file != null && file.exists()) {
            this.txtTargetFile.setText(file.getAbsolutePath());
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
