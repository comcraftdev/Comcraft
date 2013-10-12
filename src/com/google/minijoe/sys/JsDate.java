// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.minijoe.sys;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Javascript Date object implementation.
 *
 * @author Stefan Haustein
 */
public class JsDate extends JsObject {
  private static final int ID_TO_DATE_STRING = 903;
  private static final int ID_TO_TIME_STRING = 904;
  private static final int ID_TO_LOCALE_DATE_STRING = 906;
  private static final int ID_TO_LOCALE_TIME_STRING = 907;

  private static final int ID_GET_TIME = 909;
  private static final int ID_GET_FULL_YEAR = 910;
  private static final int ID_GET_UTC_FULL_YEAR = 911;
  private static final int ID_GET_MONTH = 912;
  private static final int ID_GET_UTC_MONTH = 913;
  private static final int ID_GET_DATE = 914;
  private static final int ID_GET_UTC_DATE = 915;
  private static final int ID_GET_DAY = 916;
  private static final int ID_GET_UTC_DAY = 917;
  private static final int ID_GET_HOURS = 918;
  private static final int ID_GET_UTC_HOURS = 919;
  private static final int ID_GET_MINUTES = 920;
  private static final int ID_GET_UTC_MINUTES = 921;
  private static final int ID_GET_SECONDS = 922;
  private static final int ID_GET_UTC_SECONDS = 923;
  private static final int ID_GET_MILLISECONDS = 924;
  private static final int ID_GET_UTC_MILLISECONDS = 925;
  private static final int ID_GET_TIMEZONE_OFFSET = 926;
  private static final int ID_SET_TIME = 927;
  private static final int ID_SET_MILLISECONDS = 928;
  private static final int ID_SET_UTC_MILLISECONDS = 929;
  private static final int ID_SET_SECONDS = 930;
  private static final int ID_SET_UTC_SECONDS = 931;
  private static final int ID_SET_MINUTES = 932;
  private static final int ID_SET_UTC_MINUTES = 933;
  private static final int ID_SET_HOURS = 934;
  private static final int ID_SET_UTC_HOURS = 935;
  private static final int ID_SET_DATE = 936;
  private static final int ID_SET_UTC_DATE = 937;
  private static final int ID_SET_MONTH = 938;
  private static final int ID_SET_UTC_MONTH = 939;
  private static final int ID_SET_FULL_YEAR = 940;
  private static final int ID_SET_UTC_FULL_YEAR = 941;
  private static final int ID_TO_UTC_STRING = 942;
  private static final int ID_TO_GMT_STRING = 943;

  /**
   * Prototype for JS Date instances.
   */
  public static final JsObject DATE_PROTOTYPE = new JsDate(OBJECT_PROTOTYPE)
      .addVar("toDateString", new JsFunction(ID_TO_DATE_STRING, 0))
      .addVar("toTimeString", new JsFunction(ID_TO_TIME_STRING, 0))
      .addVar("toLocaleDateString", new JsFunction(ID_TO_LOCALE_DATE_STRING, 0))
      .addVar("toLocaleTimeString", new JsFunction(ID_TO_LOCALE_TIME_STRING, 0))
      .addVar("getTime", new JsFunction(ID_GET_TIME, 0))
      .addVar("getFullYear", new JsFunction(ID_GET_FULL_YEAR, 0))
      .addVar("getUTCFullYear", new JsFunction(ID_GET_UTC_FULL_YEAR, 0))
      .addVar("getMonth", new JsFunction(ID_GET_MONTH, 0))
      .addVar("getUTCMonth", new JsFunction(ID_GET_UTC_MONTH, 0))
      .addVar("getDate", new JsFunction(ID_GET_DATE, 0))
      .addVar("getUTCDate", new JsFunction(ID_GET_UTC_DATE, 0))
      .addVar("getDay", new JsFunction(ID_GET_DAY, 0))
      .addVar("getUTCDay", new JsFunction(ID_GET_UTC_DAY, 0))
      .addVar("getHours", new JsFunction(ID_GET_HOURS, 0))
      .addVar("getUTCHours", new JsFunction(ID_GET_UTC_HOURS, 0))
      .addVar("getMinutes", new JsFunction(ID_GET_MINUTES, 0))
      .addVar("getUTCMinutes", new JsFunction(ID_GET_UTC_MINUTES, 0))
      .addVar("getSeconds", new JsFunction(ID_GET_SECONDS, 0))
      .addVar("getUTCSeconds", new JsFunction(ID_GET_UTC_SECONDS, 0))
      .addVar("getMilliseconds", new JsFunction(ID_GET_MILLISECONDS, 0))
      .addVar("getUTCMilliseconds", new JsFunction(ID_GET_UTC_MILLISECONDS, 0))
      .addVar("getTimezoneOffset", new JsFunction(ID_GET_TIMEZONE_OFFSET, 0))
      .addVar("setTime", new JsFunction(ID_SET_TIME, 1))
      .addVar("setMilliseconds", new JsFunction(ID_SET_MILLISECONDS, 1))
      .addVar("setUTCMilliseconds", new JsFunction(ID_SET_UTC_MILLISECONDS, 1))
      .addVar("setSeconds", new JsFunction(ID_SET_SECONDS, 2))
      .addVar("setUTCSeconds", new JsFunction(ID_SET_UTC_SECONDS, 2))
      .addVar("setMinutes", new JsFunction(ID_SET_MINUTES, 3))
      .addVar("setUTCMinutes", new JsFunction(ID_SET_UTC_MINUTES, 3))
      .addVar("setHours", new JsFunction(ID_SET_HOURS, 1))
      .addVar("setUTCHours", new JsFunction(ID_SET_UTC_HOURS, 1))
      .addVar("setDate", new JsFunction(ID_SET_DATE, 1))
      .addVar("setUTCDate", new JsFunction(ID_SET_UTC_DATE, 1))
      .addVar("setMonth", new JsFunction(ID_SET_MONTH, 1))
      .addVar("setUTCMonth", new JsFunction(ID_SET_UTC_MONTH, 1))
      .addVar("setFullYear", new JsFunction(ID_SET_FULL_YEAR, 1))
      .addVar("setUTCFullYear", new JsFunction(ID_SET_UTC_FULL_YEAR, 1))
      .addVar("toUTCString", new JsFunction(ID_TO_UTC_STRING, 0))
      .addVar("toGMTString", new JsFunction(ID_TO_GMT_STRING, 0));

