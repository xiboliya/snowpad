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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextAreaSpecial;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.util.Util;

/**
 * "文本格式转换"对话框
 * 
 * @author 冰原
 * 
 */
public class TextConvertDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  // 编码格式名称的数组
  private static final String[] ENCODING_NAMES = new String[] {
      CharEncoding.ANSI.getName(), CharEncoding.UBE.getName(),
      CharEncoding.ULE.getName(), CharEncoding.UTF8.getName(), CharEncoding.BASE.getName() };
  // 编码格式数据的数组
  private static final String[] ENCODING_VALUES = new String[] {
      CharEncoding.ANSI.toString(), CharEncoding.UBE.toString(),
      CharEncoding.ULE.toString(), CharEncoding.UTF8.toString(), CharEncoding.BASE.toString() };
  // 换行符格式名称的数组
  private static final String[] LINE_SEPARATOR_NAMES = new String[] {
      LineSeparator.WINDOWS.getName(), LineSeparator.UNIX.getName(), LineSeparator.MACINTOSH.getName() };
  // 换行符格式数据的数组
  private static final String[] LINE_SEPARATOR_VALUES = new String[] {
      LineSeparator.WINDOWS.toString(), LineSeparator.UNIX.toString(), LineSeparator.MACINTOSH.toString() };
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JLabel lblText = new JLabel("输入文本：");
  private BaseTextAreaSpecial txaText = new BaseTextAreaSpecial();
  private JScrollPane srpText = new JScrollPane(this.txaText);
  private JLabel lblEncoding = new JLabel("编码格式：");
  private JComboBox<String> cmbEncoding = new JComboBox<String>(ENCODING_NAMES);
  private JLabel lblResult = new JLabel("转为十六进制结果：");
  private BaseTextAreaSpecial txaResult = new BaseTextAreaSpecial();
  private JLabel lblResultSeparator = new JLabel("结果分隔符：");
  private BaseTextField txtResultSeparator = new BaseTextField(" ", true, ".{0,1}"); // 限制用户输入的字符数量不能超过1个
  private JLabel lblLineSeparator = new JLabel("换行符格式：");
  private JComboBox<String> cmbLineSeparator = new JComboBox<String>(LINE_SEPARATOR_NAMES);
  private JCheckBox chkAddBom = new JCheckBox("添加BOM", false);
  private JCheckBox chkUpperCase = new JCheckBox("结果大写(U)", false);
  private JButton btnCopy = new JButton("复制结果(C)");
  private JButton btnCancel = new JButton("取消");

  public TextConvertDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("文本格式转换");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 390);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.lblText.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpText.setBounds(10, 35, 390, 80);
    this.pnlMain.add(this.lblText);
    this.pnlMain.add(this.srpText);
    this.lblEncoding.setBounds(10, 130, 70, Util.VIEW_HEIGHT);
    this.cmbEncoding.setBounds(80, 130, 150, Util.INPUT_HEIGHT);
    this.lblResultSeparator.setBounds(280, 130, 80, Util.VIEW_HEIGHT);
    this.txtResultSeparator.setBounds(360, 130, 30, Util.INPUT_HEIGHT);
    this.lblLineSeparator.setBounds(10, 160, 80, Util.VIEW_HEIGHT);
    this.cmbLineSeparator.setBounds(90, 160, 140, Util.INPUT_HEIGHT);
    this.chkAddBom.setBounds(280, 160, 110, Util.VIEW_HEIGHT);
    this.lblResult.setBounds(10, 185, 130, Util.VIEW_HEIGHT);
    this.chkUpperCase.setBounds(280, 185, 110, Util.VIEW_HEIGHT);
    this.txaResult.setBounds(10, 210, 390, 80);
    this.pnlMain.add(this.lblEncoding);
    this.pnlMain.add(this.cmbEncoding);
    this.pnlMain.add(this.lblResultSeparator);
    this.pnlMain.add(this.txtResultSeparator);
    this.pnlMain.add(this.lblLineSeparator);
    this.pnlMain.add(this.cmbLineSeparator);
    this.pnlMain.add(this.chkAddBom);
    this.pnlMain.add(this.lblResult);
    this.pnlMain.add(this.chkUpperCase);
    this.pnlMain.add(this.txaResult);
    this.btnCopy.setBounds(60, 310, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 310, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCopy);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.cmbEncoding.setSelectedIndex(4);
    this.cmbLineSeparator.setSelectedIndex(0);
    this.txaResult.setLineWrap(true);
    this.txaResult.setWrapStyleWord(true);
    this.txaResult.setBorder(new EtchedBorder());
    this.txaResult.setEditable(false);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.chkAddBom.setMnemonic('B');
    this.chkUpperCase.setMnemonic('U');
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.txaText.addCaretListener(this);
    this.txaText.addKeyListener(this.keyAdapter);
    this.cmbEncoding.addItemListener(this);
    this.cmbEncoding.addKeyListener(this.keyAdapter);
    this.txtResultSeparator.addCaretListener(this);
    this.txtResultSeparator.addKeyListener(this.keyAdapter);
    this.cmbLineSeparator.addItemListener(this);
    this.cmbLineSeparator.addKeyListener(this.keyAdapter);
    this.txaResult.addKeyListener(this.keyAdapter);
    this.chkAddBom.addActionListener(this);
    this.chkAddBom.addKeyListener(this.keyAdapter);
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
    if (this.chkAddBom.equals(e.getSource())) {
      this.toChangeBomResult();
    } else if (this.chkUpperCase.equals(e.getSource())) {
      this.toChangeCaseResult();
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
    if (!Util.isTextEmpty(str)) {
      this.txaText.setText(str);
    }
    this.showResult();
  }

  /**
   * 显示结果
   */
  private void showResult() {
    String text = this.txaText.getText();
    if (Util.isTextEmpty(text)) {
      this.txaResult.setText("");
      return;
    }
    int indexEncoding = this.cmbEncoding.getSelectedIndex();
    String encoding = ENCODING_VALUES[indexEncoding];
    int indexSeparator = this.cmbLineSeparator.getSelectedIndex();
    text = text.replaceAll(LineSeparator.UNIX.toString(), LINE_SEPARATOR_VALUES[indexSeparator]);
    String result = this.getBomString(indexEncoding) + this.getHexString(text, encoding);
    this.toUpdateResult(result);
  }

  /**
   * 获取字符串的十六进制形式
   * 
   * @param string
   *          字符串
   * @param encoding
   *          编码类型
   * @return 字符串的十六进制形式
   */
  private String getHexString(String string, String encoding) {
    if (Util.isTextEmpty(string)) {
      return "";
    }
    StringBuilder stbHex = new StringBuilder();
    try {
      byte[] bytes = string.getBytes(encoding);
      String strSeparator = this.txtResultSeparator.getText();
      for (byte item : bytes) {
        String result = Integer.toHexString(item & 0xff);
        // 不够2位的前面补0
        if (result.length() == 1) {
          result = '0' + result;
        }
        stbHex.append(result).append(strSeparator);
      }
      stbHex.deleteCharAt(stbHex.length() - 1);
    } catch (Exception x) {
      // x.printStackTrace();
    }
    return stbHex.toString();
  }

  /**
   * 根据编码格式获取BOM
   * 
   * @param indexEncoding
   *          编码格式对应的索引
   * @return 编码格式对应的BOM
   */
  private String getBomString(int indexEncoding) {
    StringBuilder stbBom = new StringBuilder();
    if (!this.chkAddBom.isSelected()) {
      return stbBom.toString();
    }
    String strSeparator = this.txtResultSeparator.getText();
    switch (indexEncoding) {
      case 1:
        stbBom.append("fe").append(strSeparator);
        stbBom.append("ff").append(strSeparator);
        break;
      case 2:
        stbBom.append("ff").append(strSeparator);
        stbBom.append("fe").append(strSeparator);
        break;
      case 3:
        stbBom.append("ef").append(strSeparator);
        stbBom.append("bb").append(strSeparator);
        stbBom.append("bf").append(strSeparator);
        break;
    }
    return stbBom.toString();
  }

  /**
   * 更新结果
   * 
   * @param result
   *          结果
   */
  private void toUpdateResult(String result) {
    if (Util.isTextEmpty(result)) {
      this.txaResult.setText("");
    } else {
      if (this.chkUpperCase.isSelected()) {
        result = result.toUpperCase();
      } else {
        result = result.toLowerCase();
      }
      this.txaResult.setText(result);
    }
  }

  /**
   * "添加BOM"的处理方法
   */
  private void toChangeBomResult() {
    this.showResult();
  }

  /**
   * "结果大写"的处理方法
   */
  private void toChangeCaseResult() {
    String result = this.txaResult.getText();
    if (Util.isTextEmpty(result)) {
      return;
    }
    if (this.chkUpperCase.isSelected()) {
      result = result.toUpperCase();
    } else {
      result = result.toLowerCase();
    }
    this.txaResult.setText(result);
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txaResult.getText());
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
   * 当文本框的光标发生变化时，触发此事件
   */
  @Override
  public void caretUpdate(CaretEvent e) {
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
