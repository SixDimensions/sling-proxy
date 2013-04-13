/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.proxy.impl.to;

import java.lang.reflect.Method;

import org.apache.sling.commons.proxy.annotations.SlingProperty;
import org.apache.sling.commons.proxy.impl.lang.MethodType;

/**
 * InvokedPropertyTO - Transfer Object Meant as a convenient way to maintain and
 * pass a method invocation around The members are not private because the class
 * is package protected and they are final.
 */
public final class InvokedPropertyTO extends BaseInvokedTO {

	/** The default bytes. */
	private final byte[] defaultBytes;

	/** The default date. */
	private final long defaultDate;

	/** The default double. */
	private final double defaultDouble;

	/** The default long. */
	private final long defaultLong;

	/** The default string. */
	private final String defaultString;

	/** The default strings. */
	private final String[] defaultStrings;

	/** The property name specified in the annotation. */
	private final String name;

	/**
	 * The full property name, calculated from the path and name from the
	 * annotation parameters.
	 */
	private final String propertyName;

	/** The use default flag. */
	private final boolean useDefault;

	private final boolean defaultBoolean;

	/**
	 * Constructs a new Invoked Transfer Object.
	 * 
	 * @param method
	 *            the invoked method
	 * @param args
	 *            the method arguments
	 * @param path
	 *            the path annotation value
	 * @param name
	 *            the name annotation value
	 * @param mt
	 *            the type of method invoked
	 */
	protected InvokedPropertyTO(final Method method, final Object[] args,
			final String path, final String name, final MethodType mt) {
		super(method, path, mt);
		this.name = name;
		this.propertyName = (path != null ? path + "/" : "") + name;

		final SlingProperty sp = method.getAnnotation(SlingProperty.class);
		this.defaultBoolean = sp.defaultBoolean();
		this.defaultBytes = sp.defaultBytes();
		this.defaultDate = sp.defaultDate();
		this.defaultDouble = sp.defaultDouble();
		this.defaultLong = sp.defaultLong();
		this.defaultString = sp.defaultString();
		this.defaultStrings = sp.defaultStrings();
		this.useDefault = sp.useDefault();

	}

	public boolean getDefaultBoolean() {
		return this.defaultBoolean;
	}

	/**
	 * Gets the default bytes.
	 * 
	 * @return the default bytes
	 */
	public byte[] getDefaultBytes() {
		return this.defaultBytes;
	}

	/**
	 * Gets the default date.
	 * 
	 * @return the default date
	 */
	public long getDefaultDate() {
		return this.defaultDate;
	}

	/**
	 * Gets the default double.
	 * 
	 * @return the default double
	 */
	public double getDefaultDouble() {
		return this.defaultDouble;
	}

	/**
	 * Gets the default long.
	 * 
	 * @return the default long
	 */
	public long getDefaultLong() {
		return this.defaultLong;
	}

	/**
	 * Gets the default string.
	 * 
	 * @return the default string
	 */
	public String getDefaultString() {
		return this.defaultString;
	}

	/**
	 * Gets the default strings.
	 * 
	 * @return the default strings
	 */
	public String[] getDefaultStrings() {
		return this.defaultStrings;
	}

	/**
	 * Get the name annotation value.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the property name to retrieve, this is calculated from the annotation
	 * name and path.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Checks if is use default.
	 * 
	 * @return true, if is use default
	 */
	public boolean isUseDefault() {
		return this.useDefault;
	}
}
