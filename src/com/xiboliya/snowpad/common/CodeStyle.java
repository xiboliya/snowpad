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

package com.xiboliya.snowpad.common;

import com.xiboliya.snowpad.util.Util;

/**
 * 代码格式化、压缩的处理类
 * 
 * @author 冰原
 * 
 */
public class CodeStyle {

  /**
   * 压缩json字符串，即清除json字符串中冗余的空白字符，
   * 包括：制表符、换行符、键值外部的空格
   * 
   * @param json
   *          待处理的json字符串
   * @return 处理后的json字符串
   */
  public static String compressJson(String json) {
    if (Util.isTextEmpty(json.trim())) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    int length = json.length();
    int quotationCount = 0; // 双引号数量
    char key = 0; // 当前字符
    for (int i = 0; i < length; i++) {
      key = json.charAt(i);
      if (key == '"') {
        quotationCount++;
        result.append(key);
      } else if (key == ' ') {
        if (quotationCount % 2 != 0) {
          result.append(key);
        }
      } else if (key != '\t' && key != '\n') {
        result.append(key);
      }
    }
    return result.toString();
  }

  /**
   * 格式化json字符串
   * 
   * @author  yanghaitao
   * @param json
   *          待格式化的json字符串
   * @param indentSize
   *          每组空白字符的个数
   * @return 格式化后的json字符串
   */
  public static String formatJson(String json, int indentSize) {
    StringBuilder result = new StringBuilder();
    int length = json.length();
    int indentCount = 0; // 缩进次数
    int quotationCount = 0; // 双引号数量
    char key = 0; // 当前字符
    // 遍历输入字符串。
    for (int i = 0; i < length; i++) {
      // 1、获取当前字符。
      key = json.charAt(i);
      if (key == '"') {
        // 2、如果当前字符是双引号，双引号数量加一个。打印：当前字符。
        quotationCount++;
        result.append(key);
      } else if ((key == '[' || key == '{') && quotationCount % 2 == 0) {
        // 3、如果当前字符是前方括号、前花括号，并且位于双引号外部，做如下处理：
        // (1)如果前面还有字符，并且字符为“:”，打印：空格。
        if ((i > 1) && (json.charAt(i - 1) == ':')) {
          result.append(' ');
        }
        // (2)打印：当前字符。
        result.append(key);
        // (3)前方括号、前花括号的后面必须换行。打印：换行。
        result.append("\n");
        // (4)每出现一次前方括号、前花括号，缩进次数增加一次。打印：新行缩进。
        indentCount++;
        result.append(indent(indentSize, indentCount));
      } else if ((key == ']' || key == '}') && quotationCount % 2 == 0) {
        // 4、如果当前字符是后方括号、后花括号，并且位于双引号外部，做如下处理：
        // (1)后方括号、后花括号的前面必须换行。打印：换行。
        result.append('\n');
        // (2)每出现一次后方括号、后花括号，缩进次数减少一次。打印：新行缩进。
        indentCount--;
        result.append(indent(indentSize, indentCount));
        // (3)打印：当前字符。
        result.append(key);
        // (4)如果当前字符后面还有字符，并且字符不为“,”、"]"、"}"，打印：换行。
        if ((i + 1) < length) {
          char nextChar = json.charAt(i + 1);
          if (nextChar != ',' && nextChar != ']' && nextChar != '}') {
            result.append('\n');
          }
        }
      } else if (key == ',' && quotationCount % 2 == 0) {
        // 5、如果当前字符是逗号“,”，并且位于双引号外部。逗号后面换行，并缩进，不改变缩进次数。
        result.append(key);
        result.append('\n');
        result.append(indent(indentSize, indentCount));
      } else {
        // 6、打印：当前字符。
        result.append(key);
      }
    }
    return result.toString();
  }

  /**
   * 获取当前的空格缩进字符串
   * 
   * @author  yanghaitao
   * @param indentSize
   *          每组空白字符的个数
   * @param count
   *          空白字符组的数量
   * @return 空格字符串
   */
  private static String indent(int indentSize, int count) {
    StringBuilder stbIndent = new StringBuilder();
    for (int i = 0; i < indentSize; i++) {
      stbIndent.append(" ");
    }
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < count; i++) {
      result.append(stbIndent);
    }
    return result.toString();
  }
}
