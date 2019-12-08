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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
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
