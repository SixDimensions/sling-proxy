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
package org.apache.sling.commons.proxy.impl;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Activator for the Sling Proxy bundle.
 */
public class ProxyBundleActivator implements BundleActivator {
	private static final Logger log = LoggerFactory
			.getLogger(ProxyBundleActivator.class);
	private static BundleContext BUNDLE_CONTEXT;
	private static ReentrantReadWriteLock LOCK;

	/**
	 * Get the bundle context. Will lock until the bundle context is returned.
	 * 
	 * @return the bundle context
	 */
	public static BundleContext getBundleContext() {
		LOCK.readLock().lock();
		try {
			return BUNDLE_CONTEXT;
		} finally {
			LOCK.readLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bc) throws Exception {
		LOCK.writeLock().lock();
		try {
			BUNDLE_CONTEXT = bc;
		} finally {
			LOCK.readLock().unlock();
		}
		log.debug("Starting Bundle and set BundleContext");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bc) throws Exception {
		LOCK.writeLock().lock();
		try {
			BUNDLE_CONTEXT = null;
		} finally {
			LOCK.readLock().unlock();
		}
		log.debug("Stopping Bundle and clearing BundleContext");
	}

}
