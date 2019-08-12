/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.models.connectivity;

import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.services.models.streaming.AbstractEntityIdWithRevision;

/**
 * Represents the ID and revision of a connection.
 */
@Immutable
public final class ConnectionTag extends AbstractEntityIdWithRevision {

    private ConnectionTag(final String id, final long revision) {
        super(id, revision);
    }

    private ConnectionTag(final JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * Returns a new {@link org.eclipse.ditto.services.models.connectivity.ConnectionTag}.
     *
     * @param id the conneciton ID.
     * @param revision the revision.
     * @return a new {@link org.eclipse.ditto.services.models.connectivity.ConnectionTag}.
     */
    public static ConnectionTag of(final String id, final long revision) {
        return new ConnectionTag(id, revision);
    }

    /**
     * Creates a new {@link org.eclipse.ditto.services.models.connectivity.ConnectionTag} from a JSON object.
     *
     * @param jsonObject the JSON object of which a new {@link org.eclipse.ditto.services.models.connectivity.ConnectionTag} is to be created.
     * @return the {@link org.eclipse.ditto.services.models.connectivity.ConnectionTag} which was created from the given JSON object.
     * @throws NullPointerException if {@code jsonObject} is {@code null}.
     * @throws IllegalArgumentException if {@code jsonObject} is empty.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} is not valid JSON.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if the passed in {@code jsonObject} was not in the
     * expected format.
     */
    public static ConnectionTag fromJson(final JsonObject jsonObject) {
        return new ConnectionTag(jsonObject);
    }

}