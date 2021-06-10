/**
 * Copyright (C) 2020 ��ԭ
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

/**
 * "����ļ�"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class SlicingFileDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private OpenFileChooser openFileChooser = null; // "��"�ļ�ѡ����
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblKeyword = new JLabel("��ֹؼ��֣�");
  private BaseTextField txtKeyword = new BaseTextField();
  private JRadioButton radCurrentFile = new JRadioButton("��ֵ�ǰ�ļ�", true);
  private JRadioButton radTargetFile = new JRadioButton("���ָ���ļ���", false);
  private BaseTextField txtTargetFile = new BaseTextField();
  private JButton btnSelectFile = new JButton("...");
  private ButtonGroup bgpFile = new ButtonGroup();
  private JButton btnOk = new JButton("ȷ��");
  private JButton btnCancel = new JButton("ȡ��");
  private Insets insets = new Insets(0, 0, 0, 0);
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public SlicingFileDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.initView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(420, 220);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("����ļ�");
    this.pnlMain.setLayout(null);
    this.lblKeyword.setBounds(30, 12, 90, Util.VIEW_HEIGHT);
    this.txtKeyword.setBounds(130, 10, 230, 30);
    this.pnlMain.add(this.lblKeyword);
    this.pnlMain.add(this.txtKeyword);
    this.radCurrentFile.setBounds(10, 60, 120, Util.VIEW_HEIGHT);
    this.radTargetFile.setBounds(10, 107, 120, Util.VIEW_HEIGHT);
    this.txtTargetFile.setBounds(130, 105, 230, 30);
    this.btnSelectFile.setMargin(this.insets);
    this.btnSelectFile.setBounds(370, 105, 30, 30);
    this.pnlMain.add(this.radCurrentFile);
    this.pnlMain.add(this.radTargetFile);
    this.pnlMain.add(this.txtTargetFile);
    this.pnlMain.add(this.btnSelectFile);
    this.btnOk.setBounds(90, 145, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(240, 145, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpFile.add(this.radCurrentFile);
    this.bgpFile.add(this.radTargetFile);
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
    String strSel = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(strSel)) {
      this.txtKeyword.setText(strSel);
    }
    this.txtKeyword.selectAll();
  }

  /**
   * ���ݵ�ѡ��ť��ѡ����������Ƿ����
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radCurrentFile.isSelected();
    this.txtTargetFile.setEnabled(!selected);
    this.btnSelectFile.setEnabled(!selected);
  }

  /**
   * ����¼�������
   */
  private void addListeners() {
    this.txtKeyword.addKeyListener(this.keyAdapter);
    this.radCurrentFile.addActionListener(this);
    this.radCurrentFile.addKeyListener(this.keyAdapter);
    this.radTargetFile.addActionListener(this);
    this.radTargetFile.addKeyListener(this.keyAdapter);
    this.txtTargetFile.addKeyListener(this.keyAdapter);
    this.btnSelectFile.addActionListener(this);
    this.btnSelectFile.addKeyListener(this.buttonKeyAdapter);
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
    } else if (this.radCurrentFile.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radTargetFile.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
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
      this.txtTargetFile.setText(file.getAbsolutePath());
    }
  }

  /**
   * ���չؼ��ֶ��ļ����в��
   */
  private void slicingFile() {
    String keyword = this.txtKeyword.getText();
    if (Util.isTextEmpty(keyword)) {
      JOptionPane.showMessageDialog(this, "��ֹؼ��ֲ���Ϊ�գ������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    keyword = checkText(keyword);
    if (this.radCurrentFile.isSelected()) {
      this.slicingCurrentFile(keyword);
    } else {
      this.slicingTargetFile(keyword);
    }
  }

  /**
   * ��ֵ�ǰ�ļ�
   * 
   * @param keyword
   *          �ؼ���
   */
  private void slicingCurrentFile(String keyword) {
    BaseTextArea textArea = (BaseTextArea) this.txaSource;
    String strText = textArea.getText();
    if (Util.isTextEmpty(strText)) {
      JOptionPane.showMessageDialog(this, "�ļ�����Ϊ�գ��޷���֣�", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "����ļ�ʧ�ܣ�����ؼ����Ƿ���ȷ��", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = textArea.getFile();
    if (file != null && file.exists()) {
      File fileSplit = new File(file + "_split");
      if (fileSplit.exists()) {
        int result = JOptionPane.showConfirmDialog(this,
            Util.convertToMsg("�˲����������Ѵ��ڵ�" + fileSplit + "Ŀ¼��\n�Ƿ������"),
            Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
          return;
        }
        Util.deleteAllFiles(fileSplit);
      } else {
        fileSplit.mkdirs(); // ���Ŀ¼�����ڣ��򴴽�֮
      }
      int count = 0;
      for (String text : arrText) {
        if (Util.isTextEmpty(text.trim())) {
          continue;
        }
        try {
          count++;
          File fileText = new File(fileSplit + "/" + count + ".txt");
          toSaveFile(fileText, text, textArea.getCharEncoding(), textArea.getLineSeparator());
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
      JOptionPane.showMessageDialog(this, "����ļ���ɣ�\n�ɹ������ļ���" + count + "����", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    } else {
      JOptionPane.showMessageDialog(this, "����ļ�ʧ�ܣ����ȱ����ļ���", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }

  /**
   * ���ָ���ļ�
   * 
   * @param keyword
   *          �ؼ���
   */
  private void slicingTargetFile(String keyword) {
    String strPath = this.txtTargetFile.getText();
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
      JOptionPane.showMessageDialog(this, "��ǰ�ļ�Ϊ���ļ����޷���֣����������룡", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    CharEncoding charEncoding = Util.checkFileEncoding(file);
    String strText = this.toReadFile(file, charEncoding);
    if (Util.isTextEmpty(strText)) {
      JOptionPane.showMessageDialog(this, "����ļ�ʧ�ܣ�Դ�ļ���ȡ�쳣��", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    LineSeparator lineSeparator = LineSeparator.DEFAULT;
    if (strText.indexOf(LineSeparator.WINDOWS.toString()) >= 0) {
      strText = strText.replaceAll(LineSeparator.WINDOWS.toString(),
          LineSeparator.UNIX.toString());
      lineSeparator = LineSeparator.WINDOWS;
    } else if (strText.indexOf(LineSeparator.MACINTOSH.toString()) >= 0) {
      strText = strText.replaceAll(LineSeparator.MACINTOSH.toString(),
          LineSeparator.UNIX.toString());
      lineSeparator = LineSeparator.MACINTOSH;
    } else if (strText.indexOf(LineSeparator.UNIX.toString()) >= 0) {
      lineSeparator = LineSeparator.UNIX;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "����ļ�ʧ�ܣ�����ؼ����Ƿ���ȷ��", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File fileSplit = new File(file + "_split");
    if (fileSplit.exists()) {
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("�˲����������Ѵ��ڵ�" + fileSplit + "Ŀ¼��\n�Ƿ������"),
          Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
      Util.deleteAllFiles(fileSplit);
    } else {
      fileSplit.mkdirs(); // ���Ŀ¼�����ڣ��򴴽�֮
    }
    int count = 0;
    for (String text : arrText) {
      if (Util.isTextEmpty(text.trim())) {
        continue;
      }
      try {
        count++;
        File fileText = new File(fileSplit + "/" + count + ".txt");
        toSaveFile(fileText, text, charEncoding, lineSeparator);
      } catch (Exception x) {
        // x.printStackTrace();
      }
    }
    JOptionPane.showMessageDialog(this, "����ļ���ɣ�\n�ɹ������ļ���" + count + "����", Util.SOFTWARE,
        JOptionPane.CANCEL_OPTION);
  }

  private String checkText(String strText) {
    strText = strText.replace("\\", "\\\\");
    for (int i = 0; i < Util.PATTERN_META_CHARACTER.length(); i++) {
      char item = Util.PATTERN_META_CHARACTER.charAt(i);
      strText = strText.replace(String.valueOf(item), "\\" + item);
    }
    return strText;
  }

  private String toReadFile(File file, CharEncoding charEncoding) {
    String strCharset = charEncoding.toString();
    InputStreamReader inputStreamReader = null;
    String strText = "";
    try {
      inputStreamReader = new InputStreamReader(new FileInputStream(file), strCharset);
      char[] chrBuf = new char[Util.BUFFER_LENGTH];
      int len = 0;
      StringBuilder stbTemp = new StringBuilder();
      switch (charEncoding) {
      case UTF8:
      case ULE:
      case UBE:
        inputStreamReader.read(); // ȥ���ļ���ͷ��BOM
        break;
      }
      while ((len = inputStreamReader.read(chrBuf)) != -1) {
        stbTemp.append(chrBuf, 0, len);
      }
      strText = stbTemp.toString();
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
    return strText;
  }

  /**
   * ���ı����浽�ļ�
   * 
   * @param file
   *          ������ļ�
   */
  private void toSaveFile(File file, String strText, CharEncoding charEncoding, LineSeparator lineSeparator) throws Exception {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      strText = strText.replaceAll(LineSeparator.UNIX.toString(), lineSeparator.toString());
      byte[] byteStr;
      int[] charBOM = new int[] { -1, -1, -1 }; // ���ݵ�ǰ���ַ����룬���BOM������
      switch (charEncoding) {
      case UTF8:
        charBOM[0] = 0xef;
        charBOM[1] = 0xbb;
        charBOM[2] = 0xbf;
        break;
      case ULE:
        charBOM[0] = 0xff;
        charBOM[1] = 0xfe;
        break;
      case UBE:
        charBOM[0] = 0xfe;
        charBOM[1] = 0xff;
        break;
      }
      byteStr = strText.getBytes(charEncoding.toString());
      for (int i = 0; i < charBOM.length; i++) {
        if (charBOM[i] == -1) {
          break;
        }
        fileOutputStream.write(charBOM[i]);
      }
      fileOutputStream.write(byteStr);
    } catch (Exception x) {
      // x.printStackTrace();
      throw x; // ��������쳣�׳����Ա���ô����Խ����쳣����
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    this.slicingFile();
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }
}
