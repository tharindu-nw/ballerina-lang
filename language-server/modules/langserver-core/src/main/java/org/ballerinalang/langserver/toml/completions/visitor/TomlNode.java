/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.ballerinalang.langserver.toml.completions.visitor;

/**
 * Represents Toml Node in {@link TomlSchemaVisitor}.
 *
 * @since 2.0.0
 */
public interface TomlNode {

    /**
     * Get toml syntax based on key.
     *
     * @return
     */
    String getTomlSyntax();

    /**
     * Get toml key.
     *
     * @return
     */
    String getKey();

    /**
     *
     */
    TomlNodeType type();

}
