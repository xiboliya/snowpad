/**
 * Copyright (C) 2023 冰原
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;

/**
 * "三角函数"对话框
 * 
 * @author 冰原
 * 
 */
public class TrigonometricDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String[] ANGLE_UNIT_TYPES = new String[] {"角度(°)", "弧度(rad)"};
  private static final String[] TRIGONOMETRIC_TYPES = new String[] {
    "正弦(sin)", "余弦(cos)", "正切(tan)", "余切(cot)", "正割(sec)", "余割(csc)", "正矢(versin)", "余矢(vercos)",
    "反正弦(asin)", "反余弦(acos)", "反正切(atan)", "反余切(acot)", "反正割(asec)", "反余割(acsc)",
    "双曲正弦(sinh)", "双曲余弦(cosh)", "双曲正切(tanh)", "双曲余切(coth)"};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JLabel lblAngle = new JLabel("角度/弧度：");
  private JComboBox<String> cmbUnit = new JComboBox<String>();
  private BaseTextField txtAngle = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JLabel lblTrigonometric = new JLabel("三角函数：");
  private JComboBox<String> cmbTrigonometric = new JComboBox<String>();
  private BaseTextField txtResult = new BaseTextField();
  private BaseButton btnCopy = new BaseButton("复制结果(C)");
  private BaseButton btnCancel = new BaseButton("取消");

  public TrigonometricDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.setTitle("三角函数");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(350, 210);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.lblAngle.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.cmbUnit.setBounds(10, 35, 110, Util.INPUT_HEIGHT);
    this.txtAngle.setBounds(130, 35, 160, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblAngle);
    this.pnlMain.add(this.cmbUnit);
    this.pnlMain.add(this.txtAngle);

    this.lblTrigonometric.setBounds(10, 70, 70, Util.VIEW_HEIGHT);
    this.cmbTrigonometric.setBounds(10, 95, 110, Util.INPUT_HEIGHT);
    this.txtResult.setBounds(130, 95, 160, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTrigonometric);
    this.pnlMain.add(this.cmbTrigonometric);
    this.pnlMain.add(this.txtResult);

    this.btnCopy.setBounds(40, 135, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(190, 135, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCopy);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.cmbUnit.setModel(new DefaultComboBoxModel<String>(ANGLE_UNIT_TYPES));
    this.cmbUnit.setSelectedIndex(0);
    this.cmbTrigonometric.setModel(new DefaultComboBoxModel<String>(TRIGONOMETRIC_TYPES));
    this.cmbTrigonometric.setSelectedIndex(0);
    this.txtResult.setEditable(false);
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
    this.cmbUnit.addKeyListener(this.keyAdapter);
    this.cmbUnit.addItemListener(this);
    this.txtAngle.addCaretListener(this);
    this.txtAngle.addKeyListener(this.keyAdapter);
    this.cmbTrigonometric.addKeyListener(this.keyAdapter);
    this.cmbTrigonometric.addItemListener(this);
    this.txtResult.addKeyListener(this.keyAdapter);
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
    if (this.btnCopy.equals(source)) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
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
    String strAngle = this.txtAngle.getText();
    if (Util.isTextEmpty(strAngle)) {
      this.txtResult.setText("");
    } else {
      int angleUnit = this.cmbUnit.getSelectedIndex();
      int trigonometric = this.cmbTrigonometric.getSelectedIndex();
      try {
        double radians = Double.parseDouble(strAngle);
        if (angleUnit == 0) {
          radians = Math.toRadians(radians);
        }
        double result = this.getResult(radians, trigonometric);
        this.txtResult.setText(String.valueOf(result));
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtResult.setText("");
      }
    }
  }

  private double getResult(double radians, int index) {
    switch (index) {
      case 0:
        return Math.sin(radians);
      case 1:
        return Math.cos(radians);
      case 2:
        return Math.tan(radians);
      case 3:
        return 1 / Math.tan(radians);
      case 4:
        return 1 / Math.cos(radians);
      case 5:
        return 1 / Math.sin(radians);
      case 6:
        return 1 - Math.cos(radians);
      case 7:
        return 1 - Math.sin(radians);
      case 8:
        return Math.asin(radians);
      case 9:
        return Math.acos(radians);
      case 10:
        return Math.atan(radians);
      case 11:
        return 1 / Math.atan(radians);
      case 12:
        return 1 / Math.acos(radians);
      case 13:
        return 1 / Math.asin(radians);
      case 14:
        return Math.sinh(radians);
      case 15:
        return Math.cosh(radians);
      case 16:
        return Math.tanh(radians);
      case 17:
        return 1 / Math.tanh(radians);
    }
    return 0;
  }
  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txtResult.getText());
  }

  /**
   * 设置系统剪贴板的内容
   * 
   * @param strText 要存入剪贴板的文本
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
    if (this.txtAngle.equals(e.getSource())) {
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
