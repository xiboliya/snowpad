/**
 * Copyright (C) 2021 冰原
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.xiboliya.snowpad.base.BaseDialog;
import com.xiboliya.snowpad.util.Util;

/**
 * "按键检测"对话框
 * 
 * @author 冰原
 * 
 */
public class KeyCheckDialog extends BaseDialog {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblInfo = new JLabel("按下任意键可以检测到按键信息！");
  private JLabel lblName = new JLabel();
  private JLabel lblCode = new JLabel();
  private KeyAdapter keyAdapter = null;

  public KeyCheckDialog(JDialog owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
    this.setSize(280, 150);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("按键检测");
    this.pnlMain.setLayout(null);
    this.lblInfo.setBounds(20, 10, 200, Util.VIEW_HEIGHT);
    this.lblName.setBounds(20, 40, 115, 70);
    this.lblCode.setBounds(145, 40, 115, 70);
    this.lblName.setHorizontalAlignment(JLabel.CENTER);
    this.lblCode.setHorizontalAlignment(JLabel.CENTER);
    this.lblName.setBorder(new TitledBorder("按键名称"));
    this.lblCode.setBorder(new TitledBorder("按键编码"));
    this.pnlMain.add(this.lblInfo);
    this.pnlMain.add(this.lblName);
    this.pnlMain.add(this.lblCode);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.refreshKeyView(null);
    }
    super.setVisible(visible);
  }

  /**
   * 刷新按键信息
   */
  private void refreshKeyView(KeyEvent e) {
    if (e == null) {
      this.lblName.setText("");
      this.lblCode.setText("");
    } else {
      int code = e.getKeyCode();
      this.lblName.setText(KeyEvent.getKeyText(code));
      this.lblCode.setText(String.valueOf(code));
    }
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.keyAdapter = new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        refreshKeyView(e);
      }
    };
    this.pnlMain.setFocusable(true);
    this.pnlMain.addKeyListener(this.keyAdapter);
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
