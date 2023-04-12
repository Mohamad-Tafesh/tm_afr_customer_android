package com.tedmob.afrimoney.features.africellservices.airtime.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.AllowedWallets
import com.tedmob.afrimoney.data.entity.Bundlelist
import com.tedmob.afrimoney.data.entity.BundlelistParent
import com.tedmob.afrimoney.data.entity.GetFeesData
import javax.inject.Inject

class GetAirtimeServiceUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<BundlelistParent, Unit>() {

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Unit): BundlelistParent {
        val data = api.airtimeServices().BundleList.orEmpty().get(0)

            data.let {
                val bundlelist0 = mutableListOf<Bundlelist>()

                val displayName = it.getString("displayName").orEmpty()
                val icon = it.getString("icon").orEmpty()
                val receiverIDType =
                    it.getObject("operatorinfo")?.getString("receiver.idType").orEmpty()
                val receiverIDValue =
                    it.getObject("operatorinfo")?.getString("receiver.idValue").orEmpty()
                val bundlelist = it.getList("bundlelist").orEmpty().get(0)
                bundlelist.let {
                    val allowedWallets0 = mutableListOf<AllowedWallets>()
                    val BundleId = it.getString("BundleId").orEmpty()
                    val Validity = it.getString("Validity").orEmpty()
                    val description = it.getString("description").orEmpty()
                    val remark = it.getString("remark").orEmpty()
                    val transactionAmount = it.getString("transactionAmount").orEmpty()
                    val allowedReceiver = it.getValue<List<String>>("allowedReceiver").orEmpty()
                    val allowedWallets = it.getList("allowedWallets").orEmpty()
                    allowedWallets.forEach {
                        val id = it.getString("id").orEmpty()
                        val name = it.getString("name").orEmpty()
                        allowedWallets0.add(AllowedWallets(id, name))
                    }
                    bundlelist0.add(
                        Bundlelist(
                            BundleId,
                            Validity,
                            allowedReceiver,
                            allowedWallets0,
                            description,
                            remark,
                            transactionAmount
                        )
                    )
                }


                    return BundlelistParent(
                        bundlelist0,
                        displayName,
                        icon,
                        receiverIDType,
                        receiverIDValue
                    )

            }


    }


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?
    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)
}