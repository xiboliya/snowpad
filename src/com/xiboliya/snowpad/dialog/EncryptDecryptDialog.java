/**
 * Copyright (C) 2025 冰原
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.util.Util;

/**
 * "加密/解密"对话框
 * 
 * @author 冰原
 * 
 */
public class EncryptDecryptDialog extends BaseDialog implements ActionListener, DocumentListener, ChangeListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String[] DIGEST_TYPES = new String[] {"Base64"}; // 加密的类型
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  // 加密
  private JPanel pnlEncrypt = new JPanel();
  private JLabel lblTextE = new JLabel("输入文本：");
  private BaseTextAreaSpecial txaTextE = new BaseTextAreaSpecial();
  private JScrollPane srpTextE = new JScrollPane(this.txaTextE);
  private JCheckBox chkEveryLinesE = new JCheckBox("逐行加密(E)");
  // 解密
  private JPanel pnlDecrypt = new JPanel();
  private JLabel lblTextD = new JLabel("输入文本：");
  private BaseTextAreaSpecial txaTextD = new BaseTextAreaSpecial();
  private JScrollPane srpTextD = new JScrollPane(this.txaTextD);
  private JCheckBox chkEveryLinesD = new JCheckBox("逐行解密(D)");

  private JPanel pnlBottom = new JPanel();
  private JLabel lblDigestType = new JLabel("计算类型：");
  private JComboBox<String> cmbDigestType = new JComboBox<String>(DIGEST_TYPES);
  private JLabel lblResult = new JLabel("计算结果：");
  private BaseTextAreaSpecial txaResult = new BaseTextAreaSpecial();
  private JScrollPane srpResult = new JScrollPane(this.txaResult);
  private BaseButton btnCopy = new BaseButton("复制结果(C)");
  private BaseButton btnCancel = new BaseButton("取消");

  public EncryptDecryptDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("加密/解密");
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
    // 加密
    this.pnlEncrypt.setLayout(null);
    this.lblTextE.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpTextE.setBounds(10, 35, 270, 80);
    this.pnlEncrypt.add(this.lblTextE);
    this.pnlEncrypt.add(this.srpTextE);
    this.chkEveryLinesE.setBounds(290, 65, 110, Util.VIEW_HEIGHT);
    this.pnlEncrypt.add(this.chkEveryLinesE);
    // 解密
    this.pnlDecrypt.setLayout(null);
    this.lblTextD.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpTextD.setBounds(10, 35, 270, 80);
    this.pnlDecrypt.add(this.lblTextD);
    this.pnlDecrypt.add(this.srpTextD);
    this.chkEveryLinesD.setBounds(290, 65, 110, Util.VIEW_HEIGHT);
    this.pnlDecrypt.add(this.chkEveryLinesD);

    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 160, 420, 190);
    this.lblDigestType.setBounds(10, 10, 80, Util.VIEW_HEIGHT);
    this.cmbDigestType.setBounds(90, 10, 120, Util.INPUT_HEIGHT);
    this.lblResult.setBounds(10, 35, 70, Util.VIEW_HEIGHT);
    this.srpResult.setBounds(10, 60, 270, 80);
    this.pnlBottom.add(this.lblDigestType);
    this.pnlBottom.add(this.cmbDigestType);
    this.pnlBottom.add(this.lblResult);
    this.pnlBottom.add(this.srpResult);
    this.btnCopy.setBounds(290, 75, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(290, 115, 110, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCopy);
    this.pnlBottom.add(this.btnCancel);
    // 主界面
    this.tpnMain.setBounds(0, 0, 420, 160);
    this.tpnMain.add(this.pnlEncrypt, "加密");
    this.tpnMain.add(this.pnlDecrypt, "解密");
    this.pnlMain.add(this.tpnMain);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
    this.pnlMain.add(this.pnlBottom);
    this.txaResult.setEditable(false);
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
    this.chkEveryLinesE.setMnemonic('E');
    this.chkEveryLinesD.setMnemonic('D');
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // 加密
    this.txaTextE.getDocument().addDocumentListener(this);
    this.chkEveryLinesE.addActionListener(this);
    this.txaTextE.addKeyListener(this.keyAdapter);
    this.chkEveryLinesE.addKeyListener(this.keyAdapter);
    // 解密
    this.txaTextD.getDocument().addDocumentListener(this);
    this.chkEveryLinesD.addActionListener(this);
    this.txaTextD.addKeyListener(this.keyAdapter);
    this.chkEveryLinesD.addKeyListener(this.keyAdapter);

    this.cmbDigestType.addItemListener(this);
    this.cmbDigestType.addKeyListener(this.keyAdapter);
    this.txaResult.addKeyListener(this.keyAdapter);
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
    if (this.chkEveryLinesE.equals(source)) {
      this.showStringEncrypt();
    } else if (this.chkEveryLinesD.equals(source)) {
      this.showStringDecrypt();
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
    if (!Util.isTextEmpty(str)) {
      if (this.getTabbedIndex() == 0) {
        this.txaTextE.setText(str);
      } else {
        this.txaTextD.setText(str);
      }
    }
    this.showResult();
  }

  /**
   * 显示计算结果
   */
  private void showResult() {
    if (this.getTabbedIndex() == 0) {
      this.showStringEncrypt();
    } else {
      this.showStringDecrypt();
    }
  }

  /**
   * 显示字符串的加密值
   */
  private void showStringEncrypt() {
    String text = this.txaTextE.getText();
    if (Util.isTextEmpty(text)) {
      this.txaResult.setText("");
      return;
    }
    int digestType = this.cmbDigestType.getSelectedIndex();
    String result = "";
    if (this.chkEveryLinesE.isSelected()) {
      String[] arrLines = text.split("\n", -1);
      for (String str : arrLines) {
        result += (this.getStringEncrypt(str, digestType) + "\n");
      }
    } else {
      // 根据不同的操作系统使用不同的换行符
      text = text.replaceAll(LineSeparator.UNIX.toString(), Util.LINE_SEPARATOR);
      result = this.getStringEncrypt(text, digestType);
    }
    this.toUpdateResult(result);
  }

  /**
   * 获取字符串的加密值
   * 
   * @param string 待加密的字符串
   * @param digestType 加密类型，支持：Base64
   * @return 字符串的加密值
   */
  private String getStringEncrypt(String string, int digestType) {
    if (Util.isTextEmpty(string)) {
      return "";
    }
    switch (digestType) {
      case 0:
        return Util.encodeTextByBase64(string);
      default:
        return string;
    }
  }

  /**
   * 显示文件的加密值
   */
  private void showStringDecrypt() {
    String text = this.txaTextD.getText();
    if (Util.isTextEmpty(text)) {
      this.txaResult.setText("");
      return;
    }
    int digestType = this.cmbDigestType.getSelectedIndex();
    String result = "";
    if (this.chkEveryLinesD.isSelected()) {
      String[] arrLines = text.split("\n", -1);
      for (String str : arrLines) {
        result += (this.getStringDecrypt(str, digestType) + "\n");
      }
    } else {
      // 根据不同的操作系统使用不同的换行符
      text = text.replaceAll(LineSeparator.UNIX.toString(), Util.LINE_SEPARATOR);
      result = this.getStringDecrypt(text, digestType);
    }
    this.toUpdateResult(result);
  }

  /**
   * 获取字符串的解密值
   * 
   * @param string 待解密的字符串
   * @param digestType 加密类型，支持：Base64
   * @return 字符串的解密值
   */
  private String getStringDecrypt(String string, int digestType) {
    if (Util.isTextEmpty(string)) {
      return "";
    }
    switch (digestType) {
      case 0:
        return Util.decodeTextByBase64(string);
      default:
        return string;
    }
  }

  /**
   * 更新加密值
   * 
   * @param result 加密值
   */
  private void toUpdateResult(String result) {
    if (Util.isTextEmpty(result)) {
      this.txaResult.setText("");
    } else {
      this.txaResult.setText(result);
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    ListenerManager.getInstance().postClipboardEvent(this.txaResult.getText());
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

  /**
   * 当文本控件插入文本时，将触发此事件
   */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当文本控件删除文本时，将触发此事件
   */
  @Override
  public void removeUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当文本控件修改文本时，将触发此事件
   */
  @Override
  public void changedUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当选项卡改变当前视图时调用
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    this.showResult();
  }

  /**
   * 当所选项更改时调用
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
  }

}
