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

package de.gematik.idp.test.steps;

import static java.util.Locale.ENGLISH;

import de.gematik.idp.test.steps.model.ClaimLocation;
import de.gematik.idp.test.steps.model.CodeAuthType;
import de.gematik.idp.test.steps.model.ContextKey;
import de.gematik.idp.test.steps.model.DateCompareMode;
import de.gematik.idp.test.steps.model.HttpMethods;
import de.gematik.idp.test.steps.model.HttpStatus;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import java.time.Duration;
import java.util.Locale;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    @Override
    public Locale locale() {
        return ENGLISH;
    }

    @Override
    public void configureTypeRegistry(final TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType<>(
            "HttpStatus",
            "failed state|successfully|unsuccessfully|[1-5][0-9]{2}",
            HttpStatus.class,
            (Transformer<HttpStatus>) HttpStatus::new)
        );
        typeRegistry.defineParameterType(ParameterType.fromEnum(CodeAuthType.class));
        typeRegistry.defineParameterType(ParameterType.fromEnum(HttpMethods.class));
        typeRegistry.defineParameterType(ParameterType.fromEnum(ContextKey.class));
        typeRegistry.defineParameterType(ParameterType.fromEnum(ClaimLocation.class));
        typeRegistry.defineParameterType(ParameterType.fromEnum(DateCompareMode.class));
        typeRegistry.defineParameterType(new ParameterType<>(
            "Duration",
            "P[-\\d\\.DTHMS]*",
            Duration.class,
            (Transformer<Duration>) Duration::parse)
        );
    }
}
