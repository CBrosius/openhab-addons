/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.eta.internal.discovery;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.eta.internal.etaBindingConstants;
import org.openhab.binding.eta.internal.api.etaCommunication;
import org.openhab.binding.eta.internal.api.etaUdpResponse;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link etaDiscoveryService} class discovers eta Device(s) and places them in the inbox.
 *
 * @author Joe Inkenbrandt - Initial contribution
 */
@NonNullByDefault
@Component(service = DiscoveryService.class, configurationPid = "discovery.eta")
public class etaDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(etaDiscoveryService.class);

    private static final int TIMEOUT = 15;

    public etaDiscoveryService() {
        super(etaBindingConstants.SUPPORTED_THING_TYPES_UIDS, TIMEOUT, true);
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return etaBindingConstants.SUPPORTED_THING_TYPES_UIDS;
    }

    @Override
    protected void startScan() {
        for (etaUdpResponse rdp : etaCommunication.autoDiscover()) {
            if (rdp.isValid()) {
                ThingUID uid = new ThingUID(etaBindingConstants.eta_THING,
                        rdp.getAddress().replaceAll("[^A-Za-z0-9\\-_]", ""));
                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(uid)
                        .withLabel("eta " + rdp.getType() + " " + rdp.getUnqiueName())
                        .withProperty("host", rdp.getAddress()).withProperty("port", rdp.getPort()).build();
                thingDiscovered(discoveryResult);
            } else {
                logger.debug("Nothing responded to request");
            }
        }
    }
}
