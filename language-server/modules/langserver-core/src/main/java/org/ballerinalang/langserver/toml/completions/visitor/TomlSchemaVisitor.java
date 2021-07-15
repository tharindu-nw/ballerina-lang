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

import io.ballerina.toml.validator.schema.AbstractSchema;
import io.ballerina.toml.validator.schema.ArraySchema;
import io.ballerina.toml.validator.schema.BooleanSchema;
import io.ballerina.toml.validator.schema.NumericSchema;
import io.ballerina.toml.validator.schema.Schema;
import io.ballerina.toml.validator.schema.SchemaVisitor;
import io.ballerina.toml.validator.schema.StringSchema;
import io.ballerina.toml.validator.schema.Type;
import org.ballerinalang.langserver.toml.TomlCommonUtil;
import org.ballerinalang.langserver.toml.TomlSyntaxTreeUtil;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Schema Visitor for visiting toml validation schema.
 *
 * @since 2.0.0
 */
public class TomlSchemaVisitor extends SchemaVisitor {

    private final Map<TomlNode, Map<String, CompletionItem>> completions = new HashMap<>();
    private Map<String, CompletionItem> topLevelNodeStore;
    private TomlNode topLevelNodeKey;

    private Map<String, CompletionItem> initCompletionStore() {
        if (this.topLevelNodeStore == null) {
            return new HashMap<>();
        }
        return this.topLevelNodeStore;
    }

    @Override
    public void visit(Schema objectSchema) {
        Map<String, AbstractSchema> properties = objectSchema.properties();
        Map<String, CompletionItem> topLevelNodeStore = initCompletionStore();

        for (Map.Entry<String, AbstractSchema> property : properties.entrySet()) {
            String propertyKey = property.getKey();
            AbstractSchema propertyValue = property.getValue();
            switch (propertyValue.type()) {
                case OBJECT:
                    //json object
                    Table tableNode = new Table(propertyKey);
                    propertyKey = getQualifiedTableKey(propertyKey);
                    CompletionItem item = generateCompletionItem(tableNode, TomlSyntaxTreeUtil.TABLE);
                    topLevelNodeStore.put(propertyKey, item);
                    Map<String, CompletionItem> store = new HashMap<>();
                    visitNode(propertyValue, tableNode, store);
                    this.completions.put(tableNode, store);
                    break;
                case ARRAY:
                    //json array may contain json objects as elements in the array. i.e: array of tables,
                    // array of strings
                    visitNode(propertyValue, new Array(propertyKey, getQualifiedTableKey(propertyKey), ValueType.ARRAY),
                            topLevelNodeStore);
                    break;
                case STRING:
                    visitNode(propertyValue, new KeyValuePair(propertyKey, ValueType.STRING), topLevelNodeStore);
                    break;
                case INTEGER:
                    visitNode(propertyValue, new KeyValuePair(propertyKey, ValueType.NUMBER), topLevelNodeStore);
                    break;
                case BOOLEAN:
                    visitNode(propertyValue, new KeyValuePair(propertyKey, ValueType.BOOLEAN), topLevelNodeStore);
                    break;
                default:
                    visitNode(propertyValue, new Table(propertyKey), topLevelNodeStore);
            }
        }
    }

    @Override
    public void visit(ArraySchema arraySchema) {
        AbstractSchema items = arraySchema.items();
        if (items.type() == Type.OBJECT) {
            Map<String, CompletionItem> topLevelNodeStore = this.topLevelNodeStore;
            TableArray tableArray = new TableArray(((Array) this.topLevelNodeKey).getQname());
            CompletionItem item = generateCompletionItem(tableArray, TomlSyntaxTreeUtil.TABLE_ARRAY);
            topLevelNodeStore.put(tableArray.getKey(), item);
            this.topLevelNodeKey = tableArray;
            Map<String, CompletionItem> store = new HashMap<>();
            visitNode(items, this.topLevelNodeKey, store);
            this.completions.put(this.topLevelNodeKey, store);
        } else if (items.type() == Type.ARRAY) {
            String arrayKey = ((Array) this.topLevelNodeKey).getQname();
            visitNode(items, new Array(arrayKey, arrayKey, ValueType.ARRAY), this.topLevelNodeStore);
        } else {
            CompletionItem item = generateCompletionItem(this.topLevelNodeKey, TomlSyntaxTreeUtil.ARRAY);
            this.topLevelNodeStore.put(this.topLevelNodeKey.getKey(), item);
        }
    }

    @Override
    public void visit(BooleanSchema booleanSchema) {
        CompletionItem item = generateCompletionItem(this.topLevelNodeKey, TomlSyntaxTreeUtil.BOOLEAN);
        this.topLevelNodeStore.put(this.topLevelNodeKey.getKey(), item);
    }

    @Override
    public void visit(NumericSchema numericSchema) {
        CompletionItem item = generateCompletionItem(this.topLevelNodeKey, TomlSyntaxTreeUtil.NUMBER);
        this.topLevelNodeStore.put(this.topLevelNodeKey.getKey(), item);
    }

    @Override
    public void visit(StringSchema stringSchema) {
        CompletionItem item = generateCompletionItem(this.topLevelNodeKey, TomlSyntaxTreeUtil.STRING);
        this.topLevelNodeStore.put(this.topLevelNodeKey.getKey(), item);
    }

    private void visitNode(AbstractSchema schema, TomlNode newKey, Map<String, CompletionItem> newTopLevelStore) {
        TomlNode oldKey = this.topLevelNodeKey;
        Map<String, CompletionItem> oldStore = this.topLevelNodeStore;
        this.topLevelNodeKey = newKey;
        this.topLevelNodeStore = newTopLevelStore;
        schema.accept(this);
        this.topLevelNodeKey = oldKey;
        this.topLevelNodeStore = oldStore;
    }

    private String getQualifiedTableKey(String key) {
        if (this.topLevelNodeKey == null) {
            return key;
        }
        return this.topLevelNodeKey.getKey() + "." + key;
    }

    private CompletionItem generateCompletionItem(TomlNode node, String nodeType) {
        CompletionItem item = new CompletionItem();
        item.setDetail(TomlSyntaxTreeUtil.TABLE);
        item.setLabel(node.getKey());
        item.setKind(CompletionItemKind.Snippet);
        item.setInsertTextFormat(InsertTextFormat.Snippet);
        item.setDetail(nodeType);
        if (!(node.type() == TomlNodeType.TABLE || node.type() == TomlNodeType.TABLE_ARRAY)) {
            item.setSortText(TomlCommonUtil.genSortText(1));
        } else {
            item.setSortText(TomlCommonUtil.genSortText(2));
        }
        return item;
    }

    /**
     * Map of Toml document level nodes(table, table arrays) to completion items
     * that represent properties of a particular document level node.
     * eg: TableNode(build-options) -> observability, cloud, etc.
     *
     * @return {@link Map<TomlNode,Map<String,CompletionItem>} completion items map.
     */
    public Map<TomlNode, Map<String, CompletionItem>> getAllCompletionSnippets() {
        return Collections.unmodifiableMap(completions);
    }
}
