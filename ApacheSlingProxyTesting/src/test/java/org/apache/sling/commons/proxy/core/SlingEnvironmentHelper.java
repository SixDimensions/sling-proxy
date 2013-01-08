/*
 * Copyright 2012 Six Dimensions.
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
package org.apache.sling.commons.proxy.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Dictionary;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.log.internal.config.ConfigurationException;
import org.apache.sling.jcr.resource.internal.JcrResourceResolverFactoryImpl;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher - Dec 25, 2012 11:09:53 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.EnvironmentSetup
 */
public final class SlingEnvironmentHelper {

    public static final String USER = "javax.jcr.Session.user";
    public static final String PASSWORD = "javax.jcr.Session.password";
    public static final String REPO_URL = "javax.jcr.Session.url";

    private SlingEnvironmentHelper() {
    }

    /**
     * Sets the System Properties for: USER, PASSWORD, and REPO_URL to default
     * values of: admin, xxxxxx, http://kelleher.dyndns.org:4502
     */
    public static void setUp() {
        setConfiguration(null, null, null);
    }

    /**
     * Sets the System Properties for: USER, PASSWORD, and REPO_URL to default
     * values of: user, password, repositoryURL
     *
     * @param user String - The System default value for USER
     * @param password String - The System default value for PASSWORD
     * @param repositoryURL String - The System default value for REPO_URL
     */
    public static void setUp(String user, String password, String repositoryURL) {
        setConfiguration(user, password, repositoryURL);
    }

    public static void tearDown() {
    }

    /**
     * Default Logging is set to INFO Level, if you need something lower than
     * that, (DEBUG or TRACE) - use this method to reconfigure logging for the
     * logNames you provide. Otherwise this method need not be called.
     *
     * This is most beneficial if you are testing locally and want to see some
     * messages in the console.
     *
     * @param logLevel String - The LOG Level: TRACE, DEBUG, INFO, WARN, ERROR
     * @param logNames String[] - The package names of the classes to set the
     * logging level for
     * @throws ConfigurationException
     */
    public static void configureLogging(String logLevel, String... logNames) {
        Dictionary<String, String> dict = new java.util.Hashtable<String, String>();
        dict.put("org.apache.sling.commons.log.level", logLevel);
        org.apache.sling.commons.log.internal.slf4j.LogConfigManager mgr = getConfigManager();
        for (String logName : logNames) {
            dict.put("org.apache.sling.commons.log.names", logName);
            try {
                mgr.updateLoggerConfiguration(logName, dict);
            } catch (ConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static org.apache.sling.commons.log.internal.slf4j.LogConfigManager getConfigManager() {
        return (org.apache.sling.commons.log.internal.slf4j.LogConfigManager) LoggerFactory.getILoggerFactory();
    }

    public static final Resource getResource(String user, String password,
            String resource) throws RepositoryException, URISyntaxException {
        Session s = newSession(user, password);
        /**
         * This is for documentations sake.
         *
         * org.apache.sling.jcr.resource.internal.helper.jcr.JcrResourceProvider
         */
        //JcrResourceProvider p = new JcrResourceProvider(s, RepositoryUtil.class.getClassLoader(), true);
        JcrResourceResolverFactoryImpl rrf = new JcrResourceResolverFactoryImpl();
        ResourceResolver rr = rrf.getResourceResolver(s);
        Resource r = rr.getResource(resource);
        return r;
    }

    public static final Session newSession(String user, String password)
            throws RepositoryException, URISyntaxException {
        Repository r = JcrUtils.getRepository(getURI().toString());

        Session sess = r.login(getCredentials(user, password));
        return sess;
    }

    private static final Credentials getCredentials(String user, String password) {
        user = (user == null ? System.getProperty(USER) : user);
        password = (password == null ? System.getProperty(PASSWORD, password) : password);
        return new SimpleCredentials(user, password.toCharArray());
    }

    private static final URI getURI() throws URISyntaxException {
        String url = System.getProperty(REPO_URL, "http://localhost:6502");
        URI uri = new URI(url);
        return uri.resolve("/crx/server");
    }

    private static final void setConfiguration(String user, String password, String repositoryURL) {
        System.setProperty(USER, get(user, "admin"));
        System.setProperty(PASSWORD, get(password, "run_Abb1e_run"));
        System.setProperty(REPO_URL, get(repositoryURL, "http://kelleher.dyndns.org:4502"));
    }

    private static final String get(String value, String defaultIfNull) {
        return (value == null ? defaultIfNull : value);
    }
}
