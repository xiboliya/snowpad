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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * "ƴ���ļ�"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class MergeFileDialog extends BaseDialog implements ActionListener, ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private OpenFileChooser openFileChooser = null; // "��"�ļ�ѡ����
  private SaveFileChooser saveFileChooser = null; // "����"�ļ�ѡ����
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblSourcePath = new JLabel("Դ�ļ�·����");
  private JButton btnAddSource = new JButton("+");
  private JButton btnReduceSource = new JButton("-");
  private JButton btnUpSource = new JButton("��");
  private JButton btnDownSource = new JButton("��");
  private JList<String> listPath = new JList<String>();
  private JScrollPane srpPath = new JScrollPane(this.listPath);
  private JLabel lblTargetPath = new JLabel("�����ļ�·����");
  private BaseTextField txtTargetPath = new BaseTextField();
  private JButton btnTargetPath = new JButton("...");
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Insets insets = new Insets(0, 0, 0, 0);
  private DefaultListModel<String> defaultListModel = new DefaultListModel<String>();

  public MergeFileDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(340, 310);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("ƴ���ļ�");
    this.pnlMain.setLayout(null);
    this.lblSourcePath.setBounds(10, 10, 110, Util.VIEW_HEIGHT);
    this.srpPath.setBounds(10, 35, 270, 135);
    this.btnAddSource.setMargin(this.insets);
    this.btnAddSource.setBounds(290, 35, 30, 30);
    this.btnAddSource.setToolTipText("����ļ�");
    this.btnReduceSource.setMargin(this.insets);
    this.btnReduceSource.setBounds(290, 70, 30, 30);
    this.btnReduceSource.setToolTipText("�Ƴ��ļ�");
    this.btnUpSource.setMargin(this.insets);
    this.btnUpSource.setBounds(290, 105, 30, 30);
    this.btnUpSource.setToolTipText("�����ļ�");
    this.btnDownSource.setMargin(this.insets);
    this.btnDownSource.setBounds(290, 140, 30, 30);
    this.btnDownSource.setToolTipText("�����ļ�");
    this.pnlMain.add(this.lblSourcePath);
    this.pnlMain.add(this.btnAddSource);
    this.pnlMain.add(this.btnReduceSource);
    this.pnlMain.add(this.btnUpSource);
    this.pnlMain.add(this.btnDownSource);
    this.pnlMain.add(this.srpPath);
    this.lblTargetPath.setBounds(10, 180, 110, Util.VIEW_HEIGHT);
    this.txtTargetPath.setBounds(10, 205, 270, 30);
    this.btnTargetPath.setMargin(this.insets);
    this.btnTargetPath.setBounds(290, 205, 30, 30);
    this.btnTargetPath.setToolTipText("�����ļ�");
    this.pnlMain.add(this.lblTargetPath);
    this.pnlMain.add(this.txtTargetPath);
    this.pnlMain.add(this.btnTargetPath);
    this.btnOk.setBounds(50, 245, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(200, 245, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.listPath.setModel(this.defaultListModel);
    this.listPath.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
    this.setSourceButtonEnabled();
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.btnAddSource.addActionListener(this);
    this.btnAddSource.addKeyListener(this.buttonKeyAdapter);
    this.btnReduceSource.addActionListener(this);
    this.btnReduceSource.addKeyListener(this.buttonKeyAdapter);
    this.btnUpSource.addActionListener(this);
    this.btnUpSource.addKeyListener(this.buttonKeyAdapter);
    this.btnDownSource.addActionListener(this);
    this.btnDownSource.addKeyListener(this.buttonKeyAdapter);
    this.listPath.addKeyListener(this.keyAdapter);
    this.listPath.addListSelectionListener(this);
    this.btnTargetPath.addActionListener(this);
    this.btnTargetPath.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "����ļ�"�Ĵ�����
   */
  private void addSourceFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
      this.openFileChooser.setFileFilter(this.openFileChooser.getAcceptAllFileFilter()); // ����ΪĬ�Ϲ�����
    }
    this.openFileChooser.setSelectedFile(null);
    this.openFileChooser.setMultiSelectionEnabled(true);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File[] files = this.openFileChooser.getSelectedFiles();
    if (files == null || files.length <= 0) {
      return;
    }
    for (File file : files) {
      if (file != null && file.exists() && !this.defaultListModel.contains(file.getAbsolutePath())) {
        this.defaultListModel.addElement(file.getAbsolutePath());
      }
    }
  }

  /**
   * "�Ƴ��ļ�"�Ĵ�����
   */
  private void reduceSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int minIndex = arrIndex[0];
    this.defaultListModel.removeRange(minIndex, arrIndex[arrIndex.length - 1]);
    int size = this.defaultListModel.getSize();
    if (minIndex > size - 1) {
      minIndex = size - 1;
    }
    this.listPath.setSelectedIndex(minIndex);
  }

  /**
   * "�����ļ�"�Ĵ�����
   */
  private void upSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int minIndex = arrIndex[0] - 1;
    int maxIndex = arrIndex[arrIndex.length - 1];
    if (minIndex < 0) {
      return;
    }
    String element = this.defaultListModel.remove(minIndex);
    this.defaultListModel.insertElementAt(element, maxIndex);
    this.listPath.setSelectionInterval(minIndex, maxIndex - 1);
  }

  /**
   * "�����ļ�"�Ĵ�����
   */
  private void downSourceFile() {
    int[] arrIndex = this.listPath.getSelectedIndices();
    if (arrIndex == null || arrIndex.length <= 0) {
      return;
    }
    int maxIndex = arrIndex[arrIndex.length - 1] + 1;
    if (maxIndex >= this.defaultListModel.getSize()) {
      return;
    }
    String element = this.defaultListModel.remove(maxIndex);
    this.defaultListModel.insertElementAt(element, arrIndex[0]);
    this.listPath.setSelectionInterval(arrIndex[0] + 1, maxIndex);
  }

  /**
   * "�����ļ�"�Ĵ�����
   */
  private void selectFile() {
    if (this.saveFileChooser == null) {
      this.saveFileChooser = new SaveFileChooser();
    }
    this.saveFileChooser.setSelectedFile(null);
    this.saveFileChooser.setMultiSelectionEnabled(false);
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser.showSaveDialog(this)) {
      return;
    }
    File file = this.saveFileChooser.getSelectedFile();
    if (file != null) {
      this.txtTargetPath.setText(file.getAbsolutePath());
    }
  }

  /**
   * ���ø���ť�Ƿ����
   */
  private void setSourceButtonEnabled() {
    if (this.listPath.isSelectionEmpty()) {
      this.btnReduceSource.setEnabled(false);
      this.btnUpSource.setEnabled(false);
      this.btnDownSource.setEnabled(false);
    } else {
      this.btnReduceSource.setEnabled(true);
      int size = this.defaultListModel.getSize();
      int minSelectionIndex = this.listPath.getMinSelectionIndex();
      int maxSelectionIndex = this.listPath.getMaxSelectionIndex();
      if (size == 1 || (minSelectionIndex == 0 && maxSelectionIndex == size - 1)) { // �б���ֻ��һ�����ݻ�ѡ����������
        this.btnUpSource.setEnabled(false);
        this.btnDownSource.setEnabled(false);
      } else {
        if (minSelectionIndex == 0) { // ѡ���˵�һ��
          this.btnUpSource.setEnabled(false);
        } else {
          this.btnUpSource.setEnabled(true);
        }
        if (maxSelectionIndex == size - 1) { // ѡ�������һ��
          this.btnDownSource.setEnabled(false);
        } else {
          this.btnDownSource.setEnabled(true);
        }
      }      
    }
  }

  /**
   * ƴ���ļ��Ĵ�����
   */
  private void mergeFile() {
    String strTargetPath = this.txtTargetPath.getText();
    if (Util.isTextEmpty(strTargetPath)) {
      JOptionPane.showMessageDialog(this, "�����ļ�·������Ϊ�գ������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = new File(strTargetPath);
    if (file.isDirectory()) {
      JOptionPane.showMessageDialog(this, "�����ļ�·����֧��Ŀ¼���������������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    int size = this.defaultListModel.getSize();
    if (size <= 0) {
      JOptionPane.showMessageDialog(this, "Դ�ļ�·������Ϊ�գ�����ӣ�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    } else if (size == 1) {
      JOptionPane.showMessageDialog(this, "Դ�ļ�·������Ϊ2��������ӣ�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    Util.checkFile(file);
    if (file.exists()) {
      file.delete();
    }
    boolean result = true;
    for (int i = 0; i < size; i++) {
      String strFile = this.defaultListModel.get(i);
      if (!Util.isTextEmpty(strFile)) {
        File fileSource = new File(strFile);
        if (fileSource.isFile()) {
          RandomAccessFile randomAccessFile = null;
          try {
            randomAccessFile = new RandomAccessFile(fileSource, "r");
            randomAccessFile.seek(0);
            byte[] buffer = new byte[Util.BIG_BUFFER_LENGTH];
            int len = 0;
            while ((len = randomAccessFile.read(buffer)) != -1) {
              toSaveFile(file, buffer, len);
              buffer = new byte[Util.BIG_BUFFER_LENGTH];
            }
          } catch (Exception x) {
            // x.printStackTrace();
            result = false;
            break;
          } finally {
            try {
              randomAccessFile.close();
            } catch (IOException x) {
              // x.printStackTrace();
            }
          }
        }
      }
    }
    if (result) {
      JOptionPane.showMessageDialog(this, "ƴ���ļ���ɣ�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      onCancel();
    } else {
      JOptionPane.showMessageDialog(this, "ƴ���ļ�ʧ�ܣ�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }

  private void toSaveFile(File file, byte[] buffer, int len) {
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      randomAccessFile.seek(file.length());
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
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnAddSource.equals(e.getSource())) {
      this.addSourceFile();
    } else if (this.btnReduceSource.equals(e.getSource())) {
      this.reduceSourceFile();
    } else if (this.btnUpSource.equals(e.getSource())) {
      this.upSourceFile();
    } else if (this.btnDownSource.equals(e.getSource())) {
      this.downSourceFile();
    } else if (this.btnTargetPath.equals(e.getSource())) {
      this.selectFile();
    }
  }

  /**
   * ���б��ı�ѡ��ʱ���������¼�
   */
  public void valueChanged(ListSelectionEvent e) {
    if (this.listPath.equals(e.getSource())) {
      this.setSourceButtonEnabled();
    }
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.mergeFile();
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
