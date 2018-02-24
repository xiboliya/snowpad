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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

/**
 * "�Զ����"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class AutoCompleteDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JCheckBox chkEnable = new JCheckBox("�����Զ����(E)", false);
  private JTextArea txaView = new JTextArea();
  private JButton btnClose = new JButton("�ر�");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private boolean isAutoComplete = false; // �Զ����

  /**
   * ���췽��
   * 
   * @param owner
   *          ������
   * @param modal
   *          �Ƿ�Ϊģʽ����
   * @param txaSource
   *          ��Բ������ı���
   */
  public AutoCompleteDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("�Զ����");
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(160, 190);
    this.setVisible(true);
  }

  /**
   * ��д����ķ��������ñ������Ƿ�ɼ�
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.initView();
    }
    super.setVisible(visible);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.chkEnable.setBounds(15, 10, 120, Util.VIEW_HEIGHT);
    this.chkEnable.setMnemonic('E');
    this.txaView.setBounds(20, 45, 110, 70);
    this.txaView.setBorder(new EtchedBorder());
    this.txaView.setOpaque(true);
    this.txaView.setEditable(false);
    this.txaView.setFocusable(false);
    this.btnClose.setBounds(30, 125, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.chkEnable);
    this.pnlMain.add(this.txaView);
    this.pnlMain.add(this.btnClose);
  }

  /**
   * ��ʼ���ؼ���ʾ
   */
  private void initView() {
    this.isAutoComplete = ((BaseTextArea) this.txaSource).getAutoComplete();
    this.chkEnable.setSelected(this.isAutoComplete);
    String strView = "";
    for (int i = 0; i < Util.AUTO_COMPLETE_BRACKETS_LEFT.length(); i++) {
      strView += " " + Util.AUTO_COMPLETE_BRACKETS_LEFT.charAt(i) + " " + Util.AUTO_COMPLETE_BRACKETS_RIGHT.charAt(i);
      if ((i % 2) == 0) {
        strView += "    ";
      } else {
        strView += "\n";
      }
    }
    this.txaView.setText(strView);
    this.setAutoComplete(this.isAutoComplete);
  }

  /**
   * ��Ӹ�������¼�������
   */
  private void addListeners() {
    this.btnClose.addActionListener(this);
    this.chkEnable.addActionListener(this);
    // ����Ϊ���ɻ�ý���������Ӽ����¼��������û�����Esc��ʱ�رնԻ���
    this.chkEnable.addKeyListener(this.keyAdapter);
    this.btnClose.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * ��ȡ�Ƿ��Զ����
   */
  public boolean getAutoComplete() {
    return this.isAutoComplete;
  }

  /**
   * �����Ƿ��Զ����
   */
  public void setAutoComplete(boolean isAutoComplete) {
    this.isAutoComplete = isAutoComplete;
    this.txaView.setEnabled(this.isAutoComplete);
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.onCancel();
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnClose.equals(e.getSource())) {
      this.onCancel();
    } else if (this.chkEnable.equals(e.getSource())) {
      this.setAutoComplete(this.chkEnable.isSelected());
    }
  }

}
