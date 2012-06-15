package com.xiboliya.snowpad;

/**
 * 用于标识文件编码格式的枚举
 * 
 * @author chen
 * 
 */
public enum CharEncoding {
  /**
   * ANSI格式编码
   */
  ANSI,
  /**
   * 默认格式编码
   */
  BASE,
  /**
   * Unicode Big Endian格式编码
   */
  UBE,
  /**
   * Unicode Little Endian格式编码
   */
  ULE,
  /**
   * UTF-8格式编码
   */
  UTF8,
  /**
   * UTF-8无BOM格式编码
   */
  UTF8_NO_BOM;
  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case ANSI:
      return "US-ASCII";
    case UBE:
      return "UTF-16BE";
    case ULE:
      return "UTF-16LE";
    case UTF8:
    case UTF8_NO_BOM:
      return "UTF-8";
    case BASE:
    default:
      return "GB18030";
    }
  }

  /**
   * 获取编码格式的名称
   * 
   * @return 编码格式的名称
   */
  public String getName() {
    switch (this) {
    case ANSI:
      return "ANSI";
    case UBE:
      return "Unicode Big Endian";
    case ULE:
      return "Unicode Little Endian";
    case UTF8:
      return "UTF-8";
    case UTF8_NO_BOM:
      return "UTF-8 No BOM";
    case BASE:
    default:
      return "GB18030";
    }
  }
}
