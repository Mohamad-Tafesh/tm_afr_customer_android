package com.tedmob.afrimoney.features.faq.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.features.faq.FaqItem
import javax.inject.Inject

class GetFaqItemsUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
) : SuspendableUseCase<List<FaqItem>, Unit>() {

    override suspend fun execute(params: Unit): List<FaqItem> {
        return listOf(
            FaqItem(1, "Question With Long Answer 1", "Long Answer 1\nVery Long Answer 1"),
            FaqItem(2, "Question 1", "Answer 1"),
            FaqItem(3, "Question 2", "Answer 2"),
            FaqItem(4, "Question 3", "Answer 3"),
            FaqItem(5, "Question 4", "Answer 4"),
            FaqItem(6, "Question 5", "Answer 5"),
            FaqItem(7, "Question 6", "Answer 6"),
            FaqItem(8, "Question 7", "Answer 7"),
            FaqItem(9, "Question 8", "Answer 8"),
            FaqItem(10, "Question 9", "Answer 9"),
            FaqItem(11, "Question 10", "Answer 10"),
            FaqItem(12, "Question 11", "Answer 11"),
            FaqItem(13, "Question 12", "Answer 12"),
            FaqItem(14, "Question 13", "Answer 13"),
            FaqItem(15, "Question With Long Answer 2", "Long Answer 1\nVery Long Answer 2"),
            FaqItem(16, "Question With Long Answer 3", "Long Answer 2\nVery Long Answer 3")
        )
    }
}