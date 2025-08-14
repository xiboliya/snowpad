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
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.util.Util;

/**
 * "时间戳转换"对话框
 * 
 * @author 冰原
 * 
 */
public class TimeStampConvertDialog extends BaseDialog implements ActionListener, DocumentListener, ItemListener, ChangeListener {
  private static final long serialVersionUID = 1L;
  // 时间戳单位类型
  private static final String[] TIMESTAMP_UNIT_TYPES = new String[] {"毫秒(ms)", "秒(s)"};
  private static final String[] DATE_FORMATS = new String[] {"yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd HH:mm:ss"};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private JTabbedPane tpnMain = new JTabbedPane();
  // 时间戳转时间
  private JPanel pnlStampToTime = new JPanel();
  private JLabel lblStampS = new JLabel("时间戳：");
  private JComboBox<String> cmbUnitS = new JComboBox<String>();
  private BaseTextField txtStampS = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JLabel lblTimeS = new JLabel("时间：");
  private JComboBox<String> cmbTimeZoneS = new JComboBox<String>();
  private BaseTextField txtTimeS = new BaseTextField();
  // 时间转时间戳
  private JPanel pnlTimeToStamp = new JPanel();
  private JLabel lblTimeT = new JLabel("时间：");
  private JLabel lblTimeStyleT = new JLabel("格式：" + DATE_FORMATS[0]);
  private JComboBox<String> cmbTimeZoneT = new JComboBox<String>();
  private BaseTextField txtTimeT = new BaseTextField();
  private JLabel lblStampT = new JLabel("时间戳：");
  private JComboBox<String> cmbUnitT = new JComboBox<String>();
  private BaseTextField txtStampT = new BaseTextField();

  private JPanel pnlBottom = new JPanel();
  private BaseButton btnCopy = new BaseButton("复制结果(C)");
  private BaseButton btnCancel = new BaseButton("取消");

