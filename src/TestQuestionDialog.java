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
 * "���"�Ի���
 * 
 * @author ��ԭ
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
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private JLabel lblCount = new JLabel("������Ŀ��");
  private BaseTextField txtCount = new BaseTextField(true, "\\d*");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private EtchedBorder etchedBorder = new EtchedBorder();
  private MouseAdapter mouseAdapter = null;
  private String strSignIdentifier = ""; // ��ǰѡ�е���Ŀ�����ַ���
  private JLabel lblCurrent = null; // ��ǰѡ�е��ı���ǩ
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
   * @param hashtable
   *          ������ʾ�ַ��Ĺ�ϣ����Ϊ��ǩ��ֵΪ�ñ�ǩ�µ��ַ�����
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
   * ��ʼ������
   */
  private void init() {
    this.setTitle("���");
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
   * ������б�ǩҳ���ַ�
   * 
   * @param hashtable
   *          ������ʾ�ַ��Ĺ�ϣ����Ϊ��ǩ��ֵΪ�ñ�ǩ�µ��ַ�����
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
   * ���һ����ǩҳ���ַ�
   * 
   * @param strElement
   *          �ַ�����
   * @param strTitle
   *          ��ǩҳ����
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
    if (elementCount > gridTotal) { // ��֤Ԫ�صĸ��������ڲ�����Ԥ�����������������Ԫ�ؽ�������
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
   * ����һ���ı���ǩ
   * 
   * @param strElement
   *          ��ʾ���ַ�
   * @return �´������ı���ǩ
   */
  private JLabel createElement(String strElement) {
    JLabel lblElement = null;
    lblElement = new JLabel(Util.TEST_QUESTION_MATH_NAMES[Util.TEST_QUESTION_MATH.indexOf(strElement)]);
    if (!Util.isTextEmpty(strElement)) {
      lblElement.setHorizontalAlignment(SwingConstants.CENTER);
      lblElement.setFocusable(true); // ���ñ�ǩ���Ի�ý���
      lblElement.setOpaque(true); // ���ñ�ǩ���Ի��Ʊ���
      lblElement.setBorder(this.etchedBorder);
      lblElement.setBackground(Color.WHITE);
      lblElement.addKeyListener(this.keyAdapter);
      lblElement.addFocusListener(this);
      lblElement.addMouseListener(this.mouseAdapter);
    }
    return lblElement;
  }

  /**
   * ʹ�õ�ǰѡ�еĳ��ⷽʽ
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
   * ��Ӻͳ�ʼ���¼�������
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
        lblTemp.requestFocus(); // ����굥��ʱ����ý���
        if (e.getClickCount() == 2) {
          onEnter();
        }
      }
    };
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
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.testQuestion();
    this.dispose();
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * ���ı���ǩ��ý���ʱ�����������¼�
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
   * ���ı���ǩʧȥ����ʱ�����������¼�
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
   * �����������״̬�仯ʱ�����������¼�
   */
  public void stateChanged(ChangeEvent e) {
  }

  /**
   * �����ؼ��е��ı������仯ʱ�����������¼�
   */
  public void undoableEditHappened(UndoableEditEvent e) {
  }
}
