/*
 * MJVM - Copyright (C) 2009  Igor Maznitsa www.igormaznitsa.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.igormaznitsa.mjvm;

import com.igormaznitsa.mjvm.MJVMClass.ClassField;
import com.igormaznitsa.mjvm.MJVMClass.ClassMethod;
import java.util.Enumeration;

/**
 * The class describes a storage for instance fields of a MJVM class
 *
 * @author Igor Maznitsa (igor.maznitsa@igormaznitsa.com)
 * @version 1.01
 */
public final class MJVMObject
{

    /**
     * The flag shows that the instance has been finalized
     */
    protected boolean lg_isFinalized;

    /**
     * The variable contains the owner MJVMClass object
     * @see MJVMClass
     */
    protected MJVMClass p_Class;
    /**
     * The array contans values of fields
     */
    protected Object[] ap_Fields;
    /**
     * a Thread object owns the object, the variable is used for locking operations
     */
    protected volatile Thread p_ThreadOwner;
    /**
     * the lock counter, the variable is used for locking operations
     */
    protected volatile int i_LockCounter;

    /**
     * Get the thread owner which has locked the object
     * @return a Thread which is the current object owner or null
     */
    public Thread getThreadOwner()
    {
        return p_ThreadOwner;
    }

    /**
     * To get the current value of the lock counter
     * @return the current lock counter value as integer
     */
    public int getLockCounter()
    {
        return i_LockCounter;
    }

    /**
     * Get a field value for an index
     * @param _index the index of a field
     * @return the value of the field as an Object
     */
    public Object getFieldForIndex(int _index)
    {
        return ap_Fields[_index];
    }

    /**
     * Set a value in a field
     * @param _index the index of a field
     * @param _object the object which will be placed in the field
     */
    public void setFieldForIndex(int _index, Object _object)
    {
        ap_Fields[_index] = _object;
    }

    /**
     * Set a value in a field for the UID
     * @param _uid the UID of a field as Integer, must not be null
     * @param _object an object which will be placed in the field
     */
    public void setFieldForUID(Integer _uid, Object _object)
    {
        ClassField p_field = (ClassField) p_Class.p_FieldsTable.get(_uid);
        ap_Fields[p_field.i_FieldIndex] = _object;
    }

    /**
     * Get the value from a field
     * @param _uid the UID of the field as Integer, must not be null
     * @return the field value as Object or null
     */
    public Object getFieldForUID(Integer _uid)
    {
        ClassField p_field = (ClassField) p_Class.p_FieldsTable.get(_uid);
        return ap_Fields[p_field.i_FieldIndex];
    }

    /**
     * The constructor, it is protected because a user must not create it directly
     * @param _class the owner MJVMClass for the object
     * @throws java.lang.Throwable will be thrown if it is impossible to create new object
     * @see MJVMClass
     */
    protected MJVMObject(MJVMClass _class) throws Throwable
    {
        p_Class = _class;
        ap_Fields = new Object[_class.p_FieldsTable.size()];

        // init constants
        Enumeration p_iter = _class.p_FieldsTable.elements();
        while (p_iter.hasMoreElements())
        {
            ClassField p_field = (ClassField) p_iter.nextElement();
            if ((p_field.getFlags() & ClassField.FIELD_FLAG_STATIC) == 0)
            {
                ap_Fields[p_field.i_FieldIndex] = p_field.getConstantValue();
            }
        }
    }

    /**
     * To get the class which has created the object 
     * @return a MJVMClass object
     */
    public MJVMClass getMJVMClass()
    {
        return p_Class;
    }

    /**
     * Check the state of 
     * @return
     */
    public boolean isFinalized()
    {
        return lg_isFinalized;
    }

    /**
     * Finalize the instance
     */
    public void finalizeInstance()
    {
        if (lg_isFinalized)
        {
            throw new IllegalStateException("Already finalized");
        }

        try
        {
            ClassMethod p_finalize = p_Class.findMethodForName("finalize", "()V");
            if (p_finalize != null)
            {
                // we have found the finalize method
                p_Class.invoke(p_finalize, this, null);
            }
        }
        catch (Throwable _thr)
        {
            _thr.printStackTrace();
        }
        finally
        {
            lg_isFinalized = true;
            ap_Fields = null;
            p_Class = null;
        }
    }

    /**
     * Lock the object for a thread (which will become its owner until an unlock operation), if the object is already locked the function will be waiting its unblocking
     * @param _thread the thread which want to take the control over the object, must not be null
     */
    protected void lock(Thread _thread)
    {
        synchronized (this)
        {
            if (p_ThreadOwner != null)
            {
                if (p_ThreadOwner.equals(_thread))
                {
                    // just increase lock counter
                    i_LockCounter++;
                    return;
                }
            }
        }

        while (true)
        {
            synchronized (this)
            {
                if (p_ThreadOwner == null)
                {
                    p_ThreadOwner = _thread;
                    i_LockCounter = 1;
                    return;
                }
            }
            Thread.yield();
        }
    }

    /**
     * The function unlocks the object nad it can be locked by a waiting thread
     * @param _thread a thread which is current owner of the object, must not be null
     */
    protected void unlock(Thread _thread)
    {
        synchronized (this)
        {
            if (p_ThreadOwner == null)
            {
                i_LockCounter = 0;
                return;
            }
            else
            {
                if (p_ThreadOwner.equals(_thread))
                {
                    i_LockCounter--;
                    if (i_LockCounter<=0)
                    {
                        p_ThreadOwner = null;
                        i_LockCounter = 0;
                        return;
                    }
                }
                else
                {
                    throw new IllegalMonitorStateException("Illegal monitor thread owner");
                }
            }
        }
    }
}
