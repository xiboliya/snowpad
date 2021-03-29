/**
 * Copyright (C) 2020 冰原
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "拆分文件"对话框
 * 
 * @author 冰原
 * 
 */
public class SlicingFileDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblKeyword = new JLabel("拆分关键字：");
  private BaseTextField txtKeyword = new BaseTextField();
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
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
    this.addListeners();
    this.setSize(240, 110);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("拆分文件");
    this.pnlMain.setLayout(null);
    this.lblKeyword.setBounds(20, 10, 90, Util.VIEW_HEIGHT);
    this.txtKeyword.setBounds(110, 10, 105, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblKeyword);
    this.pnlMain.add(this.txtKeyword);
    this.btnOk.setBounds(20, 45, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(130, 45, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.initView();
    }
    super.setVisible(visible);
  }

  /**
   * 初始化各控件的状态
   */
  private void initView() {
    String strSel = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(strSel)) {
      this.txtKeyword.setText(strSel);
    }
    this.txtKeyword.selectAll();
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtKeyword.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 按照关键字对文件进行拆分
   */
  private void slicingFile() {
    String keyword = this.txtKeyword.getText();
    if (Util.isTextEmpty(keyword)) {
      JOptionPane.showMessageDialog(this, "拆分关键字不能为空，请输入！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    keyword = checkText(keyword);
    BaseTextArea textArea = (BaseTextArea) this.txaSource;
    String strText = textArea.getText();
    if (Util.isTextEmpty(strText)) {
      JOptionPane.showMessageDialog(this, "文件内容为空，无法拆分！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    String[] arrText = strText.split(keyword);
    if (arrText.length <= 1) {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请检查关键字是否正确！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      return;
    }
    File file = textArea.getFile();
    if (file != null && file.exists()) {
      File fileParent = new File(file.getParent() + "/" + textArea.getTitle() + "_split");
      if (fileParent.exists()) {
        int result = JOptionPane.showConfirmDialog(this,
            Util.convertToMsg("此操作将覆盖已存在的" + fileParent + "目录！\n是否继续？"),
            Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
          return;
        }
        Util.deleteAllFiles(fileParent);
      } else {
        fileParent.mkdirs(); // 如果目录不存在，则创建之
      }
      int count = 0;
      for (String text : arrText) {
        if (Util.isTextEmpty(text.trim())) {
          continue;
        }
        File fileText = new File(fileParent + "/" + count + textArea.getFileExt().toString());
        try {
          toSaveFile(fileText, text);
          count++;
        } catch (Exception x) {
          x.printStackTrace();
        }
      }
      JOptionPane.showMessageDialog(this, "拆分文件完成！\n成功生成文件：" + count + "个。", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      onCancel();
    } else {
      JOptionPane.showMessageDialog(this, "拆分文件失败，请先保存文件！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }

  private String checkText(String strText) {
    strText = strText.replace("\\", "\\\\");
    for (int i = 0; i < Util.PATTERN_META_CHARACTER.length(); i++) {
      char item = Util.PATTERN_META_CHARACTER.charAt(i);
      strText = strText.replace(String.valueOf(item), "\\" + item);
    }
    return strText;
  }

  /**
   * 将文本保存到文件
   * 
   * @param file
   *          保存的文件
   */
  private void toSaveFile(File file, String strText) throws Exception {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      BaseTextArea textArea = (BaseTextArea) this.txaSource;
      strText = strText.replaceAll(LineSeparator.UNIX.toString(),
          textArea.getLineSeparator().toString());
      byte byteStr[];
      int charBOM[] = new int[] { -1, -1, -1 }; // 根据当前的字符编码，存放BOM的数组
      switch (textArea.getCharEncoding()) {
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
      byteStr = strText.getBytes(textArea.getCharEncoding().toString());
      for (int i = 0; i < charBOM.length; i++) {
        if (charBOM[i] == -1) {
          break;
        }
        fileOutputStream.write(charBOM[i]);
      }
      fileOutputStream.write(byteStr);
    } catch (Exception x) {
      // x.printStackTrace();
      throw x; // 将捕获的异常抛出，以便调用处可以进行异常处理
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
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.slicingFile();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
