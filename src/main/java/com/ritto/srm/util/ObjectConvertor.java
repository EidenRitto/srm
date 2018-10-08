package com.ritto.srm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/13
 * @Description:
 * @Modified By:
 */
public class ObjectConvertor
{

    public <K,V> V toAnotherObj(K oneObj, Class<V> anotherClassObj)
    {
        return toAnotherObj(oneObj, anotherClassObj, null);
    }

    public <K,V> V toAnotherObj(K oneObj, Class<V> anotherClassObj, FieldFilter filter)
    {
        V anotherObj = null;
        try
        {
            anotherObj = anotherClassObj.newInstance();
            Class<?> oneClassObj = oneObj.getClass();
            Field[] fields = getMatchedFields(oneClassObj, filter);
            for (Field field : fields)
            {
                String fieldName = field.getName();
                String append = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                String getMethodName = "get" + append;
                String setMethodName = "set" + append;
                Method getMethod = oneClassObj.getDeclaredMethod(getMethodName);
                getMethod.setAccessible(true);
                Object attrForOneObj = getMethod.invoke(oneObj);
                Method setMethod = anotherClassObj.getDeclaredMethod(setMethodName, field.getType());
                setMethod.setAccessible(true);
                setMethod.invoke(anotherObj, attrForOneObj);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return anotherObj;
    }

    private Field[] getMatchedFields(Class<?> classObj, FieldFilter filter)
    {
        List<Field> matchedFields = new ArrayList<Field>();
        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields)
        {
            if (null == filter || filter.accept(field))
            {
                matchedFields.add(field);
            }
        }

        return matchedFields.toArray(new Field[] {});
    }
}
