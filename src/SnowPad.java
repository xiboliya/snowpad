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
  static String strFile = null; // 表示文件路径的字符串

  /**
   * 程序的总入口
   * 
   * @param args
   *          命令行参数，如：java -jar SnowPad.jar ../test.pl，表示打开当前目录的父目录中的test.pl文件
   */
  public static void main(String[] args) {
    if (args.length > 0) {
      strFile = args[0]; // 通过命令行启动程序时，可带一个文件路径的参数
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME);
        } catch (Exception x) {
          x.printStackTrace();
        }
        Util.setDefaultFont();
        new SnowPadFrame(strFile); // 初始化界面的同时打开文件
      }
    });
  }
}
