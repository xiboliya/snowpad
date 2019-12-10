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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "题库"对话框
 * 
 * @author 冰原
 * 
 */
public class TestQuestionDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblMath = new JLabel("数学题库：");
  private JComboBox<String> cmbMath = new JComboBox<String>(Util.TEST_QUESTION_MATH_NAMES);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private JLabel lblCount = new JLabel("出题数目：");
  private BaseTextField txtCount = new BaseTextField(true, "\\d*");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
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
   */
  public TestQuestionDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.addListeners();
    this.setSize(280, 160);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("题库");
    this.pnlMain.setLayout(null);
    this.lblMath.setBounds(20, 10, 90, Util.VIEW_HEIGHT);
    this.cmbMath.setBounds(90, 10, 160, Util.INPUT_HEIGHT);
    this.lblCount.setBounds(20, 50, 70, Util.VIEW_HEIGHT);
    this.txtCount.setBounds(90, 50, 60, Util.INPUT_HEIGHT);
    this.btnOk.setBounds(30, 90, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(150, 90, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblMath);
    this.pnlMain.add(this.cmbMath);
    this.pnlMain.add(this.lblCount);
    this.pnlMain.add(this.txtCount);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
    this.txtCount.setText("100");
  }

  /**
   * 使用当前选中的出题方式
   */
  private void testQuestion() {
    int index = this.cmbMath.getSelectedIndex();
    switch (index) {
    case 0:
      this.getListLevel1(0, 10);
      break;
    case 1:
      this.getListLevel2(0, 10);
      break;
    case 2:
      this.getListLevel3(0, 10);
      break;
    case 3:
      this.getListLevel4(0, 10);
      break;
    case 4:
      this.getListLevel1(0, 20);
      break;
    case 5:
      this.getListLevel2(0, 20);
      break;
    case 6:
      this.getListLevel3(0, 20);
      break;
    case 7:
      this.getListLevel4(0, 20);
      break;
    }
    Collections.shuffle(testList); // 随机打乱List内元素顺序
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
  }

  /**
   * 数学题目初级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel1(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 0; j <= end; j++) {
        int result1 = i + j;
        int result2 = i - j;
        // +
        if (result1 >= start && result1 <= end) {
          this.testList.add(i + "+" + j + "=");
        }
        // -
        if (result2 >= start  && result2 <= end) {
          this.testList.add(i + "-" + j + "=");
        }
      }
    }
  }

  /**
   * 数学题目中级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel2(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 0; j <= end; j++) {
        int result1 = i + j;
        int result2 = i - j;
        // +
        if (result1 >= start && result1 <= end) {
          this.testList.add("(  )" + "+" + j + "=" + result1);
          this.testList.add(i + "+" + "(  )" + "=" + result1);
        }
        // -
        if (result2 >= start  && result2 <= end) {
          this.testList.add("(  )" + "-" + j + "=" + result2);
          this.testList.add(i + "-" + "(  )" + "=" + result2);
        }
      }
    }
  }

  /**
   * 数学题目高级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel3(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 0; j <= end; j++) {
        for (int k = 0; k <= end; k++) {
          int temp1 = i + j;
          int temp2 = i - j;
          int result1 = i + j + k;
          int result2 = i + j - k;
          int result3 = i - j + k;
          int result4 = i - j - k;
          // ++
          if (temp1 <= end && result1 >= start && result1 <= end) {
            this.testList.add(i + "+" + j + "+" + k + "=");
          }
          // +-
          if (temp1 <= end && result2 >= start && result2 <= end) {
            this.testList.add(i + "+" + j + "-" + k + "=");
          }
          // -+
          if (temp2 >= start && result3 >= start && result3 <= end) {
            this.testList.add(i + "-" + j + "+" + k + "=");
          }
          // --
          if (temp2 >= start && result4 >= start && result4 <= end) {
            this.testList.add(i + "-" + j + "-" + k + "=");
          }
        }
      }
    }
  }

  /**
   * 数学题目特级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel4(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 0; j <= end; j++) {
        for (int k = 0; k <= end; k++) {
          int temp1 = i + j;
          int temp2 = i - j;
          int result1 = i + j + k;
          int result2 = i + j - k;
          int result3 = i - j + k;
          int result4 = i - j - k;
          // ++
          if (temp1 <= end && result1 >= start && result1 <= end) {
            this.testList.add("(  )" + "+" + j + "+" + k + "=" + result1);
            this.testList.add(i + "+" + "(  )" + "+" + k + "=" + result1);
            this.testList.add(i + "+" + j + "+" + "(  )" + "=" + result1);
          }
          // +-
          if (temp1 <= end && result2 >= start && result2 <= end) {
            this.testList.add("(  )" + "+" + j + "-" + k + "=" + result2);
            this.testList.add(i + "+" + "(  )" + "-" + k + "=" + result2);
            this.testList.add(i + "+" + j + "-" + "(  )" + "=" + result2);
          }
          // -+
          if (temp2 >= start && result3 >= start && result3 <= end) {
            this.testList.add("(  )" + "-" + j + "+" + k + "=" + result3);
            this.testList.add(i + "-" + "(  )" + "+" + k + "=" + result3);
            this.testList.add(i + "-" + j + "+" + "(  )" + "=" + result3);
          }
          // --
          if (temp2 >= start && result4 >= start && result4 <= end) {
            this.testList.add("(  )" + "-" + j + "-" + k + "=" + result4);
            this.testList.add(i + "-" + "(  )" + "-" + k + "=" + result4);
            this.testList.add(i + "-" + j + "-" + "(  )" + "=" + result4);
          }
        }
      }
    }
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.cmbMath.addKeyListener(this.keyAdapter);
    this.txtCount.addKeyListener(this.keyAdapter);
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
}
