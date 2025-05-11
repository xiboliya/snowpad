/**
 * Copyright (C) 2025 冰原
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

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.manager.ListenerManager;
import com.xiboliya.snowpad.util.Util;

/**
 * "URL编码/解码"对话框
 * 
 * @author 冰原
 * 
 */
public class UrlEncodeDecodeDialog extends BaseDialog implements ActionListener, DocumentListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String[] TYPES = new String[] {"编码", "解码"};
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private JComboBox<String> cmbTypes = new JComboBox<String>();
  private JLabel lblUrl = new JLabel("URL：");
  private BaseTextField txtUrl = new BaseTextField();
  private JLabel lblResult = new JLabel("结果：");
  private BaseTextField txtResult = new BaseTextField();
  private BaseButton btnCopy = new BaseButton("复制结果(C)");
  private BaseButton btnCancel = new BaseButton("取消");

  public UrlEncodeDecodeDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal, txaSource);
    this.setTitle("URL编码/解码");
    this.init();
    this.initView();
    this.setMnemonic();
    this.addListeners();
    this.refreshView();
    this.setSize(310, 190);
    this.setVisible(true);
  }

  /**
   * 界面初始化
   */
  private void init() {
    this.pnlMain.setLayout(null);
    this.cmbTypes.setBounds(10, 10, 90, Util.INPUT_HEIGHT);
    this.lblUrl.setBounds(10, 45, 50, Util.VIEW_HEIGHT);
    this.txtUrl.setBounds(60, 45, 220, Util.INPUT_HEIGHT);
    this.lblResult.setBounds(10, 80, 50, Util.VIEW_HEIGHT);
    this.txtResult.setBounds(60, 80, 220, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.cmbTypes);
    this.pnlMain.add(this.lblUrl);
    this.pnlMain.add(this.txtUrl);
    this.pnlMain.add(this.lblResult);
    this.pnlMain.add(this.txtResult);

    this.btnCopy.setBounds(30, 115, 110, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(170, 115, 110, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnCopy);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 初始化控件显示
   */
  private void initView() {
    this.cmbTypes.setModel(new DefaultComboBoxModel<String>(TYPES));
    this.cmbTypes.setSelectedIndex(0);
    this.txtResult.setEditable(false);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.btnCopy.setMnemonic('C');
  }

  /**
   * 为各组件添加监听器
   */
  private void addListeners() {
    this.cmbTypes.addKeyListener(this.keyAdapter);
    this.cmbTypes.addItemListener(this);
    this.txtUrl.getDocument().addDocumentListener(this);
    this.txtUrl.addKeyListener(this.keyAdapter);
    this.txtResult.addKeyListener(this.keyAdapter);
    this.btnCopy.addActionListener(this);
    this.btnCopy.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnCopy.equals(source)) {
      this.toCopyResult();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * 刷新各控件的显示
   */
  public void refreshView() {
    String str = this.txaSource.getSelectedText();
    if (!Util.isTextEmpty(str)) {
      int index = str.indexOf("\n");
      if (index >= 0) {
        str = str.substring(0, index);
      }
      this.txtUrl.setText(str);
    }
    this.showResult();
  }

  /**
   * 显示结果
   */
  private void showResult() {
    int index = this.cmbTypes.getSelectedIndex();
    if (index == 0) {
      this.showEncodeResult();
    } else {
      this.showDecodeResult();
    }
  }

  /**
   * 显示编码的结果
   */
  private void showEncodeResult() {
    String strUrl = this.txtUrl.getText();
    if (Util.isTextEmpty(strUrl)) {
      this.txtResult.setText("");
    } else {
      try {
        String result = URLEncoder.encode(strUrl, "UTF-8");
        this.txtResult.setText(result);
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtResult.setText("");
      }
    }
  }

  /**
   * 显示解码的结果
   */
  private void showDecodeResult() {
    String strUrl = this.txtUrl.getText();
    if (Util.isTextEmpty(strUrl)) {
      this.txtResult.setText("");
    } else {
      try {
        String result = URLDecoder.decode(strUrl, "UTF-8");
        this.txtResult.setText(result);
      } catch (Exception x) {
        // x.printStackTrace();
        this.txtResult.setText("");
      }
    }
  }

  /**
   * "复制结果"的处理方法
   */
  private void toCopyResult() {
    ListenerManager.getInstance().postClipboardEvent(this.txtResult.getText());
  }

  /**
   * 默认的“取消”操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }

  /**
   * 默认的“确定”操作方法
   */
  @Override
  public void onEnter() {
  }

  /**
   * 当文本控件插入文本时，将触发此事件
   */
  @Override
  public void insertUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当文本控件删除文本时，将触发此事件
   */
  @Override
  public void removeUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当文本控件修改文本时，将触发此事件
   */
  @Override
  public void changedUpdate(DocumentEvent e) {
    this.showResult();
  }

  /**
   * 当用户已选定或取消选定某项时，触发此事件
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    this.showResult();
  }
}
