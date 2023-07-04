/*
 *  Copyright (c) 2020, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
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
package io.ballerina.compiler.internal.parser.tree;

import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NonTerminalNode;
import io.ballerina.compiler.syntax.tree.OrderKeyNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;

import java.util.Collection;
import java.util.Collections;

/**
 * This is a generated internal syntax tree node.
 *
 * @since 2.0.0
 */
public class STOrderKeyNode extends STNode {
    public final STNode expression;
    public final STNode orderDirection;

    STOrderKeyNode(
            STNode expression,
            STNode orderDirection) {
        this(
                expression,
                orderDirection,
                Collections.emptyList());
    }

    STOrderKeyNode(
            STNode expression,
            STNode orderDirection,
            Collection<STNodeDiagnostic> diagnostics) {
        super(SyntaxKind.ORDER_KEY, diagnostics);
        this.expression = expression;
        this.orderDirection = orderDirection;

        addChildren(
                expression,
                orderDirection);
    }

    public STNode modifyWith(Collection<STNodeDiagnostic> diagnostics) {
        return new STOrderKeyNode(
                this.expression,
                this.orderDirection,
                diagnostics);
    }

    public STOrderKeyNode modify(
            STNode expression,
            STNode orderDirection) {
        if (checkForReferenceEquality(
                expression,
                orderDirection)) {
            return this;
        }

        return new STOrderKeyNode(
                expression,
                orderDirection,
                diagnostics);
    }

    public Node createFacade(int position, NonTerminalNode parent) {
        return new OrderKeyNode(this, position, parent);
    }

    @Override
    public void accept(STNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T apply(STNodeTransformer<T> transformer) {
        return transformer.transform(this);
    }
}
