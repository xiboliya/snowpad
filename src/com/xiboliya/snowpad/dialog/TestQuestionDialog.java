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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "题库"对话框
 * 
 * @author 冰原
 * 
 */
public class TestQuestionDialog extends BaseDialog implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String[] TEST_QUESTION_MATH_LEVELS = new String[] { "加减法【初级】", "加减法【中级】", "加减法【高级】", "加减法【特级】",
      "乘法【初级】", "乘法【中级】", "乘法【高级】", "乘法【特级】", "除法【初级】", "除法【中级】", "除法【高级】", "除法【特级】" }; // 数学题库难度等级的显示名称
  private static final String[] TEST_QUESTION_MATH_SPECIAL_LEVELS = new String[] { "退位减法" }; // 数学题库专项题目的显示名称
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  // 标准
  private String strMinTextC = "结果最小值";
  private String strMaxTextC = "结果最大值";
  private JPanel pnlCommon = new JPanel();
  private JLabel lblLevelC = new JLabel("难度等级：");
  private JComboBox<String> cmbLevelC = new JComboBox<String>(TEST_QUESTION_MATH_LEVELS);
  private JLabel lblMinC = new JLabel(this.strMinTextC + "：");
  private BaseTextField txtMinC = new BaseTextField(true, "\\d*");
  private JLabel lblMaxC = new JLabel(this.strMaxTextC + "：");
  private BaseTextField txtMaxC = new BaseTextField(true, "\\d*");
  private JLabel lblCountC = new JLabel("出题数目：");
  private BaseTextField txtCountC = new BaseTextField(true, "\\d*");
  // 专项
  private String strMinTextS = "结果最小值";
  private String strMaxTextS = "结果最大值";
  private JPanel pnlSpecial = new JPanel();
  private JLabel lblLevelS = new JLabel("题目类型：");
  private JComboBox<String> cmbLevelS = new JComboBox<String>(TEST_QUESTION_MATH_SPECIAL_LEVELS);
  private JLabel lblMinS = new JLabel(this.strMinTextS + "：");
  private BaseTextField txtMinS = new BaseTextField(true, "\\d*");
  private JLabel lblMaxS = new JLabel(this.strMaxTextS + "：");
  private BaseTextField txtMaxS = new BaseTextField(true, "\\d*");
  private JLabel lblCountS = new JLabel("出题数目：");
  private BaseTextField txtCountS = new BaseTextField(true, "\\d*");
  // 按钮
  private JPanel pnlBottom = new JPanel();
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
    super(owner, modal, txaSource);
    this.init();
    this.addListeners();
    this.setSize(280, 280);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("题库");
    this.pnlMain.setLayout(null);
    // 标准
    this.pnlCommon.setLayout(null);
    this.lblLevelC.setBounds(20, 10, 110, Util.VIEW_HEIGHT);
    this.cmbLevelC.setBounds(110, 10, 130, Util.INPUT_HEIGHT);
    this.lblMinC.setBounds(20, 50, 110, Util.VIEW_HEIGHT);
    this.txtMinC.setBounds(110, 50, 60, Util.INPUT_HEIGHT);
    this.lblMaxC.setBounds(20, 90, 110, Util.VIEW_HEIGHT);
    this.txtMaxC.setBounds(110, 90, 60, Util.INPUT_HEIGHT);
    this.lblCountC.setBounds(20, 130, 110, Util.VIEW_HEIGHT);
    this.txtCountC.setBounds(110, 130, 60, Util.INPUT_HEIGHT);
    this.pnlCommon.add(this.lblLevelC);
    this.pnlCommon.add(this.cmbLevelC);
    this.pnlCommon.add(this.lblMinC);
    this.pnlCommon.add(this.txtMinC);
    this.pnlCommon.add(this.lblMaxC);
    this.pnlCommon.add(this.txtMaxC);
    this.pnlCommon.add(this.lblCountC);
    this.pnlCommon.add(this.txtCountC);
    // 专项
    this.pnlSpecial.setLayout(null);
    this.lblLevelS.setBounds(20, 10, 110, Util.VIEW_HEIGHT);
    this.cmbLevelS.setBounds(110, 10, 130, Util.INPUT_HEIGHT);
    this.lblMinS.setBounds(20, 50, 110, Util.VIEW_HEIGHT);
    this.txtMinS.setBounds(110, 50, 60, Util.INPUT_HEIGHT);
    this.lblMaxS.setBounds(20, 90, 110, Util.VIEW_HEIGHT);
    this.txtMaxS.setBounds(110, 90, 60, Util.INPUT_HEIGHT);
    this.lblCountS.setBounds(20, 130, 110, Util.VIEW_HEIGHT);
    this.txtCountS.setBounds(110, 130, 60, Util.INPUT_HEIGHT);
    this.pnlSpecial.add(this.lblLevelS);
    this.pnlSpecial.add(this.cmbLevelS);
    this.pnlSpecial.add(this.lblMinS);
    this.pnlSpecial.add(this.txtMinS);
    this.pnlSpecial.add(this.lblMaxS);
    this.pnlSpecial.add(this.txtMaxS);
    this.pnlSpecial.add(this.lblCountS);
    this.pnlSpecial.add(this.txtCountS);

    this.tpnMain.setBounds(0, 0, 280, 195);
    this.tpnMain.add(this.pnlCommon, "标准");
    this.tpnMain.add(this.pnlSpecial, "专项");
    this.pnlMain.add(this.tpnMain);
    
    // 按钮
    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 195, 280, 85);
    this.btnOk.setBounds(30, 10, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(150, 10, 85, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnOk);
    this.pnlBottom.add(this.btnCancel);
    this.pnlMain.add(this.pnlBottom);
    
    this.initData();
  }

  private void initData() {
    this.txtMinC.setText("0");
    this.txtMaxC.setText("10");
    this.txtCountC.setText("100");
    this.txtMinS.setText("0");
    this.txtMaxS.setText("100");
    this.txtCountS.setText("100");
    this.tpnMain.setFocusable(false);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
  }

  /**
   * 获取选项卡当前视图的索引号
   * 
   * @return 当前视图的索引号
   */
  private int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * 刷新标签的文本
   * 
   * @param isResult
   *          是否显示“结果”
   */
  private void refreshLabelText(boolean isResult) {
    int index = this.getTabbedIndex();
    if (index == 0) {
      if (isResult) {
        this.strMinTextC = "结果最小值";
        this.strMaxTextC = "结果最大值";
      } else {
        this.strMinTextC = "运算最小值";
        this.strMaxTextC = "运算最大值";
      }
      this.lblMinC.setText(this.strMinTextC + "：");
      this.lblMaxC.setText(this.strMaxTextC + "：");
    } else {
      if (isResult) {
        this.strMinTextS = "结果最小值";
        this.strMaxTextS = "结果最大值";
      } else {
        this.strMinTextS = "运算最小值";
        this.strMaxTextS = "运算最大值";
      }
      this.lblMinS.setText(this.strMinTextS + "：");
      this.lblMaxS.setText(this.strMaxTextS + "：");
    }
  }

  /**
   * 使用当前选中的标准出题方式
   */
  private void testQuestionCommon() {
    String strMin = this.txtMinC.getText();
    if (Util.isTextEmpty(strMin)) {
      TipsWindow.show(this, "请输入" + this.strMinTextC + "！");
      return;
    }
    String strMax = this.txtMaxC.getText();
    if (Util.isTextEmpty(strMax)) {
      TipsWindow.show(this, "请输入" + this.strMaxTextC + "！");
      return;
    }
    String strCount = this.txtCountC.getText();
    if (Util.isTextEmpty(strCount)) {
      TipsWindow.show(this, "请输入出题数目！");
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
      TipsWindow.show(this, this.strMinTextC + "不能超过" + this.strMaxTextC + "！");
      return;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      TipsWindow.show(this, "出题数目必须大于0！");
      return;
    }
    int level = this.cmbLevelC.getSelectedIndex();
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
   * 使用当前选中的专项出题方式
   */
  private void testQuestionSpecial() {
    String strMin = this.txtMinS.getText();
    if (Util.isTextEmpty(strMin)) {
      TipsWindow.show(this, "请输入" + this.strMinTextS + "！");
      return;
    }
    String strMax = this.txtMaxS.getText();
    if (Util.isTextEmpty(strMax)) {
      TipsWindow.show(this, "请输入" + this.strMaxTextS + "！");
      return;
    }
    String strCount = this.txtCountS.getText();
    if (Util.isTextEmpty(strCount)) {
      TipsWindow.show(this, "请输入出题数目！");
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
      TipsWindow.show(this, this.strMinTextS + "不能超过" + this.strMaxTextC + "！");
      return;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      TipsWindow.show(this, "出题数目必须大于0！");
      return;
    }
    int level = this.cmbLevelS.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListSpecial1(min, max);
      break;
    }
    Collections.shuffle(testList); // 随机打乱List内元素顺序
    StringBuilder stbTest = new StringBuilder();
    int size = testList.size();
    if (size == 0) {
      JOptionPane.showMessageDialog(this, "实际题库数目为0个，请调整出题参数！", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    } else if (count > size) {
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
        if (result2 >= start && result2 <= end) {
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
        if (result2 >= start && result2 <= end) {
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
   * 数学题目退位减法
   * 
   * @param start
   *          最小值
   * @param end
   *          最大值
   */
  private void getListSpecial1(int start, int end) {
    this.testList.clear();
    for (int i = 11; i <= end; i++) {
      for (int j = 0; j <= end; j++) {
        int result1 = i - j;
        int single1 = this.getSingle(i);
        int single2 = this.getSingle(j);
        // -
        if (result1 >= start && result1 <= end && single1 < single2) {
          this.testList.add(i + "-" + j + "=");
        }
      }
    }
  }

  /**
   * 获取数字的个位数
   * 
   * @param number
   *          数字
   * @return 数字的个位数
   */
  private int getSingle(int number) {
    String str = String.valueOf(number);
    int result = str.charAt(str.length() - 1);
    return result;
  }

  /**
   * 添加和初始化事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.cmbLevelC.addKeyListener(this.keyAdapter);
    this.cmbLevelC.addItemListener(this);
    this.txtMinC.addKeyListener(this.keyAdapter);
    this.txtMaxC.addKeyListener(this.keyAdapter);
    this.txtCountC.addKeyListener(this.keyAdapter);
    this.cmbLevelS.addKeyListener(this.keyAdapter);
    this.cmbLevelS.addItemListener(this);
    this.txtMinS.addKeyListener(this.keyAdapter);
    this.txtMaxS.addKeyListener(this.keyAdapter);
    this.txtCountS.addKeyListener(this.keyAdapter);
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
    int index = this.getTabbedIndex();
    if (index == 0) {
      int level = this.cmbLevelC.getSelectedIndex();
      if (level > 3 && level < 8) {
        this.refreshLabelText(false);
      } else {
        this.refreshLabelText(true);
      }
    } else {
      int level = this.cmbLevelS.getSelectedIndex();
      if (level > 0) {
        this.refreshLabelText(false);
      } else {
        this.refreshLabelText(true);
      }
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    if (this.getTabbedIndex() == 0) {
      this.testQuestionCommon();
    } else if (this.getTabbedIndex() == 1) {
      this.testQuestionSpecial();
    }
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
