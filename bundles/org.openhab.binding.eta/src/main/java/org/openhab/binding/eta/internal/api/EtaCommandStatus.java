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
 * The {@link etaCommandStatus} is the response packet for Command Status
 *
 * @author Joe Inkenbrandt - Initial contribution
 */

@NonNullByDefault
public enum etaCommandStatus {
    OK("OK"),
    ER("ER"),
    NA("NA");

    private final String status;

    etaCommandStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
