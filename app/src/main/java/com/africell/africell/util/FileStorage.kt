package com.africell.africell.util

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observable
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileStorage
@Inject constructor(
    private val application: Application,
    private val gson: Gson
) {

    fun store(obj: Any): Observable<Unit> {
        return Observable.fromCallable {
            val fos = application.openFileOutput(obj.javaClass.simpleName, Context.MODE_PRIVATE)
            val data = gson.toJson(obj, obj.javaClass)
            fos.write(data.toByteArray())
            fos.flush()
            fos.close()
        }
    }

    fun store(obj: Any, name: String): Observable<Unit> {
        return Observable.fromCallable {
            val fos = application.openFileOutput(name, Context.MODE_PRIVATE)
            val data = gson.toJson(obj, obj.javaClass)
            fos.write(data.toByteArray())
            fos.flush()
            fos.close()
        }
    }

    fun <T> get(type: Class<T>): Observable<T> {
        return Observable.fromCallable {
            val fis = application.openFileInput(type.simpleName)
            val obj = gson.fromJson(InputStreamReader(fis), type)
            fis.close()
            obj
        }
    }

    fun <T> get(name: String, type: Type): Observable<T> {
        return Observable.fromCallable {
            val fis = application.openFileInput(name)
            val obj = gson.fromJson<T>(InputStreamReader(fis), type)
            fis.close()
            obj
        }
    }
}