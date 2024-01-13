/**
 * Copyright (C) 2024 冰原
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

package com.xiboliya.snowpad.manager;

import java.util.LinkedList;
import com.xiboliya.snowpad.event.ShortcutListener;

/**
 * 监听事件管理器
 * 
 * @author 冰原
 * 
 */
public class ListenerManager {
  // 监听快捷键变化的接口集合
  private LinkedList<ShortcutListener> shortcutListenerList = null;

  private ListenerManager() {
  }

  public static ListenerManager getInstance() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {
    private static final ListenerManager INSTANCE = new ListenerManager();

    private Singleton() {
    }
  }

  /**
   * 添加快捷键变化监听器
   */
  public void addShortcutListener(ShortcutListener shortcutListener) {
    if (this.shortcutListenerList == null) {
      this.shortcutListenerList = new LinkedList<ShortcutListener>();
    }
    if (!this.shortcutListenerList.contains(shortcutListener)) {
      this.shortcutListenerList.add(shortcutListener);
    }
  }

  /**
   * 移除快捷键变化监听器
   */
  public void removeShortcutListener(ShortcutListener shortcutListener) {
    if (this.shortcutListenerList != null) {
      this.shortcutListenerList.remove(shortcutListener);
    }
  }

  /**
   * 通知快捷键变化
   */
  public void postShortcutEvent() {
    if (this.shortcutListenerList != null) {
      for (ShortcutListener listener : this.shortcutListenerList) {
        listener.shortcutChanged();
      }
    }
  }
}
