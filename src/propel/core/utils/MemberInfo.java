/*
///////////////////////////////////////////////////////////
//  This file is part of Propel.
//
//  Propel is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  Propel is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with Propel.  If not, see <http://www.gnu.org/licenses/>.
///////////////////////////////////////////////////////////
//  Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
///////////////////////////////////////////////////////////
 */
package propel.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A class encapsulating a member, i.e. a property, field, method or constructor
 */
public final class MemberInfo
        implements Comparable<MemberInfo>
{

   private String name;
   private Class<?> declaringType;
   private Class<?> memberType;
   private PropertyInfo propertyInfo;
   private Field fieldInfo;
   private Method methodInfo;
   private Constructor<?> constructorInfo;

   /**
    * Initializes with property information.
    *
    * @throws NullPointerException	 An argument is null
    */
   public MemberInfo(PropertyInfo pi)
   {
      if (pi == null)
         throw new NullPointerException("pi");

      this.name = pi.getName();
      this.declaringType = pi.getDeclaringType();
      this.memberType = pi.getPropertyType();
      this.propertyInfo = pi;
   }

   /**
    * Initializes with method information.
    *
    * @throws NullPointerException	 An argument is null
    */
   public MemberInfo(Method mi)
   {
      if (mi == null)
         throw new NullPointerException("mi");

      this.name = mi.getName();
      this.declaringType = mi.getDeclaringClass();
      this.memberType = null; // methods have no 'backing' type
      this.methodInfo = mi;
   }

   /**
    * Initializes with constructor information.
    *
    * @throws NullPointerException	 An argument is null
    */
   public MemberInfo(Constructor<?> ci)
   {
      if (ci == null)
         throw new NullPointerException("ci");

      this.name = ci.getName();
      this.declaringType = ci.getDeclaringClass();
      this.memberType = null; // constructors have no 'backing' type
      this.constructorInfo = ci;
   }

   /**
    * Initializes with field information.
    *
    * @throws NullPointerException	 An argument is null
    */
   public MemberInfo(Field fi)
   {
      if (fi == null)
         throw new NullPointerException("fi");

      this.name = fi.getName();
      this.declaringType = fi.getDeclaringClass();
      this.memberType = fi.getType();
      this.fieldInfo = fi;
   }

   /**
    * Returns the name of the property, e.g. for getName() and setName(), the property is Name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Returns the class which declares this property
    */
   public Class<?> getDeclaringType()
   {
      return declaringType;
   }

   /**
    * Returns the member type
    */
   public Class<?> getMemberType()
   {
      return memberType;
   }

   /**
    * Returns true if this member is a field
    */
   public boolean isField()
   {
      return fieldInfo != null;
   }

   /**
    * Returns true if this member is a property
    */
   public boolean isProperty()
   {
      return propertyInfo != null;
   }

   /**
    * Returns true if this member is a method
    */
   public boolean isMethod()
   {
      return methodInfo != null;
   }

   /**
    * Returns true if this member is a constructor
    */
   public boolean isConstructor()
   {
      return constructorInfo != null;
   }

   public Field getFieldInfo()
   {
      return fieldInfo;
   }

   public Method getMethodInfo()
   {
      return methodInfo;
   }

   public PropertyInfo getPropertyInfo()
   {
      return propertyInfo;
   }

   public Constructor<?> getConstructorInfo()
   {
      return constructorInfo;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return getName();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(MemberInfo other)
   {
      return name.compareTo(other.getName());
   }
}
