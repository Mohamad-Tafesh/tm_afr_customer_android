package com.tedmob.afrimoney.features.bills.nawec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.api.dto.AddClientDTO
import com.tedmob.afrimoney.data.api.dto.ClientDetails
import com.tedmob.afrimoney.data.api.dto.CustomerInvoiceDTO
import com.tedmob.afrimoney.data.api.dto.ListOrObject
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.nawec.domain.DeleteNawecUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.GetCustomerInvoicesUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.GetFeesNawecUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.GetMeterUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.GetMetersUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.SubmitNawecAddUseCase
import com.tedmob.afrimoney.features.bills.nawec.domain.SubmitNawecUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NawecViewModel
@Inject constructor(

    private val confirmationUseCase: SubmitNawecAddUseCase,
    private val submitNawecUseCase: SubmitNawecUseCase,
    private val getClientsUseCase: GetMetersUseCase,
    private val getCustomerInvoicesUseCase: GetCustomerInvoicesUseCase,
    private val getMeterUseCase: GetMeterUseCase,
    private val deleteNawecUseCase: DeleteNawecUseCase,
    private val getFeesNawecUseCase: GetFeesNawecUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    var meterId: String? = null
    var nickname: String? = null
    var amount: String? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()
    val proceedToConfirmDelete: LiveData<Resource<Unit>> get() = _proceedToConfirmDelete
    private val _proceedToConfirmDelete = SingleLiveEvent<Resource<Unit>>()

    val feesData: LiveData<Resource<GetFeesData>> get() = _feesData
    private val _feesData = SingleLiveEvent<Resource<GetFeesData>>()

    val proceedToInvoices: LiveData<Resource<Unit>> get() = _proceedToInvoices
    private val _proceedToInvoices = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<NawecMeterData>> get() = _data
    private val _data = SingleLiveEvent<Resource<NawecMeterData>>()

    val clients: LiveData<Resource<ListOrObject<ClientDetails>>> get() = _clients
    private val _clients = MutableLiveData<Resource<ListOrObject<ClientDetails>>>()

    val customerInvoicesData: LiveData<Resource<CustomerInvoiceDTO>> get() = _customerInvoicesData
    private val _customerInvoicesData = MutableLiveData<Resource<CustomerInvoiceDTO>>()

    val submitted: LiveData<Resource<AddClientDTO>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<AddClientDTO>>()

    val confirmInvoiceData: LiveData<Resource<SubmitResult>> get() = _confirmInvoiceData
    private val _confirmInvoiceData = SingleLiveEvent<Resource<SubmitResult>>()

    val confirmDeleteData: LiveData<Resource<SubmitResult>> get() = _confirmDeleteData
    private val _confirmDeleteData = SingleLiveEvent<Resource<SubmitResult>>()

    val confirmEndePrePaidData: LiveData<Resource<SubmitResult>> get() = _confirmEndePrePaidData
    private val _confirmEndePrePaidData = SingleLiveEvent<Resource<SubmitResult>>()

    var prepaidFeesData: GetNawecFeesData? = null


    fun proceed(meterId: String) {

        execute(
            getMeterUseCase,
            meterId,
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                _data.emitSuccess(NawecMeterData(meterId, it.name.orEmpty()))
                this.meterId = meterId
                this.nickname = it.name
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                }
            }
        )

    }

    fun proceedDelete(meterId: String?, meterName: String?) {
        this.meterId = meterId
        this.nickname = meterName
        _proceedToConfirmDelete.emitSuccess(Unit)
    }



    fun getClients() {

        execute(
            getClientsUseCase,
            Unit,
            onLoading = {
                _clients.emitLoading()
            },
            onSuccess = {
                _clients.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _clients.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getConfirmationAdd(pin: String) {

        execute(
            confirmationUseCase,
            SubmitNawecAddUseCase.Params(meterId.orEmpty(), nickname.orEmpty(), pin),
            onLoading = {
                _submitted.emitLoading()
            },
            onSuccess = {
                _submitted.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _submitted.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getConfirmationBuyNawec(pin: String) {
        execute(
            submitNawecUseCase,
            SubmitNawecUseCase.Params(
                prepaidFeesData!!.number,
                prepaidFeesData!!.amount,
                pin,
                prepaidFeesData!!.uniqueNumber,
                prepaidFeesData!!.dateNTime
            ),
            onLoading = {
                _confirmEndePrePaidData.emitLoading()
            },
            onSuccess = {
                _confirmEndePrePaidData.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _confirmEndePrePaidData.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getDeleteConfirmation(pin: String) {

        execute(
            deleteNawecUseCase,
            DeleteNawecUseCase.Params(meterId.orEmpty(),nickname.orEmpty(), pin),
            onLoading = {
                _confirmDeleteData.emitLoading()
            },
            onSuccess = {
                _confirmDeleteData.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _confirmDeleteData.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getClientInvoices() {
        executeResource(
            getCustomerInvoicesUseCase,
            meterId.orEmpty(),
            _customerInvoicesData,
            appExceptionFactory,
            appSessionNavigator,
            action = { getClientInvoices() },
        )
    }

    fun getBuyFees(
        meterNumber: String,
        meterName: String,
        amount: String,
    ) {

        this.meterId = meterId
        this.nickname = meterName
        this.amount = amount

        execute(
            getFeesNawecUseCase,
            GetFeesNawecUseCase.Params(meterNumber, amount),
            onLoading = {
                _proceedToConfirm.emitLoading()
            },
            onSuccess = {
                prepaidFeesData = it
                _proceedToConfirm.emitSuccess(Unit)

            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _proceedToConfirm.emitError(exception.userMessage)
                }
            }
        )


    }



}