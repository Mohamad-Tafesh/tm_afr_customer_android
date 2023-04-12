package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.entity.Country
import com.tedmob.afrimoney.util.phone.PhoneNumberHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCountriesUseCase
@Inject constructor(
    /*private val context: Context,
    private val gson: Gson,*/
    private val repo: PhoneNumberHelper,
) : SuspendableUseCase<List<Country>, Unit>() {

    override suspend fun execute(params: Unit): List<Country> {
        return withContext(Dispatchers.Default) {
            /*val ims = context.resources.openRawResource(R.raw.countries)
            val reader = try {
                InputStreamReader(ims, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                InputStreamReader(ims)
            }
            val t = object : TypeToken<List<Country>>() {}.type

            gson.fromJson<List<Country>>(reader, t)*/

            repo.getCountriesAvailable()
                .sortedBy { it.name }
        }
    }
}