  Calendar time = Calendar.getInstance();

  /**
   * Constructs a new JS Date object
   */
  public JsDate(JsObject prototype) {
    super(prototype);
  }

  /**
   * Java implementation of JS date members.
   */
  public void evalNative(int index, JsArray stack, int sp, int parCount) {
    switch (index) {

      // static members

      case ID_TO_DATE_STRING:
      case ID_TO_LOCALE_DATE_STRING:
        stack.setObject(sp, toString(false, true, false));
        break;

      case ID_TO_TIME_STRING:
      case ID_TO_LOCALE_TIME_STRING:
        stack.setObject(sp, toString(false, false, true));
        break;

      case ID_VALUE_OF:
      case ID_GET_TIME:
        stack.setNumber(sp, time.getTime().getTime());
        break;

      case ID_GET_DATE:
        stack.setNumber(sp, get(false, Calendar.DAY_OF_MONTH));
        break;

      case ID_GET_FULL_YEAR:
        stack.setNumber(sp, get(false, Calendar.YEAR));
        break;

      case ID_GET_MONTH:
        stack.setNumber(sp, get(false, Calendar.MONTH));
        break;

      case ID_GET_DAY:
        stack.setNumber(sp, get(false, Calendar.DAY_OF_WEEK));
        break;

      case ID_GET_HOURS:
        stack.setNumber(sp, get(false, Calendar.HOUR_OF_DAY));
        break;

      case ID_GET_MINUTES:
        stack.setNumber(sp, get(false, Calendar.MINUTE));
        break;

      case ID_GET_SECONDS:
        stack.setNumber(sp, get(false, Calendar.SECOND));
        break;

      case ID_GET_MILLISECONDS:
        stack.setNumber(sp, get(false, Calendar.MILLISECOND));
        break;

      case ID_GET_UTC_FULL_YEAR:
        stack.setNumber(sp, get(true, Calendar.YEAR));
        break;

      case ID_GET_UTC_MONTH:
        stack.setNumber(sp, get(true, Calendar.MONTH));
        break;

      case ID_GET_UTC_DATE:
        stack.setNumber(sp, get(true, Calendar.DAY_OF_MONTH));
        break;

      case ID_GET_UTC_DAY:
        stack.setNumber(sp, get(true, Calendar.DAY_OF_WEEK));
        break;

      case ID_GET_UTC_HOURS:
        stack.setNumber(sp, get(true, Calendar.HOUR_OF_DAY));
        break;

      case ID_GET_UTC_MINUTES:
        stack.setNumber(sp, get(true, Calendar.MINUTE));
        break;

      case ID_GET_UTC_SECONDS:
        stack.setNumber(sp, get(true, Calendar.SECOND));
        break;

      case ID_GET_UTC_MILLISECONDS:
        stack.setNumber(sp, get(true, Calendar.MILLISECOND));
        break;

      case ID_GET_TIMEZONE_OFFSET:
        stack.setNumber(sp, TimeZone.getDefault().getOffset(
            time.getTime().getTime() >= 0 ? 1 : 0,
            time.get(Calendar.YEAR),
            time.get(Calendar.MONTH),
            time.get(Calendar.DAY_OF_MONTH),
            time.get(Calendar.DAY_OF_WEEK),
            time.get(Calendar.HOUR_OF_DAY) * 3600000
                + time.get(Calendar.MINUTE) * 60000
                + time.get(Calendar.SECOND) * 1000
                + time.get(Calendar.MILLISECOND)));
        break;

      case ID_SET_TIME:
        time.setTime(new Date((long) stack.getNumber(sp + 2)));
        break;

      case ID_SET_MILLISECONDS:
        setTime(
            false,
            Double.NaN,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2));
        break;

      case ID_SET_SECONDS:
        setTime(
            false,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3));
        break;

