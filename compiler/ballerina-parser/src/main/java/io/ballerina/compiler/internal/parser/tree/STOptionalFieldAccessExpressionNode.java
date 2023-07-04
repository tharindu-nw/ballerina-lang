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
import io.ballerina.compiler.syntax.tree.OptionalFieldAccessExpressionNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;

import java.util.Collection;
import java.util.Collections;

/**
 * This is a generated internal syntax tree node.
 *
 * @since 2.0.0
 */
public class STOptionalFieldAccessExpressionNode extends STExpressionNode {
    public final STNode expression;
    public final STNode optionalChainingToken;
    public final STNode fieldName;

    STOptionalFieldAccessExpressionNode(
            STNode expression,
            STNode optionalChainingToken,
            STNode fieldName) {
        this(
                expression,
                optionalChainingToken,
                fieldName,
                Collections.emptyList());
    }

    STOptionalFieldAccessExpressionNode(
            STNode expression,
            STNode optionalChainingToken,
            STNode fieldName,
            Collection<STNodeDiagnostic> diagnostics) {
        super(SyntaxKind.OPTIONAL_FIELD_ACCESS, diagnostics);
        this.expression = expression;
        this.optionalChainingToken = optionalChainingToken;
        this.fieldName = fieldName;

        addChildren(
                expression,
                optionalChainingToken,
                fieldName);
    }

    public STNode modifyWith(Collection<STNodeDiagnostic> diagnostics) {
        return new STOptionalFieldAccessExpressionNode(
                this.expression,
                this.optionalChainingToken,
                this.fieldName,
                diagnostics);
    }

    public STOptionalFieldAccessExpressionNode modify(
            STNode expression,
            STNode optionalChainingToken,
            STNode fieldName) {
        if (checkForReferenceEquality(
                expression,
                optionalChainingToken,
                fieldName)) {
            return this;
        }

        return new STOptionalFieldAccessExpressionNode(
                expression,
                optionalChainingToken,
                fieldName,
                diagnostics);
    }

    public Node createFacade(int position, NonTerminalNode parent) {
        return new OptionalFieldAccessExpressionNode(this, position, parent);
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
