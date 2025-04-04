package eg.edu.iti.weathify.utils

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.UUID

object WorkMangerHelper {
    fun addWorker(worker: OneTimeWorkRequest, context: Context){
        val workManger = WorkManager.getInstance(context)
        workManger.enqueue(worker)
    }
    fun cancelWorker(context: Context,id:String){
        val workManger = WorkManager.getInstance(context)
        workManger.cancelWorkById(UUID.fromString(id))

    }
}