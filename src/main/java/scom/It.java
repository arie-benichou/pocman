/**
 * It.java Core and Single class of SCOM project NB: Shortest name because 1.
 * used widely by client code for it's constants (e.g: It.NIL) 2. used as a
 * prefix for class names (like in 'hungarian notation') e.g: ItCons class in
 * List interpreter sample NB: originally 'ScomIt', a mix between 'Scom' and
 * 'comit' (The first string-handling and pattern-matching language) see:
 * http://en.wikipedia.org/wiki/COMIT
 * .............................................
 * ...................................... SCOM: Single Class Object Model
 * (http://code.google.com/p/scom/) Licence: MIT
 * (http://en.wikipedia.org/wiki/MIT_License) Michel Kern - 8 may 2012 - 16:34
 * Copyright (C) <2012> www.terimakasi.com
 * .......................................
 * ............................................ Permission is hereby granted,
 * free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this
 * permission notice shall be included in all copies or substantial portions of
 * the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * ......................................................
 * .............................
 */

package scom;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class It implements java.util.Map.Entry<Object, Object>
{

    public final String CLASS_NAME = "scom.ScomIt";

    public static final String K_NAME = "name";
    public static final String K_FUNCTION = "function";
    public static final String K_VALUE = "value";
    public static final String K_CLASS = "class";

    public static final String NIL_KV = "NIL";
    public static It NIL = new It(NIL_KV, NIL_KV, NIL_KV);

    public static final String ENVIRONMENT_KV = "Environment";
    public static It ENVIRONMENT = new It(ENVIRONMENT_KV, ENVIRONMENT_KV, NIL_KV);

    protected HashMap<It, It> _connections = new HashMap<It, It>();
    protected HashMap<String, It> _kv2it = new HashMap<String, It>();
    protected final Object _key;
    protected Object _value;
    protected Object _option;
    protected final UUID _uuid = UUID.randomUUID();

    //*** Private Constructor: API clients must use factory method 'New' instead
    protected It(final Object key, final Object value, final Object option)
    {
        this._key = key;
        this._value = value;
        this._option = option;
        this.init();
    } // Private Constructor

    //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
    static public It New(final Object key, final Object value)
    {
        return New(key, value, It.NIL);
    } //---- New()

    //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
    static public It New(final Object key, final Object value, final Object option)
    {
        It item = isNIL(key, value);
        if (item == null)
            item = new It(key, value, option);
        return item;
    } //---- New()

    //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
    static public It New(final Object key, final Object value, final String class_name)
    {
        return New(key, value, It.NIL, class_name);
    } //---- New()

    static public It New(final Object key, final Object value, final Object option, final String class_name)
    {
        final It is_nil = isNIL(key, value);
        //System.out.println("> ScomIt.New class_name = " + class_name);
        //System.out.println("  key:" + key + " value:" + value + " option:" + option);

        if (is_nil == null)
        {
            try
            {
                final Class cls = Class.forName(class_name);
                //System.out.println("   cls = " + cls.getName());

                final Constructor constructor = cls.getDeclaredConstructor(new Class[] { Object.class, Object.class, Object.class });
                constructor.setAccessible(true);
                return (It) constructor.newInstance(new Object[] { key, value, option });
            }
            catch (final Exception e)
            {
                System.out.println("*** Exception in ScomItem.New *** " + e.getMessage());
                return It.NIL;
            }
        }
        return It.NIL;
    } //---- New()

    static protected It isNIL(final Object key, final Object value)
    {
        if (key == null || value == null)
            return It.NIL;

        String key_str = key.toString().toUpperCase();
        String value_str = value.toString().toUpperCase();

        //---- special test when key/value is ScomIt NIL ----
        try
        {
            final It key_it = (It) key;
            if (key_it != null)
                key_str = key_it.getKey().toString();
        }
        catch (final Exception e) {}

        try
        {
            final It value_it = (It) value;
            if (value_it != null)
                value_str = value_it.getKey().toString();
        }
        catch (final Exception e) {}
        //----

        if (key_str == It.NIL_KV && value_str == It.NIL_KV)
            return It.NIL;

        return null;
    } //---- isNIL()

    private static String keyValue(final Object key, final Object value)
    {
        return "[" + key.toString() + "," + value.toString() + "]";
    } //---- KeyValuePair

    public void connect(final String relation_name, final It target)
    {
        final It relation = New(It.K_NAME, relation_name);
        this.connect(relation, target);
    } //---- connect

    public void connect(final It relation, final It target)
    {
        //System.out.println("> connect relation=" + relation.getValue() + " target=" + target.getValue());
        if (this._connections.containsKey(relation))
        {
            final It previous_target = this._connections.get(relation);
            this._connections.remove(relation);
            this._kv2it.remove(keyValue(relation._key, relation._value));
            this._kv2it.remove(keyValue(previous_target._key, previous_target._value));
        }
        this._connections.put(relation, target);
        this._kv2it.put(keyValue(relation._value, this._value), relation);
        this._kv2it.put(keyValue(target._value, this._value), target);
    } //---- connectWith()  

    public It evaluate()
    {
        return this.evaluate(null);
    } //---- evaluate() 

    public It evaluate(final ArrayList<It> input)
    {
        return this;
    } //---- evaluate() 

    static public It buildValue(final String value)
    {
        return It.New(It.K_VALUE, value);
    } //---- buildValue()

    static public ArrayList<It> asList(final Object arg)
    {
        return asList(new Object[] { arg });
    } //---- asList()

    static public ArrayList<It> asList(final Object[] args)
    {
        final ArrayList<It> arg_list = new ArrayList<It>();
        for (int i = 0; i < args.length; i++)
        {
            It item = null;
            try
            {
                item = (It) args[i];
            }
            catch (final Exception e) {}

            if (item == null) arg_list.add(It.New(It.K_VALUE, args[i]));
            else arg_list.add(item);
        }
        return arg_list;
    } //---- asList()

    protected void init()
    {} //---- init

    /*
    public It getIt(String relation_name)
    { 
      It it = _kv2it.get(relation_name);
      return getIt(it);
    } //---- getIt
    */

    public It getIt(final Object relation)
    {
        final String key_value = keyValue(relation, this.getValue());
        final It it = this._kv2it.get(key_value);
        return this.getIt(it);
    } //---- getIt

    public It getIt(final It it)
    {
        if (this._connections.containsKey(it))
        {
            final It relation = this._connections.get(it);
            return relation;
        }
        return It.NIL;
    } //---- getIt

    public It getFunction(final String class_name)
    {
        return It.New(It.K_FUNCTION, class_name, class_name);
    } //---- getFunction

    public Object getOption() {
        return this._option;
    }

    public HashMap<It, It> getConnections()
    {
        return this._connections;
    } //---- getConnections()

    @Override
    public String toString() {
        return this._value.toString();
    }

    public Object getKey() {
        return this._key;
    }

    public Object getValue() {
        return this._value;
    }

    public Object setValue(final Object new_value)
    {
        final Object old = this._value;
        this._value = new_value;
        return old;
    } //---- setValue()
} //---------- It