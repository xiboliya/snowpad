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
import com.xiboliya.snowpad.event.ClipboardListener;
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
  // 监听剪贴板变化的接口集合
  private LinkedList<ClipboardListener> clipboardListenerList = null;

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
        if (listener != null) {
          listener.shortcutChanged();
        }
      }
    }
  }

  /**
   * 添加剪贴板变化监听器
   */
  public void addClipboardListener(ClipboardListener clipboardListener) {
    if (this.clipboardListenerList == null) {
      this.clipboardListenerList = new LinkedList<ClipboardListener>();
    }
    if (!this.clipboardListenerList.contains(clipboardListener)) {
      this.clipboardListenerList.add(clipboardListener);
    }
  }

  /**
   * 移除剪贴板变化监听器
   */
  public void removeClipboardListener(ClipboardListener clipboardListener) {
    if (this.clipboardListenerList != null) {
      this.clipboardListenerList.remove(clipboardListener);
    }
  }

  /**
   * 通知剪贴板内容变化
   * @param text 待设置到剪贴板的文本
   */
  public void postClipboardEvent(String text) {
    if (this.clipboardListenerList != null) {
      for (ClipboardListener listener : this.clipboardListenerList) {
        if (listener != null) {
          listener.contentChanged(text);
        }
      }
    }
  }
}
