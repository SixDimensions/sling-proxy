/*
 * Copyright 2013 Six Dimensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.sling.commons.proxy.core.impl;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Jan 9, 2013 11:31:26 PM
 *
 * Apache Sling Proxy Core
 *
 *
 * org.apache.sling.commons.proxy.core.impl.ProxyBundleActivator
 */
public class ProxyBundleActivator implements BundleActivator {
    private static final Logger LOG = 
            LoggerFactory.getLogger(ProxyBundleActivator.class);
    
    private static BundleContext BUNDLE_CONTEXT;
    private static ReentrantReadWriteLock LOCK;
    
    public static BundleContext getBundleContext() {
        LOCK.readLock().lock();
        try {
            return BUNDLE_CONTEXT;
        } finally {
            LOCK.readLock().unlock();
        }
    }
    public void start(BundleContext bc) throws Exception {
        LOCK.writeLock().lock();
        try {
            BUNDLE_CONTEXT = bc;
        } finally {
            LOCK.readLock().unlock();
        }
        LOG.debug("Starting Bundle and set BundleContext");
    }

    public void stop(BundleContext bc) throws Exception {
        LOCK.writeLock().lock();
        try {
            BUNDLE_CONTEXT = null;
        } finally {
            LOCK.readLock().unlock();
        }
        LOG.debug("Stopping Bundle and clearing BundleContext");
    }


}
