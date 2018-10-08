package com.ritto.srm.util;

import java.lang.reflect.Field;
/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/13
 * @Description:
 * @Modified By:
 */

public interface FieldFilter
{
    boolean accept(Field field);
}
