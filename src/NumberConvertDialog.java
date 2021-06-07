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

import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * "����ת��"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class NumberConvertDialog extends BaseDialog implements ActionListener, CaretListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // ������
  private JLabel lblNumber = new JLabel("ת�����֣�");
  private JComboBox<String> cmbNumber = new JComboBox<String>();
  private BaseTextField txtNumber = new BaseTextField();
  private JLabel lblWarning = new JLabel("���棺���зǷ��ַ��򳬳���Χ��");
  private JButton btnExchange = new JButton("��������");
  private JLabel lblResult = new JLabel("ת�������");
  private JComboBox<String> cmbResult = new JComboBox<String>();
  private BaseTextField txtResult = new BaseTextField();
  private JButton btnCopy = new JButton("���ƽ��(C)");
  private JButton btnCancel = new JButton("ȡ��");

  public NumberConvertDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("���ֽ���ת��");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 240);
    this.setVisible(true);
  }

  /**
   * �����ʼ��
   */
  private void init() {
    this.lblWarning.setForeground(Color.RED);
    this.pnlMain.setLayout(null);
    this.lblNumber.setBounds(10, 10, 70, Util.VIEW_HEIGHT);
    this.lblWarning.setBounds(90, 10, 190, Util.VIEW_HEIGHT);
    this.cmbNumber.setBounds(10, 35, 70, Util.INPUT_HEIGHT);
    this.txtNumber.setBounds(90, 35, 190, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblNumber);
    this.pnlMain.add(this.lblWarning);
    this.pnlMain.add(this.cmbNumber);
    this.pnlMain.add(this.txtNumber);

    this.btnExchange.setBounds(10, 70, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnExchange);

    this.lblResult.setBounds(10, 100, 70, Util.VIEW_HEIGHT);
    this.cmbResult.setBounds(10, 125, 70, Util.INPUT_HEIGHT);
    this.txtResult.setBounds(90, 125, 190, Util.INPUT_HEIGHT);
    this.btnCopy.setBounds(290, 125, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblResult);
    this.pnlMain.add(this.cmbResult);
    this.pnlMain.add(this.txtResult);
    this.pnlMain.add(this.btnCopy);

    this.btnCancel.setBounds(160, 165, 80, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCancel);
    this.txtResult.setEditable(false);
  }

  /**
   * ��ʼ���ؼ���ʾ
   */
  private void initView() {
    String[] array = new String[35];
    for (int i = 2; i <= 36; i++) {
      array[i - 2] = i + "����";
    }
    this.cmbNumber.setModel(new DefaultComboBoxModel<String>(array));
    this.cmbNumber.setSelectedIndex(8);
    this.cmbResult.setModel(new DefaultComboBoxModel<String>(array));
    this.cmbResult.setSelectedIndex(14);
  }

  /**
   * Ϊ��������ÿ�ݼ�
   */
  private void setMnemonic() {
    this.btnCopy.setMnemonic('C');
  }

  /**
   * Ϊ�������Ӽ�����
   */
  private void addListeners() {
    this.cmbNumber.addKeyListener(this.keyAdapter);
    this.cmbNumber.addItemListener(this);
    this.txtNumber.addCaretListener(this);
    this.txtNumber.addKeyListener(this.keyAdapter);
    this.cmbResult.addKeyListener(this.keyAdapter);
    this.cmbResult.addItemListener(this);
    this.txtResult.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnExchange.addActionListener(this);
    this.btnExchange.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnExchange.equals(e.getSource())) {
      this.exchange();
    }
  }

  /**
   * ˢ�¸��ؼ�����ʾ
   */
  public void refreshView() {
    this.showResult();
  }

  /**
   * ��ʾ���
   */
  private void showResult() {
    String strNumber = this.txtNumber.getText();
    if (Util.isTextEmpty(strNumber)) {
      this.lblWarning.setVisible(false);
      this.txtResult.setText("");
    } else {
      int numberBase = this.cmbNumber.getSelectedIndex() + 2;
      int resultBase = this.cmbResult.getSelectedIndex() + 2;
      try {
        long number = Long.parseLong(strNumber, numberBase);
        String result = Long.toString(number, resultBase);
        this.txtResult.setText(result);
        this.lblWarning.setVisible(false);
      } catch (Exception x) {
        // x.printStackTrace();
        this.lblWarning.setVisible(true);
        this.txtResult.setText("");
      }
    }
  }

  /**
   * "���ƽ��"�Ĵ�����
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txtResult.getText());
  }

  /**
   * "��������"�Ĵ�����
   */
  private void exchange() {
    int indexNumber = this.cmbNumber.getSelectedIndex();
    int indexResult = this.cmbResult.getSelectedIndex();
    if (indexNumber != indexResult) {
      this.cmbNumber.setSelectedIndex(indexResult);
      this.cmbResult.setSelectedIndex(indexNumber);
    }
  }

  /**
   * ����ϵͳ�����������
   * 
   * @param strText
   *          Ҫ�����������ı�
   */
  private void setClipboardContents(String strText) {
    if (Util.isTextEmpty(strText)) {
      return;
    }
    StringSelection ss = new StringSelection(strText);
    this.clip.setContents(ss, ss);
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
  }

  /**
   * ���ı���Ĺ�귢���仯ʱ���������¼�
   */
  public void caretUpdate(CaretEvent e) {
    if (this.txtNumber.equals(e.getSource())) {
      this.showResult();
    }
  }

  /**
   * ���û���ѡ����ȡ��ѡ��ĳ��ʱ���������¼�
   */
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
  }
}
