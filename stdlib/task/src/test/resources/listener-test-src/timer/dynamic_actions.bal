// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/task;
import ballerina/runtime;

int resultValue = 0;

boolean isPaused = false;
boolean isResumed = false;

service timerService = service {
    resource function onTrigger() {
        resultValue = 1;
    }

    resource function onError(error e) {
        resultValue = -1;
    }
};

function getConfigurations(int interval, int delay, int noOfRecurrences)
             returns task:TimerConfiguration {
    task:TimerConfiguration configuration = {
        interval: interval,
        initialDelay: delay,
        noOfRecurrences: noOfRecurrences
    };

    return configuration;
}

function getConfigurationsWithoutRecurrences(int interval, int delay) returns task:TimerConfiguration {
    task:TimerConfiguration configuration = {
        interval: interval,
        initialDelay: delay
    };

    return configuration;
}

function getConfigurationsWithoutDelay(int interval, int noOfRecurrences) returns task:TimerConfiguration {
    task:TimerConfiguration configuration = {
        interval: interval,
        noOfRecurrences: noOfRecurrences
    };

    return configuration;
}

function getConfigurationsWithoutDelayAndRecurrences(int interval) returns task:TimerConfiguration {
    task:TimerConfiguration configuration = {
        interval: interval
    };

    return configuration;
}

function createTimer(task:TimerConfiguration configurations) returns task:Listener {
    task:Listener timer = new(configurations);
    return timer;
}
