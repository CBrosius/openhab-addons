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
package org.openhab.binding.eta.internal.handler;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.eta.internal.etaBindingConstants;
import org.openhab.binding.eta.internal.etaException;
import org.openhab.binding.eta.internal.api.etaCommunication;
import org.openhab.binding.eta.internal.api.etaStatusResponse;
import org.openhab.binding.eta.internal.config.etaConfiguration;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link etaHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Joe Inkenbrandt - Initial contribution
 */
@NonNullByDefault
public class etaHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(etaHandler.class);

    private @Nullable etaCommunication device = null;
    private boolean connected = false;
    private @NonNullByDefault({}) etaConfiguration config = null;

    private @Nullable ScheduledFuture<?> updateJob = null;

    private final HttpClient httpClient;

    /*
     * Constructor class. Only call the parent constructor
     */
    public etaHandler(Thing thing, HttpClient httpClient) {
        super(thing);
        this.httpClient = httpClient;
        this.updateJob = null;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command == RefreshType.REFRESH) {
            scheduler.execute(this::updateBridge);
        } else if (channelUID.getId().equals(etaBindingConstants.CHANNEL_ID_EXECUTE)) {
            execute();
            updateState(etaBindingConstants.CHANNEL_ID_EXECUTE, OnOffType.OFF);
        } else if (channelUID.getId().equals(etaBindingConstants.CHANNEL_ID_CLEAR)) {
            clear();
            updateState(etaBindingConstants.CHANNEL_ID_CLEAR, OnOffType.OFF);
        }
    }

    private boolean connectBridge() {
        logger.debug("Attempting to connect to eta with config = (Host: {}, Port: {}, Refresh: {}).", config.host,
                config.port, config.refresh);

        etaCommunication device = new etaCommunication(config.host, config.port, config.password,
                httpClient);

        try {
            device.commandStatus();
        } catch (etaException | IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                    "Could not create a connection to the eta");
            logger.debug("Could not open API connection to the eta device. Exception received: {}",
                    e.getMessage());
            this.device = null;
            updateStatus(ThingStatus.OFFLINE);
            return false;
        }
        this.device = device;

        updateStatus(ThingStatus.ONLINE);

        return true;
    }

    private void startUpdateJob() {
        logger.debug("Starting eta Update Job");
        this.updateJob = scheduler.scheduleWithFixedDelay(this::updateBridge, 0, config.refresh, TimeUnit.SECONDS);

        logger.debug("eta sucessfully initialized. Starting status poll at: {}", config.refresh);
    }

    private void stopUpdateJob() {
        logger.debug("Stopping eta Update Job");

        final ScheduledFuture<?> updateJob = this.updateJob;
        if (updateJob != null && !updateJob.isDone()) {
            updateJob.cancel(false);
        }

        this.updateJob = null;
    }

    @SuppressWarnings("null")
    private boolean updateBridge() {
        if (!connected || device == null) {
            connected = connectBridge();
            if (!connected || device == null) {
                connected = false;
                device = null;
                logger.debug("Could not connect to eta device.");
                return false;
            }
        }

        etaStatusResponse response;

        try {
            response = device.commandStatus();
        } catch (etaException | IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                    "Could not create a connection to the eta");
            logger.debug("Could not open API connection to the eta device. Exception received: {}",
                    e.getMessage());
            device = null;
            return false;
        }

        updateState(etaBindingConstants.CHANNEL_ID_OPERATING_STATUS,
                new StringType(response.getOperatingStatus().name()));

        updateState(etaBindingConstants.CHANNEL_ID_COMMAND_STATUS,
                new StringType(response.getLastCommandStatus().name()));

        switch (response.getLastCommandResult()) {
            case OK:
                updateState(etaBindingConstants.CHANNEL_ID_OPERATING_RESULT, new StringType("OK"));
                break;
            case RN:
                updateState(etaBindingConstants.CHANNEL_ID_OPERATING_RESULT, new StringType("RAIN INTERRUPTED"));
                break;
            case SH:
                updateState(etaBindingConstants.CHANNEL_ID_OPERATING_RESULT,
                        new StringType("INTERRUPPTED SHORT"));
                break;
            case NC:
                updateState(etaBindingConstants.CHANNEL_ID_OPERATING_RESULT, new StringType("DID NOT COMPLETE"));
                break;
        }

        updateState(etaBindingConstants.CHANNEL_ID_RELAY_INDEX, new DecimalType(response.getLastActiveValue()));

        OnOffType rs = OnOffType.from(response.isRainSensor());

        updateState(etaBindingConstants.CHANNEL_ID_SENSOR_RAIN, rs);

        logger.debug("Completed eta Update");

        return true;
    }

    private synchronized boolean execute() {
        etaCommunication device = this.device;

        if (device != null) {
            device.commandIrrigate(config.programDelay, config.zoneOnTime1, config.zoneOnTime2, config.zoneOnTime3,
                    config.zoneOnTime4, config.zoneOnTime5, config.zoneOnTime6, config.zoneOnTime7, config.zoneOnTime8);
            updateBridge();
        }

        return true;
    }

    private boolean clear() {
        etaCommunication device = this.device;
        if (device != null) {
            device.commandClear();
        }

        updateBridge();

        return true;
    }

    @Override
    public void initialize() {
        config = getConfigAs(etaConfiguration.class);
        startUpdateJob();
    }

    @Override
    public void dispose() {
        stopUpdateJob();
    }
}
