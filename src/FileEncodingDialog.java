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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ѡ��"�ļ������ʽ"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class FileEncodingDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblEncoding = new JLabel("�ļ������ʽ��");
  private JComboBox<String> cmbEncoding = new JComboBox<String>(Util.FILE_ENCODINGS);
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private CharEncoding charEncoding = null;
  private boolean isOk = false;
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public FileEncodingDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
    this.setSize(290, 110);
    this.setVisible(true);
  }

  /**
   * ��д����ķ��������ñ������Ƿ�ɼ�
   * 
   * @param isVisible
   *          ���ñ������Ƿ�ɼ�������ɼ���Ϊtrue
   */
  public void setVisible(boolean isVisible) {
    if (isVisible) {
      this.isOk = false;
    }
    super.setVisible(isVisible);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("�ļ������ʽ");
    this.pnlMain.setLayout(null);
    this.lblEncoding.setBounds(20, 10, 110, Util.VIEW_HEIGHT);
    this.cmbEncoding.setBounds(115, 10, 150, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblEncoding);
    this.pnlMain.add(this.cmbEncoding);
    this.btnOk.setBounds(40, 50, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(160, 50, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.cmbEncoding.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
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
   * ��ȡ��ǰѡ��ı����ʽ
   * 
   * @return �����ʽ
   */
  public CharEncoding getCharEncoding() {
    return this.charEncoding;
  }

  /**
   * ���õ�ǰ�ı����ʽ
   */
  private void setCharEncoding() {
    int index = this.cmbEncoding.getSelectedIndex();
    switch (index) {
    case 0:
      this.charEncoding = null;
      break;
    case 1:
      this.charEncoding = CharEncoding.ANSI;
      break;
    case 2:
      this.charEncoding = CharEncoding.UBE;
      break;
    case 3:
      this.charEncoding = CharEncoding.ULE;
      break;
    case 4:
      this.charEncoding = CharEncoding.UTF8;
      break;
    case 5:
      this.charEncoding = CharEncoding.UTF8_NO_BOM;
      break;
    case 6:
      this.charEncoding = CharEncoding.BASE;
      break;
    }
  }

  /**
   * ��ȡ�Ƿ�ִ����ȷ��
   * 
   * @return �Ƿ�ִ����ȷ��
   */
  public boolean getOk() {
    return this.isOk;
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.setCharEncoding();
    this.dispose();
    this.isOk = true;
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.charEncoding = null;
    this.dispose();
    this.isOk = false;
  }
}
