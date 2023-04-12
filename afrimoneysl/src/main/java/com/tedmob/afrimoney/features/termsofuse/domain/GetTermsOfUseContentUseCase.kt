package com.tedmob.afrimoney.features.termsofuse.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.TermsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTermsOfUseContentUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<TermsData, Unit>() {

    override suspend fun execute(params: Unit): TermsData {
        return withContext(Dispatchers.IO) { //TODO api call
            TermsData(
                policyText = "a course or principle of action adopted or proposed by an organization or individual.",
                termsText = "a word or phrase used to describe a thing or to express a concept, especially in a particular kind of language or branch of study."
            )
        }
    }
}