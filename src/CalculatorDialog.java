/**
 * Copyright (C) 2018 ��ԭ
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * "������"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class CalculatorDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseTextField txtView = new BaseTextField();
  private JPanel pnlButton = new JPanel(new GridLayout(5, 4, 5, 5));
  private Insets insets = new Insets(0, 0, 0, 0);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private LinkedList<String> list = new LinkedList<String>();

  /**
   * ���췽��
   * 
   * @param owner
   *          ������ʾ�öԻ���ĸ����
   * @param modal
   *          �Ƿ�Ϊģʽ�Ի���
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
   * ��ʼ������
   */
  private void init() {
    this.setTitle("������");
    this.pnlMain.setLayout(null);
    this.txtView.setBounds(3, 3, 290, 42);
    this.txtView.setEditable(false);
    this.txtView.setFont(Util.CALCULATOR_VIEW_FONT);
    this.txtView.setHorizontalAlignment(SwingConstants.RIGHT);
    this.pnlButton.setBounds(3, 50, 290, 290);
    this.pnlMain.add(this.txtView);
    this.pnlMain.add(this.pnlButton);
  }

  private void fillGridLayout() {
    for (int i = 0; i < Util.CALCULATOR_ITEM.length(); i++) {
      char item = Util.CALCULATOR_ITEM.charAt(i);
      JButton btnItem = new JButton(String.valueOf(item));
      btnItem.setActionCommand(String.valueOf(item));
      btnItem.setFont(Util.CALCULATOR_ITEM_FONT);
      btnItem.setMargin(this.insets);
      btnItem.setFocusable(false);
      btnItem.addActionListener(this);
      btnItem.addKeyListener(this.buttonKeyAdapter);
      this.pnlButton.add(btnItem);
    }
  }

  /**
   * ����¼�������
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
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();
    if (Util.isTextEmpty(actionCommand)) {
      return;
    }
    String text = this.txtView.getText();
    if (actionCommand.equals("C")) {
      this.clear();
    } else if (actionCommand.equals("��")) {
      this.backSpace(text);
    } else if (actionCommand.equals("%")) {
      this.percent(text);
    } else if (actionCommand.equals("��")) {
      this.pi(text);
    } else if (actionCommand.equals(".")) {
      this.dot(text);
    } else if (actionCommand.equals("��")) {
      this.addition(text);
    } else if (actionCommand.equals("��")) {
      this.subduction(text);
    } else if (actionCommand.equals("��")) {
      this.multiplication(text);
    } else if (actionCommand.equals("��")) {
      this.division(text);
    } else if (actionCommand.equals("=")) {
      this.onEnter();
    } else {
      this.digit(text, actionCommand);
    }
  }

  /**
   * �����ť�Ĵ�����
   */
  private void clear() {
    this.txtView.setText("");
  }

  /**
   * �˸�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void backSpace(String text) {
    if (!Util.isTextEmpty(text)) {
      this.txtView.setText(text.substring(0, text.length() - 1));
    }
  }

  /**
   * �ٷֺŰ�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void percent(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || ch == '��') {
        this.txtView.setText(text + "%");
      } else if (ch == '.') {
        this.txtView.setText(text + "0%");
      }
    }
  }

  /**
   * Բ���ʰ�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void pi(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || ch == '%' || ch == '��') {
        this.txtView.setText(text + "����");
      } else if (ch == '.') {
        this.txtView.setText(text + "0����");
      } else {
        this.txtView.setText(text + "��");
      }
    } else {
      this.txtView.setText("��");
    }
  }

  /**
   * С���㰴ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void dot(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '%' || ch == '��') {
        this.txtView.setText(text + "��0.");
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
   * �ӺŰ�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void addition(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (text.length() == 1 && ch == '��') {
        this.txtView.setText("");
      } else if (ch == '.') {
        this.txtView.setText(text + "0��");
      } else if (Character.isDigit(ch) || ch == '%' || ch == '��') {
        this.txtView.setText(text + "��");
      }
    }
  }

  /**
   * ���Ű�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void subduction(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0��");
      } else if (Character.isDigit(ch) || ch == '%' || ch == '��') {
        this.txtView.setText(text + "��");
      }
    } else {
      this.txtView.setText("��");
    }
  }

  /**
   * �˺Ű�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void multiplication(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0��");
      } else if (Character.isDigit(ch) || ch == '%' || ch == '��') {
        this.txtView.setText(text + "��");
      }
    }
  }

  /**
   * ���Ű�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   */
  private void division(String text) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (ch == '.') {
        this.txtView.setText(text + "0��");
      } else if (Character.isDigit(ch) || ch == '%' || ch == '��') {
        this.txtView.setText(text + "��");
      }
    }
  }

  /**
   * ���ְ�ť�Ĵ�����
   * 
   * @param text
   *          ��ʽ
   * @param number
   *          ��ǰ��ť������
   */
  private void digit(String text, String number) {
    if (!Util.isTextEmpty(text)) {
      char ch = text.charAt(text.length() - 1);
      if (Character.isDigit(ch) || this.isOperator(String.valueOf(ch)) || ch == '.') {
        this.txtView.setText(text + number);
      } else if (ch == '%' || ch == '��') {
        this.txtView.setText(text + "��" + number);
      }
    } else {
      this.txtView.setText(number);
    }
  }

  /**
   * ����ʽ���м���
   * 
   * @param text
   *          ��ʽ
   */
  private void equal(String text) {
    this.list.clear();
    // ƥ����������ٷֺš��С�������С��
    Pattern p = Pattern.compile("[��������%��]|-?\\d+\\.?\\d*");
    Matcher m = p.matcher(text);
    while (m.find()) {
      String str = m.group();
      if (str.equals("��")) {
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
      JOptionPane.showMessageDialog(this, "�﷨�������飡", Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * ����ٷֱ�
   * 
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
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
   * �����һ���ȼ�����
   * 
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
   */
  private boolean calculateFirst() {
    int size = this.list.size();
    for (int i = 0; i < size; i++) {
      String current = this.list.get(i);
      if (current.equals("��")) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateMultiplication(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size -2;
          i--;
        } else {
          return false;
        }
      } else if (current.equals("��")) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateDivision(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size -2;
          i--;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * ����ڶ����ȼ�����
   * 
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
   */
  private boolean calculateSecond() {
    int size = this.list.size();
    for (int i = 0; i < size; i++) {
      String current = this.list.get(i);
      if (current.equals("��")) {
        if (i == 0 || i == size - 1) {
          this.list.remove(i);
          size--;
        } else if (this.calculateAddition(i, size)) {
          this.list.remove(i);
          this.list.remove(i);
          size = size -2;
          i--;
        } else {
          return false;
        }
      } else if (current.equals("��")) {
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
            size = size -2;
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
   * �˷�
   * 
   * @param index
   *          �����λ����ʽ�е�����ֵ
   * @param size
   *          ��ʽ��Ԫ������
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
   */
  private boolean calculateMultiplication(int index, int size) {
    String previous = this.list.get(index - 1);
    String next = this.list.get(index + 1);
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
   * ����
   * 
   * @param index
   *          �����λ����ʽ�е�����ֵ
   * @param size
   *          ��ʽ��Ԫ������
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
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
   * �ӷ�
   * 
   * @param index
   *          �����λ����ʽ�е�����ֵ
   * @param size
   *          ��ʽ��Ԫ������
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
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
   * ����
   * 
   * @param index
   *          �����λ����ʽ�е�����ֵ
   * @param size
   *          ��ʽ��Ԫ������
   * @return �Ƿ�����ɹ���������﷨�����򷵻�false
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
   * �ַ����Ƿ�Ϊ�����
   * 
   * @param str
   *          �ַ���
   * @return �ַ����Ƿ�Ϊ�����
   */
  private boolean isOperator(String str) {
    if (Util.isTextEmpty(str)) {
      return false;
    } else if (str.equals("��") || str.equals("��") || str.equals("��") || str.equals("��")) {
      return true;
    }
    return false;
  }

  /**
   * ��ʽ�����һ��Ԫ���Ƿ������֣����Һ���С����
   * 
   * @param text
   *          ��ʽ
   * @return ���һ��Ԫ���Ƿ������֣����Һ���С����Ϊtrue
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
   * ��ȡ�������Ĳ�λ0��С���������ֵ
   * 
   * @param result
   *          ������
   * @return �������Ĳ�λ0��С���������ֵ
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
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.equal(this.txtView.getText());
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }

}
