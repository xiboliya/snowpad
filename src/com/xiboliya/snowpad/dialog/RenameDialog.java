/**
 * Copyright (C) 2026 冰原
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.xiboliya.snowpad.base.BaseButton;
import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.base.BaseKeyAdapter;
import com.xiboliya.snowpad.base.BaseTextField;
import com.xiboliya.snowpad.util.Util;
import com.xiboliya.snowpad.window.TipsWindow;

/**
 * "重命名文件/目录"对话框
 * 
 * @author 冰原
 * 
 */
public class RenameDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblRename = new JLabel("重命名为：");
  private BaseTextField txtRename = new BaseTextField();
  private BaseButton btnOk = new BaseButton("确定");
  private BaseButton btnCancel = new BaseButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private File file = null;

  public RenameDialog(JFrame owner, boolean modal, File file) {
    super(owner, modal);
    this.file = file;
    this.init();
    this.initView();
    this.addListeners();
    this.setSize(240, 130);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("重命名文件/目录");
    this.pnlMain.setLayout(null);
    this.lblRename.setBounds(20, 20, 70, Util.VIEW_HEIGHT);
    this.txtRename.setBounds(90, 20, 130, Util.INPUT_HEIGHT);
    this.btnOk.setBounds(20, 65, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(130, 65, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.lblRename);
    this.pnlMain.add(this.txtRename);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.initView();
    }
    super.setVisible(visible);
  }

  public void setFile(File file) {
    this.file = file;
  }

  /**
   * 初始化文本框
   */
  private void initView() {
    this.txtRename.setText(this.file.getName());
    this.txtRename.selectAll();
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtRename.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 重命名文件/目录
   */
  private void rename() {
    if (!this.file.exists()) {
      TipsWindow.show(this, "源文件/目录不存在，操作失败！");
      return;
    }
    String text = this.txtRename.getText();
    if (Util.isTextEmpty(text)) {
      TipsWindow.show(this, "文件/目录名不能为空！");
      return;
    }
    if (text.equals(this.file.getName())) {
      this.onCancel();
      return;
    }
    File newFile = new File(this.file.getParent() + "/" + text);
    if (newFile.exists()) {
    TipsWindow.show(this, "文件已存在，请重新命名！");
      return;
    }
    boolean isRenamed = this.file.renameTo(newFile);
    if (isRenamed) {
      this.onCancel();
    } else {
      TipsWindow.show(this, "重命名失败，请重试！");
    }
  }

  /**
   * 为各组件添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (this.btnOk.equals(source)) {
      this.onEnter();
    } else if (this.btnCancel.equals(source)) {
      this.onCancel();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  @Override
  public void onEnter() {
    this.rename();
  }

  /**
   * 默认的"取消"操作方法
   */
  @Override
  public void onCancel() {
    this.dispose();
  }
}
