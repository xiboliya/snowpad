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

import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * "进制转换"对话框
 * 
 * @author 冰原
 * 
 */
public class NumberConvertDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JLabel lblNumber = new JLabel("转换数字：");
  private JComboBox<String> cmbNumber = new JComboBox<String>();
  private BaseTextField txtNumber = new BaseTextField();
  private JLabel lblWarning = new JLabel("警告：含有非法字符或超出范围！");
  private JButton btnExchange = new JButton("互换进制");
  private JLabel lblResult = new JLabel("转换结果：");
  private JComboBox<String> cmbResult = new JComboBox<String>();
  private BaseTextField txtResult = new BaseTextField();
  private JButton btnCopy = new JButton("复制结果(C)");
  private JButton btnCancel = new JButton("取消");

  public NumberConvertDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("数字进制转换");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 240);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.lblWarning.setForeground(Color.RED);
    this.pnlMain.setLayout(null);
    this.lblNumber.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.lblWarning.setBounds(90, 10, 190, Util.VIEW_HEIGHT);
    this.cmbNumber.setBounds(10, 35, 70, Util.INPUT_HEIGHT);
    this.txtNumber.setBounds(90, 35, 190, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblNumber);
    this.pnlMain.add(this.lblWarning);
    this.pnlMain.add(this.cmbNumber);
    this.pnlMain.add(this.txtNumber);

    this.btnExchange.setBounds(10, 70, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnExchange);

    this.lblResult.setBounds(10, 100, 70, Util.VIEW_HEIGHT);
    this.cmbResult.setBounds(10, 125, 70, Util.INPUT_HEIGHT);
    this.txtResult.setBounds(90, 125, 190, Util.INPUT_HEIGHT);
    this.btnCopy.setBounds(290, 125, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblResult);
    this.pnlMain.add(this.cmbResult);
    this.pnlMain.add(this.txtResult);
    this.pnlMain.add(this.btnCopy);

    this.btnCancel.setBounds(160, 165, 80, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCancel);
    this.txtResult.setEditable(false);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    String[] array = new String[35];
    for (int i = 2; i <= 36; i++) {
      array[i - 2] = i + "进制";
    }
    this.cmbNumber.setModel(new DefaultComboBoxModel<String>(array));
    this.cmbNumber.setSelectedIndex(8);
    this.cmbResult.setModel(new DefaultComboBoxModel<String>(array));
    this.cmbResult.setSelectedIndex(14);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.cmbNumber.addKeyListener(this.keyAdapter);
    this.cmbNumber.addItemListener(this);
    this.txtNumber.addCaretListener(this);
    this.txtNumber.addKeyListener(this.keyAdapter);
    this.cmbResult.addKeyListener(this.keyAdapter);
    this.cmbResult.addItemListener(this);
    this.txtResult.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnExchange.addActionListener(this);
    this.btnExchange.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnExchange.equals(e.getSource())) {
      this.exchange();
    }
  }

  /**
   * 刷新各控件的显示
   */
  public void refreshView() {
    this.showResult();
  }

  /**
   * 显示结果
   */
  private void showResult() {
    String strNumber = this.txtNumber.getText();
    if (Util.isTextEmpty(strNumber)) {
      this.lblWarning.setVisible(false);
      this.txtResult.setText("");
    } else {
      int numberBase = this.cmbNumber.getSelectedIndex() + 2;
      int resultBase = this.cmbResult.getSelectedIndex() + 2;
      try {
        long number = Long.parseLong(strNumber, numberBase);
        String result = Long.toString(number, resultBase);
        this.txtResult.setText(result);
        this.lblWarning.setVisible(false);
      } catch (Exception x) {
        // x.printStackTrace();
        this.lblWarning.setVisible(true);
        this.txtResult.setText("");
      }
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txtResult.getText());
  }

  /**
   * "互换进制"的处理方法
   */
  private void exchange() {
    int indexNumber = this.cmbNumber.getSelectedIndex();
    int indexResult = this.cmbResult.getSelectedIndex();
    if (indexNumber != indexResult) {
      this.cmbNumber.setSelectedIndex(indexResult);
      this.cmbResult.setSelectedIndex(indexNumber);
    }
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
    if (this.txtNumber.equals(e.getSource())) {
      this.showResult();
    }
  }

  /**
   * 当用户已选定或取消选定某项时，触发此事件
   */
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
  }
}
