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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;

/**
 * "时间戳转换"对话框
 * 
 * @author 冰原
 * 
 */
public class TimeStampConvertDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  // 时间戳单位类型
  private static final String[] TIMESTAMP_UNIT_TYPES = new String[] {"毫秒(ms)", "秒(s)"};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JLabel lblTimeStamp = new JLabel("时间戳：");
  private JComboBox<String> cmbUnit = new JComboBox<String>();
  private BaseTextField txtTimeStamp = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JLabel lblTime = new JLabel("时间：");
  private JComboBox<String> cmbTimeZone = new JComboBox<String>();
  private BaseTextField txtTime = new BaseTextField();
  private JButton btnCopy = new JButton("复制结果(C)");
  private JButton btnCancel = new JButton("取消");

  public TimeStampConvertDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.setTitle("时间戳转换");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(310, 210);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.lblTimeStamp.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.cmbUnit.setBounds(10, 35, 90, Util.INPUT_HEIGHT);
    this.txtTimeStamp.setBounds(110, 35, 160, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTimeStamp);
    this.pnlMain.add(this.cmbUnit);
    this.pnlMain.add(this.txtTimeStamp);

    this.lblTime.setBounds(10, 70, 70, Util.VIEW_HEIGHT);
    this.cmbTimeZone.setBounds(10, 95, 90, Util.INPUT_HEIGHT);
    this.txtTime.setBounds(110, 95, 160, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTime);
    this.pnlMain.add(this.cmbTimeZone);
    this.pnlMain.add(this.txtTime);

    this.btnCopy.setBounds(30, 135, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(170, 135, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCopy);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.cmbUnit.setModel(new DefaultComboBoxModel<String>(TIMESTAMP_UNIT_TYPES));
    this.cmbUnit.setSelectedIndex(0);
    String[] array = new String[25];
    int index = 0;
    for (int i = 12; i > 0; i--) {
      array[index] = "GMT-" + i;
      index++;
    }
    array[index] = "GMT";
    index++;
    for (int i = 1; i <= 12; i++) {
      array[index] = "GMT+" + i;
      index++;
    }
    this.cmbTimeZone.setModel(new DefaultComboBoxModel<String>(array));
    this.cmbTimeZone.setSelectedIndex(20);
    this.txtTime.setEditable(false);
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
    this.txtTimeStamp.addCaretListener(this);
    this.txtTimeStamp.addKeyListener(this.keyAdapter);
    this.cmbTimeZone.addKeyListener(this.keyAdapter);
    this.cmbTimeZone.addItemListener(this);
    this.txtTime.addKeyListener(this.keyAdapter);
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
    if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
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
    String strTimeStamp = this.txtTimeStamp.getText();
    if (Util.isTextEmpty(strTimeStamp)) {
      this.txtTime.setText("");
    } else {
      int timeStampUnit = this.cmbUnit.getSelectedIndex();
      String timeZone = this.cmbTimeZone.getSelectedItem().toString();
      try {
        long timeStamp = Long.parseLong(strTimeStamp);
        SimpleDateFormat simpleDateFormat = null;
        if (timeStampUnit == 1) {
          timeStamp *= 1000L;
          simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
          simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
        Date date = new Date(timeStamp);
        // 设置时区
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String result = simpleDateFormat.format(date);
        this.txtTime.setText(result);
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtTime.setText("");
      }
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txtTime.getText());
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
    if (this.txtTimeStamp.equals(e.getSource())) {
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
