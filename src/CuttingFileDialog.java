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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
  private JRadioButton radCutSize = new JRadioButton("���ļ���С�и", true);
  private BaseTextField txtCutSize = new BaseTextField(true, "\\d{0,4}"); // �����û�ֻ���������֣����Ҳ��ܳ���4λ
  private JComboBox<String> cmbCutUnit = new JComboBox<String>(Util.STORAGE_UNIT);
  private JRadioButton radCutCount = new JRadioButton("���ļ������и", false);
  private BaseTextField txtCutCount = new BaseTextField(true, "\\d{0,4}"); // �����û�ֻ���������֣����Ҳ��ܳ���4λ
  private JLabel lblCutCount = new JLabel("��");
  private ButtonGroup bgpCut = new ButtonGroup();
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public CuttingFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(420, 260);
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
    this.radCutSize.setBounds(10, 87, 130, Util.VIEW_HEIGHT);
    this.txtCutSize.setBounds(140, 85, 50, 30);
    this.cmbCutUnit.setBounds(200, 87, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.radCutSize);
    this.pnlMain.add(this.txtCutSize);
    this.pnlMain.add(this.cmbCutUnit);
    this.radCutCount.setBounds(10, 130, 130, Util.VIEW_HEIGHT);
    this.txtCutCount.setBounds(140, 128, 50, 30);
    this.lblCutCount.setBounds(200, 130, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.radCutCount);
    this.pnlMain.add(this.txtCutCount);
    this.pnlMain.add(this.lblCutCount);
    this.btnOk.setBounds(90, 180, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 180, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpCut.add(this.radCutSize);
    this.bgpCut.add(this.radCutCount);
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
   * ���ݵ�ѡ��ť��ѡ����������Ƿ����
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radCutSize.isSelected();
    this.txtCutSize.setEnabled(selected);
    this.cmbCutUnit.setEnabled(selected);
    this.txtCutCount.setEnabled(!selected);
    this.lblCutCount.setEnabled(!selected);
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.txtPath.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
    this.radCutSize.addActionListener(this);
    this.radCutSize.addKeyListener(this.keyAdapter);
    this.radCutCount.addActionListener(this);
    this.radCutCount.addKeyListener(this.keyAdapter);
    this.txtCutSize.addKeyListener(this.keyAdapter);
    this.cmbCutUnit.addKeyListener(this.keyAdapter);
    this.txtCutCount.addKeyListener(this.keyAdapter);
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
    } else if (this.radCutSize.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radCutCount.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
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
    if (this.radCutSize.isSelected()) {
      this.cuttingFileBySize(file, length);
    } else {
      this.cuttingFileByCount(file, length);
    }
  }

  private void cuttingFileBySize(File file, long length) {
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
      Util.deleteAllFiles(fileParent);
    } else {
      fileParent.mkdirs(); // ���Ŀ¼�����ڣ��򴴽�֮
    }
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte[] buffer = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        count++;
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "�и��ļ���ɣ�\n�ɹ������ļ���" + count + "����", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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

  private void cuttingFileByCount(File file, long length) {
    String strCutCount = this.txtCutCount.getText();
    if (Util.isTextEmpty(strCutCount)) {
      JOptionPane.showMessageDialog(this, "�и��ļ���������Ϊ�գ������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int cutCount = 0;
    try {
      cutCount = Integer.parseInt(strCutCount);
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "�и��ļ�������ʽ�������������֣�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    if (cutCount <= 1) {
      JOptionPane.showMessageDialog(this, "�и��ļ������������1�����������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    } else if (cutCount > length) {
      JOptionPane.showMessageDialog(this, "Ҫ�иǰ�ļ����и��ļ��������ܴ���" + length + "�������������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int cutSize = 0;
    cutSize = (int)Math.ceil(length / (double)cutCount); // ����С������ȫ����λ�ķ�ʽ����������������������룬С������1~4Ҳ��λ
    File fileParent = new File(file.getParent() + "/" + file.getName() + "_cutting");
    if (fileParent.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("�˲����������Ѵ��ڵ�" + fileParent + "Ŀ¼��\n�Ƿ������"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
      Util.deleteAllFiles(fileParent);
    } else {
      fileParent.mkdirs(); // ���Ŀ¼�����ڣ��򴴽�֮
    }
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      randomAccessFile.seek(0);
      byte[] buffer = new byte[cutSize];
      int len = 0;
      int count = 0;
      while ((len = randomAccessFile.read(buffer)) != -1) {
        count++;
        File fileCutting = new File(fileParent + "/" + count);
        toCuttingFile(fileCutting, buffer, len);
        buffer = new byte[cutSize];
      }
      JOptionPane.showMessageDialog(this, "�и��ļ���ɣ�\n�ɹ������ļ���" + count + "����", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
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

  private void toCuttingFile(File file, byte[] buffer, int len) {
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
