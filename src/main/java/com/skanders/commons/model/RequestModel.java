/*
 * Copyright (c) 2020 Alexander Iskander
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.skanders.commons.model;


import com.skanders.commons.result.Result;

/**
 * Base Request Model class. Incoming requests should use this class to hold
 * request data as well as override the {@link #validate()} function for data
 * validation.
 */
public abstract class RequestModel
{
    /**
     * Used in conjunction with Result to validate incoming RequestModels.
     *
     * @return either Result.VALID or custom Result stating reason for failure
     * @see com.skanders.commons.result.Resulted
     * @see Result
     */
    public abstract Result validate();
}
