/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua;

public enum pjsua_invalid_id_const_ {
  PJSUA_INVALID_ID(pjsuaJNI.PJSUA_INVALID_ID_get());

  public final int swigValue() {
    return swigValue;
  }

  public static pjsua_invalid_id_const_ swigToEnum(int swigValue) {
    pjsua_invalid_id_const_[] swigValues = pjsua_invalid_id_const_.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (pjsua_invalid_id_const_ swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + pjsua_invalid_id_const_.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private pjsua_invalid_id_const_() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private pjsua_invalid_id_const_(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private pjsua_invalid_id_const_(pjsua_invalid_id_const_ swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

