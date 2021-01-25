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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "�и��ļ�"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class CuttingFileDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private OpenFileChooser openFileChooser = null; // "��"�ļ�ѡ����
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblPath = new JLabel("�����ļ�·����");
  private BaseTextField txtPath = new BaseTextField();
  private JButton btnSelectFile = new JButton("ѡ���ļ�(S)");
  private JLabel lblCutSize = new JLabel("�и��ļ���С��");
  private BaseTextField txtCutSize = new BaseTextField(true, "\\d{0,4}"); // �����û�ֻ���������֣����Ҳ��ܳ���4λ
  private JComboBox<String> cmbCutUnit = new JComboBox<String>(Util.STORAGE_UNIT);
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public CuttingFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(420, 200);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("�и��ļ�");
    this.pnlMain.setLayout(null);
    this.lblPath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.txtPath.setBounds(10, 35, 270, 30);
    this.pnlMain.add(this.lblPath);
    this.pnlMain.add(this.txtPath);
    this.btnSelectFile.setBounds(290, 37, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnSelectFile);
    this.lblCutSize.setBounds(10, 87, 100, Util.VIEW_HEIGHT);
    this.txtCutSize.setBounds(110, 85, 50, 30);
    this.cmbCutUnit.setBounds(170, 87, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblCutSize);
    this.pnlMain.add(this.txtCutSize);
    this.pnlMain.add(this.cmbCutUnit);
    this.btnOk.setBounds(90, 130, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 130, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
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
   * ��ʼ�����ؼ���״̬
   */
  private void initView() {
    this.txtPath.selectAll();
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.txtPath.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
    this.txtCutSize.addKeyListener(this.keyAdapter);
    this.cmbCutUnit.addKeyListener(this.keyAdapter);
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
    } else if (this.btnSelectFile.equals(e.getSource())) {
      this.selectFile();
    }
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
      this.txtPath.setText(file.getAbsolutePath());
    }
  }

  private void cuttingFile() {
    String strPath = this.txtPath.getText();
    if (Util.isTextEmpty(strPath)) {
      JOptionPane.showMessageDialog(this, "�ļ�·������Ϊ�գ������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = new File(strPath);
    if (!file.exists()) {
      JOptionPane.showMessageDialog(this, "�ļ������ڣ����������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    } else if (file.isDirectory()) {
      JOptionPane.showMessageDialog(this, "��֧��Ŀ¼���������������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    long length = file.length();
    if (length == 0) {
      JOptionPane.showMessageDialog(this, "��ǰ�ļ�Ϊ���ļ����޷��и���������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    String strCutSize = this.txtCutSize.getText();
    if (Util.isTextEmpty(strCutSize)) {
      JOptionPane.showMessageDialog(this, "�и��ļ���С����Ϊ�գ������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int cutSize = 0;
    try {
      cutSize = Integer.parseInt(strCutSize);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "�и��ļ���С��ʽ�������������֣�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    if (cutSize <= 0) {
      JOptionPane.showMessageDialog(this, "�и��ļ���С�������0�����������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int unitIndex = this.cmbCutUnit.getSelectedIndex();
    switch (unitIndex) {
    case 1:
      cutSize = cutSize * 1024;
      break;
    case 2:
      cutSize = cutSize * 1024 * 1024;
      break;
    }
    if (cutSize >= length) {
      JOptionPane.showMessageDialog(this, "Ҫ�иǰ�ļ����и��ļ���С����С��" + length + "�ֽڣ����������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File fileParent = new File(file.getParent() + "/" + file.getName() + "_cutting");
    if (fileParent.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("�˲����������Ѵ��ڵ�" + fileParent + "Ŀ¼��\n�Ƿ������"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
    } else {
      fileParent.mkdirs(); // ���Ŀ¼�����ڣ��򴴽�֮
    }
    RandomAccessFile randomAccessFile = null;
    byte byteArr[] = new byte[cutSize];
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte buffer[] = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        count++;
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "�и��ļ���ɣ�\n�ɹ������ļ���" + count + "����", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      onCancel();
    } catch (Exception x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "�и��ļ�ʧ�ܣ�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } finally {
      try {
        randomAccessFile.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  private void toCuttingFile(File file, byte buffer[], int len) {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      randomAccessFile.seek(0);
      randomAccessFile.write(buffer, 0, len);
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        randomAccessFile.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.cuttingFile();
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
