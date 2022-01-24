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
 * The {@link etaStatusResponse} is a encapsulation of responses from the eta
 *
 * @author Joe Inkenbrandt - Initial contribution
 */

@NonNullByDefault
public class etaStatusResponse {

    private String uniqueName = "";
    private String macAddress = "";
    private String serviceAccount = "";

    private etaOperatingStatus operatingStatus = etaOperatingStatus.WT;
    private etaCommandStatus lastCommandStatus = etaCommandStatus.ER;
    private etaCommandResult lastCommandResult = etaCommandResult.NC;

    private int lastActiveValue;
    private boolean rainSensor;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public etaOperatingStatus getOperatingStatus() {
        return operatingStatus;
    }

    public void setOperatingStatus(etaOperatingStatus operatingStatus) {
        this.operatingStatus = operatingStatus;
    }

    public etaCommandStatus getLastCommandStatus() {
        return lastCommandStatus;
    }

    public void setLastCommandStatus(etaCommandStatus lastCommandStatus) {
        this.lastCommandStatus = lastCommandStatus;
    }

    public etaCommandResult getLastCommandResult() {
        return lastCommandResult;
    }

    public void setLastCommandResult(etaCommandResult lastCommandResult) {
        this.lastCommandResult = lastCommandResult;
    }

    public int getLastActiveValue() {
        return lastActiveValue;
    }

    public void setLastActiveValue(int lastActiveValue) {
        this.lastActiveValue = lastActiveValue;
    }

    public boolean isRainSensor() {
        return rainSensor;
    }

    public void setRainSensor(boolean rainSensor) {
        this.rainSensor = rainSensor;
    }
}
