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

package com.xiboliya.snowpad;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * "加密"对话框
 * 
 * @author 冰原
 * 
 */
public class EncryptDialog extends BaseDialog implements ActionListener, CaretListener, ChangeListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
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
  private JButton btnSelectFileF = new JButton("选择文件(S)");

  private JPanel pnlBottom = new JPanel();
  private JLabel lblDigestType = new JLabel("加密类型：");
  private JComboBox<String> cmbDigestType = new JComboBox<String>(Util.DIGEST_TYPES);
  private JLabel lblEncrypt = new JLabel("加密值：");
  private BaseTextAreaSpecial txaEncrypt = new BaseTextAreaSpecial();
  private JScrollPane srpEncrypt = new JScrollPane(this.txaEncrypt);
  private JButton btnCopy = new JButton("复制结果(C)");
  private JButton btnCancel = new JButton("取消");

  public EncryptDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
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
    this.txtPathF.setBounds(10, 50, 270, 30);
    this.pnlFile.add(this.lblPathF);
    this.pnlFile.add(this.txtPathF);
    this.btnSelectFileF.setBounds(290, 52, 110, Util.BUTTON_HEIGHT);
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
    this.btnCopy.setBounds(290, 75, 110, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCopy);
    this.btnCancel.setBounds(290, 115, 110, Util.BUTTON_HEIGHT);
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
   * @param index
   *          视图的索引号
   */
  public void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  public int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.chkEveryLinesT.setMnemonic('E');
    this.btnSelectFileF.setMnemonic('S');
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // 文本
    this.txaTextT.addCaretListener(this);
    this.chkEveryLinesT.addActionListener(this);
    this.txaTextT.addKeyListener(this.keyAdapter);
    this.chkEveryLinesT.addKeyListener(this.keyAdapter);
    // 文件
    this.txtPathF.addCaretListener(this);
    this.btnSelectFileF.addActionListener(this);
    this.txtPathF.addKeyListener(this.keyAdapter);
    this.btnSelectFileF.addKeyListener(this.buttonKeyAdapter);

    this.cmbDigestType.addItemListener(this);
    this.cmbDigestType.addKeyListener(this.keyAdapter);
    this.txaEncrypt.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.chkEveryLinesT.equals(e.getSource())) {
      this.showStringEncrypt();
    } else if (this.btnSelectFileF.equals(e.getSource())) {
      this.selectFile();
    } else if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
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
        encrypt += (Util.getStringDigest(str, digestType) + "\n");
      }
    } else {
      // 根据不同的操作系统使用不同的换行符
      text = text.replaceAll(LineSeparator.UNIX.toString(), Util.LINE_SEPARATOR);
      encrypt = Util.getStringDigest(text, digestType);
    }
    if (Util.isTextEmpty(encrypt)) {
      this.txaEncrypt.setText("");
    } else {
      this.txaEncrypt.setText(encrypt);
    }
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
    String encrypt = Util.getFileDigest(new File(path), digestType);
    if (Util.isTextEmpty(encrypt)) {
      this.txaEncrypt.setText("");
    } else {
      this.txaEncrypt.setText(encrypt);
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txaEncrypt.getText());
  }

  /**
   * 设置系统剪贴板的内容
   * 
   * @param strText
   *          要存入剪贴板的文本
   */
  private void setClipboardContents(String strText) {
    if (Util.isTextEmpty(strText)) {
      return;
    }
    StringSelection ss = new StringSelection(strText);
    this.clip.setContents(ss, ss);
  }

  /**
   * 默认的“取消”操作方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  public void onEnter() {
  }

  /**
   * 当文本框的光标发生变化时，触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    if (this.txaTextT.equals(e.getSource())) {
      this.showStringEncrypt();
    } else if (this.txtPathF.equals(e.getSource())) {
      this.showFileEncrypt();
    }
  }

  /**
   * 当选项卡改变当前视图时调用
   */
  public void stateChanged(ChangeEvent e) {
    this.showEncrypt();
  }

  /**
   * 当所选项更改时调用
   */
  public void itemStateChanged(ItemEvent e) {
    this.showEncrypt();
  }
}
