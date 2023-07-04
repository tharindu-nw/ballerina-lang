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
package io.ballerina.compiler.syntax.tree;

import io.ballerina.compiler.internal.parser.tree.STNode;

import java.util.Objects;
import java.util.Optional;

/**
 * This is a generated syntax tree node.
 *
 * @since 2.0.0
 */
public class ImplicitNewExpressionNode extends NewExpressionNode {

    public ImplicitNewExpressionNode(STNode internalNode, int position, NonTerminalNode parent) {
        super(internalNode, position, parent);
    }

    public Token newKeyword() {
        return childInBucket(0);
    }

    public Optional<ParenthesizedArgList> parenthesizedArgList() {
        return optionalChildInBucket(1);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <T> T apply(NodeTransformer<T> visitor) {
        return visitor.transform(this);
    }

    @Override
    protected String[] childNames() {
        return new String[]{
                "newKeyword",
                "parenthesizedArgList"};
    }

    public ImplicitNewExpressionNode modify(
            Token newKeyword,
            ParenthesizedArgList parenthesizedArgList) {
        if (checkForReferenceEquality(
                newKeyword,
                parenthesizedArgList)) {
            return this;
        }

        return NodeFactory.createImplicitNewExpressionNode(
                newKeyword,
                parenthesizedArgList);
    }

    public ImplicitNewExpressionNodeModifier modify() {
        return new ImplicitNewExpressionNodeModifier(this);
    }

    /**
     * This is a generated tree node modifier utility.
     *
     * @since 2.0.0
     */
    public static class ImplicitNewExpressionNodeModifier {
        private final ImplicitNewExpressionNode oldNode;
        private Token newKeyword;
        private ParenthesizedArgList parenthesizedArgList;

        public ImplicitNewExpressionNodeModifier(ImplicitNewExpressionNode oldNode) {
            this.oldNode = oldNode;
            this.newKeyword = oldNode.newKeyword();
            this.parenthesizedArgList = oldNode.parenthesizedArgList().orElse(null);
        }

        public ImplicitNewExpressionNodeModifier withNewKeyword(
                Token newKeyword) {
            Objects.requireNonNull(newKeyword, "newKeyword must not be null");
            this.newKeyword = newKeyword;
            return this;
        }

        public ImplicitNewExpressionNodeModifier withParenthesizedArgList(
                ParenthesizedArgList parenthesizedArgList) {
            this.parenthesizedArgList = parenthesizedArgList;
            return this;
        }

        public ImplicitNewExpressionNode apply() {
            return oldNode.modify(
                    newKeyword,
                    parenthesizedArgList);
        }
    }
}
