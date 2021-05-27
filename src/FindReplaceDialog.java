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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * "����"��"�滻"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class FindReplaceDialog extends BaseDialog implements ActionListener,
    CaretListener, ChangeListener, WindowFocusListener {
  private static final long serialVersionUID = 1L;
  private SearchResultPanel searchResultPanel; // ���ҽ�����
  private Setting setting = null; // �������������
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private boolean isFindDown = true; // ���²���
  private boolean isMatchCase = false; // ���ִ�Сд
  private boolean isWrap = false; // ѭ������
  private SearchStyle searchStyle = SearchStyle.DEFAULT; // ����ģʽ
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private String strFind = ""; // ���ҵ��ַ���
  private Matcher matcher = null; // ͨ������Pattern��ָ���ı�ִ��ƥ�����������
  // ����
  private JPanel pnlFind = new JPanel();
  private JLabel lblFindTextF = new JLabel("�������ݣ�");
  private BaseTextField txtFindTextF = new BaseTextField();
  private JCheckBox chkMatchCaseF = new JCheckBox("���ִ�Сд(C)", true);
  private JCheckBox chkIsWrapF = new JCheckBox("ѭ������(W)", false);
  private JRadioButton radDefaultF = new JRadioButton("��ͨ(E)", true);
  private JRadioButton radTransferF = new JRadioButton("ת����չ(T)", false);
  private JRadioButton radPatternF = new JRadioButton("������ʽ(P)", false);
  private JRadioButton radFindUpF = new JRadioButton("����(U)", false);
  private JRadioButton radFindDownF = new JRadioButton("����(D)", true);
  private JButton btnFindF = new JButton("����(F)");
  private JButton btnCountAllF = new JButton("ȫ��ͳ��(N)");
  private JButton btnCountSelF = new JButton("ѡ����ͳ��(S)");
  private JButton btnSearchInFileF = new JButton("�ļ��в���(I)");
  private JButton btnCancelF = new JButton("ȡ��");
  private ButtonGroup bgpSearchStyleF = new ButtonGroup();
  private ButtonGroup bgpFindUpDownF = new ButtonGroup();
  private JPanel pnlSearchStyleF = new JPanel(new GridLayout(3, 1));
  private JPanel pnlFindUpDownF = new JPanel(new GridLayout(2, 1));
  // �滻
  private JPanel pnlReplace = new JPanel();
  private JLabel lblFindTextR = new JLabel("�������ݣ�");
  private JLabel lblReplaceTextR = new JLabel("�滻Ϊ��");
  private BaseTextField txtFindTextR = new BaseTextField();
  private BaseTextField txtReplaceTextR = new BaseTextField();
  private JCheckBox chkMatchCaseR = new JCheckBox("���ִ�Сд(C)", true);
  private JCheckBox chkIsWrapR = new JCheckBox("ѭ������(W)", false);
  private JRadioButton radDefaultR = new JRadioButton("��ͨ(E)", true);
  private JRadioButton radTransferR = new JRadioButton("ת����չ(T)", false);
  private JRadioButton radPatternR = new JRadioButton("������ʽ(P)", false);
  private JRadioButton radFindUpR = new JRadioButton("����(U)", false);
  private JRadioButton radFindDownR = new JRadioButton("����(D)", true);
  private JButton btnFindR = new JButton("����(F)");
  private JButton btnReplaceR = new JButton("�滻(R)");
  private JButton btnReplaceAllR = new JButton("ȫ���滻(A)");
  private JButton btnReplaceSelR = new JButton("ѡ�����滻(S)");
  private JButton btnCancelR = new JButton("ȡ��");
  private ButtonGroup bgpSearchStyleR = new ButtonGroup();
  private ButtonGroup bgpFindUpDownR = new ButtonGroup();
  private JPanel pnlSearchStyleR = new JPanel(new GridLayout(3, 1));
  private JPanel pnlFindUpDownR = new JPanel(new GridLayout(2, 1));

  public FindReplaceDialog(JFrame owner, boolean modal, JTextArea txaSource, SearchResultPanel searchResultPanel,
    Setting setting, boolean visible) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.setting = setting;
    this.txaSource = txaSource;
    this.searchResultPanel = searchResultPanel;
    this.setTitle("����");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.setSize(400, 255);
    this.setVisible(visible);
  }

  /**
   * �����ʼ��
   */
  private void init() {
    // ����
    this.pnlFind.setLayout(null);
    this.lblFindTextF.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.txtFindTextF.setBounds(80, 9, 180, Util.INPUT_HEIGHT);
    this.pnlFind.add(this.lblFindTextF);
    this.pnlFind.add(this.txtFindTextF);
    this.chkMatchCaseF.setBounds(10, 50, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkMatchCaseF);
    this.chkIsWrapF.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlFind.add(this.chkIsWrapF);
    this.pnlFindUpDownF.setBounds(10, 90, 95, 70);
    this.pnlFindUpDownF.setBorder(new TitledBorder("����"));
    this.pnlFindUpDownF.add(this.radFindUpF);
    this.pnlFindUpDownF.add(this.radFindDownF);
    this.pnlFind.add(this.pnlFindUpDownF);
    this.pnlSearchStyleF.setBounds(130, 70, 130, 90);
    this.pnlSearchStyleF.setBorder(new TitledBorder("ģʽ"));
    this.pnlSearchStyleF.add(this.radDefaultF);
    this.pnlSearchStyleF.add(this.radTransferF);
    this.pnlSearchStyleF.add(this.radPatternF);
    this.radTransferF.setToolTipText("��ʹ��\\n��\\tת���ַ�");
    this.pnlFind.add(this.pnlSearchStyleF);
    this.btnFindF.setEnabled(false);
    this.btnCountAllF.setEnabled(false);
    this.btnCountSelF.setEnabled(false);
    this.btnSearchInFileF.setEnabled(false);
    this.btnFindF.setBounds(270, 10, 110, Util.BUTTON_HEIGHT);
    this.btnCountAllF.setBounds(270, 45, 110, Util.BUTTON_HEIGHT);
    this.btnCountSelF.setBounds(270, 80, 110, Util.BUTTON_HEIGHT);
    this.btnSearchInFileF.setBounds(270, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCancelF.setBounds(270, 150, 110, Util.BUTTON_HEIGHT);
    this.pnlFind.add(this.btnFindF);
    this.pnlFind.add(this.btnCountAllF);
    this.pnlFind.add(this.btnCountSelF);
    this.pnlFind.add(this.btnSearchInFileF);
    this.pnlFind.add(this.btnCancelF);
    this.bgpSearchStyleF.add(this.radDefaultF);
    this.bgpSearchStyleF.add(this.radTransferF);
    this.bgpSearchStyleF.add(this.radPatternF);
    this.bgpFindUpDownF.add(this.radFindDownF);
    this.bgpFindUpDownF.add(this.radFindUpF);
    // �滻
    this.pnlReplace.setLayout(null);
    this.lblFindTextR.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.txtFindTextR.setBounds(80, 9, 180, Util.INPUT_HEIGHT);
    this.pnlReplace.add(this.lblFindTextR);
    this.pnlReplace.add(this.txtFindTextR);
    this.lblReplaceTextR.setBounds(10, 38, 70, Util.VIEW_HEIGHT);
    this.txtReplaceTextR.setBounds(80, 37, 180, Util.INPUT_HEIGHT);
    this.pnlReplace.add(this.lblReplaceTextR);
    this.pnlReplace.add(this.txtReplaceTextR);
    this.chkMatchCaseR.setBounds(10, 70, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkMatchCaseR);
    this.chkIsWrapR.setBounds(10, 90, 110, Util.VIEW_HEIGHT);
    this.pnlReplace.add(this.chkIsWrapR);
    this.pnlFindUpDownR.setBounds(10, 110, 95, 70);
    this.pnlFindUpDownR.setBorder(new TitledBorder("����"));
    this.pnlFindUpDownR.add(this.radFindUpR);
    this.pnlFindUpDownR.add(this.radFindDownR);
    this.pnlReplace.add(this.pnlFindUpDownR);
    this.pnlSearchStyleR.setBounds(130, 90, 130, 90);
    this.pnlSearchStyleR.setBorder(new TitledBorder("ģʽ"));
    this.pnlSearchStyleR.add(this.radDefaultR);
    this.pnlSearchStyleR.add(this.radTransferR);
    this.pnlSearchStyleR.add(this.radPatternR);
    this.radTransferR.setToolTipText("��ʹ��\\n��\\tת���ַ�");
    this.pnlReplace.add(this.pnlSearchStyleR);
    this.btnFindR.setEnabled(false);
    this.btnReplaceR.setEnabled(false);
    this.btnReplaceAllR.setEnabled(false);
    this.btnReplaceSelR.setEnabled(false);
    this.btnFindR.setBounds(270, 10, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceR.setBounds(270, 45, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceAllR.setBounds(270, 80, 110, Util.BUTTON_HEIGHT);
    this.btnReplaceSelR.setBounds(270, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCancelR.setBounds(270, 150, 110, Util.BUTTON_HEIGHT);
    this.pnlReplace.add(this.btnFindR);
    this.pnlReplace.add(this.btnReplaceR);
    this.pnlReplace.add(this.btnReplaceAllR);
    this.pnlReplace.add(this.btnReplaceSelR);
    this.pnlReplace.add(this.btnCancelR);
    this.bgpSearchStyleR.add(this.radDefaultR);
    this.bgpSearchStyleR.add(this.radTransferR);
    this.bgpSearchStyleR.add(this.radPatternR);
    this.bgpFindUpDownR.add(this.radFindDownR);
    this.bgpFindUpDownR.add(this.radFindUpR);
    // ������
    this.tpnMain.add(this.pnlFind, "����");
    this.tpnMain.add(this.pnlReplace, "�滻");
    this.pnlMain.add(this.tpnMain, BorderLayout.CENTER);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
  }

  /**
   * ��ʼ����ѡ���״̬
   */
  private void initView() {
    this.isMatchCase = this.setting.matchCase;
    this.isWrap = this.setting.isWrap;
    this.isFindDown = this.setting.findDown;
    this.searchStyle = this.setting.searchStyle;
    this.chkMatchCaseF.setSelected(this.setting.matchCase);
    this.chkMatchCaseR.setSelected(this.setting.matchCase);
    this.chkIsWrapF.setSelected(this.setting.isWrap);
    this.chkIsWrapR.setSelected(this.setting.isWrap);
    if (this.setting.findDown) {
      this.radFindDownF.setSelected(true);
      this.radFindDownR.setSelected(true);
    } else {
      this.radFindUpF.setSelected(true);
      this.radFindUpR.setSelected(true);
    }
    switch (this.setting.searchStyle) {
    case DEFAULT:
      this.radDefaultF.setSelected(true);
      this.radDefaultR.setSelected(true);
      break;
    case TRANSFER:
      this.radTransferF.setSelected(true);
      this.radTransferR.setSelected(true);
      break;
    case PATTERN:
      this.radPatternF.setSelected(true);
      this.radPatternR.setSelected(true);
      break;
    }
  }

  /**
   * ����ѡ��ĵ�ǰ��ͼ
   * 
   * @param index
   *          ��ͼ��������
   */
  public void setTabbedIndex(int index) {
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * ��ȡѡ���ǰ��ͼ��������
   * 
   * @return ��ǰ��ͼ��������
   */
  public int getTabbedIndex() {
    return this.tpnMain.getSelectedIndex();
  }

  /**
   * ѡ�С��������ݡ��е��ı�
   */
  public void setFindTextSelect() {
    int tabbedIndex = this.getTabbedIndex();
    if (tabbedIndex == 0) {
      this.txtFindTextF.selectAll();
    } else {
      this.txtFindTextR.selectAll();
    }
  }

  /**
   * ��ȡ���ҵ��ַ���
   * 
   * @return ���ҵ��ַ���
   */
  public String getFindText() {
    return this.strFind;
  }

  /**
   * ���ò��ҵ��ַ���
   * 
   * @param strFind
   *          ���ҵ��ַ���
   * @param isUpdate
   *          �Ƿ�ͬ�������ı������ʾ������Ϊtrue��������Ϊfalse
   */
  public void setFindText(String strFind, boolean isUpdate) {
    this.strFind = strFind;
    if (isUpdate) {
      this.txtFindTextF.setText(strFind);
      this.txtFindTextR.setText(strFind);
      if (this.getTabbedIndex() == 0) {
        this.txtFindTextF.selectAll();
      } else if (this.getTabbedIndex() == 1) {
        this.txtFindTextR.selectAll();
      }
    }
  }

  /**
   * ��ȡ�����滻���ַ���
   * 
   * @return �����滻���ַ���
   */
  public String getReplaceText() {
    return this.txtReplaceTextR.getText();
  }

  /**
   * ���������滻���ַ���
   * 
   * @param strReplace
   *          �����滻���ַ���
   */
  public void setReplaceText(String strReplace) {
    this.txtReplaceTextR.setText(strReplace);
  }

  /**
   * Ϊ��������ÿ�ݼ�
   */
  private void setMnemonic() {
    // ����
    this.chkMatchCaseF.setMnemonic('C');
    this.chkIsWrapF.setMnemonic('W');
    this.radDefaultF.setMnemonic('E');
    this.radTransferF.setMnemonic('T');
    this.radPatternF.setMnemonic('P');
    this.btnFindF.setMnemonic('F');
    this.btnCountAllF.setMnemonic('N');
    this.btnCountSelF.setMnemonic('S');
    this.btnSearchInFileF.setMnemonic('I');
    this.radFindUpF.setMnemonic('U');
    this.radFindDownF.setMnemonic('D');
    // �滻
    this.chkMatchCaseR.setMnemonic('C');
    this.chkIsWrapR.setMnemonic('W');
    this.radDefaultR.setMnemonic('E');
    this.radTransferR.setMnemonic('T');
    this.radPatternR.setMnemonic('P');
    this.btnFindR.setMnemonic('F');
    this.btnReplaceR.setMnemonic('R');
    this.btnReplaceAllR.setMnemonic('A');
    this.btnReplaceSelR.setMnemonic('S');
    this.radFindUpR.setMnemonic('U');
    this.radFindDownR.setMnemonic('D');
  }

  /**
   * Ϊ�������Ӽ�����
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // ����
    this.txtFindTextF.addCaretListener(this);
    this.btnFindF.addActionListener(this);
    this.btnCountAllF.addActionListener(this);
    this.btnCountSelF.addActionListener(this);
    this.btnSearchInFileF.addActionListener(this);
    this.btnCancelF.addActionListener(this);
    this.radFindDownF.addActionListener(this);
    this.radFindUpF.addActionListener(this);
    this.chkMatchCaseF.addActionListener(this);
    this.chkIsWrapF.addActionListener(this);
    this.radDefaultF.addActionListener(this);
    this.radTransferF.addActionListener(this);
    this.radPatternF.addActionListener(this);
    this.txtFindTextF.addKeyListener(this.keyAdapter);
    this.chkMatchCaseF.addKeyListener(this.keyAdapter);
    this.chkIsWrapF.addKeyListener(this.keyAdapter);
    this.radDefaultF.addKeyListener(this.keyAdapter);
    this.radTransferF.addKeyListener(this.keyAdapter);
    this.radPatternF.addKeyListener(this.keyAdapter);
    this.radFindDownF.addKeyListener(this.keyAdapter);
    this.radFindUpF.addKeyListener(this.keyAdapter);
    this.btnCancelF.addKeyListener(this.buttonKeyAdapter);
    this.btnFindF.addKeyListener(this.buttonKeyAdapter);
    this.btnCountAllF.addKeyListener(this.buttonKeyAdapter);
    this.btnCountSelF.addKeyListener(this.buttonKeyAdapter);
    this.btnSearchInFileF.addKeyListener(this.buttonKeyAdapter);
    // �滻
    this.txtFindTextR.addCaretListener(this);
    this.btnFindR.addActionListener(this);
    this.btnReplaceR.addActionListener(this);
    this.btnReplaceAllR.addActionListener(this);
    this.btnReplaceSelR.addActionListener(this);
    this.btnCancelR.addActionListener(this);
    this.radFindDownR.addActionListener(this);
    this.radFindUpR.addActionListener(this);
    this.chkMatchCaseR.addActionListener(this);
    this.chkIsWrapR.addActionListener(this);
    this.radDefaultR.addActionListener(this);
    this.radTransferR.addActionListener(this);
    this.radPatternR.addActionListener(this);
    this.txtFindTextR.addKeyListener(this.keyAdapter);
    this.txtReplaceTextR.addKeyListener(this.keyAdapter);
    this.chkMatchCaseR.addKeyListener(this.keyAdapter);
    this.chkIsWrapR.addKeyListener(this.keyAdapter);
    this.radDefaultR.addKeyListener(this.keyAdapter);
    this.radTransferR.addKeyListener(this.keyAdapter);
    this.radPatternR.addKeyListener(this.keyAdapter);
    this.radFindDownR.addKeyListener(this.keyAdapter);
    this.radFindUpR.addKeyListener(this.keyAdapter);
    this.btnCancelR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceR.addKeyListener(this.buttonKeyAdapter);
    this.btnFindR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceAllR.addKeyListener(this.buttonKeyAdapter);
    this.btnReplaceSelR.addKeyListener(this.buttonKeyAdapter);
    // Ϊ������ӽ��������
    this.addWindowFocusListener(this);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    // ����
    if (this.btnCancelF.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnFindF.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCountAllF.equals(e.getSource())) {
      this.getTextCountAll();
    } else if (this.btnCountSelF.equals(e.getSource())) {
      this.getTextCountSel();
    } else if (this.btnSearchInFileF.equals(e.getSource())) {
      this.searchInFile();
    } else if (this.chkMatchCaseF.equals(e.getSource())) {
      this.setting.matchCase = this.isMatchCase = this.chkMatchCaseF
          .isSelected();
      this.chkMatchCaseR.setSelected(this.isMatchCase);
    } else if (this.chkIsWrapF.equals(e.getSource())) {
      boolean selected = this.chkIsWrapF.isSelected();
      this.setting.isWrap = this.isWrap = selected;
      this.chkIsWrapR.setSelected(selected);
    } else if (this.radDefaultF.equals(e.getSource())) {
      this.radDefaultR.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.DEFAULT;
    } else if (this.radTransferF.equals(e.getSource())) {
      this.radTransferR.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.TRANSFER;
    } else if (this.radPatternF.equals(e.getSource())) {
      this.radPatternR.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.PATTERN;
    } else if (this.radFindDownF.equals(e.getSource())) {
      this.setting.findDown = this.isFindDown = true;
      this.radFindDownR.setSelected(true);
    } else if (this.radFindUpF.equals(e.getSource())) {
      this.setting.findDown = this.isFindDown = false;
      this.radFindUpR.setSelected(true);
    }
    // �滻
    else if (this.btnCancelR.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnFindR.equals(e.getSource())) {
      this.findText(this.isFindDown);
    } else if (this.btnReplaceR.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnReplaceAllR.equals(e.getSource())) {
      this.replaceAllText();
    } else if (this.btnReplaceSelR.equals(e.getSource())) {
      this.replaceSelText();
    } else if (this.chkMatchCaseR.equals(e.getSource())) {
      this.setting.matchCase = this.isMatchCase = this.chkMatchCaseR
          .isSelected();
      this.chkMatchCaseF.setSelected(this.isMatchCase);
    } else if (this.chkIsWrapR.equals(e.getSource())) {
      boolean selected = this.chkIsWrapR.isSelected();
      this.setting.isWrap = this.isWrap = selected;
      this.chkIsWrapF.setSelected(selected);
    } else if (this.radDefaultR.equals(e.getSource())) {
      this.radDefaultF.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.DEFAULT;
    } else if (this.radTransferR.equals(e.getSource())) {
      this.radTransferF.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.TRANSFER;
    } else if (this.radPatternR.equals(e.getSource())) {
      this.radPatternF.setSelected(true);
      this.setting.searchStyle = this.searchStyle = SearchStyle.PATTERN;
    } else if (this.radFindDownR.equals(e.getSource())) {
      this.setting.findDown = this.isFindDown = true;
      this.radFindDownF.setSelected(true);
    } else if (this.radFindUpR.equals(e.getSource())) {
      this.setting.findDown = this.isFindDown = false;
      this.radFindUpF.setSelected(true);
    }
  }

  /**
   * Ĭ�ϵġ�ȡ������������
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * Ĭ�ϵġ�ȷ������������
   */
  public void onEnter() {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.findText(this.isFindDown);
    } else {
      this.replaceText();
    }
  }

  /**
   * �����ַ���
   * 
   * @param isFindDown
   *          ���ҵķ���������²�����Ϊtrue����֮��Ϊfalse
   * @return ���ҽ����������ҳɹ�����true����֮��Ϊfalse
   */
  public boolean findText(boolean isFindDown) {
    if (!Util.isTextEmpty(this.strFind)) {
      int index = Util.findText(this.strFind, this.txaSource, isFindDown,
          this.isMatchCase, this.isWrap, this.searchStyle);
      if (index >= 0) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          this.txaSource.select(index, index + Util.matcher_length);
        } else if (this.searchStyle == SearchStyle.TRANSFER) {
          this.txaSource.select(index, index + this.strFind.length() - Util.transfer_count);
        } else {
          this.txaSource.select(index, index + this.strFind.length());
        }
        return true;
      } else {
        JOptionPane.showMessageDialog(this, "�Ҳ���\"" + this.strFind + "\"",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    }
    return false;
  }

  /**
   * �����滻�ַ���
   */
  private void replaceText() {
    boolean isEquals = false;
    String strSel = this.txaSource.getSelectedText();
    String strFindTemp = this.txtFindTextR.getText();
    String strReplaceTemp = this.txtReplaceTextR.getText();
    if (strSel != null) {
      if (this.searchStyle == SearchStyle.PATTERN) {
        strReplaceTemp = Util.transfer(strReplaceTemp);
      } else if (this.searchStyle == SearchStyle.TRANSFER) {
        strFindTemp = Util.transfer(strFindTemp);
        strReplaceTemp = Util.transfer(strReplaceTemp);
      }
      if (!this.isMatchCase) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches("(?i)" + strFindTemp)) { // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
            isEquals = true;
          }
        } else if (strSel.equalsIgnoreCase(strFindTemp)) {
          isEquals = true;
        }
      } else {
        if (this.searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches(strFindTemp)) {
            isEquals = true;
          }
        } else if (strSel.equals(strFindTemp)) {
          isEquals = true;
        }
      }
    }
    if (isEquals) {
      this.txaSource.replaceSelection(strReplaceTemp);
    }
    this.findText(this.isFindDown);
  }

  /**
   * �滻�����ַ���
   */
  private void replaceAllText() {
    this.replaceText(false);
  }

  /**
   * �滻ѡ���ַ���
   */
  private void replaceSelText() {
    this.replaceText(true);
  }

  /**
   * ����滻�ַ���
   * 
   * @param isSel
   *          �Ƿ����ѡ��������true��ʾѡ��������false��ʾȫ�Ĳ���
   */
  private void replaceText(boolean isSel) {
    String str = this.txaSource.getText();
    int selStart = this.txaSource.getSelectionStart();
    if (isSel) {
      str = this.txaSource.getSelectedText();
    }
    String strFindText = this.txtFindTextR.getText();
    String strReplaceText = this.txtReplaceTextR.getText();
    StringBuilder stbTextAll = new StringBuilder(str);
    StringBuilder stbTextAllTemp = new StringBuilder(stbTextAll);
    if (this.searchStyle == SearchStyle.PATTERN) {
      strReplaceText = Util.transfer(strReplaceText);
    } else if (this.searchStyle == SearchStyle.TRANSFER) {
      strFindText = Util.transfer(strFindText);
      strReplaceText = Util.transfer(strReplaceText);
    }
    StringBuilder stbFindTextTemp = new StringBuilder(strFindText);
    if (!Util.isTextEmpty(strFindText)) {
      int caretPos = 0; // ��ǰ���ĸ�����ֵ��ʼ�����ַ���
      int times = 0; // ѭ������
      int oldPos = this.txaSource.getCaretPosition(); // �滻֮ǰ�ı���Ĳ����λ��
      int newPos = oldPos; // �滻֮��Ĳ����λ��
      int offset = strFindText.length() - strReplaceText.length(); // �������滻���ַ������ȵĲ�ֵ
      if (!this.isMatchCase) {
        if (this.searchStyle == SearchStyle.PATTERN) {
          stbFindTextTemp = new StringBuilder("(?i)" + stbFindTextTemp); // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
        } else {
          stbFindTextTemp = new StringBuilder(stbFindTextTemp.toString()
              .toLowerCase());
        }
        stbTextAllTemp = new StringBuilder(stbTextAllTemp.toString()
            .toLowerCase());
      }
      if (this.searchStyle == SearchStyle.PATTERN) {
        try {
          this.matcher = Pattern.compile(stbFindTextTemp.toString()).matcher(
              stbTextAll);
        } catch (PatternSyntaxException x) {
          JOptionPane.showMessageDialog(this, "������ʽ�﷨����\n" + x.getMessage(),
              Util.SOFTWARE, JOptionPane.NO_OPTION);
          return;
        }
        int length = 0;
        while (this.matcher.find()) {
          if (this.matcher.end() < oldPos) {
            length = this.matcher.end() - this.matcher.start();
            offset = length - strReplaceText.length();
            newPos -= offset;
          }
          times++;
        }
        if (times > 0) {
          stbTextAll = new StringBuilder(this.matcher
              .replaceAll(strReplaceText));
        }
      } else {
        int findLength = stbFindTextTemp.length();
        int replaceLength = strReplaceText.length();
        int indexTemp = 0;
        for (int index = 0; caretPos >= 0; times++) {
          index = stbTextAllTemp.indexOf(stbFindTextTemp.toString(), caretPos);
          if (index >= 0) {
            indexTemp = index - (findLength - replaceLength) * times;
            stbTextAll.replace(indexTemp, indexTemp + findLength,
                strReplaceText);
            caretPos = index + findLength;
            if (caretPos < oldPos) {
              newPos -= offset;
            }
          } else {
            break;
          }
        }
      }
      if (times > 0) {
        if (isSel) {
          this.txaSource.replaceSelection(stbTextAll.toString());
          this.txaSource.select(selStart, selStart + stbTextAll.length());
        } else {
          this.txaSource.setText(stbTextAll.toString());
          this.txaSource.setCaretPosition(Util.checkCaretPosition(this.txaSource,
              newPos));
        }
        JOptionPane.showMessageDialog(this, "���滻 " + times + " ����",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      } else {
        JOptionPane.showMessageDialog(this, "�Ҳ���\"" + this.strFind + "\"",
            Util.SOFTWARE, JOptionPane.NO_OPTION);
      }
    }
  }

  /**
   * "ȫ��ͳ��"�Ĵ�����
   */
  private void getTextCountAll() {
    int times = this.getTextCount(this.txaSource.getText());
    if (times >= 0) {
      JOptionPane.showMessageDialog(this, "���ҵ� " + times + " ����", Util.SOFTWARE, JOptionPane.NO_OPTION);
    }
  }

  /**
   * "ѡ����ͳ��"�Ĵ�����
   */
  private void getTextCountSel() {
    int times = this.getTextCount(this.txaSource.getSelectedText());
    if (times >= 0) {
      JOptionPane.showMessageDialog(this, "���ҵ� " + times + " ����", Util.SOFTWARE, JOptionPane.NO_OPTION);
    }
  }

  /**
   * �ڸ������ı���ͳ���ַ�������
   * 
   * @param strText
   *          �������ı�
   * @return �ַ������ִ���
   */
  private int getTextCount(String strText) {
    int times = 0; // �ַ������ִ���
    if (Util.isTextEmpty(this.strFind) || Util.isTextEmpty(strText)) {
      return times;
    }
    String strFindTemp = this.strFind;
    if (this.searchStyle == SearchStyle.TRANSFER) {
      strFindTemp = Util.transfer(strFindTemp);
    }
    if (!this.isMatchCase) {
      if (this.searchStyle == SearchStyle.PATTERN) {
        strFindTemp = "(?i)" + strFindTemp; // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
      } else {
        strFindTemp = strFindTemp.toLowerCase();
      }
      strText = strText.toLowerCase();
    }
    if (this.searchStyle == SearchStyle.PATTERN) {
      try {
        this.matcher = Pattern.compile(strFindTemp).matcher(strText);
      } catch (PatternSyntaxException x) {
        JOptionPane.showMessageDialog(this, "������ʽ�﷨����\n" + x.getMessage(),
            Util.SOFTWARE, JOptionPane.NO_OPTION);
        return -1;
      }
      while (this.matcher.find()) {
        times++;
      }
    } else {
      int index = strText.indexOf(strFindTemp);
      while (index >= 0) {
        index = strText.indexOf(strFindTemp, index + strFindTemp.length());
        times++;
      }
    }
    return times;
  }

  /**
   * ���ļ��в��ҡ��Ĵ�����
   */
  private void searchInFile() {
    LinkedList<SearchBean> listIndex = new LinkedList<SearchBean>();
    if (!Util.isTextEmpty(this.strFind)) {
      int index = 0;
      while (index >= 0) {
        index = Util.findText(this.strFind, this.txaSource, index,
            this.isMatchCase, this.searchStyle);
        if (index >= 0) {
          SearchBean searchBean = new SearchBean();
          searchBean.setStart(index);
          if (this.searchStyle == SearchStyle.PATTERN) {
            index = index + Util.matcher_length;
          } else if (this.searchStyle == SearchStyle.TRANSFER) {
            index = index + this.strFind.length() - Util.transfer_count;
          } else {
            index = index + this.strFind.length();
          }
          searchBean.setEnd(index);
          listIndex.add(searchBean);
        }
      }
    }
    if (listIndex.isEmpty()) {
      JOptionPane.showMessageDialog(this, "�Ҳ���\"" + this.strFind + "\"",
          Util.SOFTWARE, JOptionPane.NO_OPTION);
    } else {
      SearchResult searchResult = new SearchResult((BaseTextArea)this.txaSource, this.strFind, listIndex);
      this.searchResultPanel.setSearchResult(searchResult);
      this.searchResultPanel.refreshResult();
      ((SnowPadFrame) this.getOwner()).viewSearchResult(true);
      this.dispose();
    }
  }

  /**
   * ����ѡ��������ť��״̬
   */
  private void setBtnSelEnabled() {
    String strSel = this.txaSource.getSelectedText();
    boolean isSelEmpty = Util.isTextEmpty(strSel);
    if (this.tpnMain.getSelectedIndex() == 0) {
      if (isSelEmpty || Util.isTextEmpty(this.txtFindTextF.getText())){
        this.btnCountSelF.setEnabled(false);
      } else {
        this.btnCountSelF.setEnabled(true);
      }
    } else {
      if (isSelEmpty || Util.isTextEmpty(this.txtFindTextR.getText())){
        this.btnReplaceSelR.setEnabled(false);
      } else {
        this.btnReplaceSelR.setEnabled(true);
      }
    }
  }

  /**
   * ���ı���Ĺ�귢���仯ʱ���������¼�
   */
  public void caretUpdate(CaretEvent e) {
    // ����
    if (this.txtFindTextF.equals(e.getSource())) {
      this.strFind = this.txtFindTextF.getText();
      if (Util.isTextEmpty(this.strFind)) {
        this.btnFindF.setEnabled(false);
        this.btnCountAllF.setEnabled(false);
        this.btnSearchInFileF.setEnabled(false);
      } else {
        this.btnFindF.setEnabled(true);
        this.btnCountAllF.setEnabled(true);
        this.btnSearchInFileF.setEnabled(true);
      }
    }
    // �滻
    else if (this.txtFindTextR.equals(e.getSource())) {
      this.strFind = this.txtFindTextR.getText();
      if (Util.isTextEmpty(this.strFind)) {
        this.btnFindR.setEnabled(false);
        this.btnReplaceR.setEnabled(false);
        this.btnReplaceAllR.setEnabled(false);
      } else {
        this.btnFindR.setEnabled(true);
        this.btnReplaceR.setEnabled(true);
        this.btnReplaceAllR.setEnabled(true);
      }
    }
    this.setBtnSelEnabled();
  }

  /**
   * ��ѡ��ı䵱ǰ��ͼʱ����
   */
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.getSelectedIndex() == 0) {
      this.setTitle(this.tpnMain.getTitleAt(0));
      this.txtFindTextF.setText(this.strFind);
      this.txtFindTextF.selectAll();
    } else if (this.tpnMain.getSelectedIndex() == 1) {
      this.setTitle(this.tpnMain.getTitleAt(1));
      this.txtFindTextR.setText(this.strFind);
      this.txtFindTextR.selectAll();
    }
    this.setBtnSelEnabled();
  }

  /**
   * �����ڻ�ý���ʱ�����������¼�
   */
  public void windowGainedFocus(WindowEvent e) {
    this.setBtnSelEnabled();
  }

  /**
   * ������ʧȥ����ʱ�����������¼�
   */
  public void windowLostFocus(WindowEvent e) {
  }
}
