/*
 * Copyright (c) 2017-2018 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.base.config;

import java.time.Duration;
import java.util.Objects;

import org.eclipse.ditto.services.base.config.ServiceSpecificConfig.HealthCheckConfig;
import org.eclipse.ditto.services.utils.config.ConfigWithFallback;
import org.eclipse.ditto.services.utils.config.KnownConfigValue;

import com.typesafe.config.Config;

/**
 * This class is the default implementation of {@link HealthCheckConfig}.
 */
public final class DefaultHealthCheckConfig implements HealthCheckConfig {

    private enum HealthCheckConfigValue implements KnownConfigValue {

        ENABLED("enabled", true),

        INTERVAL("interval", Duration.ofMinutes(1L)),

        PERSISTENCE_ENABLED("persistence.enabled", false),

        PERSISTENCE_TIMEOUT("persistence.timeout", Duration.ofMinutes(1));

        private final String path;
        private final Object defaultValue;

        private HealthCheckConfigValue(final String thePath, final Object theDefaultValue) {
            path = thePath;
            defaultValue = theDefaultValue;
        }

        @Override
        public String getConfigPath() {
            return path;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

    }

    private static final String CONFIG_PATH = "health-check";

    private final Config config;

    private DefaultHealthCheckConfig(final Config theConfig) {
        config = theConfig;
    }

    /**
     * Returns an instance of {@code DefaultHealthCheckConfig} based on the settings of the specified Config.
     *
     * @param config is supposed to provide the settings of the health check config at {@value #CONFIG_PATH}.
     * @return the instance.
     * @throws org.eclipse.ditto.services.utils.config.DittoConfigError if {@code config} is {@code null} if the
     * value of {@code config} at {@code configPath} is not of type
     * {@link com.typesafe.config.ConfigValueType#OBJECT}.
     */
    public static DefaultHealthCheckConfig of(final Config config) {
        return new DefaultHealthCheckConfig(
                ConfigWithFallback.newInstance(config, CONFIG_PATH, HealthCheckConfigValue.values()));
    }

    @Override
    public boolean isEnabled() {
        return config.getBoolean(HealthCheckConfigValue.ENABLED.getConfigPath());
    }

    @Override
    public Duration getInterval() {
        return config.getDuration(HealthCheckConfigValue.INTERVAL.getConfigPath());
    }

    @Override
    public boolean isPersistenceEnabled() {
        return config.getBoolean(HealthCheckConfigValue.PERSISTENCE_ENABLED.getConfigPath());
    }

    @Override
    public Duration getPersistenceTimeout() {
        return config.getDuration(HealthCheckConfigValue.PERSISTENCE_TIMEOUT.getConfigPath());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DefaultHealthCheckConfig that = (DefaultHealthCheckConfig) o;
        return config.equals(that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                "config=" + config +
                "]";
    }

}
