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
package org.eclipse.ditto.services.utils.pubsub.ddata.compressed;

import java.util.List;
import java.util.concurrent.Executor;

import org.eclipse.ditto.services.utils.ddata.DistributedDataConfig;
import org.eclipse.ditto.services.utils.pubsub.config.PubSubConfig;
import org.eclipse.ditto.services.utils.pubsub.ddata.AbstractDDataHandler;
import org.eclipse.ditto.services.utils.pubsub.ddata.Hashes;

import akka.actor.ActorRefFactory;
import akka.actor.ActorSystem;
import akka.util.ByteString;
import scala.collection.JavaConverters;

/**
 * A distributed collection of hashes of strings indexed by ActorRef.
 * The hash functions for all filter should be identical.
 */
public final class CompressedDDataHandler extends AbstractDDataHandler<ByteString, CompressedUpdate> implements Hashes {

    private final List<Integer> seeds;

    private CompressedDDataHandler(final DistributedDataConfig config,
            final ActorRefFactory actorRefFactory,
            final ActorSystem actorSystem,
            final Executor ddataExecutor,
            final String topicType,
            final List<Integer> seeds) {
        super(config, actorRefFactory, actorSystem, ddataExecutor, topicType);
        this.seeds = seeds;
    }

    /**
     * Start distributed-data replicator for compressed topics under an actor system's user guardian using the default
     * dispatcher.
     *
     * @param system the actor system.
     * @param ddataConfig the distributed data config.
     * @param topicType the type of messages, typically "message-type-name-aware".
     * @param pubSubConfig the pub-sub config.
     * @return access to the distributed data.
     */
    public static CompressedDDataHandler create(final ActorSystem system, final DistributedDataConfig ddataConfig,
            final String topicType, final PubSubConfig pubSubConfig) {

        final List<Integer> seeds =
                Hashes.digestStringsToIntegers(pubSubConfig.getSeed(), pubSubConfig.getHashFamilySize());

        return new CompressedDDataHandler(ddataConfig, system, system, system.dispatcher(), topicType, seeds);
    }

    @Override
    public List<Integer> getSeeds() {
        return seeds;
    }

    /**
     * Lossy-compress a topic into a ByteString consisting of hash codes from the family of hash functions.
     *
     * @param topic the topic.
     * @return the compressed topic.
     */
    @Override
    public ByteString approximate(final String topic) {
        return hashCodesToByteString(getHashes(topic));
    }

    @SuppressWarnings("unchecked")
    static ByteString hashCodesToByteString(final List<Integer> hashes) {
        // force-casting to List<Object> to interface with covariant Scala collection
        final List<Object> hashesForScala = (List<Object>) (Object) hashes;
        return ByteString.fromInts(JavaConverters.asScalaBuffer(hashesForScala).toSeq());
    }
}
