/**
 * Copyright (C) 2018 冰原
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

import java.util.LinkedList;

/**
 * 文件中查找的结果
 * 
 * @author 冰原
 * 
 */
public class SearchResult {
  private BaseTextArea textArea;
  private String strSearch = "";
  private LinkedList<SearchBean> listIndex;

  public SearchResult(BaseTextArea textArea, String strSearch, LinkedList<SearchBean> listIndex) {
    this.textArea = textArea;
    this.strSearch = strSearch;
    this.listIndex = listIndex;
  }

  public BaseTextArea getTextArea() {
    return this.textArea;
  }

  public String getStrSearch() {
    return this.strSearch;
  }

  public LinkedList<SearchBean> getListIndex() {
    return this.listIndex;
  }
}
