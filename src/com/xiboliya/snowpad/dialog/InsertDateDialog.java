/**
 * Copyright (C) 2013 冰原
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "插入时间/日期"对话框
 * 
 * @author 冰原
 * 
 */
public class InsertDateDialog extends BaseDialog implements ActionListener,
    ListSelectionListener, CaretListener {
  private static final long serialVersionUID = 1L;
  private static final String[] DATE_STYLES = new String[] { "yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd KK:mm:ss",
      "yyyy-MM-dd KK:mm:ss a", "yyyy-MM-dd HH:mm:ss E",
      "yyyy-MM-dd HH:mm:ss zZ", "yyyy年MM月dd日 HH时mm分ss秒",
      "G yyyy-MM-dd HH:mm:ss E zZ", "yy-M-d H:m:s", "yyyy/MM/dd HH:mm:ss",
      "yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy.MM.dd",
      "yy/MM/dd", "HH:mm:ss", "KK:mm:ss a", "HH:mm:ss:SSS" }; // 时间/日期格式字符串
  private JRadioButton radSelect = new JRadioButton("使用选中的格式(S)", true);
  private JList<String> listStyles = new JList<String>();
  private JScrollPane srpStyles = new JScrollPane(this.listStyles);
  private JRadioButton radUser = new JRadioButton("使用自定义格式(U)", false);
  private JLabel lblWarning = new JLabel("");
  private BaseTextField txtUser = new BaseTextField(DATE_STYLES[0]);
  private JLabel lblView = new JLabel("");
  private JButton btnOk = new JButton("插入");
  private JButton btnCancel = new JButton("关闭");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private BaseMouseAdapter baseMouseAdapter = new BaseMouseAdapter();
  private ButtonGroup bgpStyle = new ButtonGroup();
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
  private String[] arrStyles = new String[DATE_STYLES.length];
  private int currentIndex = 0;

  /**
   * 构造方法
   * 
   * @param owner
   *          父窗口
   * @param modal
   *          是否为模式窗口
   * @param txaSource
   *          针对操作的文本域
   */
  public InsertDateDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("插入时间/日期");
    this.init();
    this.setMnemonic();
    this.setView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(320, 335);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.lblWarning.setForeground(Color.RED);
    this.lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
    this.pnlMain.setLayout(null);
    this.radSelect.setBounds(10, 10, 150, Util.VIEW_HEIGHT);
    this.srpStyles.setBounds(10, 35, 300, 150);
    this.radUser.setBounds(10, 190, 150, Util.VIEW_HEIGHT);
    this.lblWarning.setBounds(170, 190, 140, Util.VIEW_HEIGHT);
    this.txtUser.setBounds(10, 212, 300, Util.INPUT_HEIGHT);
    this.lblView.setBounds(10, 240, 300, Util.VIEW_HEIGHT);
    this.btnOk.setBounds(45, 268, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(185, 268, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.radSelect);
    this.pnlMain.add(this.srpStyles);
    this.pnlMain.add(this.radUser);
    this.pnlMain.add(this.lblWarning);
    this.pnlMain.add(this.txtUser);
    this.pnlMain.add(this.lblView);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpStyle.add(this.radSelect);
    this.bgpStyle.add(this.radUser);
    this.listStyles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.fillStyleList();
    }
    super.setVisible(visible);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.radSelect.setMnemonic('S');
    this.radUser.setMnemonic('U');
  }

  /**
   * 根据单选按钮的选择，设置组件是否可用
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radSelect.isSelected();
    this.listStyles.setEnabled(selected);
    this.txtUser.setEnabled(!selected);
    this.lblWarning.setEnabled(!selected);
    this.lblView.setEnabled(!selected);
  }

  /**
   * 填充"格式"列表
   */
  private void fillStyleList() {
    Date date = new Date();
    for (int n = 0; n < DATE_STYLES.length; n++) {
      this.simpleDateFormat.applyPattern(DATE_STYLES[n]);
      this.arrStyles[n] = this.simpleDateFormat.format(date);
    }
    this.listStyles.removeListSelectionListener(this); // 为了避免在重新填充列表框时，而产生的当前选择项索引为-1，而导致异常，所以先移除监听器。
    this.listStyles.setListData(this.arrStyles);
    this.listStyles.addListSelectionListener(this); // 重新添加监听器。
    this.listStyles.setSelectedIndex(this.currentIndex);
  }

  /**
   * 根据自定义输入框是否符合规范，来更新警告标签的显示
   * 
   * @param warning
   *          是否需要显示警告，需要显示则为true，反之为false
   */
  private void updateWarning(boolean warning) {
    if (warning) {
      this.lblWarning.setText("警告：格式错误！");
    } else {
      this.lblWarning.setText("");
    }
  }

  /**
   * 设置预览标签的显示
   */
  private void setView() {
    if (!this.lblView.isEnabled()) {
      return;
    }
    try {
      this.simpleDateFormat.applyPattern(this.txtUser.getText());
      String strDate = this.simpleDateFormat.format(new Date());
      this.lblView.setText(strDate);
      this.updateWarning(false);
    } catch (IllegalArgumentException x) {
      this.updateWarning(true);
      // x.printStackTrace();
    }
  }

  /**
   * 接收鼠标事件的抽象适配器的自定义类
   * 
   * @author 冰原
   * 
   */
  private class BaseMouseAdapter extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (listStyles.equals(e.getSource()) && listStyles.isEnabled()) {
        if (e.getClickCount() == 2) { // 鼠标双击时，执行操作
          onEnter();
        }
      }
    }
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.btnCancel.addActionListener(this);
    this.btnOk.addActionListener(this);
    this.radSelect.addActionListener(this);
    this.radUser.addActionListener(this);
    this.listStyles.addMouseListener(this.baseMouseAdapter);
    this.txtUser.addCaretListener(this);
    // 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭对话框
    this.radSelect.addKeyListener(this.keyAdapter);
    this.listStyles.addKeyListener(this.keyAdapter);
    this.radUser.addKeyListener(this.keyAdapter);
    this.txtUser.addKeyListener(this.keyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "关闭"按钮的处理方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * "插入"按钮的处理方法
   */
  @Override
  public void onEnter() {
    String strStyles = "";
    if (this.radSelect.isSelected()) {
      strStyles = DATE_STYLES[this.currentIndex];
    } else {
      strStyles = this.txtUser.getText();
    }
    if (Util.isTextEmpty(strStyles)) {
      TipsWindow.show(this, "格式不能为空！");
      return;
    }
    try {
      this.simpleDateFormat.applyPattern(strStyles);
      this.txaSource.replaceSelection(this.simpleDateFormat.format(new Date()));
      this.dispose();
    } catch (IllegalArgumentException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请重新输入！");
    }
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnCancel.equals(source)) {
      this.onCancel();
    } else if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.radSelect.equals(source)) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radUser.equals(source)) {
      this.setComponentEnabledByRadioButton();
    }
  }

  /**
   * 当列表框改变选择时，触发此事件
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (this.listStyles.equals(e.getSource())) {
      this.currentIndex = this.listStyles.getSelectedIndex();
      this.txtUser.setText(DATE_STYLES[this.currentIndex]);
      this.lblView.setText(this.listStyles.getSelectedValue().toString());
      this.updateWarning(false);
    }
  }

  /**
   * 当文本框的光标发生变化时，触发此事件
   */
  @Override
  public void caretUpdate(CaretEvent e) {
    if (this.txtUser.equals(e.getSource())) {
      this.setView();
    }
  }

}