      case ID_SET_MINUTES:
        setTime(
            false,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4));
        break;

      case ID_SET_HOURS:
        setTime(
            false,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4),
            stack.getNumber(sp + 5));
        break;

      case ID_SET_DATE:
        setDate(
            false,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2));
        break;

      case ID_SET_MONTH:
        setDate(
            false,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3));
        break;

      case ID_SET_FULL_YEAR:
        setDate(
            false,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4));
        break;

      case ID_SET_UTC_MILLISECONDS:
        setTime(
            true,
            Double.NaN,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2));
        break;

      case ID_SET_UTC_SECONDS:
        setTime(
            true,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3));
        break;

      case ID_SET_UTC_MINUTES:
        setTime(
            true,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4));
        break;

      case ID_SET_UTC_HOURS:
        setTime(
            true,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4),
            stack.getNumber(sp + 5));
        break;

      case ID_SET_UTC_DATE:
        setDate(
            true,
            Double.NaN,
            Double.NaN,
            stack.getNumber(sp + 2));
        break;

      case ID_SET_UTC_MONTH:
        setDate(
            true,
            Double.NaN,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3));
        break;

      case ID_SET_UTC_FULL_YEAR:
        setDate(
            true,
            stack.getNumber(sp + 2),
            stack.getNumber(sp + 3),
            stack.getNumber(sp + 4));
        break;

      case ID_TO_UTC_STRING:
      case ID_TO_GMT_STRING:
        stack.setObject(sp, toString(true, true, true));
        break;

      default:
        super.evalNative(index, stack, sp, parCount);
    }
  }

  int get(boolean utc, int field) {
    time.setTimeZone(utc ? TimeZone.getTimeZone("GMT") : TimeZone.getDefault());
    int i = time.get(field);
    return field == Calendar.DAY_OF_WEEK ? i - 1 : i;
  }


  void setDate(boolean utc, double year, double month, double date) {
    time.setTimeZone(utc ? TimeZone.getTimeZone("GMT") : TimeZone.getDefault());

    if (!Double.isNaN(year)) {
      time.set(Calendar.YEAR, (int) year);
    }
    if (!Double.isNaN(month)) {
      time.set(Calendar.MONTH, (int) month);
    }
    if (!Double.isNaN(date)) {
      time.set(Calendar.DAY_OF_MONTH, (int) date);
    }
  }

  void setTime(boolean utc, double hours, double minutes, double seconds, double ms) {
    time.setTimeZone(utc ? TimeZone.getTimeZone("GMT") : TimeZone.getDefault());

    if (!Double.isNaN(hours)) {
      time.set(Calendar.HOUR_OF_DAY, (int) hours);
    }
    if (!Double.isNaN(minutes)) {
      time.set(Calendar.MINUTE, (int) minutes);
    }
    if (!Double.isNaN(seconds)) {
      time.set(Calendar.SECOND, (int) seconds);
    }
    if (!Double.isNaN(ms)) {
      time.set(Calendar.MILLISECOND, (int) ms);
    }
  }

  /**
   * Returns a string representation of the date stored in this object.
   *
   * @param utc if true, return time in UTC, otherwise as local time
   * @param date if true, the date is included in the output
   * @param time if true, the time is included in the output
   * @return the string representation of this date object
   */
  public String toString(boolean utc, boolean addDate, boolean addTime) {
    StringBuffer buf = new StringBuffer();

    if (addDate) {
      append(buf, utc, Calendar.YEAR);
      buf.append('-');
      append(buf, utc, Calendar.MONTH);
      buf.append('-');
      append(buf, utc, Calendar.DAY_OF_MONTH);

      if (addTime) {
        buf.append(' ');
      }
    }

    if (addTime) {
      append(buf, utc, Calendar.HOUR_OF_DAY);
      buf.append(':');
      append(buf, utc, Calendar.MINUTE);
      buf.append(':');
      append(buf, utc, Calendar.SECOND);

      if (utc && addDate) {
        buf.append(" GMT");
      }
    }

    return buf.toString();
  }

  /**
   * Helper method to append two-digit numbers to a string buffer.
   * @param buf The target buffer to write to
   * @param utc utc if true, local time zone if false
   * @param field the value to append
   */
  private void append(StringBuffer buf, boolean utc, int field) {
    int i = get(utc, field);
    if (i < 10) {
      buf.append('0');
    }
    buf.append(i);
  }

  /**
   * Returns a string representation of this date object (including both,
   * date and time).
   */
  public String toString() {
    return toString(true, true, true);
  }
}
