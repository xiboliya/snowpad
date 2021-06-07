/**
 * Copyright (C) 2014 ��ԭ
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * "ͳ����Ϣ"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class InformationDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlCenter = new JPanel();
  private JPanel pnlSouth = new JPanel();
  private GridBagLayout gblMain = new GridBagLayout(); // ���������
  private GridBagConstraints gbc = new GridBagConstraints(); // ����������಼�������Լ��
  private JTextArea txaFile = new JTextArea();
  private JTextArea txaDoc = new JTextArea();
  private JButton btnOk = new JButton(" ȷ�� ");
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

  public InformationDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("ͳ����Ϣ");
    this.init();
    this.addListeners();
    this.setSize(400, 300);
    this.setVisible(true);
  }

  /**
   * ��д����ķ��������ñ������Ƿ�ɼ�
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.initInfo();
    }
    super.setVisible(visible);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    // ���������ʾ������������������ʾ�����Сʱ����ε��������С��GridBagConstraints.BOTH��ʾ�����ȫ������ʾ����Ĭ��ֵΪGridBagConstraints.NONE��
    this.gbc.fill = GridBagConstraints.BOTH;
    // �������������ռ�ĵ�λ���ȣ�Ĭ��ֵΪ1��GridBagConstraints.REMAINDER��ʾ�����Ϊ���е����һ����������һ�ռ������ʣ��ռ䡣
    this.gbc.gridwidth = GridBagConstraints.REMAINDER;
    // �������������ռ�ĵ�λ�߶ȣ�Ĭ��ֵΪ1��
    this.gbc.gridheight = 2;
    // �������ô��ڱ��ʱ�������ˮƽ���ű���������Խ�󣬱�ʾ����ܵõ�����Ŀռ䡣Ĭ��ֵΪ0��
    this.gbc.weightx = 1.0;
    // �������ô��ڱ��ʱ������Ĵ�ֱ���ű���������Խ�󣬱�ʾ����ܵõ�����Ŀռ䡣Ĭ��ֵΪ0��
    this.gbc.weighty = 1.0;
    this.gblMain.setConstraints(this.txaFile, this.gbc);
    this.pnlCenter.add(this.txaFile);
    this.gbc.gridheight = 1;
    this.gblMain.setConstraints(this.txaDoc, this.gbc);
    this.pnlCenter.setLayout(this.gblMain);
    this.pnlCenter.add(this.txaDoc);
    this.pnlMain.add(this.pnlCenter, BorderLayout.CENTER);
    this.pnlSouth.add(this.btnOk);
    this.pnlMain.add(this.pnlSouth, BorderLayout.SOUTH);

    this.simpleDateFormat.applyPattern(Util.DATE_STYLES[1]);
    this.txaFile.setBorder(new TitledBorder("�ļ���Ϣ"));
    this.txaDoc.setBorder(new TitledBorder("�ı�����Ϣ"));
    this.txaFile.setEditable(false);
    this.txaDoc.setEditable(false);
    this.txaFile.setLineWrap(true);
    this.txaDoc.setLineWrap(true);
    this.txaFile.setFocusable(false);
    this.txaDoc.setFocusable(false);
  }

  /**
   * ��ʼ�������Ϣ
   */
  private void initInfo() {
    File file = ((BaseTextArea) this.txaSource).getFile();
    if (file != null) {
      this.txaFile.setVisible(true);
      this.txaFile.setText(Util.INFO_FILE_PATH + file.getAbsolutePath() + "\n" +
          Util.INFO_FILE_MODIFY_TIME + this.simpleDateFormat.format(file.lastModified()) + "\n" +
          Util.INFO_FILE_SIZE + file.length() + " �ֽ�");
    } else {
      this.txaFile.setVisible(false);
    }
    String strText = this.txaSource.getText();
    int blanks = 0; // �ո�
    int digits = 0; // ����
    int letters = 0; // ��ĸ
    for (char ch : strText.toCharArray()) {
      if (Character.isDigit(ch)) {
        digits++;
      } else if (Character.isLowerCase(ch) || Character.isUpperCase(ch)) { // �˴�������Character.isLetter(ch)����Ϊ�Ὣ����Ҳ��������
        letters++;
      } else if (ch == ' ') {
        blanks++;
      }
    }
    this.txaDoc.setText(Util.INFO_DOC_CHARS + strText.length() + "\n" +
        Util.INFO_DOC_LINES + this.txaSource.getLineCount() + "\n" +
        Util.INFO_DOC_DIGITS + digits + "\n" + Util.INFO_DOC_LETTERS +
        letters + "\n" + Util.INFO_DOC_BLANKS + blanks);
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.dispose();
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
