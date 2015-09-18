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
        Setting setting = new Setting();
        SettingAdapter settingAdapter = new SettingAdapter(setting);
        settingAdapter.parse();
        try {
          if (setting.viewLookAndFeel < 0) {
            UIManager.setLookAndFeel(Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME);
          } else {
            UIManager
                .setLookAndFeel(Util.LOOK_AND_FEEL_INFOS[setting.viewLookAndFeel]
                    .getClassName());
          }
        } catch (Exception x) {
          // x.printStackTrace();
        }
        Util.setDefaultFont();
        System.setProperty("java.awt.im.style", "on-the-spot"); // 去掉文本框输入中文时所弹出的输入窗口
        for (String arg : args) {
          int index = setting.fileHistoryList.indexOf(arg);
          if (index < 0) {
            setting.fileHistoryList.add(arg);
          }
        }
        new SnowPadFrame(setting, settingAdapter); // 初始化界面和设置的同时打开文件
      }
    });
  }
}
