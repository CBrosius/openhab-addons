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
package org.openhab.binding.eta.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link etaOperatingStatus} is the response packet for Operating Status
 *
 * @author Joe Inkenbrandt - Initial contribution
 */

@NonNullByDefault
public enum etaOperatingStatus {
    RD("RD"),
    WT("WT"),
    BZ("BZ");

    private final String status;

    etaOperatingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
