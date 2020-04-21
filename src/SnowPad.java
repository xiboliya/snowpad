/**
 * Copyright (C) 2013 ��ԭ
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
 * �����������
 * 
 * @author ��ԭ
 * 
 */
public class SnowPad {

  /**
   * ���������ڣ���������ʱָ�����򿪵��ļ���������Ϊ���
   * 
   * @param args
   *          �����в������磺java -jar SnowPad.jar test1.pl test2.pl��
   *          ��ʾ�򿪵�ǰĿ¼�е�test1.pl��test2.pl�ļ�
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
        System.setProperty("java.awt.im.style", "on-the-spot"); // ȥ���ı�����������ʱ�����������봰��
        for (String arg : args) {
          boolean isExist = false;
          for (FileHistoryBean bean : setting.fileHistoryList) {
            if (arg.equals(bean.getFileName())) {
              isExist = true;
              break;
            }
          }
          if (!isExist) {
            setting.fileHistoryList.add(new FileHistoryBean(arg, false));
          }
        }
        new SnowPadFrame(setting, settingAdapter); // ��ʼ����������õ�ͬʱ���ļ�
      }
    });
  }
}
