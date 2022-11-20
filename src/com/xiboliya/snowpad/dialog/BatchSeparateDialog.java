/**
 * Copyright (C) 2013 冰原
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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * 批处理"分割行"对话框
 * 
 * @author 冰原
 * 
 */
public class BatchSeparateDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JRadioButton radLineStart = new JRadioButton("行首(S)", false);
  private JRadioButton radLineEnd = new JRadioButton("行尾(E)", true);
  private JPanel pnlLineStartEnd = new JPanel(new GridLayout(2, 1));
  private JLabel lblOffset = new JLabel("偏移量：");
  private BaseTextField txtOffset = new BaseTextField(true, "\\d*"); // 限制用户只能输入数字
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private ButtonGroup bgpLineStartEnd = new ButtonGroup();

  public BatchSeparateDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal, txaSource);
    this.init();
    this.addListeners();
    this.setSize(220, 150);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.txtOffset.setText("");
    }
    super.setVisible(visible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("分割行");
    this.pnlMain.setLayout(null);
    this.lblOffset.setBounds(20, 10, 60, Util.VIEW_HEIGHT);
    this.txtOffset.setBounds(80, 10, 100, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblOffset);
    this.pnlMain.add(this.txtOffset);
    this.pnlLineStartEnd.setBounds(10, 40, 95, 70);
    this.pnlLineStartEnd.setBorder(new TitledBorder("分割位置"));
    this.pnlLineStartEnd.add(this.radLineStart);
    this.pnlLineStartEnd.add(this.radLineEnd);
    this.pnlMain.add(this.pnlLineStartEnd);
    this.radLineStart.setMnemonic('S');
    this.radLineEnd.setMnemonic('E');
    this.bgpLineStartEnd.add(radLineStart);
    this.bgpLineStartEnd.add(radLineEnd);
    this.radLineStart.setSelected(true);
    this.btnOk.setBounds(115, 50, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(115, 85, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.radLineStart.addKeyListener(this.keyAdapter);
    this.radLineEnd.addKeyListener(this.keyAdapter);
    this.txtOffset.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 分割行
   */
  private void separateText() {
    int offset = 0;
    try {
      offset = Integer.parseInt(this.txtOffset.getText().trim());
    } catch (NumberFormatException x) {
      // x.printStackTrace();
      TipsWindow.show(this, "格式错误，请输入数字！");
      this.txtOffset.requestFocus();
      this.txtOffset.selectAll();
      return;
    }
    if (offset <= 0) {
      TipsWindow.show(this, "数值必须大于0！");
      this.txtOffset.requestFocus();
      this.txtOffset.selectAll();
    } else {
      this.toSeparateText(offset);
      this.dispose();
    }
  }

  /**
   * 从行首/行尾以指定的偏移量分割行
   * 
   * @param offset
   *          从行首/行尾进行分割的偏移量
   */
  private void toSeparateText(int offset) {
    CurrentLines currentLines = new CurrentLines(this.txaSource);
    String strContent = currentLines.getStrContent();
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    String[] arrText = strContent.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    boolean isLineStart = this.radLineStart.isSelected();
    StringBuilder stbText = new StringBuilder();
    for (int n = 0; n < arrText.length; n++) {
      int strLen = arrText[n].length();
      if (strLen > offset) {
        if (isLineStart) {
          arrText[n] = arrText[n].substring(0, offset) + "\n" +
              arrText[n].substring(offset);
        } else {
          arrText[n] = arrText[n].substring(0, strLen - offset) + "\n" +
              arrText[n].substring(strLen - offset);
        }
      }
      stbText.append(arrText[n] + "\n");
    }
    this.txaSource.replaceRange(stbText.deleteCharAt(stbText.length() - 1)
        .toString(), startIndex, endIndex);
    endIndex = startIndex + stbText.length() - 1; // 分割行后，当前选区内末行的行尾偏移量
    if (this.txaSource.getText().length() == endIndex + 1) {
      endIndex++;
    }
    this.txaSource.select(startIndex, endIndex);
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.separateText();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
