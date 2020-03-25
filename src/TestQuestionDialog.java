/**
 * Copyright (C) 2019 ��ԭ
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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * "���"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class TestQuestionDialog extends BaseDialog implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  // ��׼
  private String strMinTextC = "�����Сֵ";
  private String strMaxTextC = "������ֵ";
  private JPanel pnlCommon = new JPanel();
  private JLabel lblLevelC = new JLabel("�Ѷȵȼ���");
  private JComboBox<String> cmbLevelC = new JComboBox<String>(Util.TEST_QUESTION_MATH_LEVELS);
  private JLabel lblMinC = new JLabel(this.strMinTextC + "��");
  private BaseTextField txtMinC = new BaseTextField(true, "\\d*");
  private JLabel lblMaxC = new JLabel(this.strMaxTextC + "��");
  private BaseTextField txtMaxC = new BaseTextField(true, "\\d*");
  private JLabel lblCountC = new JLabel("������Ŀ��");
  private BaseTextField txtCountC = new BaseTextField(true, "\\d*");
  // ר��
  private String strMinTextS = "�����Сֵ";
  private String strMaxTextS = "������ֵ";
  private JPanel pnlSpecial = new JPanel();
  private JLabel lblLevelS = new JLabel("��Ŀ���ͣ�");
  private JComboBox<String> cmbLevelS = new JComboBox<String>(Util.TEST_QUESTION_MATH_SPECIAL_LEVELS);
  private JLabel lblMinS = new JLabel(this.strMinTextS + "��");
  private BaseTextField txtMinS = new BaseTextField(true, "\\d*");
  private JLabel lblMaxS = new JLabel(this.strMaxTextS + "��");
  private BaseTextField txtMaxS = new BaseTextField(true, "\\d*");
  private JLabel lblCountS = new JLabel("������Ŀ��");
  private BaseTextField txtCountS = new BaseTextField(true, "\\d*");
  // ��ť
  private JPanel pnlBottom = new JPanel();
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private LinkedList<String> testList = new LinkedList<String>(); // �����Ŀ������

  /**
   * ���췽��
   * 
   * @param owner
   *          ������ʾ�öԻ���ĸ����
   * @param modal
   *          �Ƿ�Ϊģʽ�Ի���
   * @param txaSource
   *          ��Բ������ı���
   */
  public TestQuestionDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.addListeners();
    this.setSize(280, 280);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("���");
    this.pnlMain.setLayout(null);
    // ��׼
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
    // ר��
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
    this.tpnMain.add(this.pnlCommon, "��׼");
    this.tpnMain.add(this.pnlSpecial, "ר��");
    this.pnlMain.add(this.tpnMain);
    
    // ��ť
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
   * ��ȡѡ���ǰ��ͼ��������
   * 
   * @return ��ǰ��ͼ��������
   */
  private int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * ˢ�±�ǩ���ı�
   * 
   * @param isResult
   *          �Ƿ���ʾ�������
   */
  private void refreshLabelText(boolean isResult) {
    int index = this.getTabbedIndex();
    if (index == 0) {
      if (isResult) {
        this.strMinTextC = "�����Сֵ";
        this.strMaxTextC = "������ֵ";
      } else {
        this.strMinTextC = "������Сֵ";
        this.strMaxTextC = "�������ֵ";
      }
      this.lblMinC.setText(this.strMinTextC + "��");
      this.lblMaxC.setText(this.strMaxTextC + "��");
    } else {
      if (isResult) {
        this.strMinTextS = "�����Сֵ";
        this.strMaxTextS = "������ֵ";
      } else {
        this.strMinTextS = "������Сֵ";
        this.strMaxTextS = "�������ֵ";
      }
      this.lblMinS.setText(this.strMinTextS + "��");
      this.lblMaxS.setText(this.strMaxTextS + "��");
    }
  }

  /**
   * ʹ�õ�ǰѡ�еı�׼���ⷽʽ
   */
  private void testQuestionCommon() {
    String strMin = this.txtMinC.getText();
    if (Util.isTextEmpty(strMin)) {
      JOptionPane.showMessageDialog(this, "������" + this.strMinTextC + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strMax = this.txtMaxC.getText();
    if (Util.isTextEmpty(strMax)) {
      JOptionPane.showMessageDialog(this, "������" + this.strMaxTextC + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strCount = this.txtCountC.getText();
    if (Util.isTextEmpty(strCount)) {
      JOptionPane.showMessageDialog(this, "�����������Ŀ��", Util.SOFTWARE, JOptionPane.NO_OPTION);
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
      JOptionPane.showMessageDialog(this, this.strMinTextC + "���ܳ���" + this.strMaxTextC + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      JOptionPane.showMessageDialog(this, "������Ŀ�������0��", Util.SOFTWARE, JOptionPane.NO_OPTION);
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
    Collections.shuffle(testList); // �������List��Ԫ��˳��
    StringBuilder stbTest = new StringBuilder();
    int size = testList.size();
    if (count > size) {
      int result = JOptionPane.showConfirmDialog(this, "ʵ�������Ŀֻ�У�" + size + "���������մ���Ŀ���⣬�Ƿ������",
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
   * ʹ�õ�ǰѡ�е�ר����ⷽʽ
   */
  private void testQuestionSpecial() {
    String strMin = this.txtMinS.getText();
    if (Util.isTextEmpty(strMin)) {
      JOptionPane.showMessageDialog(this, "������" + this.strMinTextS + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strMax = this.txtMaxS.getText();
    if (Util.isTextEmpty(strMax)) {
      JOptionPane.showMessageDialog(this, "������" + this.strMaxTextS + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    String strCount = this.txtCountS.getText();
    if (Util.isTextEmpty(strCount)) {
      JOptionPane.showMessageDialog(this, "�����������Ŀ��", Util.SOFTWARE, JOptionPane.NO_OPTION);
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
      JOptionPane.showMessageDialog(this, this.strMinTextS + "���ܳ���" + this.strMaxTextC + "��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int count = 0;
    try {
      count = Integer.parseInt(strCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    if (count <= 0) {
      JOptionPane.showMessageDialog(this, "������Ŀ�������0��", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    }
    int level = this.cmbLevelS.getSelectedIndex();
    switch (level) {
    case 0:
      this.getListSpecial1(min, max);
      break;
    }
    Collections.shuffle(testList); // �������List��Ԫ��˳��
    StringBuilder stbTest = new StringBuilder();
    int size = testList.size();
    if (size == 0) {
      JOptionPane.showMessageDialog(this, "ʵ�������ĿΪ0������������������", Util.SOFTWARE, JOptionPane.NO_OPTION);
      return;
    } else if (count > size) {
      int result = JOptionPane.showConfirmDialog(this, "ʵ�������Ŀֻ�У�" + size + "���������մ���Ŀ���⣬�Ƿ������",
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
   * ��ѧ��Ŀ�Ӽ�������
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
   * ��ѧ��Ŀ�Ӽ����м�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
   * ��ѧ��Ŀ�Ӽ����߼�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
   * ��ѧ��Ŀ�Ӽ����ؼ�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
   * ��ѧ��Ŀ�˷�����
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
   */
  private void getListLevel5(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        this.testList.add(i + "��" + j + "=");
      }
    }
  }

  /**
   * ��ѧ��Ŀ�˷��м�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
   */
  private void getListLevel6(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        int result = i * j;
        if (i == 0) {
          this.testList.add("(  )" + "��" + j + "=" + result);
        } else if (j == 0) {
          this.testList.add(i + "��" + "(  )" + "=" + result);
        } else {
          this.testList.add("(  )" + "��" + j + "=" + result);
          this.testList.add(i + "��" + "(  )" + "=" + result);
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ�˷��߼�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
   */
  private void getListLevel7(int start, int end) {
    this.testList.clear();
    for (int i = start; i <= end; i++) {
      for (int j = start; j <= end; j++) {
        for (int k = start; k <= end; k++) {
          this.testList.add(i + "��" + j + "��" + k + "=");
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ�˷��ؼ�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
            this.testList.add("(  )" + "��" + j + "��" + k + "=" + result);
          } else if (j == 0) {
            this.testList.add(i + "��" + "(  )" + "��" + k + "=" + result);
          } else if (k == 0) {
            this.testList.add(i + "��" + j + "��" + "(  )" + "=" + result);
          } else {
            this.testList.add("(  )" + "��" + j + "��" + k + "=" + result);
            this.testList.add(i + "��" + "(  )" + "��" + k + "=" + result);
            this.testList.add(i + "��" + j + "��" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ��������
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
   */
  private void getListLevel9(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        if ((i % j) == 0) {
          int result = i / j;
          if (result >= start && result <= end) {
            this.testList.add(i + "��" + j + "=");
          }
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ�����м�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
   */
  private void getListLevel10(int start, int end) {
    this.testList.clear();
    for (int i = 0; i <= end; i++) {
      for (int j = 1; j <= end; j++) {
        if ((i % j) == 0) {
          int result = i / j;
          if (result >= start && result <= end) {
            this.testList.add("(  )" + "��" + j + "=" + result);
            this.testList.add(i + "��" + "(  )" + "=" + result);
          }
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ�����߼�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
              this.testList.add(i + "��" + j + "��" + k + "=");
            }
          }
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ�����ؼ�
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
                this.testList.add("(  )" + "��" + j + "��" + k + "=" + result);
              } else {
                this.testList.add("(  )" + "��" + j + "��" + k + "=" + result);
                this.testList.add(i + "��" + "(  )" + "��" + k + "=" + result);
                this.testList.add(i + "��" + j + "��" + "(  )" + "=" + result);
              }
            }
          }
        }
      }
    }
  }

  /**
   * ��ѧ��Ŀ��λ����
   * 
   * @param start
   *          ��Сֵ
   * @param end
   *          ���ֵ
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
   * ��ȡ���ֵĸ�λ��
   * 
   * @param number
   *          ����
   * @return ���ֵĸ�λ��
   */
  private int getSingle(int number) {
    String str = String.valueOf(number);
    int result = str.charAt(str.length() - 1);
    return result;
  }

  /**
   * ��Ӻͳ�ʼ���¼�������
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.cmbLevelC.addKeyListener(this.keyAdapter);
    this.cmbLevelC.addItemListener(this);
    this.txtMinC.addKeyListener(this.keyAdapter);
    this.txtMaxC.addKeyListener(this.keyAdapter);
    this.txtCountC.addKeyListener(this.keyAdapter);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * ����ѡ�����ʱ����
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
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    if (this.getTabbedIndex() == 0) {
      this.testQuestionCommon();
    } else if (this.getTabbedIndex() == 1) {
      this.testQuestionSpecial();
    }
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
