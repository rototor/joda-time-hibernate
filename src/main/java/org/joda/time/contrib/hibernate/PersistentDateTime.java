/*
 *  Copyright 2001-2012 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.contrib.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Persist {@link org.joda.time.DateTime} via hibernate.
 * 
 * @author Mario Ivankovits (mario@ops.co.at)
 */
public class PersistentDateTime implements EnhancedUserType, Serializable {

    public static final PersistentDateTime INSTANCE = new PersistentDateTime();

    private static final int[] SQL_TYPES = new int[] { Types.TIMESTAMP, };

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return DateTime.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        DateTime dtx = (DateTime) x;
        DateTime dty = (DateTime) y;

        return dtx.equals(dty);
    }

    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }

    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor session, Object object) throws HibernateException, SQLException {
        Object timestamp = StandardBasicTypes.TIMESTAMP.nullSafeGet(resultSet, strings[0],session);
        if (timestamp == null) {
            return null;
        }

        return new DateTime(timestamp);
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, null, index,session);
        } else {
            StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, ((DateTime) value).toDate(), index,session);
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object value) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public String objectToSQLString(Object object) {
        throw new UnsupportedOperationException();
    }

    public String toXMLString(Object object) {
        return object.toString();
    }

    public Object fromXMLString(String string) {
        return new DateTime(string);
    }

}
