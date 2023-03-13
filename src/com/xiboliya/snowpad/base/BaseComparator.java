/**
 * Copyright (C) 2023 冰原
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

package com.xiboliya.snowpad.base;

import java.io.File;
import java.util.Comparator;

/**
 * 用于文件和目录排序的比较器
 * 
 * @author 冰原
 * 
 */
public class BaseComparator implements Comparator<File> {
  @Override
  public int compare(File file1, File file2) {
    if (file1 == null || file2 == null) {
      return 0;
    }
    try {
      String name1 = file1.getName();
      String name2 = file2.getName();
      // 文件和目录按升序排序，并忽略大小写
      return name1.compareToIgnoreCase(name2);
    } catch (Exception x) {
      // x.printStackTrace();
    }
    return 0;
  }
}
