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

package com.xiboliya.snowpad;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * "字体"对话框
 * 
 * @author 冰原
 * 
 */
public class FontChooser extends BaseDialog implements ActionListener,
    ListSelectionListener, CaretListener {
  private static final long serialVersionUID = 1L;
  private JLabel lblFont = new JLabel("字体：");
  private JLabel lblStyle = new JLabel("字形：");
  private JLabel lblSize = new JLabel("大小：");
  private JLabel lblView = new JLabel("A※文");
  private BaseTextField txtFont = new BaseTextField();
  private BaseTextField txtStyle = new BaseTextField();
  private BaseTextField txtSize = new BaseTextField(true, "\\d{0,3}"); // 限制用户只能输入数字，并且不能超过3位
  private JList<String> listFont = new JList<String>();
  private JList<String> listStyle = new JList<String>();
  private JList<Integer> listSize = new JList<Integer>();
  private JScrollPane srpFont = new JScrollPane(this.listFont);
  private JScrollPane srpStyle = new JScrollPane(this.listStyle);
  private JScrollPane srpSize = new JScrollPane(this.listSize);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private BaseMouseAdapter baseMouseAdapter = new BaseMouseAdapter();
  private boolean isOk = false;

  /**
   * 构造方法
   * 
   * @param owner
   *          父窗口
   * @param modal
   *          是否为模式窗口
   */
  public FontChooser(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("字体");
    this.init();
    this.fillFontList();
    this.fillStyleList();
    this.fillSizeList();
    this.setView();
    this.addListeners();
    this.setSize(365, 315);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.lblView.setBorder(new TitledBorder("示例"));
    this.lblView.setHorizontalAlignment(JLabel.CENTER);
    this.pnlMain.setLayout(null);
    this.lblFont.setBounds(10, 10, 140, Util.VIEW_HEIGHT);
    this.txtFont.setBounds(10, 30, 140, Util.INPUT_HEIGHT);
    this.srpFont.setBounds(10, 53, 140, 120);
    this.pnlMain.add(this.lblFont);
    this.pnlMain.add(this.txtFont);
    this.pnlMain.add(this.srpFont);
    this.lblStyle.setBounds(160, 10, 100, Util.VIEW_HEIGHT);
    this.txtStyle.setBounds(160, 30, 100, Util.INPUT_HEIGHT);
    this.srpStyle.setBounds(160, 53, 100, 120);
    this.pnlMain.add(this.lblStyle);
    this.pnlMain.add(this.txtStyle);
    this.pnlMain.add(this.srpStyle);
    this.lblSize.setBounds(270, 10, 80, Util.VIEW_HEIGHT);
    this.txtSize.setBounds(270, 30, 80, Util.INPUT_HEIGHT);
    this.srpSize.setBounds(270, 53, 80, 120);
    this.pnlMain.add(this.lblSize);
    this.pnlMain.add(this.txtSize);
    this.pnlMain.add(this.srpSize);
    this.lblView.setBounds(10, 180, 245, 100);
    this.pnlMain.add(this.lblView);
    this.btnOk.setBounds(265, 200, 80, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(265, 240, 80, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 填充"字体"列表
   */
  private void fillFontList() {
    String[] fontFamilys = Util.FONT_FAMILY_NAMES; // 获取系统所有字体的名称列表
    this.listFont.setListData(fontFamilys);
    Font font = this.txaSource.getFont();
    this.listFont.setSelectedValue(font.getFamily(), true);
    this.listFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setFontView();
  }

  /**
   * 填充"字形"列表
   */
  private void fillStyleList() {
    String[] fontStyles = new String[] { "常规", "粗体", "斜体", "粗斜体" };
    this.listStyle.setListData(fontStyles);
    Font font = this.txaSource.getFont();
    this.listStyle.setSelectedIndex(font.getStyle());
    this.listStyle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setStyleView();
  }

  /**
   * 填充"大小"列表
   */
  private void fillSizeList() {
    Integer[] fontSizes = new Integer[Util.MAX_FONT_SIZE - Util.MIN_FONT_SIZE + 1];
    for (int i = 0; i < fontSizes.length; i++) {
      fontSizes[i] = Util.MIN_FONT_SIZE + i; // Integer类的自动装箱
    }
    this.listSize.setListData(fontSizes);
    Font font = this.txaSource.getFont();
    this.listSize.setSelectedValue(font.getSize(), true);
    this.listSize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setSizeView();
  }

  /**
   * 更新列表的显示
   */
  public void updateListView() {
    Font font = this.txaSource.getFont();
    this.listFont.setSelectedValue(font.getFamily(), true);
    this.listStyle.setSelectedIndex(font.getStyle());
    this.listSize.setSelectedValue(font.getSize(), true);
  }

  /**
   * 根据当前"字体"列表的选择，更新顶部文本框的显示
   */
  public void setFontView() {
    String strFont = this.listFont.getSelectedValue().toString();
    if (strFont != null) {
      this.txtFont.setText(strFont);
    }
  }

  /**
   * 根据当前"字形"列表的选择，更新顶部文本框的显示
   */
  public void setStyleView() {
    String strStyle = this.listStyle.getSelectedValue().toString();
    if (strStyle != null) {
      this.txtStyle.setText(strStyle);
    }
  }

  /**
   * 根据当前"大小"列表的选择，更新顶部文本框的显示
   */
  public void setSizeView() {
    String strSize = this.listSize.getSelectedValue().toString();
    if (strSize != null) {
      this.txtSize.setText(strSize);
    }
  }

  /**
   * 获取设置后的字体
   * 
   * @return 设置后的字体
   */
  public Font getTextAreaFont() {
    return this.lblView.getFont();
  }

  /**
   * 设置示例文字和文本域的字体
   */
  private void setView() {
    Font font = new Font(this.listFont.getSelectedValue().toString(),
        this.listStyle.getSelectedIndex(), (Integer) this.listSize
            .getSelectedValue());
    this.lblView.setFont(font);
  }

  /**
   * 接收鼠标事件的抽象适配器的自定义类
   * 
   * @author 冰原
   * 
   */
  private class BaseMouseAdapter extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (listFont.equals(e.getSource())) {
        txtFont.setText(listFont.getSelectedValue().toString());
      } else if (listStyle.equals(e.getSource())) {
        txtStyle.setText(listStyle.getSelectedValue().toString());
      } else if (listSize.equals(e.getSource())) {
        txtSize.setText(listSize.getSelectedValue().toString());
      }
      if (e.getClickCount() == 2) { // 鼠标双击时，执行操作
        onEnter();
      }
    }
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.btnCancel.addActionListener(this);
    this.btnOk.addActionListener(this);
    this.listFont.addListSelectionListener(this);
    this.listStyle.addListSelectionListener(this);
    this.listSize.addListSelectionListener(this);
    this.listFont.addMouseListener(this.baseMouseAdapter);
    this.listStyle.addMouseListener(this.baseMouseAdapter);
    this.listSize.addMouseListener(this.baseMouseAdapter);
    this.txtFont.addCaretListener(this);
    this.txtStyle.addCaretListener(this);
    this.txtSize.addCaretListener(this);
    // 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭"字体"对话框
    this.txtFont.addKeyListener(this.keyAdapter);
    this.txtStyle.addKeyListener(this.keyAdapter);
    this.txtSize.addKeyListener(this.keyAdapter);
    this.listFont.addKeyListener(this.keyAdapter);
    this.listStyle.addKeyListener(this.keyAdapter);
    this.listSize.addKeyListener(this.keyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "取消"按钮的处理方法
   */
  public void onCancel() {
    this.dispose();
    this.isOk = false;
  }

  /**
   * "确定"按钮的处理方法
   */
  public void onEnter() {
    this.dispose();
    this.isOk = true;
  }

  /**
   * 获取是否执行了确定
   * 
   * @return 是否执行了确定
   */
  public boolean getOk() {
    return this.isOk;
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * 当列表框改变选择时，触发此事件
   */
  public void valueChanged(ListSelectionEvent e) {
    if (this.listFont.equals(e.getSource())) {
      if (!this.txtFont.getText().equals(this.listFont.getSelectedValue())) {
        this.txtFont.setText(this.listFont.getSelectedValue().toString());
      }
    } else if (this.listStyle.equals(e.getSource())) {
      if (!this.txtStyle.getText().equals(this.listStyle.getSelectedValue())) {
        this.txtStyle.setText(this.listStyle.getSelectedValue().toString());
      }
    } else if (this.listSize.equals(e.getSource())) {
      try {
        String strSize = this.txtSize.getText();
        if (strSize.length() > 0) {
          if (!new Integer(strSize).equals(this.listSize.getSelectedValue())) {
            this.txtSize.setText(this.listSize.getSelectedValue().toString());
          }
        }
      } catch (NumberFormatException x) {
        // 被转化的字符串不是数字
        // x.printStackTrace();
      }
    }
    this.setView();
  }

  /**
   * 当文本框的光标发生变化时，触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    if (this.txtFont.equals(e.getSource())) {
      this.listFont.setSelectedValue(this.txtFont.getText(), true);
    } else if (this.txtStyle.equals(e.getSource())) {
      this.listStyle.setSelectedValue(this.txtStyle.getText(), true);
    } else if (this.txtSize.equals(e.getSource())) {
      try {
        String strSize = this.txtSize.getText();
        if (strSize.length() > 0) {
          this.listSize.setSelectedValue(new Integer(strSize), true);
        }
      } catch (NumberFormatException x) {
        // 被转化的字符串不是数字
        // x.printStackTrace();
      }
    }
  }
}
