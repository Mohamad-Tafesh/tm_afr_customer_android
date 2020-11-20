package com.tedmob.africell.features.authentication.domain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tedmob.africell.R
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.entity.Country
import io.reactivex.Observable
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import javax.inject.Inject

class GetCountriesUseCase
@Inject constructor(
    private val context: Context,
    private val gson: Gson,
    schedulers: ExecutionSchedulers
) : UseCase<List<Country>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<Country>> {
        return Observable.fromCallable {
            val ims = context.resources.openRawResource(R.raw.countries)
            val reader = try {
                InputStreamReader(ims, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                InputStreamReader(ims)
            }
            val t = object : TypeToken<List<Country>>() {}.type

            gson.fromJson<List<Country>>(reader, t)
        }
    }
}