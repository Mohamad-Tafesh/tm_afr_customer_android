package com.tedmob.africell.features.profile.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi

import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
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