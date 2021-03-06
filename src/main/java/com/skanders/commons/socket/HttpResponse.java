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

package com.skanders.commons.socket;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class HttpResponse<T>
{
    private int                            status;
    private MultivaluedMap<String, String> headers;

    private boolean hasEntity;
    private T       entity;

    private HttpResponse(Response response, T entity)
    {
        this.status    = response.getStatus();
        this.headers   = response.getStringHeaders();
        this.hasEntity = entity != null;
        this.entity    = entity;

        response.close();
    }

    public static <T> HttpResponse<T> create(Response response, Class<T> className)
    {
        T entity = response.hasEntity() ? response.readEntity(className) : null;

        return new HttpResponse<>(response, entity);
    }

    public int status()
    {
        return status;
    }

    public MultivaluedMap<String, String> headers()
    {
        return headers;
    }

    public boolean hasEntity()
    {
        return hasEntity;
    }

    public T entity()
    {
        return entity;
    }
}
