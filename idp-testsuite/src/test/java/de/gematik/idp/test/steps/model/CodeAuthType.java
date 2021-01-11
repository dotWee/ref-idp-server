/*
 * Copyright (c) 2021 gematik GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.idp.test.steps.model;

public enum CodeAuthType {
    SIGNED_CHALLENGE("signed_challenge"), SSO_TOKEN("sso_token"), SSO_TOKEN_NO_CHALLENGE(
        "sso_token_no_challenge"), NO_PARAMS("no_params");

    private final String value;

    CodeAuthType(final String value) {
        this.value = value.toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }

}
