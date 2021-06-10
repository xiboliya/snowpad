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

package com.xiboliya.snowpad;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 * "拆分文件"对话框
 * 
 * @author 冰原
 * 
 */
public class SlicingFileDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblKeyword = new JLabel("拆分关键字：");
  private BaseTextField txtKeyword = new BaseTextField();
  private JRadioButton radCurrentFile = new JRadioButton("拆分当前文件", true);
  private JRadioButton radTargetFile = new JRadioButton("拆分指定文件：", false);
  private BaseTextField txtTargetFile = new BaseTextField();
  private JButton btnSelectFile = new JButton("...");
  private ButtonGroup bgpFile = new ButtonGroup();
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private Insets insets = new Insets(0, 0, 0, 0);
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public SlicingFileDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.initView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(420, 220);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("拆分文件");
    this.pnlMain.setLayout(null);
    this.lblKeyword.setBounds(30, 12, 90, Util.VIEW_HEIGHT);
    this.txtKeyword.setBounds(130, 10, 230, 30);
    this.pnlMain.add(this.lblKeyword);
    this.pnlMain.add(this.txtKeyword);
    this.radCurrentFile.setBounds(10, 60, 120, Util.VIEW_HEIGHT);
    this.radTargetFile.setBounds(10, 107, 120, Util.VIEW_HEIGHT);
    this.txtTargetFile.setBounds(130, 105, 230, 30);
    this.btnSelectFile.setMargin(this.insets);
    this.btnSelectFile.setBounds(370, 105, 30, 30);
    this.pnlMain.add(this.radCurrentFile);
    this.pnlMain.add(this.radTargetFile);
    this.pnlMain.add(this.txtTargetFile);
    this.pnlMain.add(this.btnSelectFile);
    this.btnOk.setBounds(90, 145, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 145, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpFile.add(this.radCurrentFile);
    this.bgpFile.add(this.radTargetFile);
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
    String strSel = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(strSel)) {
      this.txtKeyword.setText(strSel);
    }
    this.txtKeyword.selectAll();
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
    this.txtKeyword.addKeyListener(this.keyAdapter);
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
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.radCurrentFile.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radTargetFile.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
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
      this.txtTargetFile.setText(file.getAbsolutePath());
    }
  }

  /**
   * 按照关键字对文件进行拆分
   */
  private void slicingFile() {
    String keyword = this.txtKeyword.getText();
    if (Util.isTextEmpty(keyword)) {
      JOptionPane.showMessageDialog(this, "拆分关键字不能为空，请输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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
   * @param keyword
   *          关键字
   */
  private void slicingCurrentFile(String keyword) {
    BaseTextArea textArea = (BaseTextArea) this.txaSource;
    String strText = textArea.getText();
    if (Util.isTextEmpty(strText)) {
      JOptionPane.showMessageDialog(this, "文件内容为空，无法拆分！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请检查关键字是否正确！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = textArea.getFile();
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
          toSaveFile(fileText, text, textArea.getCharEncoding(), textArea.getLineSeparator());
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
      JOptionPane.showMessageDialog(this, "拆分文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请先保存文件！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }

  /**
   * 拆分指定文件
   * 
   * @param keyword
   *          关键字
   */
  private void slicingTargetFile(String keyword) {
    String strPath = this.txtTargetFile.getText();
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
      JOptionPane.showMessageDialog(this, "当前文件为空文件，无法拆分，请重新输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    CharEncoding charEncoding = Util.checkFileEncoding(file);
    String strText = this.toReadFile(file, charEncoding);
    if (Util.isTextEmpty(strText)) {
      JOptionPane.showMessageDialog(this, "拆分文件失败，源文件读取异常！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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
      JOptionPane.showMessageDialog(this, "拆分文件失败，请检查关键字是否正确！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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
    for (int i = 0; i < Util.PATTERN_META_CHARACTER.length(); i++) {
      char item = Util.PATTERN_META_CHARACTER.charAt(i);
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
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
    return strText;
  }

  /**
   * 将文本保存到文件
   * 
   * @param file
   *          保存的文件
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
      for (int i = 0; i < charBOM.length; i++) {
        if (charBOM[i] == -1) {
          break;
        }
        fileOutputStream.write(charBOM[i]);
      }
      fileOutputStream.write(byteStr);
    } catch (Exception x) {
      // x.printStackTrace();
      throw x; // 将捕获的异常抛出，以便调用处可以进行异常处理
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.slicingFile();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