  public TimeStampConvertDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.setTitle("时间戳转换");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(340, 240);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    // 时间戳转时间
    this.pnlStampToTime.setLayout(null);
    this.lblStampS.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.cmbUnitS.setBounds(10, 35, 110, Util.INPUT_HEIGHT);
    this.txtStampS.setBounds(130, 35, 190, Util.INPUT_HEIGHT);
    this.pnlStampToTime.add(this.lblStampS);
    this.pnlStampToTime.add(this.cmbUnitS);
    this.pnlStampToTime.add(this.txtStampS);
    this.lblTimeS.setBounds(10, 70, 70, Util.VIEW_HEIGHT);
    this.cmbTimeZoneS.setBounds(10, 95, 110, Util.INPUT_HEIGHT);
    this.txtTimeS.setBounds(130, 95, 190, Util.INPUT_HEIGHT);
    this.pnlStampToTime.add(this.lblTimeS);
    this.pnlStampToTime.add(this.cmbTimeZoneS);
    this.pnlStampToTime.add(this.txtTimeS);
    // 时间转时间戳
    this.pnlTimeToStamp.setLayout(null);
    this.lblTimeT.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.lblTimeStyleT.setBounds(100, 10, 220, Util.VIEW_HEIGHT);
    this.lblTimeStyleT.setForeground(Color.BLUE);
    this.cmbTimeZoneT.setBounds(10, 35, 110, Util.INPUT_HEIGHT);
    this.txtTimeT.setBounds(130, 35, 190, Util.INPUT_HEIGHT);
    this.pnlTimeToStamp.add(this.lblTimeT);
    this.pnlTimeToStamp.add(this.lblTimeStyleT);
    this.pnlTimeToStamp.add(this.cmbTimeZoneT);
    this.pnlTimeToStamp.add(this.txtTimeT);
    this.lblStampT.setBounds(10, 70, 70, Util.VIEW_HEIGHT);
    this.cmbUnitT.setBounds(10, 95, 110, Util.INPUT_HEIGHT);
    this.txtStampT.setBounds(130, 95, 190, Util.INPUT_HEIGHT);
    this.pnlTimeToStamp.add(this.lblStampT);
    this.pnlTimeToStamp.add(this.cmbUnitT);
    this.pnlTimeToStamp.add(this.txtStampT);

    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 160, 340, 80);
    this.btnCopy.setBounds(30, 5, 115, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(195, 5, 115, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCopy);
    this.pnlBottom.add(this.btnCancel);
    // 主界面
    this.tpnMain.setBounds(0, 0, 340, 160);
    this.tpnMain.add(this.pnlStampToTime, "时间戳转时间");
    this.tpnMain.add(this.pnlTimeToStamp, "时间转时间戳");
    this.pnlMain.add(this.tpnMain);
    this.tpnMain.setSelectedIndex(0);
    this.tpnMain.setFocusable(false);
    this.pnlMain.add(this.pnlBottom);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    String[] timeZoneArray = new String[25];
    int index = 0;
    for (int i = 12; i > 0; i--) {
      timeZoneArray[index] = "GMT-" + i;
      index++;
    }
    timeZoneArray[index] = "GMT";
    index++;
    for (int i = 1; i <= 12; i++) {
      timeZoneArray[index] = "GMT+" + i;
      index++;
    }
    // 时间戳转时间
    this.cmbUnitS.setModel(new DefaultComboBoxModel<String>(TIMESTAMP_UNIT_TYPES));
    this.cmbUnitS.setSelectedIndex(0);
    this.cmbTimeZoneS.setModel(new DefaultComboBoxModel<String>(timeZoneArray));
    this.cmbTimeZoneS.setSelectedIndex(20);
    this.txtTimeS.setEditable(false);
    // 时间转时间戳
    this.cmbTimeZoneT.setModel(new DefaultComboBoxModel<String>(timeZoneArray));
    this.cmbTimeZoneT.setSelectedIndex(20);
    this.cmbUnitT.setModel(new DefaultComboBoxModel<String>(TIMESTAMP_UNIT_TYPES));
    this.cmbUnitT.setSelectedIndex(0);
    this.txtStampT.setEditable(false);
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
    // 时间戳转时间
    this.cmbUnitS.addKeyListener(this.keyAdapter);
    this.cmbUnitS.addItemListener(this);
    this.txtStampS.getDocument().addDocumentListener(this);
    this.txtStampS.addKeyListener(this.keyAdapter);
    this.cmbTimeZoneS.addKeyListener(this.keyAdapter);
    this.cmbTimeZoneS.addItemListener(this);
    this.txtTimeS.addKeyListener(this.keyAdapter);
    // 时间转时间戳
    this.txtTimeT.getDocument().addDocumentListener(this);
    this.txtTimeT.addKeyListener(this.keyAdapter);
    this.cmbTimeZoneT.addKeyListener(this.keyAdapter);
    this.cmbTimeZoneT.addItemListener(this);
    this.cmbUnitT.addKeyListener(this.keyAdapter);
    this.cmbUnitT.addItemListener(this);
    this.txtStampT.addKeyListener(this.keyAdapter);

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
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.showStampToTime();
    } else {
      this.showTimeToStamp();
    }
  }

  /**
   * 显示时间戳转时间的结果
   */
  private void showStampToTime() {
    String strTimeStamp = this.txtStampS.getText();
    if (Util.isTextEmpty(strTimeStamp)) {
      this.txtTimeS.setText("");
    } else {
      int timeStampUnit = this.cmbUnitS.getSelectedIndex();
      String timeZone = this.cmbTimeZoneS.getSelectedItem().toString();
      try {
        long timeStamp = Long.parseLong(strTimeStamp);
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat(DATE_FORMATS[timeStampUnit]);
        if (timeStampUnit == 1) {
          timeStamp *= 1000L;
        }
        Date date = new Date(timeStamp);
        // 设置时区
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String result = simpleDateFormat.format(date);
        this.txtTimeS.setText(result);
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtTimeS.setText("");
      }
    }
  }

  /**
   * 显示时间转时间戳的结果
   */
  private void showTimeToStamp() {
    String strTime = this.txtTimeT.getText();
    if (Util.isTextEmpty(strTime)) {
      this.txtStampT.setText("");
    } else {
      String timeZone = this.cmbTimeZoneT.getSelectedItem().toString();
      int timeStampUnit = this.cmbUnitT.getSelectedIndex();
      try {
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat(DATE_FORMATS[timeStampUnit]);
        // 设置时区
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = simpleDateFormat.parse(strTime);
        long result = date.getTime();
        if (timeStampUnit == 1) {
          result /= 1000L;
        }
        this.txtStampT.setText(String.valueOf(result));
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtStampT.setText("");
      }
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    if (this.tpnMain.getSelectedIndex() == 0) {
      ListenerManager.getInstance().postClipboardEvent(this.txtTimeS.getText());
    } else {
      ListenerManager.getInstance().postClipboardEvent(this.txtStampT.getText());
    }
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
   * 当用户已选定或取消选定某项时，触发此事件
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
    if (this.cmbUnitT.equals(e.getSource())) {
      int timeStampUnit = this.cmbUnitT.getSelectedIndex();
      this.lblTimeStyleT.setText("格式：" + DATE_FORMATS[timeStampUnit]);
    }
  }

  /**
   * 当选项卡改变当前视图时调用
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    this.showResult();
  }
}
