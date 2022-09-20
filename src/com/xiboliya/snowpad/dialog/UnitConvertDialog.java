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

import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;

/**
 * "单位换算"对话框
 * 
 * @author 冰原
 * 
 */
public class UnitConvertDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  // 单位类型
  private static final String[] UNIT_TYPES = new String[] {"存储"};
  // 存储单位
  private static final String[] UNIT_TYPE_MEMORY_NAME =
    new String[] {"比特(bit)","字节(B)","千字节(KB)","兆字节(MB)","千兆字节(GB)","太字节(TB)","拍字节(PB)","艾字节(EB)"};
  // 存储换算比例
  private static final int[] UNIT_TYPE_MEMORY_RATE = new int[] {1, 8, 1024, 1024, 1024, 1024, 1024, 1024};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JLabel lblUnitType = new JLabel("单位类型：");
  private JComboBox<String> cmbUnitType = new JComboBox<String>();
  private JLabel lblNumber = new JLabel("换算数值：");
  private JComboBox<String> cmbNumber = new JComboBox<String>();
  private BaseTextField txtNumber = new BaseTextField();
  private JLabel lblWarning = new JLabel("警告：含有非法字符或超出范围！");
  private JButton btnExchange = new JButton("互换单位");
  private JLabel lblResult = new JLabel("换算结果：");
  private JCheckBox chkUpperCase = new JCheckBox("结果大写(U)", false);
  private JComboBox<String> cmbResult = new JComboBox<String>();
  private BaseTextField txtResult = new BaseTextField();
  private JButton btnCopy = new JButton("复制结果(C)");
  private JButton btnCancel = new JButton("取消");

  public UnitConvertDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.setTitle("单位换算");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 300);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.lblUnitType.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.cmbUnitType.setBounds(10, 35, 100, Util.INPUT_HEIGHT);
    this.lblNumber.setBounds(10, 70, 70, Util.VIEW_HEIGHT);
    this.lblWarning.setBounds(90, 70, 190, Util.VIEW_HEIGHT);
    this.cmbNumber.setBounds(10, 95, 100, Util.INPUT_HEIGHT);
    this.txtNumber.setBounds(120, 95, 160, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblUnitType);
    this.pnlMain.add(this.cmbUnitType);
    this.pnlMain.add(this.lblNumber);
    this.pnlMain.add(this.lblWarning);
    this.pnlMain.add(this.cmbNumber);
    this.pnlMain.add(this.txtNumber);

    this.btnExchange.setBounds(10, 130, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnExchange);

    this.lblResult.setBounds(10, 160, 70, Util.VIEW_HEIGHT);
    this.chkUpperCase.setBounds(290, 160, 110, Util.VIEW_HEIGHT);
    this.cmbResult.setBounds(10, 185, 100, Util.INPUT_HEIGHT);
    this.txtResult.setBounds(120, 185, 160, Util.INPUT_HEIGHT);
    this.btnCopy.setBounds(290, 185, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblResult);
    this.pnlMain.add(this.chkUpperCase);
    this.pnlMain.add(this.cmbResult);
    this.pnlMain.add(this.txtResult);
    this.pnlMain.add(this.btnCopy);

    this.btnCancel.setBounds(160, 225, 80, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCancel);
    this.txtResult.setEditable(false);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.lblWarning.setForeground(Color.RED);
    this.lblWarning.setVisible(false);
    this.cmbUnitType.setModel(new DefaultComboBoxModel<String>(UNIT_TYPES));
    this.cmbUnitType.setSelectedIndex(0);
  }

  /**
   * 刷新控件显示
   */
  private void refreshView() {
    int index = this.cmbUnitType.getSelectedIndex();
    String[] unitType = null;
    switch (index) {
      case 0:
        unitType = UNIT_TYPE_MEMORY_NAME;
        break;
    }
    this.cmbNumber.setModel(new DefaultComboBoxModel<String>(unitType));
    this.cmbNumber.setSelectedIndex(0);
    this.cmbResult.setModel(new DefaultComboBoxModel<String>(unitType));
    this.cmbResult.setSelectedIndex(0);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.btnCopy.setMnemonic('C');
    this.chkUpperCase.setMnemonic('U');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.cmbUnitType.addKeyListener(this.keyAdapter);
    this.cmbUnitType.addItemListener(this);
    this.cmbNumber.addKeyListener(this.keyAdapter);
    this.cmbNumber.addItemListener(this);
    this.txtNumber.addCaretListener(this);
    this.txtNumber.addKeyListener(this.keyAdapter);
    this.cmbResult.addKeyListener(this.keyAdapter);
    this.cmbResult.addItemListener(this);
    this.chkUpperCase.addActionListener(this);
    this.chkUpperCase.addKeyListener(this.keyAdapter);
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
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnExchange.equals(e.getSource())) {
      this.exchange();
    } else if (this.chkUpperCase.equals(e.getSource())) {
      this.toChangeCaseResult();
    }
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
      int index = this.cmbUnitType.getSelectedIndex();
      int[] unitTypeRate = null;
      switch (index) {
        case 0:
          unitTypeRate = UNIT_TYPE_MEMORY_RATE;
          break;
      }
      int indexNumber = this.cmbNumber.getSelectedIndex();
      int indexResult = this.cmbResult.getSelectedIndex();
      try {
        BigDecimal number = new BigDecimal(strNumber);
        String result = strNumber;
        if (indexNumber > indexResult) {
          int count = indexNumber - indexResult;
          BigDecimal rate = new BigDecimal(1);
          for (; count > 0; count--) {
            rate = rate.multiply(new BigDecimal(unitTypeRate[indexResult + count]));
          }
          result = number.multiply(rate).toString();
        } else if (indexNumber < indexResult) {
          int count = indexResult - indexNumber;
          BigDecimal rate = new BigDecimal(1);
          for (; count > 0; count--) {
            rate = rate.multiply(new BigDecimal(unitTypeRate[indexNumber + count]));
          }
          result = number.divide(rate).toString();
        }
        if (this.chkUpperCase.isSelected()) {
          result = result.toUpperCase();
        } else {
          result = result.toLowerCase();
        }
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
   * "互换单位"的处理方法
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
   * "结果大写"的处理方法
   */
  private void toChangeCaseResult() {
    String result = this.txtResult.getText();
    if (Util.isTextEmpty(result)) {
      return;
    }
    if (this.chkUpperCase.isSelected()) {
      result = result.toUpperCase();
    } else {
      result = result.toLowerCase();
    }
    this.txtResult.setText(result);
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
    if (this.txtNumber.equals(e.getSource())) {
      this.showResult();
    }
  }

  /**
   * 当用户已选定或取消选定某项时，触发此事件
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
  }
}
