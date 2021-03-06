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

package com.skanders.commons.worker;

import com.skanders.commons.result.Result;
import com.skanders.commons.worker.def.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TaskWorker extends Worker
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskWorker.class);

    private TaskWorkerPool workerPool;

    TaskWorker(String name, TaskWorkerPool workerPool)
    {
        super(name);
        this.workerPool = workerPool;
    }

    @Override
    protected void runTask()
    {
        Task task = workerPool.takeTask();
        if (task == null)
            return;

        LOG.info(name + "Attempting Request");
        Result result = task.executeTask();
        LOG.info(name + "Has received a result of: " + result.message());
    }
}
