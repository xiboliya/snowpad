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

package com.xiboliya.snowpad.window;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.xiboliya.snowpad.util.Util;

/**
 * 信息提示窗口
 * 
 * @author 冰原
 */
public class TipsWindow extends JWindow implements ActionListener {
  private static final long serialVersionUID = 1L;
  private static final Color BG_RED = new Color(200, 60, 0);
  private static final Color BG_PINK = new Color(255, 180, 180);
  private static final Color BG_PURPLE = new Color(200, 120, 150);
  private static final Color BG_GREEN = new Color(60, 210, 90);
  private static final Color BG_BLUE = new Color(60, 120, 180);
  private JLabel lblMain = new JLabel();
  private Timer timer = null;

  private TipsWindow(Window owner, String message, Background bgColor, TimerLength timerLength, WindowSize windowSize) {
    super(owner);
    this.setSize(windowSize.getWindowWidth(), 36);
    this.setLocationRelativeTo(owner);
    this.init(message, bgColor);
    this.setVisible(true);
    this.delayClose(timerLength);
  }

  private void init(String message, Background bgColor) {
    this.lblMain.setText(message);
    this.lblMain.setOpaque(true); // 设置标签可以绘制背景
    this.lblMain.setBorder(new EtchedBorder());
    this.lblMain.setBackground(bgColor.getColor());
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
    new TipsWindow(owner, message, Background.PINK, TimerLength.DEFAULT, WindowSize.DEFAULT);
  }

  /**
   * 显示信息提示窗口
   * 
   * @param owner 当前窗口的所有者
   * @param message 信息提示窗口显示的文本
   * @param bgColor 窗口背景颜色
   */
  public static void show(Window owner, String message, Background bgColor) {
    if (Util.isTextEmpty(message) || bgColor == null) {
      return;
    }
    new TipsWindow(owner, message, bgColor, TimerLength.DEFAULT, WindowSize.DEFAULT);
  }

  /**
   * 显示信息提示窗口
   * 
   * @param owner 当前窗口的所有者
   * @param message 信息提示窗口显示的文本
   * @param timerLength 窗口显示时长
   */
  public static void show(Window owner, String message, TimerLength timerLength) {
    if (Util.isTextEmpty(message) || timerLength == null) {
      return;
    }
    new TipsWindow(owner, message, Background.PINK, timerLength, WindowSize.DEFAULT);
  }

  /**
   * 显示信息提示窗口
   * 
   * @param owner 当前窗口的所有者
   * @param message 信息提示窗口显示的文本
   * @param windowSize 窗口显示大小
   */
  public static void show(Window owner, String message, WindowSize windowSize) {
    if (Util.isTextEmpty(message) || windowSize == null) {
      return;
    }
    new TipsWindow(owner, message, Background.PINK, TimerLength.DEFAULT, windowSize);
  }

  /**
   * 显示信息提示窗口
   * 
   * @param owner 当前窗口的所有者
   * @param message 信息提示窗口显示的文本
   * @param bgColor 窗口背景颜色
   * @param timerLength 窗口显示时长
   * @param windowSize 窗口显示大小
   */
  public static void show(Window owner, String message, Background bgColor, TimerLength timerLength, WindowSize windowSize) {
    if (Util.isTextEmpty(message) || bgColor == null || timerLength == null || windowSize == null) {
      return;
    }
    new TipsWindow(owner, message, bgColor, timerLength, windowSize);
  }

  /**
   * 当前窗口自动延时关闭
   * 
   * @param timerLength 窗口显示时长
   */
  private void delayClose(TimerLength timerLength) {
    this.timer = new Timer(timerLength.getMillisecond(), this);
    this.timer.start();
  }

  /**
   * 添加事件的处理方法
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    this.timer.stop();
    this.dispose();
  }

  /**
   * 信息提示窗口背景颜色
   * 
   * @author 冰原
   */
  public enum Background {
    RED,
    PINK,
    PURPLE,
    GREEN,
    BLUE;

    /**
     * 获取背景颜色
     * 
     * @return 背景颜色
     */
    public Color getColor() {
      switch (this) {
        case RED:
          return BG_RED;
        case PINK:
          return BG_PINK;
        case PURPLE:
          return BG_PURPLE;
        case GREEN:
          return BG_GREEN;
        case BLUE:
          return BG_BLUE;
        default:
          return BG_PINK;
      }
    }
  }

  /**
   * 信息提示窗口显示时长
   * 
   * @author 冰原
   */
  public enum TimerLength {
    SHORT(1000),
    DEFAULT(1500),
    LONG(2000),
    LONGER(2500),
    LONGEST(3000);

    // 显示时长，单位：毫秒
    private int millisecond;

    private TimerLength(int millisecond) {
      this.millisecond = millisecond;
    }

    /**
     * 获取显示时长
     * 
     * @return 显示时长，单位：毫秒
     */
    public int getMillisecond() {
      return this.millisecond;
    }
  }

  /**
   * 信息提示窗口显示大小
   * 
   * @author 冰原
   */
  public enum WindowSize {
    SMALLER(140),
    SMALL(170),
    DEFAULT(200),
    BIG(230),
    BIGGER(260);

    // 窗口宽度，单位：像素
    private int windowWidth;

    private WindowSize(int windowWidth) {
      this.windowWidth = windowWidth;
    }

    /**
     * 获取窗口宽度
     * 
     * @return 窗口宽度，单位：像素
     */
    public int getWindowWidth() {
      return this.windowWidth;
    }
  }
}
