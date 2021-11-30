/**
 * Copyright (C) 2021 ��ԭ
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * "�������"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class KeyCheckDialog extends BaseDialog {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblInfo = new JLabel("������������Լ�⵽������Ϣ��");
  private JLabel lblName = new JLabel();
  private JLabel lblCode = new JLabel();
  private KeyAdapter keyAdapter = null;

  public KeyCheckDialog(JDialog owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
    this.setSize(280, 150);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("�������");
    this.pnlMain.setLayout(null);
    this.lblInfo.setBounds(20, 10, 200, Util.VIEW_HEIGHT);
    this.lblName.setBounds(20, 40, 115, 70);
    this.lblCode.setBounds(145, 40, 115, 70);
    this.lblName.setHorizontalAlignment(JLabel.CENTER);
    this.lblCode.setHorizontalAlignment(JLabel.CENTER);
    this.lblName.setBorder(new TitledBorder("��������"));
    this.lblCode.setBorder(new TitledBorder("��������"));
    this.pnlMain.add(this.lblInfo);
    this.pnlMain.add(this.lblName);
    this.pnlMain.add(this.lblCode);
  }

  /**
   * ��д����ķ��������ñ������Ƿ�ɼ�
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.refreshKeyView(null);
    }
    super.setVisible(visible);
  }

  /**
   * ˢ�°�����Ϣ
   */
  private void refreshKeyView(KeyEvent e) {
    if (e == null) {
      this.lblName.setText("");
      this.lblCode.setText("");
    } else {
      int code = e.getKeyCode();
      this.lblName.setText(KeyEvent.getKeyText(code));
      this.lblCode.setText(String.valueOf(code));
    }
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.keyAdapter = new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        refreshKeyView(e);
      }
    };
    this.pnlMain.setFocusable(true);
    this.pnlMain.addKeyListener(this.keyAdapter);
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
