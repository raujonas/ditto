/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
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
package org.eclipse.ditto.services.connectivity.mapping.javascript.benchmark;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.ditto.services.connectivity.mapping.MessageMapper;
import org.eclipse.ditto.services.connectivity.mapping.javascript.JavaScriptMessageMapperFactory;
import org.eclipse.ditto.services.models.connectivity.ExternalMessage;
import org.eclipse.ditto.services.models.connectivity.ExternalMessageFactory;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Test4ConstructJsonPayloadToDitto implements MapToDittoProtocolScenario {

    private static final String CONTENT_TYPE = "application/json";

    private static final String MAPPING_INCOMING_PLAIN =
            "function mapToDittoProtocolMsg(\n" +
                    "    headers,\n" +
                    "    textPayload,\n" +
                    "    bytePayload,\n" +
                    "    contentType\n" +
                    ") {\n" +
                    "\n" +
                    "    // ###\n" +
                    "    // Insert your mapping logic here\n" +
                    "    let namespace = \"org.eclipse.ditto\";\n" +
                    "    let id = \"jmh-test\";\n" +
                    "    let group = \"things\";\n" +
                    "    let channel = \"twin\";\n" +
                    "    let criterion = \"commands\";\n" +
                    "    let action = \"modify\";\n" +
                    "    let path = \"/\";\n" +
                    "    let dittoHeaders = {};\n" +
                    "    dittoHeaders[\"correlation-id\"] = headers[\"correlation-id\"];\n" +
                    "    const input = {\n" +
                    "       a: 123, b: 456, c: \"lore ipsum bla bla bla\"\n" +
                    "    };\n" +
                    "    let value = {\n" +
                    "       aa: input.a + 99,\n" +
                    "       bb: input.a + input.b,\n" +
                    "       cc: 'xxx/' + input.b + '/yyy',\n" +
                    "       dd: [\n" +
                    "       'u',\n" +
                    "       'v',\n" +
                    "       input.c,\n" +
                    "       'x',\n" +
                    "       'y'\n" +
                    "       ]\n" +
                    "    };\n" +
                    "    // ###\n" +
                    "\n" +
                    "    return Ditto.buildDittoProtocolMsg(\n" +
                    "        namespace,\n" +
                    "        id,\n" +
                    "        group,\n" +
                    "        channel,\n" +
                    "        criterion,\n" +
                    "        action,\n" +
                    "        path,\n" +
                    "        dittoHeaders,\n" +
                    "        value\n" +
                    "    );\n" +
                    "}";

    private final ExternalMessage externalMessage;

    public Test4ConstructJsonPayloadToDitto() {
        final String correlationId = UUID.randomUUID().toString();
        final Map<String, String> headers = new HashMap<>();
        headers.put("correlation-id", correlationId);
        headers.put(ExternalMessage.CONTENT_TYPE_HEADER, CONTENT_TYPE);
        externalMessage = ExternalMessageFactory.newExternalMessageBuilder(headers)
                .build();
    }

    @Override
    public MessageMapper getMessageMapper() {
        final MessageMapper javaScriptRhinoMapperPlain =
                JavaScriptMessageMapperFactory.createJavaScriptMessageMapperRhino();
        javaScriptRhinoMapperPlain.configure(MAPPING_CONFIG,
                JavaScriptMessageMapperFactory
                        .createJavaScriptMessageMapperConfigurationBuilder("construct", Collections.emptyMap())
                        .incomingScript(MAPPING_INCOMING_PLAIN)
                        .build()
        );
        return javaScriptRhinoMapperPlain;
    }

    @Override
    public ExternalMessage getExternalMessage() {
        return externalMessage;
    }

}

