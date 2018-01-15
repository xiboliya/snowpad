/**
 * Copyright (C) 2013 ��ԭ
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
import java.util.Enumeration;
import java.util.Hashtable;
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
 * "�б��������"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class SignIdentifierDialog extends BaseDialog implements ActionListener,
    FocusListener, ChangeListener, UndoableEditListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTabbedPane tpnMain = new JTabbedPane();
  private GridLayout gridLayout = new GridLayout(Util.SIGN_MAX_ROW,
      Util.SIGN_MAX_COLUMN, 5, 5);
  private GridLayout specialGridLayout = new GridLayout(Util.SIGN_MAX_ROW, 1, 5, 5);
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private JLabel lblStart = new JLabel("��ʼ��ţ�");
  private BaseTextField txtStart = new BaseTextField(true, "\\d*");
  private JTextArea txaView = new JTextArea();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private EtchedBorder etchedBorder = new EtchedBorder();
  private MouseAdapter mouseAdapter = null;
  private String strSignIdentifier = ""; // ��ǰѡ�еķ��Ż����ַ���

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
  public SignIdentifierDialog(JFrame owner, boolean modal, JTextArea txaSource,
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
    this.setTitle("�б��������");
    this.pnlMain.setLayout(null);
    this.pnlLeft.setBounds(0, 0, 200, 245);
    this.pnlRight.setBounds(200, 0, 120, 240);
    this.pnlMain.add(this.pnlLeft);
    this.pnlMain.add(this.pnlRight);
    this.pnlLeft.add(this.tpnMain, BorderLayout.CENTER);
    this.btnOk.setBounds(15, 30, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(15, 70, 90, Util.BUTTON_HEIGHT);
    this.lblStart.setBounds(6, 120, 70, Util.VIEW_HEIGHT);
    this.txtStart.setBounds(76, 120, 40, Util.INPUT_HEIGHT);
    this.txaView.setBounds(12, 160, 98, 65);
    this.txaView.setBorder(new EtchedBorder());
    this.txaView.setOpaque(true);
    this.txaView.setEditable(false);
    this.txaView.setFocusable(false);
    this.pnlRight.add(this.btnOk);
    this.pnlRight.add(this.btnCancel);
    this.pnlRight.add(this.lblStart);
    this.pnlRight.add(this.txtStart);
    this.pnlRight.add(this.txaView);
    this.tpnMain.setFocusable(false);
    this.btnOk.setFocusable(false);
    this.btnCancel.setFocusable(false);
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
    boolean isSpecial = false;
    JPanel pnlTemp = null;
    int gridTotal = 0;
    if (strElement.equals(Util.IDENTIFIER_CHARS)) {
      isSpecial = true;
    }
    if (isSpecial) {
      pnlTemp = new JPanel(this.specialGridLayout); // ���������ʽ�Ĳ���
      gridTotal = this.specialGridLayout.getRows()
          * this.specialGridLayout.getColumns();
    } else {
      pnlTemp = new JPanel(this.gridLayout); // ������ͨ��ʽ�Ĳ���
      gridTotal = this.gridLayout.getRows() * this.gridLayout.getColumns();
    }
    if (elementCount > gridTotal) { // ��֤Ԫ�صĸ��������ڲ�����Ԥ�����������������Ԫ�ؽ�������
      elementCount = gridTotal;
    }
    int index = 0;
    for (; index < elementCount; index++) {
      char charElement = strElement.charAt(index);
      JLabel lblElement = this.createElement(String.valueOf(charElement),
          isSpecial);
      pnlTemp.add(lblElement);
    }
    for (; index < gridTotal; index++) {
      JLabel lblElement = this.createElement("", false);
      pnlTemp.add(lblElement);
    }
    this.tpnMain.add(pnlTemp, strTitle);
  }

  /**
   * ����һ���ı���ǩ
   * 
   * @param strElement
   *          ��ʾ���ַ�
   * @param isSpecial
   *          �Ƿ�Ϊ�����ʽ
   * @return �´������ı���ǩ
   */
  private JLabel createElement(String strElement, boolean isSpecial) {
    JLabel lblElement = null;
    if (isSpecial) {
      lblElement = new JLabel(Util.SIGN_IDENTIFIER_NAMES[Util.IDENTIFIER_CHARS
          .indexOf(strElement)]);
    } else {
      lblElement = new JLabel(strElement);
      lblElement.setFont(Util.SIGN_VIEW_FONT);
    }
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
   * ʹ�û�Ԥ����ǰѡ�еķ��Ż��ŷ�ʽ
   * 
   * @param isView
   *          �Ƿ�ΪԤ��ģʽ����Ϊtrueʱ��ʾΪԤ����ǰģʽ������Ϊʹ�õ�ǰģʽ
   */
  private void signIdentifier(boolean isView) {
    String[] arrText = null;
    if (isView) {
      arrText = Util.SIGN_CHARS_VIEW.split("\n");
    } else {
      arrText = Util.getCurrentLinesArray(this.txaSource);
    }
    if (arrText == null || arrText.length <= 0) {
      return;
    }
    boolean isSpecial = false;
    if (this.tpnMain.getSelectedIndex() == 0) {
      isSpecial = true;
    }
    String strStart = this.txtStart.getText().trim();
    int start = 1;
    try {
      start = Integer.parseInt(strStart);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
    }
    this.toConvertArray(arrText, isSpecial, start - 1);
    StringBuilder stbIndent = new StringBuilder();
    for (String str : arrText) {
      stbIndent.append(str + "\n");
    }
    if (isView) {
      this.txaView.setText(stbIndent.deleteCharAt(stbIndent.length() - 1)
          .toString()); // ɾ���ַ���ĩβ����Ļ��з�
    } else {
      this.txaSource.replaceSelection(stbIndent.deleteCharAt(
          stbIndent.length() - 1).toString()); // ɾ���ַ���ĩβ����Ļ��з�
    }
  }

  /**
   * Ϊָ�����ַ���������ӵ�ǰѡ�еķ��Ż���
   * 
   * @param arrText
   *          ��������ַ�������
   * @param isSpecial
   *          �Ƿ�Ϊ�����ʽ
   * @param start
   *          ��ʼ���
   */
  private void toConvertArray(String[] arrText, boolean isSpecial, int start) {
    int n = 0;
    if (isSpecial) {
      int index = 0;
      for (; index < Util.SIGN_IDENTIFIER_NAMES.length; index++) {
        if (this.strSignIdentifier.equals(Util.SIGN_IDENTIFIER_NAMES[index])) {
          break;
        }
      }
      switch (index) {
      case 0: // ����
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = (n + start + 1) + "." + arrText[n];
        }
        break;
      case 1: // ����
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToChinese(n + start + 1, false) + "." + arrText[n];
        }
        break;
      case 2: // ��֧
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToGanZhi(n + start) + "." + arrText[n];
        }
        break;
      case 3: // ��ĸ
        for (n = 0; n < arrText.length; n++) {
          arrText[n] = Util.intToLetter(n + start + 1, false) + "." + arrText[n];
        }
        break;
      }
    } else {
      for (n = 0; n < arrText.length; n++) {
        arrText[n] = this.strSignIdentifier + arrText[n];
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
    this.txtStart.addKeyListener(this.keyAdapter);
    this.txtStart.getDocument().addUndoableEditListener(this);
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
    this.signIdentifier(false); // Ӧ�õ�ǰ���б���Ż���Ч��
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
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.PINK);
    this.strSignIdentifier = lblTemp.getText();
    this.signIdentifier(true); // Ԥ����ǰ���б���Ż���Ч��
  }

  /**
   * ���ı���ǩʧȥ����ʱ�����������¼�
   */
  public void focusLost(FocusEvent e) {
    JLabel lblTemp = (JLabel) e.getSource();
    lblTemp.setBackground(Color.WHITE);
  }

  /**
   * �����������״̬�仯ʱ�����������¼�
   */
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.txtStart.setEnabled(true);
      this.txtStart.setFocusable(true);
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      this.txtStart.setEnabled(false);
      this.txtStart.setFocusable(false);
    }
  }

  /**
   * �����ؼ��е��ı������仯ʱ�����������¼�
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.signIdentifier(true); // Ԥ����ǰ���б���Ż���Ч��
  }
}
