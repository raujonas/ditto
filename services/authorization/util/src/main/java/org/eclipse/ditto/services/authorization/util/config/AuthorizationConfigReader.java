/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 */
package org.eclipse.ditto.services.authorization.util.config;

import java.util.function.Function;

import org.eclipse.ditto.services.base.config.AbstractServiceConfigReader;

import com.typesafe.config.Config;

/**
 * Configuration reader for authorization service.
 */
public final class AuthorizationConfigReader extends AbstractServiceConfigReader {

    private static final String PATH_CACHES = "caches";

    private AuthorizationConfigReader(final Config config, final String serviceName) {
        super(config, serviceName);
    }

    /**
     * Create configuration reader for authorization service.
     *
     * @param serviceName name of the authorization service.
     * @return function to create an authorization service configuration reader.
     */
    public static Function<Config, AuthorizationConfigReader> from(final String serviceName) {
        return config -> new AuthorizationConfigReader(config, serviceName);
    }

    /**
     * Retrieve configuration reader of caches.
     *
     * @return the configuration reader.
     */
    public CachesConfigReader caches() {
        return new CachesConfigReader(getChild(PATH_CACHES));
    }

    /**
     * Get the index of this service instance.
     *
     * @return the instance index.
     */
    public int instanceIndex() {
        final Config rawConfig = getRawConfig();
        return getIfPresent("ditto.cluster.instance-index", rawConfig::getInt).orElse(0);
    }
}
