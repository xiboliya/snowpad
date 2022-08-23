/**
 * Copyright (C) 2022 冰原
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

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 * 信息提示窗口
 * 
 * @author 冰原
 * 
 */
public class TipsWindow extends JWindow implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JLabel lblMain = new JLabel();
  private String message = null;
  private Timer timer = null;

  private TipsWindow(Window owner, String message) {
    super(owner);
    this.message = message;
    this.setSize(200, 36);
    this.setLocationRelativeTo(owner);
    this.init();
    this.setVisible(true);
    this.delayClose();
  }

  private void init() {
    this.lblMain.setText(this.message);
    this.lblMain.setOpaque(true); // 设置标签可以绘制背景
    this.lblMain.setBorder(new EtchedBorder());
    this.lblMain.setBackground(Color.PINK);
    this.lblMain.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(this.lblMain);
  }

  /**
   * 显示信息提示窗口
   * 
   * @param owner
   *          当前窗口的所有者
   * @param message
   *          信息提示窗口显示的文本
   */
  public static void show(Window owner, String message) {
    if (Util.isTextEmpty(message)) {
      return;
    }
    new TipsWindow(owner, message);
  }

  /**
   * 当前窗口自动延时关闭
   */
  private void delayClose() {
    // 延时2秒
    this.timer = new Timer(2000, this);
    this.timer.start();
  }

  /**
   * 添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    this.timer.stop();
    this.dispose();
  }
}
