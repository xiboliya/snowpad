/**
 * Copyright (C) 2018 冰原
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

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.security.MessageDigest;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextAreaSpecial;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.chooser.OpenFileChooser;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.util.Util;

/**
 * "加密"对话框
 * 
 * @author 冰原
 * 
 */
public class EncryptDialog extends BaseDialog implements ActionListener, DocumentListener, ChangeListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String[] DIGEST_TYPES = new String[] {"MD5","SHA","SHA-224","SHA-256","SHA-384","SHA-512"}; // 加密的类型
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  // 文本
  private JPanel pnlText = new JPanel();
  private JLabel lblTextT = new JLabel("输入文本：");
  private BaseTextAreaSpecial txaTextT = new BaseTextAreaSpecial();
  private JScrollPane srpTextT = new JScrollPane(this.txaTextT);
  private JCheckBox chkEveryLinesT = new JCheckBox("逐行生成(E)");
  // 文件
  private JPanel pnlFile = new JPanel();
  private JLabel lblPathF = new JLabel("输入文件路径：");
  private BaseTextField txtPathF = new BaseTextField();
  private BaseButton btnSelectFileF = new BaseButton("选择文件(S)");

  private JPanel pnlBottom = new JPanel();
  private JLabel lblDigestType = new JLabel("加密类型：");
  private JComboBox<String> cmbDigestType = new JComboBox<String>(DIGEST_TYPES);
  private JLabel lblEncrypt = new JLabel("加密值：");
  private BaseTextAreaSpecial txaEncrypt = new BaseTextAreaSpecial();
  private JScrollPane srpEncrypt = new JScrollPane(this.txaEncrypt);
  private JCheckBox chkUpperCase = new JCheckBox("结果大写(U)", false);
  private BaseButton btnCopy = new BaseButton("复制结果(C)");
  private BaseButton btnCancel = new BaseButton("取消");

  public EncryptDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("加密值生成");
    this.init();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 350);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    // 文本
    this.pnlText.setLayout(null);
    this.lblTextT.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpTextT.setBounds(10, 35, 270, 80);
    this.pnlText.add(this.lblTextT);
    this.pnlText.add(this.srpTextT);
    this.chkEveryLinesT.setBounds(290, 65, 110, Util.VIEW_HEIGHT);
    this.pnlText.add(this.chkEveryLinesT);
    // 文件
    this.pnlFile.setLayout(null);
    this.lblPathF.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.txtPathF.setBounds(10, 50, 270, Util.INPUT_HEIGHT);
    this.pnlFile.add(this.lblPathF);
    this.pnlFile.add(this.txtPathF);
    this.btnSelectFileF.setBounds(290, 50, 110, Util.BUTTON_HEIGHT);
    this.pnlFile.add(this.btnSelectFileF);

    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 160, 420, 190);
    this.lblDigestType.setBounds(10, 10, 80, Util.VIEW_HEIGHT);
    this.cmbDigestType.setBounds(90, 10, 80, Util.INPUT_HEIGHT);
    this.lblEncrypt.setBounds(10, 35, 70, Util.VIEW_HEIGHT);
    this.srpEncrypt.setBounds(10, 60, 270, 80);
    this.pnlBottom.add(this.lblDigestType);
    this.pnlBottom.add(this.cmbDigestType);
    this.pnlBottom.add(this.lblEncrypt);
    this.pnlBottom.add(this.srpEncrypt);
    this.chkUpperCase.setBounds(290, 40, 110, Util.VIEW_HEIGHT);
    this.btnCopy.setBounds(290, 75, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(290, 115, 110, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.chkUpperCase);
    this.pnlBottom.add(this.btnCopy);
    this.pnlBottom.add(this.btnCancel);
    // 主界面
    this.tpnMain.setBounds(0, 0, 420, 160);
    this.tpnMain.add(this.pnlText, "文本");
    this.tpnMain.add(this.pnlFile, "文件");
    this.pnlMain.add(this.tpnMain);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
    this.pnlMain.add(this.pnlBottom);
    this.txaEncrypt.setEditable(false);
  }

  /**
   * 设置选项卡的当前视图
   * 
   * @param index 视图的索引号
   */
  private void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  private int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.chkEveryLinesT.setMnemonic('E');
    this.btnSelectFileF.setMnemonic('S');
    this.chkUpperCase.setMnemonic('U');
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // 文本
    this.txaTextT.getDocument().addDocumentListener(this);
    this.chkEveryLinesT.addActionListener(this);
    this.txaTextT.addKeyListener(this.keyAdapter);
    this.chkEveryLinesT.addKeyListener(this.keyAdapter);
    // 文件
    this.txtPathF.getDocument().addDocumentListener(this);
    this.btnSelectFileF.addActionListener(this);
    this.txtPathF.addKeyListener(this.keyAdapter);
    this.btnSelectFileF.addKeyListener(this.buttonKeyAdapter);

    this.cmbDigestType.addItemListener(this);
    this.cmbDigestType.addKeyListener(this.keyAdapter);
    this.txaEncrypt.addKeyListener(this.keyAdapter);
    this.chkUpperCase.addActionListener(this);
    this.chkUpperCase.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.chkEveryLinesT.equals(source)) {
      this.showStringEncrypt();
    } else if (this.btnSelectFileF.equals(source)) {
      this.selectFile();
    } else if (this.chkUpperCase.equals(source)) {
      this.toChangeCaseResult();
    } else if (this.btnCopy.equals(source)) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * 刷新各控件的显示
   */
  public void refreshView() {
    String str = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(str) && this.getTabbedIndex() == 0) {
      this.txaTextT.setText(str);
    }
    this.showEncrypt();
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
      this.txtPathF.setText(file.getAbsolutePath());
    }
  }

  /**
   * 显示加密值
   */
  private void showEncrypt() {
    if (this.getTabbedIndex() == 0) {
      this.showStringEncrypt();
    } else {
      this.showFileEncrypt();
    }
  }

  /**
   * 显示字符串的加密值
   */
  private void showStringEncrypt() {
    String text = this.txaTextT.getText();
    if (Util.isTextEmpty(text)) {
      this.txaEncrypt.setText("");
      return;
    }
    String digestType = this.cmbDigestType.getSelectedItem().toString();
    String encrypt = "";
    if (this.chkEveryLinesT.isSelected()) {
      String[] arrLines = text.split("\n", -1);
      for (String str : arrLines) {
        encrypt += (this.getStringDigest(str, digestType) + "\n");
      }
    } else {
      // 根据不同的操作系统使用不同的换行符
      text = text.replaceAll(LineSeparator.UNIX.toString(), Util.LINE_SEPARATOR);
      encrypt = this.getStringDigest(text, digestType);
    }
    this.toUpdateEncrypt(encrypt);
  }

  /**
   * 获取字符串的加密值
   * 
   * @param string 待加密的字符串
   * @param digestType 加密类型，支持：MD5、SHA、SHA-224、SHA-256、SHA-384、SHA-512
   * @return 字符串的加密值
   */
  private String getStringDigest(String string, String digestType) {
    if (Util.isTextEmpty(string)) {
      return "";
    }
    MessageDigest digest = null;
    StringBuilder hex = new StringBuilder();
    try {
      digest = MessageDigest.getInstance(digestType);
      byte[] bytes = digest.digest(string.getBytes("utf-8"));
      String hexStr = "0123456789abcdef";
      for (int i = 0; i < bytes.length; i++) {
        // 字节高4位
        hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
        // 字节低4位
        hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
      }
    } catch (Exception x) {
      // x.printStackTrace();
    }
    return hex.toString();
  }

  /**
   * 显示文件的加密值
   */
  private void showFileEncrypt() {
    String path = this.txtPathF.getText();
    if (Util.isTextEmpty(path)) {
      this.txaEncrypt.setText("");
      return;
    }
    String digestType = this.cmbDigestType.getSelectedItem().toString();
    String encrypt = this.getFileDigest(new File(path), digestType);
    this.toUpdateEncrypt(encrypt);
  }

  /**
   * 获取文件的加密值
   * 
   * @param file 待加密的文件
   * @param digestType 加密类型，支持：MD5、SHA、SHA-224、SHA-256、SHA-384、SHA-512
   * @return 文件的加密值
   */
  private String getFileDigest(File file, String digestType) {
    if (!file.isFile()) {
      return "";
    }
    MessageDigest digest = null;
    FileInputStream fileInputStream = null;
    byte[] buffer = new byte[1024];
    int len;
    try {
      digest = MessageDigest.getInstance(digestType);
      fileInputStream = new FileInputStream(file);
      while ((len = fileInputStream.read(buffer)) != -1) {
        digest.update(buffer, 0, len);
      }
    } catch (Exception x) {
      // x.printStackTrace();
      return "";
    } finally {
      try {
        fileInputStream.close();
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
    BigInteger bigInt = new BigInteger(1, digest.digest());
    return bigInt.toString(16);
  }

  /**
   * 更新加密值
   * 
   * @param encrypt 加密值
   */
  private void toUpdateEncrypt(String encrypt) {
    if (Util.isTextEmpty(encrypt)) {
      this.txaEncrypt.setText("");
    } else {
      if (this.chkUpperCase.isSelected()) {
        encrypt = encrypt.toUpperCase();
      } else {
        encrypt = encrypt.toLowerCase();
      }
      this.txaEncrypt.setText(encrypt);
    }
  }

  /**
   * "结果大写"的处理方法
   */
  private void toChangeCaseResult() {
    String result = this.txaEncrypt.getText();
    if (Util.isTextEmpty(result)) {
      return;
    }
    if (this.chkUpperCase.isSelected()) {
      result = result.toUpperCase();
    } else {
      result = result.toLowerCase();
    }
    this.txaEncrypt.setText(result);
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    ListenerManager.getInstance().postClipboardEvent(this.txaEncrypt.getText());
  }

  /**
   * 默认的“取消”操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  @Override
  public void onEnter() {
  }

  private void execDocumentEvent(DocumentEvent e) {
    Object source = e.getDocument();
    if (this.txaTextT.getDocument().equals(source)) {
      this.showStringEncrypt();
    } else if (this.txtPathF.getDocument().equals(source)) {
      this.showFileEncrypt();
    }
  }

  /**
   * 当文本控件插入文本时，将触发此事件
   */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }

  /**
   * 当文本控件删除文本时，将触发此事件
   */
  @Override
  public void removeUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }

  /**
   * 当文本控件修改文本时，将触发此事件
   */
  @Override
  public void changedUpdate(DocumentEvent e) {
    this.execDocumentEvent(e);
  }

  /**
   * 当选项卡改变当前视图时调用
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    this.showEncrypt();
  }

  /**
   * 当所选项更改时调用
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    this.showEncrypt();
  }
}
