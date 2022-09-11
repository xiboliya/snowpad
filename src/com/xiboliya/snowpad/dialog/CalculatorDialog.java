/**
 * Copyright (C) 2018 冰原
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

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "计算器"对话框
 * 
 * @author 冰原
 * 
 */
public class CalculatorDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final String CALCULATOR_ITEM = "C←%÷789×456－123＋π0.="; // 计算器按钮
  private static final Font CALCULATOR_VIEW_FONT = new Font("宋体", Font.PLAIN, 16); // 显示区域的字体
  private static final Font CALCULATOR_ITEM_FONT = new Font("宋体", Font.BOLD, 30); // 计算区域的字体
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseTextField txtView = new BaseTextField();
  private JPanel pnlButton = new JPanel(new GridLayout(5, 4, 5, 5));
  private Insets insets = new Insets(0, 0, 0, 0);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private LinkedList<String> list = new LinkedList<String>();

  /**
   * 构造方法
   * 
   * @param owner
   *          用于显示该对话框的父组件
   * @param modal
   *          是否为模式对话框
   */
  public CalculatorDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.fillGridLayout();
    this.addListeners();
    this.setSize(300, 390);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("计算器");
    this.pnlMain.setLayout(null);
    this.txtView.setBounds(3, 3, 290, 42);
    this.txtView.setEditable(false);
    this.txtView.setFont(CALCULATOR_VIEW_FONT);
    this.txtView.setHorizontalAlignment(SwingConstants.RIGHT);
    this.pnlButton.setBounds(3, 50, 290, 290);
    this.pnlMain.add(this.txtView);
    this.pnlMain.add(this.pnlButton);
  }

  private void fillGridLayout() {
    for (int i = 0; i < CALCULATOR_ITEM.length(); i++) {
      char item = CALCULATOR_ITEM.charAt(i);
      JButton btnItem = new JButton(String.valueOf(item));
      btnItem.setActionCommand(String.valueOf(item));
      btnItem.setFont(CALCULATOR_ITEM_FONT);
      btnItem.setMargin(this.insets);
      btnItem.setFocusable(false);
      btnItem.addActionListener(this);
      btnItem.addKeyListener(this.buttonKeyAdapter);
      this.pnlButton.add(btnItem);
    }
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtView.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        String text = txtView.getText();
        switch (e.getKeyCode()) {
          case KeyEvent.VK_0:
          case KeyEvent.VK_NUMPAD0:
            digit(text, "0");
            break;
          case KeyEvent.VK_1:
          case KeyEvent.VK_NUMPAD1:
            digit(text, "1");
            break;
          case KeyEvent.VK_2:
          case KeyEvent.VK_NUMPAD2:
            digit(text, "2");
            break;
          case KeyEvent.VK_3:
          case KeyEvent.VK_NUMPAD3:
            digit(text, "3");
            break;
          case KeyEvent.VK_4:
          case KeyEvent.VK_NUMPAD4:
            digit(text, "4");
            break;
          case KeyEvent.VK_5:
          case KeyEvent.VK_NUMPAD5:
            digit(text, "5");
            break;
          case KeyEvent.VK_6:
          case KeyEvent.VK_NUMPAD6:
            digit(text, "6");
            break;
          case KeyEvent.VK_7:
          case KeyEvent.VK_NUMPAD7:
            digit(text, "7");
            break;
          case KeyEvent.VK_8:
          case KeyEvent.VK_NUMPAD8:
            digit(text, "8");
            break;
          case KeyEvent.VK_9:
          case KeyEvent.VK_NUMPAD9:
            digit(text, "9");
            break;
          case KeyEvent.VK_ADD:
            addition(text);
            break;
          case KeyEvent.VK_SUBTRACT:
            subduction(text);
            break;
          case KeyEvent.VK_MULTIPLY:
            multiplication(text);
            break;
          case KeyEvent.VK_DIVIDE:
            division(text);
            break;
          case KeyEvent.VK_DECIMAL:
          case KeyEvent.VK_PERIOD:
            dot(text);
            break;
          case KeyEvent.VK_BACK_SPACE:
            backSpace(text);
            break;
          case KeyEvent.VK_EQUALS:
          case KeyEvent.VK_ENTER:
            onEnter();
            break;
          case KeyEvent.VK_ESCAPE:
            onCancel();
            break;
        }
      }
    });
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();
    if (Util.isTextEmpty(actionCommand)) {
      return;
    }
    String text = this.txtView.getText();
    if (actionCommand.equals("C")) {
      this.clear();
    } else if (actionCommand.equals("←")) {
      this.backSpace(text);
    } else if (actionCommand.equals("%")) {
      this.percent(text);
    } else if (actionCommand.equals("π")) {
      this.pi(text);
    } else if (actionCommand.equals(".")) {
      this.dot(text);
    } else if (actionCommand.equals("＋")) {
      this.addition(text);
    } else if (actionCommand.equals("－")) {
      this.subduction(text);
    } else if (actionCommand.equals("×")) {
      this.multiplication(text);
    } else if (actionCommand.equals("÷")) {
      this.division(text);
    } else if (actionCommand.equals("=")) {
      this.onEnter();
    } else {
      this.digit(text, actionCommand);
    }
  }

  /**
   * 清除按钮的处理方法
   */
  private void clear() {
    this.txtView.setText("");
  }

  /**
   * 退格按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void backSpace(String text) {
    if (!Util.isTextEmpty(text)) {
      this.txtView.setText(text.substring(0, text.length() - 1));
    }
  }

  /**
   * 百分号按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void percent(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || ch == 'π') {
        this.txtView.setText(text + "%");
      } else if (ch == '.') {
        this.txtView.setText(text + "0%");
      }
    }
  }

  /**
   * 圆周率按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void pi(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || ch == '%' || ch == 'π') {
        this.txtView.setText(text + "×π");
      } else if (ch == '.') {
        this.txtView.setText(text + "0×π");
      } else {
        this.txtView.setText(text + "π");
      }
    } else {
      this.txtView.setText("π");
    }
  }

  /**
   * 小数点按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void dot(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '%' || ch == 'π') {
        this.txtView.setText(text + "×0.");
      } else if (this.isOperator(String.valueOf(ch))) {
        this.txtView.setText(text + "0.");
      } else if (Character.isDigit(ch) && !this.hasDot(text)) {
        this.txtView.setText(text + ".");
      }
    } else {
      this.txtView.setText("0.");
    }
  }

  /**
   * 加号按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void addition(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (text.length() == 1 && ch == '－') {
        this.txtView.setText("");
      } else if (ch == '.') {
        this.txtView.setText(text + "0＋");
      } else if (Character.isDigit(ch) || ch == '%' || ch == 'π') {
        this.txtView.setText(text + "＋");
      }
    }
  }

  /**
   * 减号按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void subduction(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0－");
      } else if (Character.isDigit(ch) || ch == '%' || ch == 'π') {
        this.txtView.setText(text + "－");
      }
    } else {
      this.txtView.setText("－");
    }
  }

  /**
   * 乘号按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void multiplication(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0×");
      } else if (Character.isDigit(ch) || ch == '%' || ch == 'π') {
        this.txtView.setText(text + "×");
      }
    }
  }

  /**
   * 除号按钮的处理方法
   * 
   * @param text
   *          算式
   */
  private void division(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0÷");
      } else if (Character.isDigit(ch) || ch == '%' || ch == 'π') {
        this.txtView.setText(text + "÷");
      }
    }
  }

  /**
   * 数字按钮的处理方法
   * 
   * @param text
   *          算式
   * @param number
   *          当前按钮的数字
   */
  private void digit(String text, String number) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || this.isOperator(String.valueOf(ch)) || ch == '.') {
        this.txtView.setText(text + number);
      } else if (ch == '%' || ch == 'π') {
        this.txtView.setText(text + "×" + number);
      }
    } else {
      this.txtView.setText(number);
    }
  }

  /**
   * 对算式进行计算
   * 
   * @param text
   *          算式
   */
  private void equal(String text) {
    this.list.clear();
    // 匹配运算符、百分号、π、整数、小数
    Pattern p = Pattern.compile("[＋－×÷%π]|-?\\d+\\.?\\d*");
    Matcher m = p.matcher(text);
    while (m.find()) {
      String str = m.group();
      if (str.equals("π")) {
        str = String.valueOf(Math.PI);
      }
      this.list.add(str);
    }
    if (this.calculatePercent() && this.calculateFirst() && this.calculateSecond()) {
      if (this.list.size() == 1) {
        String result = this.list.get(0);
        int index = this.getZeroIndex(result);
        if (index >= 0) {
          result = result.substring(0, index);
        }
        this.txtView.setText(result);
      }
    } else {
      TipsWindow.show(this, "语法错误，请检查！");
    }
  }

  /**
   * 运算百分比
   * 
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculatePercent() {
    int size = this.list.size();
    for (int i = 0; i < size; i++) {
      String current = this.list.get(i);
      if (current.equals("%")) {
        String previous = this.list.get(i - 1);
        try {
          BigDecimal number1 = new BigDecimal(previous);
          BigDecimal number2 = new BigDecimal("0.01");
          BigDecimal number = number1.multiply(number2);
          this.list.set(i - 1, number.toString());
          this.list.remove(i);
          size--;
        } catch (Exception x) {
          // x.printStackTrace();
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 运算第一优先级×÷
   * 
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateFirst() {
    int size = this.list.size();
    for (int i = 0; i < size; i++) {
      String current = this.list.get(i);
      if ("×".equals(current)) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateMultiplication(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size - 2;
          i--;
        } else {
          return false;
        }
      } else if ("÷".equals(current)) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateDivision(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size - 2;
          i--;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 运算第二优先级＋－
   * 
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateSecond() {
    int size = this.list.size();
    for (int i = 0; i < size; i++) {
      String current = this.list.get(i);
      if ("＋".equals(current)) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateAddition(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size - 2;
          i--;
        } else {
          return false;
        }
      } else if ("－".equals(current)) {
        if (i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateSubduction(i, size)) {
          if (i == 0) {
            this.list.remove(i + 1);
            size--;
          } else {
            this.list.remove(i);
            this.list.remove(i);
            size = size - 2;
            i--;
          }
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 乘法
   * 
   * @param index
   *          运算符位于算式中的索引值
   * @param size
   *          算式的元素总数
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateMultiplication(int index, int size) {
    String previous = this.list.get(index - 1);
    String next = this.list.get(index + 1);
    // 解决如果一个乘数的值为0而另一个乘数的精度过高时，比如为π时，运算结果不为0的问题
    boolean isHasZero = this.isHasZero(previous, next);
    if (isHasZero) {
      this.list.set(index - 1, "0");
      return true;
    }
    try {
      BigDecimal number1 = new BigDecimal(previous);
      BigDecimal number2 = new BigDecimal(next);
      BigDecimal number = number1.multiply(number2);
      this.list.set(index - 1, number.toString());
    } catch (Exception x) {
      // x.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 判断在乘法算式中是否存在值为0的乘数
   * 
   * @param previous
   *          乘法算式中的第一个乘数
   * @param next
   *          乘法算式中的第二个乘数
   * @return 在乘法算式中是否存在值为0的乘数，如果含有则返回true
   */
  private boolean isHasZero(String previous, String next) {
    boolean isZero = false;
    isZero = this.isZero(previous);
    if (!isZero) {
      isZero = this.isZero(next);
    }
    return isZero;
  }

  /**
   * 判断字符串表示的数字是否为0
   * 
   * @param number
   *          表示数字的字符串
   * @return 字符串表示的数字是否为0，如果为0则返回true
   */
  private boolean isZero(String number) {
    int i = 0;
    int size = number.length();
    for (i = 0; i < size; i++) {
      char c = number.charAt(i);
      if (c != '0' && c != '.') {
        break;
      }
    }
    if (i == size) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 除法
   * 
   * @param index
   *          运算符位于算式中的索引值
   * @param size
   *          算式的元素总数
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateDivision(int index, int size) {
    String previous = this.list.get(index - 1);
    String next = this.list.get(index + 1);
    try {
      BigDecimal number1 = new BigDecimal(previous);
      BigDecimal number2 = new BigDecimal(next);
      BigDecimal number = number1.divide(number2, 10, BigDecimal.ROUND_HALF_UP);
      this.list.set(index - 1, number.toString());
    } catch (Exception x) {
      // x.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 加法
   * 
   * @param index
   *          运算符位于算式中的索引值
   * @param size
   *          算式的元素总数
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateAddition(int index, int size) {
    String previous = this.list.get(index - 1);
    String next = this.list.get(index + 1);
    try {
      BigDecimal number1 = new BigDecimal(previous);
      BigDecimal number2 = new BigDecimal(next);
      BigDecimal number = number1.add(number2);
      this.list.set(index - 1, number.toString());
    } catch (Exception x) {
      // x.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 减法
   * 
   * @param index
   *          运算符位于算式中的索引值
   * @param size
   *          算式的元素总数
   * @return 是否运算成功，如果有语法错误则返回false
   */
  private boolean calculateSubduction(int index, int size) {
    String next = this.list.get(index + 1);
    if (index == 0) {
      try {
        BigDecimal number = new BigDecimal(next);
        this.list.set(index, "-" + number.toString());
      } catch (Exception x) {
        // x.printStackTrace();
        return false;
      }
      return true;
    }
    String previous = this.list.get(index - 1);
    try {
      BigDecimal number1 = new BigDecimal(previous);
      BigDecimal number2 = new BigDecimal(next);
      BigDecimal number = number1.subtract(number2);
      this.list.set(index - 1, number.toString());
    } catch (Exception x) {
      // x.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 字符串是否为运算符
   * 
   * @param str
   *          字符串
   * @return 字符串是否为运算符
   */
  private boolean isOperator(String str) {
    if (Util.isTextEmpty(str)) {
      return false;
    } else if (str.equals("＋") || str.equals("－") || str.equals("×") || str.equals("÷")) {
      return true;
    }
    return false;
  }

  /**
   * 算式中最后一个元素是否是数字，并且含有小数点
   * 
   * @param text
   *          算式
   * @return 最后一个元素是否是数字，并且含有小数点为true
   */
  private boolean hasDot(String text) {
    int length = text.length();
    for (int i = length - 1; i >= 0; i--) {
      char ch = text.charAt(i);
      if (ch == '.') {
        return true;
      } else if (Character.isDigit(ch)) {
        continue;
      } else {
        return false;
      }
    }
    return false;
  }

  /**
   * 获取运算结果的补位0或小数点的索引值
   * 
   * @param result
   *          运算结果
   * @return 运算结果的补位0或小数点的索引值
   */
  private int getZeroIndex(String result) {
    if (Util.isTextEmpty(result) || !result.contains(".")) {
      return -1;
    }
    int length = result.length();
    int index = -1;
    for (int i = length - 1; i >= 0; i--) {
      char ch = result.charAt(i);
      if (ch == '.') {
        return i;
      } else if (ch == '0') {
        index = i;
        continue;
      } else {
        if (index > -1) {
          return index;
        } else {
          return -1;
        }
      }
    }
    return -1;
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.equal(this.txtView.getText());
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

}
