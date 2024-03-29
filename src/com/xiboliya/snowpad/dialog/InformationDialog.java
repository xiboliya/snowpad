/**
 * Copyright (C) 2014 冰原
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

package com.xiboliya.snowpad.dialog;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;

/**
 * "统计信息"对话框
 * 
 * @author 冰原
 * 
 */
public class InformationDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final String DATE_STYLE = "yyyy-MM-dd HH:mm:ss";
  private static final String INFO_FILE_PATH = "文件路径："; // 统计信息窗口中使用的字符串
  private static final String INFO_FILE_MODIFY_TIME = "修改时间："; // 统计信息窗口中使用的字符串
  private static final String INFO_FILE_SIZE = "文件大小："; // 统计信息窗口中使用的字符串
  private static final String INFO_DOC_CHARS = "总字数："; // 统计信息窗口中使用的字符串
  private static final String INFO_DOC_LINES = "总行数："; // 统计信息窗口中使用的字符串
  private static final String INFO_DOC_DIGITS = "数字数："; // 统计信息窗口中使用的字符串
  private static final String INFO_DOC_LETTERS = "字母数："; // 统计信息窗口中使用的字符串
  private static final String INFO_DOC_BLANKS = "空格数："; // 统计信息窗口中使用的字符串
  // 文件大小的单位换算比例
  private static final int UNIT_RATE = 1024;
  // 文件大小的单位
  private static final String[] FILE_SIZE_UNIT = new String[] {"字节(B)", "千字节(KB)", "兆字节(MB)"};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlCenter = new JPanel();
  private JPanel pnlSouth = new JPanel();
  private GridBagLayout gblMain = new GridBagLayout(); // 网格袋布局
  private GridBagConstraints gbc = new GridBagConstraints(); // 网格袋布局类布置组件的约束
  private JTextArea txaFile = new JTextArea();
  private JTextArea txaDoc = new JTextArea();
  private BaseButton btnOk = new BaseButton(" 确定 ");
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

  public InformationDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("统计信息");
    this.init();
    this.addListeners();
    this.setSize(400, 300);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.initInfo();
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    // 当组件的显示区域大于它所请求的显示区域大小时，如何调整组件大小。GridBagConstraints.BOTH表示组件完全填满显示区域。默认值为GridBagConstraints.NONE。
    this.gbc.fill = GridBagConstraints.BOTH;
    // 用来设置组件所占的单位长度，默认值为1。GridBagConstraints.REMAINDER表示此组件为此行的最后一个组件，而且会占据所有剩余空间。
    this.gbc.gridwidth = GridBagConstraints.REMAINDER;
    // 用来设置组件所占的单位高度，默认值为1。
    this.gbc.gridheight = 2;
    // 用来设置窗口变大时，组件的水平缩放比例。数字越大，表示组件能得到更多的空间。默认值为0。
    this.gbc.weightx = 1.0;
    // 用来设置窗口变大时，组件的垂直缩放比例。数字越大，表示组件能得到更多的空间。默认值为0。
    this.gbc.weighty = 1.0;
    this.gblMain.setConstraints(this.txaFile, this.gbc);
    this.pnlCenter.add(this.txaFile);
    this.gbc.gridheight = 1;
    this.gblMain.setConstraints(this.txaDoc, this.gbc);
    this.pnlCenter.setLayout(this.gblMain);
    this.pnlCenter.add(this.txaDoc);
    this.pnlMain.add(this.pnlCenter, BorderLayout.CENTER);
    this.btnOk.setMargin(new Insets(3, 15, 3, 15));
    this.pnlSouth.add(this.btnOk);
    this.pnlMain.add(this.pnlSouth, BorderLayout.SOUTH);

    this.simpleDateFormat.applyPattern(DATE_STYLE);
    this.txaFile.setBorder(new TitledBorder("文件信息"));
    this.txaDoc.setBorder(new TitledBorder("文本域信息"));
    this.txaFile.setEditable(false);
    this.txaDoc.setEditable(false);
    this.txaFile.setLineWrap(true);
    this.txaDoc.setLineWrap(true);
    this.txaFile.setFocusable(false);
    this.txaDoc.setFocusable(false);
  }

  /**
   * 初始化相关信息
   */
  private void initInfo() {
    File file = this.txaSource.getFile();
    if (file != null) {
      this.txaFile.setVisible(true);
      long fileSize = file.length();
      StringBuilder stbFileInfo = new StringBuilder();
      stbFileInfo.append(INFO_FILE_PATH).append(file.getAbsolutePath()).append("\n");
      stbFileInfo.append(INFO_FILE_MODIFY_TIME).append(this.simpleDateFormat.format(file.lastModified())).append("\n");
      stbFileInfo.append(INFO_FILE_SIZE).append(this.formatFileSize(fileSize, 0));
      if (fileSize >= UNIT_RATE) {
        stbFileInfo.append(" | " + fileSize + " " + FILE_SIZE_UNIT[0]);
      }
      this.txaFile.setText(stbFileInfo.toString());
    } else {
      this.txaFile.setVisible(false);
    }
    String strText = this.txaSource.getText();
    int blanks = 0; // 空格
    int digits = 0; // 数字
    int letters = 0; // 字母
    for (char ch : strText.toCharArray()) {
      if (Character.isDigit(ch)) {
        digits++;
      } else if (Character.isLowerCase(ch) || Character.isUpperCase(ch)) { // 此处不可用Character.isLetter(ch)，因为会将汉字也计算在内
        letters++;
      } else if (ch == ' ') {
        blanks++;
      }
    }
    this.txaDoc.setText(INFO_DOC_CHARS + strText.length() + "\n" + INFO_DOC_LINES + this.txaSource.getLineCount() + "\n" +
        INFO_DOC_DIGITS + digits + "\n" + INFO_DOC_LETTERS + letters + "\n" + INFO_DOC_BLANKS + blanks);
  }

  /**
   * 格式化文件大小的显示
   * 
   * @param fileSize 文件字节数
   * @param unitIndex 文件大小的单位索引值
   * @return 格式化后的文件大小的显示
   */
  private String formatFileSize(float fileSize, int unitIndex) {
    if (fileSize < UNIT_RATE || unitIndex >= FILE_SIZE_UNIT.length - 1) {
      String strFileSize = String.valueOf(fileSize);
      if (strFileSize.endsWith(".0")) {
        strFileSize = strFileSize.substring(0, strFileSize.length() - 2);
      }
      return strFileSize + " " + FILE_SIZE_UNIT[unitIndex];
    }
    fileSize = (float)(fileSize / UNIT_RATE);
    unitIndex++;
    return this.formatFileSize(fileSize, unitIndex);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.dispose();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
