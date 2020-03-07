package com.task.cn.task

import com.task.cn.Result
import com.task.cn.jbean.TaskBean

/**
 * Description:
 * Created by Quinin on 2020-03-04.
 **/
interface ITaskControllerView {
    fun onTaskPrepared(result: Result<TaskBean>)
}