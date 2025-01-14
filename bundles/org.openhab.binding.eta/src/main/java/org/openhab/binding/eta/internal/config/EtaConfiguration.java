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
package org.openhab.binding.eta.internal.config;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link etaConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Joe Inkenbrandt - Initial contribution
 */

@NonNullByDefault
public class etaConfiguration {

    public static final String BINDING_ID = "etherrrain";

    public static final ThingTypeUID eta_THING_TYPE = new ThingTypeUID(BINDING_ID, "eta");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(eta_THING_TYPE);

    /**
     * Hostname of the eta API.
     */
    public String host = "";

    /**
     * The port the eta API is listening on.
     * Default: 80 per specification
     */
    public int port = 80;

    /**
     * The password to connect to the eta API.
     * Default: pw per specification
     */
    public String password = "pw";

    /**
     * Number of seconds in between refreshes from the eta device.
     */
    public int refresh = 60;

    /**
     * Default Delay for Program Timer
     */
    public int programDelay = 0;

    /**
     * Default Zone on times
     */
    public int zoneOnTime1 = 0;
    public int zoneOnTime2 = 0;
    public int zoneOnTime3 = 0;
    public int zoneOnTime4 = 0;
    public int zoneOnTime5 = 0;
    public int zoneOnTime6 = 0;
    public int zoneOnTime7 = 0;
    public int zoneOnTime8 = 0;
}
