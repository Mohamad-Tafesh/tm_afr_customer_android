package com.africell.africell.features.profile.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.UserDTO
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetProfileUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<UserDTO, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<UserDTO> {
        return restApi.getProfile().map {
            sessionRepository.user=it
            it
        }

    }


}