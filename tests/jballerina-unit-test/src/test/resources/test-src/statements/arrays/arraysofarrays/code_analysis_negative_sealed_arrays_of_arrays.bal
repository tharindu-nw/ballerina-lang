// Copyright (c) 2022 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

function invalidSealedLiteralUsage() {
    int[*][*] a1 = [[1, 2], [3, 4]];
    int[][2][*] _ = [[[5, 6], [7, 8]], a1];
    int[*][2][*] _ = [[[5, 6], [7, 8]], a1];
    int[][2][][*] _ = [[[[5, 5], [6, 6]], [[7, 7], [8, 9]]], [a1, a1]];

    int[][*]|string _ = "a1";
    int[][*][]|string _ = [[[5, 6], [7, 8]], a1];

    string[*][*] & readonly a2 = [["1", "2"], ["3", "4"]];
    string[][*] & readonly _ = a2;

    float[*] & readonly a3 = [3, 4];
    float[][*] & readonly|string _ = [[1, 2], a3];
    (float[][*]|string) & readonly _ = [[1, 2], a3];
}
