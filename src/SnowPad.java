package com.xiboliya.snowpad;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 本程序的主类
 * 
 * @author 冰原
 * 
 */
public class SnowPad {

  /**
   * 程序的总入口，可在运行时指定所打开的文件，并可以为多个
   * 
   * @param args
   *          命令行参数，如：java -jar SnowPad.jar test1.pl test2.pl，
   *          表示打开当前目录中的test1.pl、test2.pl文件
   */
  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME);
        } catch (Exception x) {
          x.printStackTrace();
        }
        Util.setDefaultFont();
        System.setProperty("java.awt.im.style", "on-the-spot"); // 去掉文本框输入中文时所弹出的输入窗口
        new SnowPadFrame(args); // 初始化界面的同时打开文件
      }
    });
  }
}
