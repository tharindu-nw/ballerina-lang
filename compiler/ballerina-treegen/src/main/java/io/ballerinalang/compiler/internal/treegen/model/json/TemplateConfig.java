/*
 *  Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package io.ballerinalang.compiler.internal.treegen.model.json;

import java.util.HashMap;
import java.util.List;

/**
 * TemplateConfig.
 *
 * @since 2.8.0
 */
public class TemplateConfig {

    private final List<TemplateNodeConfig> nodes;
    private HashMap<String, TemplateNodeConfig> nodeMap;

    public TemplateConfig(List<TemplateNodeConfig> nodes) {
        this.nodes = nodes;
    }

    public TemplateNodeConfig getNode(String name) {
        populateMap();
        return nodeMap.get(name);
    }

    private void populateMap() {
        if (nodeMap != null) {
            return;
        }
        nodeMap = new HashMap<>();
        for (TemplateNodeConfig node : nodes) {
            nodeMap.put(node.getName(), node);
        }
    }
}
