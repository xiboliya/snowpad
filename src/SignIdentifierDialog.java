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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * "列表符号与编号"对话框
 * 
 * @author 冰原
 * 
 */
public class SignIdentifierDialog extends BaseDialog implements ActionListener,
    FocusListener, ChangeListener, UndoableEditListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTabbedPane tpnMain = new JTabbedPane();
  private GridLayout gridLayout = new GridLayout(Util.SIGN_MAX_ROW,
      Util.SIGN_MAX_COLUMN, 5, 5);
  private GridLayout specialGridLayout = new GridLayout(Util.SIGN_MAX_ROW, 1, 5, 5);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private JLabel lblStart = new JLabel("起始编号：");
  private BaseTextField txtStart = new BaseTextField(true, "\\d*");
  private JTextArea txaView = new JTextArea();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private EtchedBorder etchedBorder = new EtchedBorder();
  private MouseAdapter mouseAdapter = null;
  private String strSignIdentifier = ""; // 当前选中的符号或编号字符串

  /**
   * 构造方法
   * 
   * @param owner
   *          用于显示该对话框的父组件
   * @param modal
   *          是否为模式对话框
   * @param txaSource
   *          针对操作的文本域
   * @param hashtable
   *          用于显示字符的哈希表。键为标签，值为该标签下的字符序列
   */
  public SignIdentifierDialog(JFrame owner, boolean modal, JTextArea txaSource,
      Hashtable<String, String> hashtable) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.addListeners();
    this.setSize(330, 275);
    this.fillTabbedPane(hashtable);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("列表符号与编号");
    this.pnlMain.setLayout(null);
    this.pnlLeft.setBounds(0, 0, 200, 245);
    this.pnlRight.setBounds(200, 0, 120, 240);
    this.pnlMain.add(this.pnlLeft);
    this.pnlMain.add(this.pnlRight);
    this.pnlLeft.add(this.tpnMain, BorderLayout.CENTER);
    this.btnOk.setBounds(15, 30, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(15, 70, 90, Util.BUTTON_HEIGHT);
    this.lblStart.setBounds(6, 120, 70, Util.VIEW_HEIGHT);
    this.txtStart.setBounds(76, 120, 40, Util.INPUT_HEIGHT);
    this.txaView.setBounds(12, 160, 98, 65);
    this.txaView.setBorder(new EtchedBorder());
    this.txaView.setOpaque(true);
    this.txaView.setEditable(false);
    this.txaView.setFocusable(false);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnCancel);
    this.pnlRight.add(this.lblStart);
    this.pnlRight.add(this.txtStart);
    this.pnlRight.add(this.txaView);
    this.tpnMain.setFocusable(false);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
  }

  /**
   * 填充所有标签页的字符
   * 
   * @param hashtable
   *          用于显示字符的哈希表。键为标签，值为该标签下的字符序列
   */
  private void fillTabbedPane(Hashtable<String, String> hashtable) {
    if (hashtable.isEmpty()) {
      return;
    }
    Enumeration<String> enumeration = hashtable.keys();
    while (enumeration.hasMoreElements()) {
      String strTitle = enumeration.nextElement();
      String strElement = hashtable.get(strTitle);
      this.fillElements(strElement, strTitle);
    }
  }

  /**
   * 填充一个标签页的字符
   * 
   * @param strElement
   *          字符序列
   * @param strTitle
   *          标签页标题
   */
  private void fillElements(String strElement, String strTitle) {
    if (Util.isTextEmpty(strElement) || Util.isTextEmpty(strTitle)) {
      return;
    }
    int elementCount = strElement.length();
    boolean isSpecial = false;
    JPanel pnlTemp = null;
    int gridTotal = 0;
    if (strElement.equals(Util.IDENTIFIER_CHARS)) {
      isSpecial = true;
    }
    if (isSpecial) {
      pnlTemp = new JPanel(this.specialGridLayout); // 创建特殊格式的布局
      gridTotal = this.specialGridLayout.getRows() *
          this.specialGridLayout.getColumns();
    } else {
      pnlTemp = new JPanel(this.gridLayout); // 创建普通格式的布局
      gridTotal = this.gridLayout.getRows() * this.gridLayout.getColumns();
    }
    if (elementCount > gridTotal) { // 保证元素的个数不大于布局中预留的网格数，多出的元素将被舍弃
      elementCount = gridTotal;
    }
    int index = 0;
    for (; index < elementCount; index++) {
      char charElement = strElement.charAt(index);
      JLabel lblElement = this.createElement(String.valueOf(charElement),
          isSpecial);
      pnlTemp.add(lblElement);
    }
    for (; index < gridTotal; index++) {
      JLabel lblElement = this.createElement("", false);
      pnlTemp.add(lblElement);
    }
    this.tpnMain.add(pnlTemp, strTitle);
  }

  /**
   * 创建一个文本标签
   * 
   * @param strElement
   *          显示的字符
   * @param isSpecial
   *          是否为特殊格式
   * @return 新创建的文本标签
   */
  private JLabel createElement(String strElement, boolean isSpecial) {
    JLabel lblElement = null;
    if (isSpecial) {
      lblElement = new JLabel(Util.SIGN_IDENTIFIER_NAMES[Util.IDENTIFIER_CHARS
          .indexOf(strElement)]);
    } else {
      lblElement = new JLabel(strElement);
      lblElement.setFont(Util.SIGN_VIEW_FONT);
    }
    if (!Util.isTextEmpty(strElement)) {
      lblElement.setHorizontalAlignment(SwingConstants.CENTER);
      lblElement.setFocusable(true); // 设置标签可以获得焦点
      lblElement.setOpaque(true); // 设置标签可以绘制背景
      lblElement.setBorder(this.etchedBorder);
      lblElement.setBackground(Color.WHITE);
      lblElement.addKeyListener(this.keyAdapter);
      lblElement.addFocusListener(this);
      lblElement.addMouseListener(this.mouseAdapter);
    }
    return lblElement;
  }

  /**
   * 使用或预览当前选中的符号或编号方式
   * 
   * @param isView
   *          是否为预览模式，当为true时表示为预览当前模式，否则为使用当前模式
   */
  private void signIdentifier(boolean isView) {
    String[] arrText = null;
    boolean isSelected = false;
    if (isView) {
      arrText = Util.SIGN_CHARS_VIEW.split("\n");
    } else {
      String selText = this.txaSource.getSelectedText();
      if (Util.isTextEmpty(selText)) {
        arrText = this.txaSource.getText().split("\n");
      } else {
        isSelected = true;
        arrText = Util.getCurrentLinesArray(this.txaSource);
      }
    }
    if (arrText == null || arrText.length <= 0) {
      return;
    }
    boolean isSpecial = false;
    if (this.tpnMain.getSelectedIndex() == 0) {
      isSpecial = true;
    }
    String strStart = this.txtStart.getText().trim();
    int start = 1;
    try {
      start = Integer.parseInt(strStart);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    this.toConvertArray(arrText, isSpecial, start - 1);
    StringBuilder stbIndent = new StringBuilder();
    for (String str : arrText) {
      stbIndent.append(str + "\n");
    }
    if (isView) {
      this.txaView.setText(stbIndent.deleteCharAt(stbIndent.length() - 1)
          .toString()); // 删除字符串末尾多余的换行符
    } else if (isSelected) {
      this.txaSource.replaceSelection(stbIndent.deleteCharAt(
          stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    } else {
      this.txaSource.setText(stbIndent.deleteCharAt(
          stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    }
  }

  /**
   * 为指定的字符串数组添加当前选中的符号或编号
   * 
   * @param arrText
   *          待处理的字符串数组
   * @param isSpecial
   *          是否为特殊格式
   * @param start
   *          起始编号
   */
  private void toConvertArray(String[] arrText, boolean isSpecial, int start) {
    int n = 0;
    if (isSpecial) {
      int index = 0;
      for (; index < Util.SIGN_IDENTIFIER_NAMES.length; index++) {
        if (this.strSignIdentifier.equals(Util.SIGN_IDENTIFIER_NAMES[index])) {
          break;
        }
      }
      switch (index) {
      case 0: // 数字
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = (n + start + 1) + "." + arrText[n];
        }
        break;
      case 1: // 汉字
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToChinese(n + start + 1, false) + "." + arrText[n];
        }
        break;
      case 2: // 干支
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToGanZhi(n + start) + "." + arrText[n];
        }
        break;
      case 3: // 字母
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToLetter(n + start + 1, false) + "." + arrText[n];
        }
        break;
      }
    } else {
      for (n = 0; n < arrText.length; n++) {
        arrText[n] = this.strSignIdentifier + arrText[n];
      }
    }
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.txtStart.addKeyListener(this.keyAdapter);
    this.txtStart.getDocument().addUndoableEditListener(this);
    this.mouseAdapter = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        JLabel lblTemp = (JLabel) e.getSource();
        lblTemp.requestFocus(); // 当鼠标单击时，获得焦点
        if (e.getClickCount() == 2) {
          onEnter();
        }
      }
    };
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.signIdentifier(false); // 应用当前的列表符号或编号效果
    this.dispose();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * 当文本标签获得焦点时，将触发此事件
   */
  public void focusGained(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.PINK);
    this.strSignIdentifier = lblTemp.getText();
    this.signIdentifier(true); // 预览当前的列表符号或编号效果
  }

  /**
   * 当文本标签失去焦点时，将触发此事件
   */
  public void focusLost(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.WHITE);
  }

  /**
   * 当监听的组件状态变化时，将触发此事件
   */
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.txtStart.setEnabled(true);
      this.txtStart.setFocusable(true);
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      this.txtStart.setEnabled(false);
      this.txtStart.setFocusable(false);
    }
  }

  /**
   * 当本控件中的文本发生变化时，将触发此事件
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.signIdentifier(true); // 预览当前的列表符号或编号效果
  }
}
