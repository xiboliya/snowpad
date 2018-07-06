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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * "����"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class EncryptDialog extends BaseDialog implements ActionListener, CaretListener, ChangeListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTabbedPane tpnMain = new JTabbedPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // ������
  private OpenFileChooser openFileChooser = null; // "��"�ļ�ѡ����
  // �ı�
  private JPanel pnlText = new JPanel();
  private JLabel lblTextT = new JLabel("�����ı���");
  private BaseTextAreaSpecial txaTextT = new BaseTextAreaSpecial();
  private JScrollPane srpTextT = new JScrollPane(this.txaTextT);
  private JCheckBox chkEveryLinesT = new JCheckBox("��������(E)");
  // �ļ�
  private JPanel pnlFile = new JPanel();
  private JLabel lblPathF = new JLabel("�����ļ�·����");
  private BaseTextField txtPathF = new BaseTextField();
  private JButton btnSelectFileF = new JButton("ѡ���ļ�(S)");

  private JPanel pnlBottom = new JPanel();
  private JLabel lblDigestType = new JLabel("�������ͣ�");
  private JComboBox<String> cmbDigestType = new JComboBox<String>(Util.DIGEST_TYPES);
  private JLabel lblEncrypt = new JLabel("����ֵ��");
  private BaseTextAreaSpecial txaEncrypt = new BaseTextAreaSpecial();
  private JScrollPane srpEncrypt = new JScrollPane(this.txaEncrypt);
  private JButton btnCopy = new JButton("���ƽ��(C)");
  private JButton btnCancel = new JButton("ȡ��");

  public EncryptDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("����ֵ����");
    this.init();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(420, 350);
    this.setVisible(true);
  }

  /**
   * �����ʼ��
   */
  private void init() {
    this.pnlMain.setLayout(null);
    // �ı�
    this.pnlText.setLayout(null);
    this.lblTextT.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpTextT.setBounds(10, 35, 270, 80);
    this.pnlText.add(this.lblTextT);
    this.pnlText.add(this.srpTextT);
    this.chkEveryLinesT.setBounds(290, 65, 110, Util.VIEW_HEIGHT);
    this.pnlText.add(this.chkEveryLinesT);
    // �ļ�
    this.pnlFile.setLayout(null);
    this.lblPathF.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.txtPathF.setBounds(10, 50, 270, 30);
    this.pnlFile.add(this.lblPathF);
    this.pnlFile.add(this.txtPathF);
    this.btnSelectFileF.setBounds(290, 52, 110, Util.BUTTON_HEIGHT);
    this.pnlFile.add(this.btnSelectFileF);

    this.pnlBottom.setLayout(null);
    this.pnlBottom.setBounds(0, 160, 420, 190);
    this.lblDigestType.setBounds(10, 10, 80, Util.VIEW_HEIGHT);
    this.cmbDigestType.setBounds(90, 10, 80, Util.INPUT_HEIGHT);
    this.lblEncrypt.setBounds(10, 35, 70, Util.VIEW_HEIGHT);
    this.srpEncrypt.setBounds(10, 60, 270, 80);
    this.pnlBottom.add(this.lblDigestType);
    this.pnlBottom.add(this.cmbDigestType);
    this.pnlBottom.add(this.lblEncrypt);
    this.pnlBottom.add(this.srpEncrypt);
    this.btnCopy.setBounds(290, 75, 110, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCopy);
    this.btnCancel.setBounds(290, 115, 110, Util.BUTTON_HEIGHT);
    this.pnlBottom.add(this.btnCancel);
    // ������
    this.tpnMain.setBounds(0, 0, 420, 160);
    this.tpnMain.add(this.pnlText, "�ı�");
    this.tpnMain.add(this.pnlFile, "�ļ�");
    this.pnlMain.add(this.tpnMain);
    this.setTabbedIndex(0);
    this.tpnMain.setFocusable(false);
    this.pnlMain.add(this.pnlBottom);
    this.txaEncrypt.setEditable(false);
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
   * Ϊ��������ÿ�ݼ�
   */
  private void setMnemonic() {
    this.chkEveryLinesT.setMnemonic('E');
    this.btnSelectFileF.setMnemonic('S');
    this.btnCopy.setMnemonic('C');
  }

  /**
   * Ϊ�������Ӽ�����
   */
  private void addListeners() {
    this.tpnMain.addChangeListener(this);
    // �ı�
    this.txaTextT.addCaretListener(this);
    this.chkEveryLinesT.addActionListener(this);
    this.txaTextT.addKeyListener(this.keyAdapter);
    this.chkEveryLinesT.addKeyListener(this.keyAdapter);
    // �ļ�
    this.txtPathF.addCaretListener(this);
    this.btnSelectFileF.addActionListener(this);
    this.txtPathF.addKeyListener(this.keyAdapter);
    this.btnSelectFileF.addKeyListener(this.buttonKeyAdapter);

    this.cmbDigestType.addItemListener(this);
    this.cmbDigestType.addKeyListener(this.keyAdapter);
    this.txaEncrypt.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.chkEveryLinesT.equals(e.getSource())) {
      this.showStringEncrypt();
    } else if (this.btnSelectFileF.equals(e.getSource())) {
      this.selectFile();
    } else if (this.btnCopy.equals(e.getSource())) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * ˢ�¸��ؼ�����ʾ
   */
  public void refreshView() {
    String str = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(str) && this.getTabbedIndex() == 0) {
      this.txaTextT.setText(str);
    }
    this.showEncrypt();
  }

  /**
   * "ѡ���ļ�"�Ĵ�����
   */
  private void selectFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
      this.openFileChooser.setFileFilter(this.openFileChooser.getAcceptAllFileFilter()); // ����ΪĬ�Ϲ�����
    }
    this.openFileChooser.setSelectedFile(null);
    this.openFileChooser.setMultiSelectionEnabled(false);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File file = this.openFileChooser.getSelectedFile();
    if (file != null && file.exists()) {
      this.txtPathF.setText(file.getAbsolutePath());
    }
  }

  /**
   * ��ʾ����ֵ
   */
  private void showEncrypt() {
    if (this.getTabbedIndex() == 0) {
      this.showStringEncrypt();
    } else {
      this.showFileEncrypt();
    }
  }

  /**
   * ��ʾ�ַ����ļ���ֵ
   */
  private void showStringEncrypt() {
    String text = this.txaTextT.getText();
    if (Util.isTextEmpty(text)) {
      this.txaEncrypt.setText("");
      return;
    }
    String digestType = this.cmbDigestType.getSelectedItem().toString();
    String encrypt = "";
    if (this.chkEveryLinesT.isSelected()) {
      String[] arrLines = text.split("\n", -1);
      for (String str : arrLines) {
        encrypt += (Util.getStringDigest(str, digestType) + "\n");
      }
    } else {
      // ���ݲ�ͬ�Ĳ���ϵͳʹ�ò�ͬ�Ļ��з�
      text = text.replaceAll(LineSeparator.UNIX.toString(), Util.LINE_SEPARATOR);
      encrypt = Util.getStringDigest(text, digestType);
    }
    if (Util.isTextEmpty(encrypt)) {
      this.txaEncrypt.setText("");
    } else {
      this.txaEncrypt.setText(encrypt);
    }
  }

  /**
   * ��ʾ�ļ��ļ���ֵ
   */
  private void showFileEncrypt() {
    String path = this.txtPathF.getText();
    if (Util.isTextEmpty(path)) {
      this.txaEncrypt.setText("");
      return;
    }
    String digestType = this.cmbDigestType.getSelectedItem().toString();
    String encrypt = Util.getFileDigest(new File(path), digestType);
    if (Util.isTextEmpty(encrypt)) {
      this.txaEncrypt.setText("");
    } else {
      this.txaEncrypt.setText(encrypt);
    }
  }

  /**
   * "���ƽ��"�Ĵ�����
   */
  private void toCopyResult() {
    this.setClipboardContents(this.txaEncrypt.getText());
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
    if (this.txaTextT.equals(e.getSource())) {
      this.showStringEncrypt();
    } else if (this.txtPathF.equals(e.getSource())) {
      this.showFileEncrypt();
    }
  }

  /**
   * ��ѡ��ı䵱ǰ��ͼʱ����
   */
  public void stateChanged(ChangeEvent e) {
    this.showEncrypt();
  }

  /**
   * ����ѡ�����ʱ����
   */
  public void itemStateChanged(ItemEvent e) {
    this.showEncrypt();
  }
}
