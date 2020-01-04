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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "题库"对话框
 * 
 * @author 冰原
 * 
 */
public class TestQuestionDialog extends BaseDialog implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblLevel = new JLabel("难度等级：");
  private JComboBox<String> cmbLevel = new JComboBox<String>(Util.TEST_QUESTION_MATH_LEVELS);
  private JLabel lblMin = new JLabel("结果最小值：");
  private BaseTextField txtMin = new BaseTextField(true, "\\d*");
  private JLabel lblMax = new JLabel("结果最大值：");
  private BaseTextField txtMax = new BaseTextField(true, "\\d*");
  private JLabel lblCount = new JLabel("出题数目：");
  private BaseTextField txtCount = new BaseTextField(true, "\\d*");
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
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
    this.setSize(280, 245);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("题库");
    this.pnlMain.setLayout(null);
    this.lblLevel.setBounds(20, 10, 110, Util.VIEW_HEIGHT);
    this.cmbLevel.setBounds(110, 10, 130, Util.INPUT_HEIGHT);
    this.lblMin.setBounds(20, 50, 110, Util.VIEW_HEIGHT);
    this.txtMin.setBounds(110, 50, 60, Util.INPUT_HEIGHT);
    this.lblMax.setBounds(20, 90, 110, Util.VIEW_HEIGHT);
    this.txtMax.setBounds(110, 90, 60, Util.INPUT_HEIGHT);
    this.lblCount.setBounds(20, 130, 110, Util.VIEW_HEIGHT);
    this.txtCount.setBounds(110, 130, 60, Util.INPUT_HEIGHT);
    this.btnOk.setBounds(30, 170, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(150, 170, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblLevel);
    this.pnlMain.add(this.cmbLevel);
    this.pnlMain.add(this.lblMin);
    this.pnlMain.add(this.txtMin);
    this.pnlMain.add(this.lblMax);
    this.pnlMain.add(this.txtMax);
    this.pnlMain.add(this.lblCount);
    this.pnlMain.add(this.txtCount);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
    this.txtMin.setText("0");
    this.txtMax.setText("10");
    this.txtCount.setText("100");
  }

  /**
   * 刷新标签的文本
   * 
   * @param isResult
   *          是否显示“结果”
   */
  private void refreshLabelText(boolean isResult) {
    if (isResult) {
      this.lblMin.setText("结果最小值：");
      this.lblMax.setText("结果最大值：");
    } else {
      this.lblMin.setText("运算最小值：");
      this.lblMax.setText("运算最大值：");
    }
  }

  /**
   * 使用当前选中的出题方式
   */
  private void testQuestion() {
    String strMin = this.txtMin.getText();
    if (Util.isTextEmpty(strMin)) {
      JOptionPane.showMessageDialog(this, "请输入结果最小值！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strMax = this.txtMax.getText();
    if (Util.isTextEmpty(strMax)) {
      JOptionPane.showMessageDialog(this, "请输入结果最大值！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strCount = this.txtCount.getText();
    if (Util.isTextEmpty(strCount)) {
      JOptionPane.showMessageDialog(this, "请输入出题数目！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int min = 0;
    try {
      min = Integer.parseInt(strMin);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    int max = 0;
    try {
      max = Integer.parseInt(strMax);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (min > max) {
      JOptionPane.showMessageDialog(this, "结果最小值不能超过结果最大值！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      JOptionPane.showMessageDialog(this, "出题数目必须大于0！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int level = this.cmbLevel.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListLevel1(min, max);
      break;
    case 1:
      this.getListLevel2(min, max);
      break;
    case 2:
      this.getListLevel3(min, max);
      break;
    case 3:
      this.getListLevel4(min, max);
      break;
    case 4:
      this.getListLevel5(min, max);
      break;
    case 5:
      this.getListLevel6(min, max);
      break;
    case 6:
      this.getListLevel7(min, max);
      break;
    case 7:
      this.getListLevel8(min, max);
      break;
    case 8:
      this.getListLevel9(min, max);
      break;
    case 9:
      this.getListLevel10(min, max);
      break;
    case 10:
      this.getListLevel11(min, max);
      break;
    case 11:
      this.getListLevel12(min, max);
      break;
    }
    Collections.shuffle(testList); // 随机打乱List内元素顺序
    StringBuilder stbTest = new StringBuilder();
    int size = testList.size();
    if (count > size) {
      int result = JOptionPane.showConfirmDialog(this, "实际题库数目只有：" + size + "个，将按照此数目出题，是否继续？",
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
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
    this.dispose();
  }

  /**
   * 数学题目加减法初级
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
   * 数学题目加减法中级
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
   * 数学题目加减法高级
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
   * 数学题目加减法特级
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
   * 数学题目乘法初级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel5(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        this.testList.add(i + "×" + j + "=");
      }
    }
  }

  /**
   * 数学题目乘法中级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel6(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        int result = i * j;
        if (i == 0) {
          this.testList.add("(  )" + "×" + j + "=" + result);
        } else if (j == 0) {
          this.testList.add(i + "×" + "(  )" + "=" + result);
        } else {
          this.testList.add("(  )" + "×" + j + "=" + result);
          this.testList.add(i + "×" + "(  )" + "=" + result);
        }
      }
    }
  }

  /**
   * 数学题目乘法高级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel7(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        for (int k = start; k <= end; k++) {
          this.testList.add(i + "×" + j + "×" + k + "=");
        }
      }
    }
  }

  /**
   * 数学题目乘法特级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel8(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        for (int k = start; k <= end; k++) {
          if ((i == 0 && j == 0) || (i == 0 && k == 0) || (j == 0 && k == 0)) {
            continue;
          }
          int result = i * j * k;
          if (i == 0) {
            this.testList.add("(  )" + "×" + j + "×" + k + "=" + result);
          } else if (j == 0) {
            this.testList.add(i + "×" + "(  )" + "×" + k + "=" + result);
          } else if (k == 0) {
            this.testList.add(i + "×" + j + "×" + "(  )" + "=" + result);
          } else {
            this.testList.add("(  )" + "×" + j + "×" + k + "=" + result);
            this.testList.add(i + "×" + "(  )" + "×" + k + "=" + result);
            this.testList.add(i + "×" + j + "×" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * 数学题目除法初级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel9(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        if ((i % j) == 0) {
          int result = i / j;
          if (result >= start && result <= end) {
            this.testList.add(i + "÷" + j + "=");
          }
        }
      }
    }
  }

  /**
   * 数学题目除法中级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel10(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        if ((i % j) == 0) {
          int result = i / j;
          if (result >= start && result <= end) {
            this.testList.add("(  )" + "÷" + j + "=" + result);
            this.testList.add(i + "÷" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * 数学题目除法高级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel11(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        for (int k = 1; k <= end; k++) {
          int temp1 = i / j;
          int temp2 = i % j;
          int result = i / j / k;
          if (temp2 == 0 && (temp1 % k) == 0) {
            if (temp1 <= end && result >= start && result <= end) {
              this.testList.add(i + "÷" + j + "÷" + k + "=");
            }
          }
        }
      }
    }
  }

  /**
   * 数学题目除法特级
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListLevel12(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        for (int k = 1; k <= end; k++) {
          int temp1 = i / j;
          int temp2 = i % j;
          int result = i / j / k;
          if (temp2 == 0 && (temp1 % k) == 0) {
            if (temp1 <= end && result >= start && result <= end) {
              if (i == 0) {
                this.testList.add("(  )" + "÷" + j + "÷" + k + "=" + result);
              } else {
                this.testList.add("(  )" + "÷" + j + "÷" + k + "=" + result);
                this.testList.add(i + "÷" + "(  )" + "÷" + k + "=" + result);
                this.testList.add(i + "÷" + j + "÷" + "(  )" + "=" + result);
              }
            }
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
    this.cmbLevel.addKeyListener(this.keyAdapter);
    this.cmbLevel.addItemListener(this);
    this.txtMin.addKeyListener(this.keyAdapter);
    this.txtMax.addKeyListener(this.keyAdapter);
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
   * 当所选项更改时调用
   */
  public void itemStateChanged(ItemEvent e) {
    int level = this.cmbLevel.getSelectedIndex();
    if (level > 3 && level < 8) {
      this.refreshLabelText(false);
    } else {
      this.refreshLabelText(true);
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.testQuestion();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
