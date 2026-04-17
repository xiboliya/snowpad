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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
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
  // 加减法难度等级的显示名称
  private static final String[] TEST_QUESTION_MATH_LEVELS_1 = new String[] { "加法【初级】", "加法【中级】", "加法【高级】", "加法【特级】",
      "减法【初级】", "减法【中级】", "减法【高级】", "减法【特级】", "加减法【初级】", "加减法【中级】", "加减法【高级】", "加减法【特级】" };
  // 乘除法难度等级的显示名称
  private static final String[] TEST_QUESTION_MATH_LEVELS_2 = new String[] { "乘法【初级】", "乘法【中级】", "乘法【高级】", "乘法【特级】",
      "除法【初级】", "除法【中级】", "除法【高级】", "除法【特级】" };
  // 数学题库专项题目的显示名称
  private static final String[] TEST_QUESTION_MATH_SPECIAL_LEVELS = new String[] { "退位减法" };
  // 出题规则的显示名称
  private static final String[] TEST_QUESTION_MATH_STYLE = new String[] { "运算数范围", "计算结果范围" };
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  // 加减
  private JPanel pnlCommon1 = new JPanel();
  private JLabel lblLevel1 = new JLabel("难度等级：");
  private JComboBox<String> cmbLevel1 = new JComboBox<String>(TEST_QUESTION_MATH_LEVELS_1);
  // 乘除
  private JPanel pnlCommon2 = new JPanel();
  private JLabel lblLevel2 = new JLabel("难度等级：");
  private JComboBox<String> cmbLevel2 = new JComboBox<String>(TEST_QUESTION_MATH_LEVELS_2);
  // 专项
  private JPanel pnlSpecial = new JPanel();
  private JLabel lblLevelS = new JLabel("题目类型：");
  private JComboBox<String> cmbLevelS = new JComboBox<String>(TEST_QUESTION_MATH_SPECIAL_LEVELS);
  // 按钮
  private JPanel pnlBottom = new JPanel();
  private JLabel lblStyle = new JLabel("出题规则：");
  private JComboBox<String> cmbStyle = new JComboBox<String>(TEST_QUESTION_MATH_STYLE);
  private JLabel lblMin = new JLabel("运算最小值：");
  private BaseTextField txtMin = new BaseTextField(true, "\\d*");
  private JLabel lblMax = new JLabel("运算最大值：");
  private BaseTextField txtMax = new BaseTextField(true, "\\d*");
  private JLabel lblCount = new JLabel("出题数目：");
  private BaseTextField txtCount = new BaseTextField(true, "\\d*");
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private LinkedList<String> testList = new LinkedList<String>(); // 存放题目的链表

  /**
   * 构造方法
   * 
   * @param owner 用于显示该对话框的父组件
   * @param modal 是否为模式对话框
   * @param txaSource 针对操作的文本域
   */
  public TestQuestionDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
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
    // 加减
    this.pnlCommon1.setLayout(null);
    this.lblLevel1.setBounds(20, 10, 100, Util.VIEW_HEIGHT);
    this.cmbLevel1.setBounds(100, 10, 140, Util.INPUT_HEIGHT);
    this.pnlCommon1.add(this.lblLevel1);
    this.pnlCommon1.add(this.cmbLevel1);
    // 乘除
    this.pnlCommon2.setLayout(null);
    this.lblLevel2.setBounds(20, 10, 100, Util.VIEW_HEIGHT);
    this.cmbLevel2.setBounds(100, 10, 140, Util.INPUT_HEIGHT);
    this.pnlCommon2.add(this.lblLevel2);
    this.pnlCommon2.add(this.cmbLevel2);
    // 专项
    this.pnlSpecial.setLayout(null);
    this.lblLevelS.setBounds(20, 10, 100, Util.VIEW_HEIGHT);
    this.cmbLevelS.setBounds(100, 10, 140, Util.INPUT_HEIGHT);
    this.pnlSpecial.add(this.lblLevelS);
    this.pnlSpecial.add(this.cmbLevelS);

    this.tpnMain.setBounds(0, 0, 280, 75);
    this.tpnMain.add(this.pnlCommon1, "加减");
    this.tpnMain.add(this.pnlCommon2, "乘除");
    this.tpnMain.add(this.pnlSpecial, "专项");
    this.pnlMain.add(this.tpnMain);

    // 按钮
    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 75, 280, 205);
    this.lblStyle.setBounds(20, 10, 100, Util.VIEW_HEIGHT);
    this.cmbStyle.setBounds(100, 10, 140, Util.INPUT_HEIGHT);
    this.lblMin.setBounds(20, 40, 100, Util.VIEW_HEIGHT);
    this.txtMin.setBounds(100, 40, 140, Util.INPUT_HEIGHT);
    this.lblMax.setBounds(20, 70, 100, Util.VIEW_HEIGHT);
    this.txtMax.setBounds(100, 70, 140, Util.INPUT_HEIGHT);
    this.lblCount.setBounds(20, 100, 100, Util.VIEW_HEIGHT);
    this.txtCount.setBounds(100, 100, 140, Util.INPUT_HEIGHT);
    this.btnOk.setBounds(30, 130, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(160, 130, 90, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.lblStyle);
    this.pnlBottom.add(this.cmbStyle);
    this.pnlBottom.add(this.lblMin);
    this.pnlBottom.add(this.txtMin);
    this.pnlBottom.add(this.lblMax);
    this.pnlBottom.add(this.txtMax);
    this.pnlBottom.add(this.lblCount);
    this.pnlBottom.add(this.txtCount);
    this.pnlBottom.add(this.btnOk);
    this.pnlBottom.add(this.btnCancel);
    this.pnlMain.add(this.pnlBottom);
    
    this.initData();
  }

  private void initData() {
    this.txtMin.setText("0");
    this.txtMax.setText("10");
    this.txtCount.setText("100");
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
   * @param isResult true显示“结果”，false显示“运算 ”
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
   * 加减法按难度等级出题
   */
  private void testQuestionCommon1() {
    boolean isCorrect = this.checkCorrect();
    if (!isCorrect) {
      return;
    }
    int min = Integer.parseInt(this.txtMin.getText());
    int max = Integer.parseInt(this.txtMax.getText());
    int count = Integer.parseInt(this.txtCount.getText());
    boolean isStyleResult = this.isStyleResult();
    this.testList.clear();
    int level = this.cmbLevel1.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListAddition1(min, max, isStyleResult);
      break;
    case 1:
      this.getListAddition2(min, max, isStyleResult);
      break;
    case 2:
      this.getListAddition3(min, max, isStyleResult);
      break;
    case 3:
      this.getListAddition4(min, max, isStyleResult);
      break;
    case 4:
      this.getListSubduction1(min, max, isStyleResult);
      break;
    case 5:
      this.getListSubduction2(min, max, isStyleResult);
      break;
    case 6:
      this.getListSubduction3(min, max, isStyleResult);
      break;
    case 7:
      this.getListSubduction4(min, max, isStyleResult);
      break;
    case 8:
      this.getListAdditionSubduction1(min, max, isStyleResult);
      break;
    case 9:
      this.getListAdditionSubduction2(min, max, isStyleResult);
      break;
    case 10:
      this.getListAdditionSubduction3(min, max, isStyleResult);
      break;
    case 11:
      this.getListAdditionSubduction4(min, max, isStyleResult);
      break;
    }
    this.testQuestion(count);
  }

  /**
   * 乘除法按难度等级出题
   */
  private void testQuestionCommon2() {
    boolean isCorrect = this.checkCorrect();
    if (!isCorrect) {
      return;
    }
    int min = Integer.parseInt(this.txtMin.getText());
    int max = Integer.parseInt(this.txtMax.getText());
    int count = Integer.parseInt(this.txtCount.getText());
    boolean isStyleResult = this.isStyleResult();
    this.testList.clear();
    int level = this.cmbLevel2.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListMultiplication1(min, max, isStyleResult);
      break;
    case 1:
      this.getListMultiplication2(min, max, isStyleResult);
      break;
    case 2:
      this.getListMultiplication3(min, max, isStyleResult);
      break;
    case 3:
      this.getListMultiplication4(min, max, isStyleResult);
      break;
    case 4:
      this.getListDivision1(min, max, isStyleResult);
      break;
    case 5:
      this.getListDivision2(min, max, isStyleResult);
      break;
    case 6:
      this.getListDivision3(min, max, isStyleResult);
      break;
    case 7:
      this.getListDivision4(min, max, isStyleResult);
      break;
    }
    this.testQuestion(count);
  }

  /**
   * 使用当前选中的专项出题方式
   */
  private void testQuestionSpecial() {
    boolean isCorrect = this.checkCorrect();
    if (!isCorrect) {
      return;
    }
    int min = Integer.parseInt(this.txtMin.getText());
    int max = Integer.parseInt(this.txtMax.getText());
    int count = Integer.parseInt(this.txtCount.getText());
    boolean isStyleResult = this.isStyleResult();
    this.testList.clear();
    int level = this.cmbLevelS.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListSpecial1(min, max, isStyleResult);
      break;
    }
    this.testQuestion(count);
  }

  /**
   * 检查输入的值是否合法
   * @return true显示合法，false显示不合法
   */
  private boolean checkCorrect() {
    String strMin = this.txtMin.getText();
    if (Util.isTextEmpty(strMin)) {
      TipsWindow.show(this, "请输入最小值！");
      return false;
    }
    String strMax = this.txtMax.getText();
    if (Util.isTextEmpty(strMax)) {
      TipsWindow.show(this, "请输入最大值！");
      return false;
    }
    String strCount = this.txtCount.getText();
    if (Util.isTextEmpty(strCount)) {
      TipsWindow.show(this, "请输入出题数目！");
      return false;
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
      TipsWindow.show(this, "最小值不能超过最大值！");
      return false;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      TipsWindow.show(this, "出题数目必须大于0！");
      return false;
    }
    return true;
  }

  /**
   * 出题
   * @param count 出题数目
   */
  private void testQuestion(int count) {
    Collections.shuffle(this.testList); // 随机打乱List内元素顺序
    StringBuilder stbTest = new StringBuilder();
    int size = this.testList.size();
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
      String str = this.testList.get(i);
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
   * 获取出题规则是否为计算结果
   * @return true显示为计算结果，false显示不是计算结果
   */
  private boolean isStyleResult() {
    int index = this.cmbStyle.getSelectedIndex();
    return index == 1;
  }

  /**
   * 加法初级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAddition1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i + j;
          // +
          if (result >= start && result <= end) {
            this.testList.add(i + "+" + j + "=");
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          // +
          this.testList.add(i + "+" + j + "=");
        }
      }
    }
  }

  /**
   * 加法中级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAddition2(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i + j;
          // +
          if (result >= start && result <= end) {
            this.testList.add("(  )" + "+" + j + "=" + result);
            this.testList.add(i + "+" + "(  )" + "=" + result);
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result = i + j;
          // +
          this.testList.add("(  )" + "+" + j + "=" + result);
          this.testList.add(i + "+" + "(  )" + "=" + result);
        }
      }
    }
  }

  /**
   * 加法高级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAddition3(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            int temp = i + j;
            int result = i + j + k;
            // ++
            if (temp <= end && result >= start && result <= end) {
              this.testList.add(i + "+" + j + "+" + k + "=");
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            // ++
            this.testList.add(i + "+" + j + "+" + k + "=");
          }
        }
      }
    }
  }

  /**
   * 加法特级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAddition4(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            int temp = i + j;
            int result = i + j + k;
            // ++
            if (temp <= end && result >= start && result <= end) {
              this.testList.add("(  )" + "+" + j + "+" + k + "=" + result);
              this.testList.add(i + "+" + "(  )" + "+" + k + "=" + result);
              this.testList.add(i + "+" + j + "+" + "(  )" + "=" + result);
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            int result = i + j + k;
            // ++
            this.testList.add("(  )" + "+" + j + "+" + k + "=" + result);
            this.testList.add(i + "+" + "(  )" + "+" + k + "=" + result);
            this.testList.add(i + "+" + j + "+" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * 减法初级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListSubduction1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i - j;
          // -
          if (result >= start && result <= end) {
            this.testList.add(i + "-" + j + "=");
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result = i - j;
          // -
          if (result >= 0) {
            this.testList.add(i + "-" + j + "=");
          }
        }
      }
    }
  }

  /**
   * 减法中级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListSubduction2(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i - j;
          // -
          if (result >= start && result <= end) {
            this.testList.add("(  )" + "-" + j + "=" + result);
            this.testList.add(i + "-" + "(  )" + "=" + result);
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result = i - j;
          // -
          if (result >= 0) {
            this.testList.add("(  )" + "-" + j + "=" + result);
            this.testList.add(i + "-" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * 减法高级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListSubduction3(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            int temp = i - j;
            int result = i - j - k;
            // --
            if (temp >= start && result >= start && result <= end) {
              this.testList.add(i + "-" + j + "-" + k + "=");
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            int temp = i - j;
            int result = i - j - k;
            // --
            if (temp >= 0 && result >= 0) {
              this.testList.add(i + "-" + j + "-" + k + "=");
            }
          }
        }
      }
    }
  }

  /**
   * 减法特级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListSubduction4(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            int temp = i - j;
            int result = i - j - k;
            // --
            if (temp >= start && result >= start && result <= end) {
              this.testList.add("(  )" + "-" + j + "-" + k + "=" + result);
              this.testList.add(i + "-" + "(  )" + "-" + k + "=" + result);
              this.testList.add(i + "-" + j + "-" + "(  )" + "=" + result);
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            int temp = i - j;
            int result = i - j - k;
            // --
            if (temp >= 0 && result >= 0) {
              this.testList.add("(  )" + "-" + j + "-" + k + "=" + result);
              this.testList.add(i + "-" + "(  )" + "-" + k + "=" + result);
              this.testList.add(i + "-" + j + "-" + "(  )" + "=" + result);
            }
          }
        }
      }
    }
  }

  /**
   * 加减法初级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAdditionSubduction1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result2 = i - j;
          // +
          this.testList.add(i + "+" + j + "=");
          // -
          if (result2 >= 0) {
            this.testList.add(i + "-" + j + "=");
          }
        }
      }
    }
  }

  /**
   * 加减法中级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAdditionSubduction2(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result1 = i + j;
          int result2 = i - j;
          // +
          this.testList.add("(  )" + "+" + j + "=" + result1);
          this.testList.add(i + "+" + "(  )" + "=" + result1);
          // -
          if (result2 >= 0) {
            this.testList.add("(  )" + "-" + j + "=" + result2);
            this.testList.add(i + "-" + "(  )" + "=" + result2);
          }
        }
      }
    }
  }

  /**
   * 加减法高级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAdditionSubduction3(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
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
            if (result1 >= start && result1 <= end) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            int temp1 = i + j;
            int temp2 = i - j;
            int result1 = i + j + k;
            int result2 = i + j - k;
            int result3 = i - j + k;
            int result4 = i - j - k;
            // ++
            this.testList.add(i + "+" + j + "+" + k + "=");
            // +-
            if (result2 >= 0) {
              this.testList.add(i + "+" + j + "-" + k + "=");
            }
            // -+
            if (temp2 >= 0 && result3 >= 0) {
              this.testList.add(i + "-" + j + "+" + k + "=");
            }
            // --
            if (temp2 >= 0 && result4 >= 0) {
              this.testList.add(i + "-" + j + "-" + k + "=");
            }
          }
        }
      }
    }
  }

  /**
   * 加减法特级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListAdditionSubduction4(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
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
            if (result1 >= start && result1 <= end) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            int temp1 = i + j;
            int temp2 = i - j;
            int result1 = i + j + k;
            int result2 = i + j - k;
            int result3 = i - j + k;
            int result4 = i - j - k;
            // ++
            this.testList.add("(  )" + "+" + j + "+" + k + "=" + result1);
            this.testList.add(i + "+" + "(  )" + "+" + k + "=" + result1);
            this.testList.add(i + "+" + j + "+" + "(  )" + "=" + result1);
            // +-
            if (result2 >= 0) {
              this.testList.add("(  )" + "+" + j + "-" + k + "=" + result2);
              this.testList.add(i + "+" + "(  )" + "-" + k + "=" + result2);
              this.testList.add(i + "+" + j + "-" + "(  )" + "=" + result2);
            }
            // -+
            if (temp2 >= 0 && result3 >= 0) {
              this.testList.add("(  )" + "-" + j + "+" + k + "=" + result3);
              this.testList.add(i + "-" + "(  )" + "+" + k + "=" + result3);
              this.testList.add(i + "-" + j + "+" + "(  )" + "=" + result3);
            }
            // --
            if (temp2 >= 0 && result4 >= 0) {
              this.testList.add("(  )" + "-" + j + "-" + k + "=" + result4);
              this.testList.add(i + "-" + "(  )" + "-" + k + "=" + result4);
              this.testList.add(i + "-" + j + "-" + "(  )" + "=" + result4);
            }
          }
        }
      }
    }
  }

  /**
   * 乘法初级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListMultiplication1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i * j;
          // ×
          if (result >= start && result <= end) {
            this.testList.add(i + "×" + j + "=");
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          // ×
          this.testList.add(i + "×" + j + "=");
        }
      }
    }
  }

  /**
   * 乘法中级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListMultiplication2(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          if (i == 0 && j == 0) {
            continue;
          }
          int result = i * j;
          // ×
          if (result >= start && result <= end) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          if (i == 0 && j == 0) {
            continue;
          }
          int result = i * j;
          // ×
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
  }

  /**
   * 乘法高级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListMultiplication3(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            int result = i * j * k;
            // ××
            if (result >= start && result <= end) {
              this.testList.add(i + "×" + j + "×" + k + "=");
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            // ××
            this.testList.add(i + "×" + j + "×" + k + "=");
          }
        }
      }
    }
  }

  /**
   * 乘法特级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListMultiplication4(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          for (int k = 0; k <= end; k++) {
            if ((i == 0 && j == 0) || (i == 0 && k == 0) || (j == 0 && k == 0)) {
              continue;
            }
            int result = i * j * k;
            // ××
            if (result >= start && result <= end) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            if ((i == 0 && j == 0) || (i == 0 && k == 0) || (j == 0 && k == 0)) {
              continue;
            }
            int result = i * j * k;
            // ××
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
  }

  /**
   * 除法初级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListDivision1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 1; j <= end; j++) {
          if ((i % j) == 0) {
            int result = i / j;
            // ÷
            if (result >= start && result <= end) {
              this.testList.add(i + "÷" + j + "=");
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          if (j == 0) {
            continue;
          }
          if ((i % j) == 0) {
            int result = i / j;
            // ÷
            this.testList.add(i + "÷" + j + "=");
          }
        }
      }
    }
  }

  /**
   * 除法中级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListDivision2(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 1; j <= end; j++) {
          if ((i % j) == 0) {
            int result = i / j;
            // ÷
            if (result >= start && result <= end) {
              if (i == 0) {
                this.testList.add("(  )" + "÷" + j + "=" + result);
              } else {
                this.testList.add("(  )" + "÷" + j + "=" + result);
                this.testList.add(i + "÷" + "(  )" + "=" + result);
              }
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          if (j == 0) {
            continue;
          }
          if ((i % j) == 0) {
            int result = i / j;
            // ÷
            if (i == 0) {
              this.testList.add("(  )" + "÷" + j + "=" + result);
            } else {
              this.testList.add("(  )" + "÷" + j + "=" + result);
              this.testList.add(i + "÷" + "(  )" + "=" + result);
            }
          }
        }
      }
    }
  }

  /**
   * 除法高级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListDivision3(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 1; j <= end; j++) {
          for (int k = 1; k <= end; k++) {
            int temp1 = i / j;
            int temp2 = i % j;
            int result = i / j / k;
            // ÷÷
            if (temp2 == 0 && (temp1 % k) == 0 && result >= start && result <= end) {
              this.testList.add(i + "÷" + j + "÷" + k + "=");
            }
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            if (j == 0 || k == 0) {
              continue;
            }
            int temp1 = i / j;
            int temp2 = i % j;
            int result = i / j / k;
            // ÷÷
            if (temp2 == 0 && (temp1 % k) == 0) {
              this.testList.add(i + "÷" + j + "÷" + k + "=");
            }
          }
        }
      }
    }
  }

  /**
   * 除法特级
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListDivision4(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 0; i <= end; i++) {
        for (int j = 1; j <= end; j++) {
          for (int k = 1; k <= end; k++) {
            int temp1 = i / j;
            int temp2 = i % j;
            int result = i / j / k;
            // ÷÷
            if (temp2 == 0 && (temp1 % k) == 0 && result >= start && result <= end) {
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
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          for (int k = start; k <= end; k++) {
            if (j == 0 || k == 0) {
              continue;
            }
            int temp1 = i / j;
            int temp2 = i % j;
            int result = i / j / k;
            // ÷÷
            if (temp2 == 0 && (temp1 % k) == 0) {
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
   * 退位减法
   * 
   * @param start 最小值
   * @param end 最大值
   * @param isStyleResult 是否按计算结果出题，true表示按计算结果出题
   */
  private void getListSpecial1(int start, int end, boolean isStyleResult) {
    if (isStyleResult) {
      for (int i = 11; i <= end; i++) {
        for (int j = 0; j <= end; j++) {
          int result = i - j;
          int single1 = this.getSingle(i);
          int single2 = this.getSingle(j);
          // -
          if (result >= start && result <= end && single1 < single2) {
            this.testList.add(i + "-" + j + "=");
          }
        }
      }
    } else {
      for (int i = start; i <= end; i++) {
        for (int j = start; j <= end; j++) {
          int result = i - j;
          int single1 = this.getSingle(i);
          int single2 = this.getSingle(j);
          // -
          if (result > 0 && single1 < single2) {
            this.testList.add(i + "-" + j + "=");
          }
        }
      }
    }
  }

  /**
   * 获取数字的个位数
   * 
   * @param number 数字
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
    this.cmbLevel1.addKeyListener(this.keyAdapter);
    this.cmbLevel2.addKeyListener(this.keyAdapter);
    this.cmbLevelS.addKeyListener(this.keyAdapter);
    this.cmbStyle.addKeyListener(this.keyAdapter);
    this.cmbStyle.addItemListener(this);
    this.txtMin.addKeyListener(this.keyAdapter);
    this.txtMax.addKeyListener(this.keyAdapter);
    this.txtCount.addKeyListener(this.keyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * 当所选项更改时调用
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    Object source = e.getSource();
    if (this.cmbStyle.equals(source)) {
      int index = this.cmbStyle.getSelectedIndex();
      if (index == 0) {
        this.refreshLabelText(false);
      } else {
        this.refreshLabelText(true);
      }
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    if (this.getTabbedIndex() == 0) {
      this.testQuestionCommon1();
    } else if (this.getTabbedIndex() == 1) {
      this.testQuestionCommon2();
    } else if (this.getTabbedIndex() == 2) {
      this.testQuestionSpecial();
    }
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
