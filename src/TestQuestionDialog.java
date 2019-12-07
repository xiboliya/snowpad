/**
 * Copyright (C) 2019 冰原
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
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
 * "题库"对话框
 * 
 * @author 冰原
 * 
 */
public class TestQuestionDialog extends BaseDialog implements ActionListener,
    FocusListener, ChangeListener, UndoableEditListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTabbedPane tpnMain = new JTabbedPane();
  private GridLayout gridLayout = new GridLayout(Util.SIGN_MAX_ROW, 1, 5, 5);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private JLabel lblCount = new JLabel("出题数目：");
  private BaseTextField txtCount = new BaseTextField(true, "\\d*");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private EtchedBorder etchedBorder = new EtchedBorder();
  private MouseAdapter mouseAdapter = null;
  private String strSignIdentifier = ""; // 当前选中的题目类型字符串
  private JLabel lblCurrent = null; // 当前选中的文本标签
  private LinkedList<String> testList = new LinkedList<String>(); // 存放题目的链表

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
  public TestQuestionDialog(JFrame owner, boolean modal, JTextArea txaSource,
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
    this.setTitle("题库");
    this.pnlMain.setLayout(null);
    this.pnlLeft.setBounds(0, 0, 200, 245);
    this.pnlRight.setBounds(200, 0, 120, 240);
    this.pnlMain.add(this.pnlLeft);
    this.pnlMain.add(this.pnlRight);
    this.pnlLeft.add(this.tpnMain, BorderLayout.CENTER);
    this.btnOk.setBounds(15, 30, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(15, 70, 90, Util.BUTTON_HEIGHT);
    this.lblCount.setBounds(6, 120, 70, Util.VIEW_HEIGHT);
    this.txtCount.setBounds(76, 120, 40, Util.INPUT_HEIGHT);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnCancel);
    this.pnlRight.add(this.lblCount);
    this.pnlRight.add(this.txtCount);
    this.tpnMain.setFocusable(false);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
    this.txtCount.setText("100");
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
    JPanel pnlTemp = null;
    int gridTotal = 0;
    pnlTemp = new JPanel(this.gridLayout);
    gridTotal = this.gridLayout.getRows() * this.gridLayout.getColumns();
    if (elementCount > gridTotal) { // 保证元素的个数不大于布局中预留的网格数，多出的元素将被舍弃
      elementCount = gridTotal;
    }
    int index = 0;
    for (; index < elementCount; index++) {
      char charElement = strElement.charAt(index);
      JLabel lblElement = this.createElement(String.valueOf(charElement));
      pnlTemp.add(lblElement);
    }
    for (; index < gridTotal; index++) {
      JLabel lblElement = this.createElement("");
      pnlTemp.add(lblElement);
    }
    this.tpnMain.add(pnlTemp, strTitle);
  }

  /**
   * 创建一个文本标签
   * 
   * @param strElement
   *          显示的字符
   * @return 新创建的文本标签
   */
  private JLabel createElement(String strElement) {
    JLabel lblElement = null;
    lblElement = new JLabel(Util.TEST_QUESTION_MATH_NAMES[Util.TEST_QUESTION_MATH.indexOf(strElement)]);
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
   * 使用当前选中的出题方式
   */
  private void testQuestion() {
    int index = 0;
    for (; index < Util.TEST_QUESTION_MATH_NAMES.length; index++) {
      if (this.strSignIdentifier.equals(Util.TEST_QUESTION_MATH_NAMES[index])) {
        break;
      }
    }
    URL url = ClassLoader.getSystemResource("res/study/" + Util.TEST_QUESTION_MATH_PATHS[index]);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(url.openStream(), "GB18030"));
      String line = reader.readLine();
      testList.clear();
      while (line != null) {
        testList.add(line);
        line = reader.readLine();
      }
      Collections.shuffle(testList);
      StringBuilder stbTest = new StringBuilder();
      int size = testList.size();
      String strCount = this.txtCount.getText().trim();
      int count = 0;
      try {
        count = Integer.parseInt(strCount);
      } catch (NumberFormatException x) {
        // x.printStackTrace();
      }
      if (count <= 0) {
        count = size;
      } else if (count > size) {
        count = size;
      }
      for (int i = 0; i < count; i++) {
        String str = testList.get(i);
        stbTest.append(str);
        if ((i+1)%4 == 0) {
          stbTest.append("\n");
        } else {
          stbTest.append("\t\t");
        }
      }
      this.txaSource.replaceSelection(stbTest.toString());
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException x) {
        // x.printStackTrace();
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
    this.txtCount.addKeyListener(this.keyAdapter);
    this.txtCount.addFocusListener(this);
    this.txtCount.getDocument().addUndoableEditListener(this);
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
    this.testQuestion();
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
    Object objTemp = e.getSource();
    if (objTemp instanceof JLabel) {
      JLabel lblTemp = (JLabel) objTemp;
      this.lblCurrent = lblTemp;
      lblTemp.setBackground(Color.PINK);
      this.strSignIdentifier = lblTemp.getText();
    } else {
      if (this.lblCurrent != null) {
        this.lblCurrent.setBackground(Color.PINK);
      }
    }
  }

  /**
   * 当文本标签失去焦点时，将触发此事件
   */
  public void focusLost(FocusEvent e) {
    Object objTemp = e.getSource();
    if (objTemp instanceof JLabel) {
      JLabel lblTemp = (JLabel) objTemp;
      lblTemp.setBackground(Color.WHITE);
    } else {
      if (this.lblCurrent != null) {
        this.lblCurrent.setBackground(Color.WHITE);
      }
    }
  }

  /**
   * 当监听的组件状态变化时，将触发此事件
   */
  public void stateChanged(ChangeEvent e) {
  }

  /**
   * 当本控件中的文本发生变化时，将触发此事件
   */
  public void undoableEditHappened(UndoableEditEvent e) {
  }
}
