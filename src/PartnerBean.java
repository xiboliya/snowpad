package com.xiboliya.snowpad;

/**
 * 保存广义键/值对的简单类
 * 
 * @author chen
 * 
 */
public class PartnerBean {
  private Object object = null;
  private int index = -1;

  /**
   * 构造方法
   */
  public PartnerBean() {
  }

  /**
   * 构造方法
   * 
   * @param object
   *          将要保存的值
   * @param index
   *          将要保存的键
   */
  public PartnerBean(Object object, int index) {
    this.setObject(object);
    this.setIndex(index);
  }

  /**
   * 获取当前值
   * 
   * @return 当前值
   */
  public Object getObject() {
    return this.object;
  }

  /**
   * 获取当前键
   * 
   * @return 当前键
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * 设置当前值
   * 
   * @param object
   *          将要保存的值
   */
  public void setObject(Object object) {
    this.object = object;
  }

  /**
   * 设置当前键
   * 
   * @param index
   *          将要保存的键
   */
  public void setIndex(int index) {
    this.index = index;
  }
}
