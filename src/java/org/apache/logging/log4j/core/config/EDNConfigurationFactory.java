/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.net.URI;
import java.util.List;

@Plugin(name = "EDNConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(7)
public class EDNConfigurationFactory extends ConfigurationFactory {

    private static ConfigurationFactory configFactory = new Factory();
    protected static final String ALT_CONFIG_NAME = "config.edn";
    protected static final String ALT_CONFIG_PROP_NAME = "conf";
    /**
     * The file extensions supported by this factory.
     */
    private static final String[] SUFFIXES = new String[]{".edn"};

    private static final String[] dependencies = new String[]{
            "com.fasterxml.jackson.databind.ObjectMapper",
            "com.fasterxml.jackson.databind.JsonNode",
            "com.fasterxml.jackson.core.JsonParser"
    };

    private final boolean isActive;

    private boolean isAltConfigFile() {
        ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
        return null != loader.getResource(ALT_CONFIG_NAME);

    }

    private boolean isAltConfigProp() {
        return null != this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty(ALT_CONFIG_PROP_NAME));
    }

    public EDNConfigurationFactory() {
        for (final String dependency : dependencies) {
            if (!Loader.isClassAvailable(dependency)) {
                LOGGER.debug("Missing dependencies for EDN support");
                isActive = false;
                return;
            }
        }

        if (isAltConfigProp() || isAltConfigFile()) {
            ConfigurationFactory.setConfigurationFactory(configFactory);
        }

        isActive = true;
    }

    @Override
    protected boolean isActive() {
        return isActive;
    }

    @Override
    public Configuration getConfiguration(final ConfigurationSource source) {
        if (!isActive) {
            return null;
        }
        return configFactory.getConfiguration(source);
    }

    @Override
    public Configuration getConfiguration(final String name, final URI configLocation) {
        if (!isActive()) return null;
        return configFactory.getConfiguration(name, configLocation);
    }


    @Override
    public String[] getSupportedTypes() {
        return SUFFIXES;
    }

    private static class Factory extends ConfigurationFactory {

        private Configuration getConfiguration() {
            ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
            ConfigurationSource source;
            String fileName;

            fileName = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty(CONFIGURATION_FILE_PROPERTY));
            if (null == fileName)
                fileName = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty(ALT_CONFIG_PROP_NAME));

            source = (fileName == null) ? null : this.getInputFromUri(NetUtils.toURI(fileName));

            if (source == null) {
                source = this.getInputFromResource(DEFAULT_PREFIX + ".edn", loader);
                if (null == source) source = this.getInputFromResource(ALT_CONFIG_NAME, loader);
            }
            return (source == null) ? null : getConfiguration(source);
        }

        @Override
        public Configuration getConfiguration(final String name, final URI configLocation) {
            if (!isActive()) return null;
            return getConfiguration();
        }

        @Override
        public String[] getSupportedTypes() {
            return SUFFIXES;
        }

        @Override
        public Configuration getConfiguration(final ConfigurationSource source) {
            if (!isActive()) return null;
            return new EDNConfiguration(source);
        }
    }
}
